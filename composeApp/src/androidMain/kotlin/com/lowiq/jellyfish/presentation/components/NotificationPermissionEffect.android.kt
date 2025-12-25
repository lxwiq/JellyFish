package com.lowiq.jellyfish.presentation.components

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.lowiq.jellyfish.domain.download.NotificationPermissionHandler

@Composable
actual fun NotificationPermissionEffect(handler: NotificationPermissionHandler) {
    // Only needed on Android 13+ (API 33+)
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
        return
    }

    val shouldRequest by handler.shouldRequestPermission.collectAsState()

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        handler.onPermissionResult(granted)
    }

    LaunchedEffect(shouldRequest) {
        if (shouldRequest) {
            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }
}
