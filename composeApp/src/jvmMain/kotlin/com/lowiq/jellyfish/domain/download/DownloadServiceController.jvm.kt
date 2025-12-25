package com.lowiq.jellyfish.domain.download

actual class DownloadServiceController {
    actual fun startService() {
        // Desktop doesn't need a foreground service
    }

    actual fun stopService() {
        // No-op
    }

    actual fun isServiceRunning(): Boolean {
        return false
    }
}
