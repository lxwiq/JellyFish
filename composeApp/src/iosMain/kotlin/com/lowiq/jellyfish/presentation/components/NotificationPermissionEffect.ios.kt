package com.lowiq.jellyfish.presentation.components

import androidx.compose.runtime.Composable
import com.lowiq.jellyfish.domain.download.NotificationPermissionHandler

@Composable
actual fun NotificationPermissionEffect(handler: NotificationPermissionHandler) {
    // No-op on iOS - no runtime permission needed for local notifications
}
