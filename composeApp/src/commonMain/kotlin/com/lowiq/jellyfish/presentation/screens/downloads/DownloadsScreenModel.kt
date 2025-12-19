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
    val isLoading: Boolean = true
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
            combine(
                downloadRepository.getActiveDownloads(),
                downloadRepository.getCompletedDownloads()
            ) { active, completed ->
                _state.update {
                    it.copy(
                        activeDownloads = active,
                        completedDownloads = completed,
                        isLoading = false
                    )
                }
            }.collect()
        }

        screenModelScope.launch {
            val info = downloadRepository.getStorageInfo()
            _state.update { it.copy(storageInfo = info) }
        }
    }

    private fun observeEvents() {
        screenModelScope.launch {
            downloadManager.downloadEvents.collect { event ->
                when (event) {
                    is DownloadEvent.Completed -> refreshStorageInfo()
                    else -> {}
                }
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
}
