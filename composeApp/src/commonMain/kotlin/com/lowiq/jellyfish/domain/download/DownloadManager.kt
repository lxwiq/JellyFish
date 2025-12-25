package com.lowiq.jellyfish.domain.download

import com.lowiq.jellyfish.data.local.DownloadSettingsStorage
import com.lowiq.jellyfish.data.local.DownloadStorage
import com.lowiq.jellyfish.data.local.FileManager
import com.lowiq.jellyfish.data.local.SecureStorage
import com.lowiq.jellyfish.data.local.ServerStorage
import com.lowiq.jellyfish.data.remote.DownloadClient
import com.lowiq.jellyfish.data.remote.DownloadResult
import com.lowiq.jellyfish.data.remote.JellyfinDataSource
import com.lowiq.jellyfish.domain.model.Download
import com.lowiq.jellyfish.domain.model.DownloadStatus
import com.lowiq.jellyfish.util.currentTimeMillis
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import okio.FileSystem
import okio.Path.Companion.toPath
import okio.buffer

class DownloadManager(
    private val downloadStorage: DownloadStorage,
    private val downloadSettings: DownloadSettingsStorage,
    private val serverStorage: ServerStorage,
    private val secureStorage: SecureStorage,
    private val fileManager: FileManager,
    private val jellyfinDataSource: JellyfinDataSource,
    private val downloadClient: DownloadClient
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val activeJobs = mutableMapOf<String, Job>()

    // Throttling: track last save time and progress per download (thread-safe for parallel downloads)
    private val lastSaveTime = java.util.concurrent.ConcurrentHashMap<String, Long>()
    private val lastSavedProgress = java.util.concurrent.ConcurrentHashMap<String, Float>()

    private fun log(message: String) {
        println("[DownloadManager] $message")
    }

    companion object {
        private const val SAVE_INTERVAL_MS = 500L      // Max one DataStore write per 500ms
        private const val MIN_PROGRESS_DELTA = 0.01f   // Save only if progress changed by 1%
    }

    private val _downloadEvents = MutableSharedFlow<DownloadEvent>()
    val downloadEvents: SharedFlow<DownloadEvent> = _downloadEvents.asSharedFlow()

    init {
        observeDownloads()
    }

    private fun observeDownloads() {
        scope.launch {
            log("observeDownloads: Starting to observe downloads")
            downloadStorage.getAllDownloads().collect { downloads ->
                val maxParallel = downloadSettings.parallelDownloads.first()
                val activeCount = activeJobs.count { it.value.isActive }
                val queued = downloads.filter { it.status == DownloadStatus.QUEUED }

                log("observeDownloads: ${downloads.size} downloads, $activeCount active, ${queued.size} queued, maxParallel=$maxParallel")

                queued.take(maxParallel - activeCount).forEach { download ->
                    if (activeJobs[download.id]?.isActive != true) {
                        log("observeDownloads: Starting job for ${download.title}")
                        startDownloadJob(download)
                    }
                }
            }
        }
    }

    private fun startDownloadJob(download: Download) {
        val job = scope.launch {
            try {
                log("startDownloadJob: Starting download for ${download.title} (id=${download.id})")
                downloadStorage.updateStatus(download.id, DownloadStatus.DOWNLOADING)
                _downloadEvents.emit(DownloadEvent.Started(download.id, download.title))

                val servers = serverStorage.getServers().first()
                val server = servers.find { it.id == download.serverId } ?: throw Exception("Server not found")
                val token = secureStorage.getToken(download.serverId) ?: throw Exception("Not authenticated")
                log("startDownloadJob: Got server ${server.url} and token")

                // Use direct download for Original quality, transcoding for others
                val url = if (download.quality == "Original") {
                    jellyfinDataSource.getDirectDownloadUrl(
                        serverUrl = server.url,
                        token = token,
                        itemId = download.itemId
                    )
                } else {
                    jellyfinDataSource.getTranscodingDownloadUrl(
                        serverUrl = server.url,
                        token = token,
                        itemId = download.itemId,
                        bitrate = download.bitrate
                    )
                }
                log("startDownloadJob: ${if (download.quality == "Original") "Direct" else "Transcoding"} URL = $url")

                val fileName = "${download.itemId}_${download.quality}.mp4"
                val filePath = "${fileManager.getDownloadsDirectory()}/$fileName"
                val path = filePath.toPath()
                log("startDownloadJob: Saving to $filePath")
                val sink = FileSystem.SYSTEM.sink(path).buffer()

                var lastLoggedProgress = 0
                downloadClient.downloadFile(url, filePath) { bytes ->
                    sink.write(bytes)
                }.collect { result ->
                    when (result) {
                        is DownloadResult.Progress -> {
                            val progressPercent = (result.progress.progress * 100).toInt()
                            if (progressPercent >= lastLoggedProgress + 10) {
                                log("startDownloadJob: Progress $progressPercent% (${result.progress.bytesDownloaded}/${result.progress.totalBytes})")
                                lastLoggedProgress = progressPercent
                            }

                            // Always emit UI event (lightweight, no disk I/O)
                            _downloadEvents.emit(DownloadEvent.Progress(
                                download.id,
                                result.progress.progress,
                                result.progress.bytesDownloaded,
                                result.progress.totalBytes
                            ))

                            // Throttle DataStore writes to prevent performance issues
                            val now = currentTimeMillis()
                            val lastTime = lastSaveTime[download.id] ?: 0L
                            val lastProgress = lastSavedProgress[download.id] ?: 0f
                            val progressDelta = result.progress.progress - lastProgress
                            val timeDelta = now - lastTime

                            if (timeDelta >= SAVE_INTERVAL_MS || progressDelta >= MIN_PROGRESS_DELTA) {
                                downloadStorage.updateProgress(
                                    download.id,
                                    result.progress.progress,
                                    result.progress.bytesDownloaded
                                )
                                lastSaveTime[download.id] = now
                                lastSavedProgress[download.id] = result.progress.progress
                            }
                        }
                        is DownloadResult.Success -> {
                            log("startDownloadJob: SUCCESS - Download complete for ${download.title}")
                            sink.close()
                            downloadStorage.updateStatus(download.id, DownloadStatus.COMPLETED, filePath)
                            _downloadEvents.emit(DownloadEvent.Completed(download.id, download.title))
                            // Cleanup throttle tracking
                            lastSaveTime.remove(download.id)
                            lastSavedProgress.remove(download.id)
                        }
                        is DownloadResult.Error -> {
                            log("startDownloadJob: ERROR - ${result.message}")
                            sink.close()
                            FileSystem.SYSTEM.delete(path)
                            downloadStorage.updateStatus(download.id, DownloadStatus.FAILED, errorMessage = result.message)
                            _downloadEvents.emit(DownloadEvent.Failed(download.id, download.title, result.message))
                            // Cleanup throttle tracking
                            lastSaveTime.remove(download.id)
                            lastSavedProgress.remove(download.id)
                        }
                    }
                }
            } catch (e: CancellationException) {
                log("startDownloadJob: CANCELLED - ${download.title}")
                downloadStorage.updateStatus(download.id, DownloadStatus.PAUSED)
            } catch (e: Exception) {
                log("startDownloadJob: EXCEPTION - ${e.message}")
                e.printStackTrace()
                downloadStorage.updateStatus(download.id, DownloadStatus.FAILED, errorMessage = e.message)
                _downloadEvents.emit(DownloadEvent.Failed(download.id, download.title, e.message ?: "Unknown error"))
            }
        }
        activeJobs[download.id] = job
    }

    fun pauseDownload(downloadId: String) {
        activeJobs[downloadId]?.cancel()
        activeJobs.remove(downloadId)
        // Cleanup throttle tracking to prevent stale state on resume
        lastSaveTime.remove(downloadId)
        lastSavedProgress.remove(downloadId)
    }

    fun cancelAll() {
        activeJobs.values.forEach { it.cancel() }
        activeJobs.clear()
        // Cleanup all throttle tracking
        lastSaveTime.clear()
        lastSavedProgress.clear()
    }
}

sealed class DownloadEvent {
    data class Started(val downloadId: String, val title: String) : DownloadEvent()
    data class Progress(val downloadId: String, val progress: Float, val bytesDownloaded: Long, val totalBytes: Long) : DownloadEvent()
    data class Completed(val downloadId: String, val title: String) : DownloadEvent()
    data class Failed(val downloadId: String, val title: String, val error: String) : DownloadEvent()
}
