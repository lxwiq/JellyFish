package com.lowiq.jellyfish.domain.download

expect class DownloadNotifier {
    fun updateProgress(downloadId: String, title: String, progress: Float)
    fun showCompleted(downloadId: String, title: String)
    fun showFailed(downloadId: String, title: String, error: String)
    fun cancel(downloadId: String)
    fun cancelAll()
    fun setForegroundEnabled(enabled: Boolean)
}
