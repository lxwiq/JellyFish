package com.lowiq.jellyfish.domain.download

actual class NotificationPermissionHandler {
    /**
     * iOS doesn't require runtime permission for local notifications in the same way Android does.
     * If needed, UNUserNotificationCenter.requestAuthorization would be called here.
     * For now, we return true to indicate no permission barrier exists.
     */
    actual suspend fun requestPermissionIfNeeded(): Boolean {
        return true
    }
}
