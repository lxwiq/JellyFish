package com.lowiq.jellyfish.domain.download

actual class AppLifecycleObserver actual constructor(
    private val downloadManager: DownloadManager,
    private val downloadNotifier: DownloadNotifier
) {
    actual fun start() {}
}
