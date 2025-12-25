package com.lowiq.jellyfish.domain.download

actual class NotificationPermissionHandler {
    /**
     * Desktop/JVM doesn't require runtime notification permissions.
     */
    actual suspend fun requestPermissionIfNeeded(): Boolean {
        return true
    }
}
