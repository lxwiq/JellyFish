package com.lowiq.jellyfish.domain.download

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

actual class AppLifecycleObserver actual constructor(
    private val downloadManager: DownloadManager,
    private val downloadNotifier: DownloadNotifier
) : DefaultLifecycleObserver {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private var isObservingEvents = false

    actual fun start() {
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    override fun onStart(owner: LifecycleOwner) {
        // App came to foreground
        downloadNotifier.setForegroundEnabled(false)
        isObservingEvents = false
    }

    override fun onStop(owner: LifecycleOwner) {
        // App went to background
        downloadNotifier.setForegroundEnabled(true)
        if (!isObservingEvents) {
            isObservingEvents = true
            observeDownloadEvents()
        }
    }

    private fun observeDownloadEvents() {
        scope.launch {
            downloadManager.downloadEvents.collect { event ->
                when (event) {
                    is DownloadEvent.Started -> {
                        downloadNotifier.updateProgress(event.downloadId, event.title, 0f)
                    }
                    is DownloadEvent.Progress -> {
                        // We need title - get from active downloads in DownloadStateHolder
                        // For now use empty title - will be improved in Task 10
                        downloadNotifier.updateProgress(event.downloadId, "", event.progress)
                    }
                    is DownloadEvent.Completed -> {
                        downloadNotifier.showCompleted(event.downloadId, event.title)
                    }
                    is DownloadEvent.Failed -> {
                        downloadNotifier.showFailed(event.downloadId, event.title, event.error)
                    }
                }
            }
        }
    }
}
