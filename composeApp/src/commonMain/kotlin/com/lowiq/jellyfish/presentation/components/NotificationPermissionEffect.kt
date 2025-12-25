package com.lowiq.jellyfish.presentation.components

import androidx.compose.runtime.Composable
import com.lowiq.jellyfish.domain.download.NotificationPermissionHandler

/**
 * A platform-specific effect that handles notification permission requests.
 * On Android 13+, this will show the system permission dialog when the handler requests it.
 * On iOS and Desktop, this is a no-op.
 */
@Composable
expect fun NotificationPermissionEffect(handler: NotificationPermissionHandler)
