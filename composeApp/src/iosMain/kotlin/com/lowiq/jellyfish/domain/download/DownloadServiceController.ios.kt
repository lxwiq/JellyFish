package com.lowiq.jellyfish.domain.download

actual class DownloadServiceController {
    actual fun startService() {
        // iOS doesn't need a foreground service - background tasks are handled differently
    }

    actual fun stopService() {
        // No-op
    }

    actual fun isServiceRunning(): Boolean {
        return false
    }
}
