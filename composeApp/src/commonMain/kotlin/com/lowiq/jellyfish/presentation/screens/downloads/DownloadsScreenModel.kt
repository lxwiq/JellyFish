package com.lowiq.jellyfish.presentation.screens.downloads

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.lowiq.jellyfish.domain.download.DownloadEvent
import com.lowiq.jellyfish.domain.download.DownloadManager
import com.lowiq.jellyfish.domain.model.Download
import com.lowiq.jellyfish.domain.model.DownloadStatus
import com.lowiq.jellyfish.domain.repository.DownloadRepository
import com.lowiq.jellyfish.domain.repository.StorageInfo
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class DownloadsState(
    val activeDownloads: List<Download> = emptyList(),
    val completedDownloads: List<Download> = emptyList(),
    val storageInfo: StorageInfo? = null,
    val isLoading: Boolean = true,
    val showDeleteAllDialog: Boolean = false,
    val totalDownloadCount: Int = 0,
    val totalDownloadBytes: Long = 0
)

class DownloadsScreenModel(
    private val downloadRepository: DownloadRepository,
    private val downloadManager: DownloadManager
) : ScreenModel {

    private val _state = MutableStateFlow(DownloadsState())
    val state = _state.asStateFlow()

    init {
        loadDownloads()
        observeEvents()
    }

    private fun loadDownloads() {
        screenModelScope.launch {
            // Load ONCE at startup, then rely on events for updates
            val active = downloadRepository.getActiveDownloads().first()
            val completed = downloadRepository.getCompletedDownloads().first()
            val totalCount = active.size + completed.size
            val totalBytes = active.sumOf { it.totalBytes } + completed.sumOf { it.downloadedBytes }
            _state.update {
                it.copy(
                    activeDownloads = active,
                    completedDownloads = completed,
                    isLoading = false,
                    totalDownloadCount = totalCount,
                    totalDownloadBytes = totalBytes
                )
            }
        }

        screenModelScope.launch {
            val info = downloadRepository.getStorageInfo()
            _state.update { it.copy(storageInfo = info) }
        }
    }

    private fun observeEvents() {
        screenModelScope.launch {
            println("[DownloadsScreenModel] observeEvents: Starting to collect events")
            downloadManager.downloadEvents.collect { event ->
                when (event) {
                    is DownloadEvent.Progress -> {
                        val progressPercent = (event.progress * 100).toInt()
                        val currentDownloads = _state.value.activeDownloads
                        val foundDownload = currentDownloads.find { it.id == event.downloadId }

                        if (progressPercent % 10 == 0) {
                            println("[DownloadsScreenModel] Progress: ${event.downloadId} -> $progressPercent%, activeDownloads=${currentDownloads.size}, found=${foundDownload != null}")
                        }

                        if (foundDownload != null) {
                            // Update progress in real-time from events
                            _state.update { currentState ->
                                currentState.copy(
                                    activeDownloads = currentState.activeDownloads.map { download ->
                                        if (download.id == event.downloadId) {
                                            download.copy(
                                                progress = event.progress,
                                                downloadedBytes = event.bytesDownloaded
                                            )
                                        } else download
                                    }
                                )
                            }
                        } else {
                            println("[DownloadsScreenModel] WARNING: Download ${event.downloadId} not in activeDownloads list!")
                        }
                    }
                    is DownloadEvent.Completed -> {
                        println("[DownloadsScreenModel] Completed event: ${event.downloadId}")
                        refreshDownloadLists()
                    }
                    is DownloadEvent.Started -> {
                        println("[DownloadsScreenModel] Started event: ${event.downloadId} - ${event.title}")
                        refreshDownloadLists()
                    }
                    is DownloadEvent.Failed -> {
                        println("[DownloadsScreenModel] Failed event: ${event.downloadId} - ${event.error}")
                        refreshDownloadLists()
                    }
                }
            }
        }
    }

    private fun refreshDownloadLists() {
        screenModelScope.launch {
            val active = downloadRepository.getActiveDownloads().first()
            val completed = downloadRepository.getCompletedDownloads().first()
            val info = downloadRepository.getStorageInfo()
            println("[DownloadsScreenModel] Refreshed lists: ${active.size} active, ${completed.size} completed")
            _state.update {
                it.copy(
                    activeDownloads = active,
                    completedDownloads = completed,
                    storageInfo = info
                )
            }
        }
    }

    private fun refreshStorageInfo() {
        screenModelScope.launch {
            val info = downloadRepository.getStorageInfo()
            _state.update { it.copy(storageInfo = info) }
        }
    }

    fun pauseDownload(downloadId: String) {
        screenModelScope.launch {
            downloadManager.pauseDownload(downloadId)
            downloadRepository.pauseDownload(downloadId)
        }
    }

    fun resumeDownload(downloadId: String) {
        screenModelScope.launch {
            downloadRepository.resumeDownload(downloadId)
        }
    }

    fun cancelDownload(downloadId: String) {
        screenModelScope.launch {
            downloadManager.pauseDownload(downloadId)
            downloadRepository.cancelDownload(downloadId)
        }
    }

    fun deleteDownload(downloadId: String) {
        screenModelScope.launch {
            downloadRepository.deleteDownload(downloadId)
            refreshStorageInfo()
        }
    }

    fun showDeleteAllConfirmation() {
        _state.update { it.copy(showDeleteAllDialog = true) }
    }

    fun hideDeleteAllConfirmation() {
        _state.update { it.copy(showDeleteAllDialog = false) }
    }

    fun deleteAllDownloads() {
        screenModelScope.launch {
            hideDeleteAllConfirmation()
            downloadManager.cancelAll()
            downloadRepository.deleteAllDownloads()
            refreshDownloadLists()
        }
    }
}
