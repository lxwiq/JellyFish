package com.lowiq.jellyfish.presentation.screens.player

import android.app.Activity
import android.content.pm.ActivityInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.ui.PlayerView
import com.lowiq.jellyfish.domain.player.VideoPlayer

@Composable
actual fun VideoSurface(
    videoPlayer: VideoPlayer,
    modifier: Modifier
) {
    val context = LocalContext.current
    val exoPlayer = videoPlayer.getExoPlayer()

    // Force landscape orientation when video player is shown
    DisposableEffect(Unit) {
        val activity = context as? Activity
        val originalOrientation = activity?.requestedOrientation
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE

        onDispose {
            // Restore original orientation when leaving video player
            activity?.requestedOrientation = originalOrientation ?: ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        }
    }

    AndroidView(
        factory = { ctx ->
            PlayerView(ctx).apply {
                player = exoPlayer
                useController = false // We use our own controls
            }
        },
        update = { playerView ->
            playerView.player = exoPlayer
        },
        modifier = modifier
    )
}
