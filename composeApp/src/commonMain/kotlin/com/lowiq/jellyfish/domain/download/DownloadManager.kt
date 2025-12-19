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
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.io.File
import java.io.FileOutputStream

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

    private val _downloadEvents = MutableSharedFlow<DownloadEvent>()
    val downloadEvents: SharedFlow<DownloadEvent> = _downloadEvents.asSharedFlow()

    init {
        observeDownloads()
    }

    private fun observeDownloads() {
        scope.launch {
            downloadStorage.getAllDownloads().collect { downloads ->
                val maxParallel = downloadSettings.parallelDownloads.first()
                val activeCount = activeJobs.count { it.value.isActive }
                val queued = downloads.filter { it.status == DownloadStatus.QUEUED }

                queued.take(maxParallel - activeCount).forEach { download ->
                    if (activeJobs[download.id]?.isActive != true) {
                        startDownloadJob(download)
                    }
                }
            }
        }
    }

    private fun startDownloadJob(download: Download) {
        val job = scope.launch {
            try {
                downloadStorage.updateStatus(download.id, DownloadStatus.DOWNLOADING)
                _downloadEvents.emit(DownloadEvent.Started(download.id, download.title))

                val servers = serverStorage.getServers().first()
                val server = servers.find { it.id == download.serverId } ?: throw Exception("Server not found")
                val token = secureStorage.getToken(download.serverId) ?: throw Exception("Not authenticated")

                val url = jellyfinDataSource.getTranscodingDownloadUrl(
                    serverUrl = server.url,
                    token = token,
                    itemId = download.itemId,
                    bitrate = download.bitrate
                )

                val fileName = "${download.itemId}_${download.quality}.mp4"
                val filePath = "${fileManager.getDownloadsDirectory()}/$fileName"
                val file = File(filePath)
                val outputStream = FileOutputStream(file)

                downloadClient.downloadFile(url, filePath) { bytes ->
                    outputStream.write(bytes)
                }.collect { result ->
                    when (result) {
                        is DownloadResult.Progress -> {
                            downloadStorage.updateProgress(
                                download.id,
                                result.progress.progress,
                                result.progress.bytesDownloaded
                            )
                            _downloadEvents.emit(DownloadEvent.Progress(
                                download.id,
                                result.progress.progress,
                                result.progress.bytesDownloaded,
                                result.progress.totalBytes
                            ))
                        }
                        is DownloadResult.Success -> {
                            outputStream.close()
                            downloadStorage.updateStatus(download.id, DownloadStatus.COMPLETED, filePath)
                            _downloadEvents.emit(DownloadEvent.Completed(download.id, download.title))
                        }
                        is DownloadResult.Error -> {
                            outputStream.close()
                            file.delete()
                            downloadStorage.updateStatus(download.id, DownloadStatus.FAILED, errorMessage = result.message)
                            _downloadEvents.emit(DownloadEvent.Failed(download.id, download.title, result.message))
                        }
                    }
                }
            } catch (e: CancellationException) {
                downloadStorage.updateStatus(download.id, DownloadStatus.PAUSED)
            } catch (e: Exception) {
                downloadStorage.updateStatus(download.id, DownloadStatus.FAILED, errorMessage = e.message)
                _downloadEvents.emit(DownloadEvent.Failed(download.id, download.title, e.message ?: "Unknown error"))
            }
        }
        activeJobs[download.id] = job
    }

    fun pauseDownload(downloadId: String) {
        activeJobs[downloadId]?.cancel()
        activeJobs.remove(downloadId)
    }

    fun cancelAll() {
        activeJobs.values.forEach { it.cancel() }
        activeJobs.clear()
    }
}

sealed class DownloadEvent {
    data class Started(val downloadId: String, val title: String) : DownloadEvent()
    data class Progress(val downloadId: String, val progress: Float, val bytesDownloaded: Long, val totalBytes: Long) : DownloadEvent()
    data class Completed(val downloadId: String, val title: String) : DownloadEvent()
    data class Failed(val downloadId: String, val title: String, val error: String) : DownloadEvent()
}
