package com.lowiq.jellyfish.domain.download

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ActiveDownload(
    val id: String,
    val title: String,
    val progress: Float
)

class DownloadStateHolder(
    private val downloadManager: DownloadManager
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val _activeDownloads = MutableStateFlow<List<ActiveDownload>>(emptyList())
    val activeDownloads: StateFlow<List<ActiveDownload>> = _activeDownloads.asStateFlow()

    private val _activeCount = MutableStateFlow(0)
    val activeCount: StateFlow<Int> = _activeCount.asStateFlow()

    private val _averageProgress = MutableStateFlow(0f)
    val averageProgress: StateFlow<Float> = _averageProgress.asStateFlow()

    init {
        observeDownloadEvents()
    }

    private fun observeDownloadEvents() {
        scope.launch {
            downloadManager.downloadEvents.collect { event ->
                when (event) {
                    is DownloadEvent.Started -> {
                        _activeDownloads.update { list ->
                            list + ActiveDownload(event.downloadId, event.title, 0f)
                        }
                        updateDerivedState()
                    }
                    is DownloadEvent.Progress -> {
                        _activeDownloads.update { list ->
                            list.map { download ->
                                if (download.id == event.downloadId) {
                                    download.copy(progress = event.progress)
                                } else download
                            }
                        }
                        updateDerivedState()
                    }
                    is DownloadEvent.Completed, is DownloadEvent.Failed -> {
                        val downloadId = when (event) {
                            is DownloadEvent.Completed -> event.downloadId
                            is DownloadEvent.Failed -> event.downloadId
                            else -> return@collect
                        }
                        _activeDownloads.update { list ->
                            list.filter { it.id != downloadId }
                        }
                        updateDerivedState()
                    }
                }
            }
        }
    }

    private fun updateDerivedState() {
        val downloads = _activeDownloads.value
        _activeCount.value = downloads.size
        _averageProgress.value = if (downloads.isEmpty()) 0f
            else downloads.map { it.progress }.average().toFloat()
    }
}
