package com.lowiq.jellyfish.domain.download

import android.Manifest
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first

actual class NotificationPermissionHandler(
    private val context: Context
) {
    private val prefs: SharedPreferences = context.getSharedPreferences(
        PREFS_NAME, Context.MODE_PRIVATE
    )

    private val _shouldRequestPermission = MutableStateFlow(false)
    val shouldRequestPermission: StateFlow<Boolean> = _shouldRequestPermission.asStateFlow()

    private val _permissionResult = MutableStateFlow<Boolean?>(null)

    companion object {
        private const val PREFS_NAME = "notification_permission"
        private const val KEY_ALREADY_ASKED = "already_asked"
    }

    actual suspend fun requestPermissionIfNeeded(): Boolean {
        // No runtime permission needed before Android 13 (API 33)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            return true
        }

        // Check if already granted
        if (isPermissionGranted()) {
            return true
        }

        // Check if we already asked before - don't annoy user
        if (hasAlreadyAsked()) {
            return false
        }

        // Mark that we're about to ask
        markAsAsked()

        // Reset the result and trigger the permission request
        _permissionResult.value = null
        _shouldRequestPermission.value = true

        // Wait for the result from the UI
        val result = _permissionResult.first { it != null } ?: false

        // Reset the request flag
        _shouldRequestPermission.value = false

        return result
    }

    /**
     * Called from the Composable when permission result is received.
     */
    fun onPermissionResult(granted: Boolean) {
        _permissionResult.value = granted
    }

    /**
     * Checks if notification permission is currently granted.
     */
    fun isPermissionGranted(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }

    private fun hasAlreadyAsked(): Boolean {
        return prefs.getBoolean(KEY_ALREADY_ASKED, false)
    }

    private fun markAsAsked() {
        prefs.edit().putBoolean(KEY_ALREADY_ASKED, true).apply()
    }
}
