package com.lowiq.jellyfish.domain.download

actual class DownloadNotifier {
    actual fun updateProgress(downloadId: String, title: String, progress: Float) {}
    actual fun showCompleted(downloadId: String, title: String) {}
    actual fun showFailed(downloadId: String, title: String, error: String) {}
    actual fun cancel(downloadId: String) {}
    actual fun cancelAll() {}
    actual fun setForegroundEnabled(enabled: Boolean) {}
}
