package com.lowiq.jellyfish.domain.download

expect class NotificationPermissionHandler {
    /**
     * Request notification permission if needed.
     * Returns true if permission is granted or not needed on this platform.
     * Returns false if permission was denied.
     *
     * This method only requests permission once - subsequent calls return immediately
     * based on the current permission state.
     */
    suspend fun requestPermissionIfNeeded(): Boolean
}
