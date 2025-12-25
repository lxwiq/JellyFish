package com.lowiq.jellyfish.domain.download

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
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

    fun close() {
        scope.cancel()
    }

    private fun observeDownloadEvents() {
        scope.launch {
            downloadManager.downloadEvents.collect { event ->
                when (event) {
                    is DownloadEvent.Started -> {
                        updateState { list ->
                            list + ActiveDownload(event.downloadId, event.title, 0f)
                        }
                    }
                    is DownloadEvent.Progress -> {
                        updateState { list ->
                            list.map { download ->
                                if (download.id == event.downloadId) {
                                    download.copy(progress = event.progress)
                                } else download
                            }
                        }
                    }
                    is DownloadEvent.Completed -> {
                        updateState { list ->
                            list.filter { it.id != event.downloadId }
                        }
                    }
                    is DownloadEvent.Failed -> {
                        updateState { list ->
                            list.filter { it.id != event.downloadId }
                        }
                    }
                }
            }
        }
    }

    private fun updateState(transform: (List<ActiveDownload>) -> List<ActiveDownload>) {
        _activeDownloads.update { list ->
            val newList = transform(list)
            _activeCount.value = newList.size
            _averageProgress.value = if (newList.isEmpty()) 0f
                else newList.map { it.progress }.average().toFloat()
            newList
        }
    }
}
