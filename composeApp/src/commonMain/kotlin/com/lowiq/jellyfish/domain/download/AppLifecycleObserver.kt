package com.lowiq.jellyfish.domain.download

expect class AppLifecycleObserver(
    downloadManager: DownloadManager,
    downloadNotifier: DownloadNotifier
) {
    fun start()
}
