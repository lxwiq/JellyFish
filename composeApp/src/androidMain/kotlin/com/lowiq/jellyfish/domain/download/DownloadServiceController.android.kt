package com.lowiq.jellyfish.domain.download

import android.content.Context

actual class DownloadServiceController(
    private val context: Context
) {
    actual fun startService() {
        DownloadForegroundService.start(context)
    }

    actual fun stopService() {
        DownloadForegroundService.stop(context)
    }

    actual fun isServiceRunning(): Boolean {
        return DownloadForegroundService.isServiceRunning()
    }
}
