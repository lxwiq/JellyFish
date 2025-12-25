package com.lowiq.jellyfish.presentation.screens.player

import android.app.Activity
import android.content.pm.ActivityInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.lowiq.jellyfish.domain.player.VideoPlayer
import org.videolan.libvlc.util.VLCVideoLayout

@Composable
actual fun VideoSurface(
    videoPlayer: VideoPlayer,
    modifier: Modifier
) {
    val context = LocalContext.current

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
            VLCVideoLayout(ctx).also { layout ->
                videoPlayer.attachToLayout(layout)
            }
        },
        update = { _ ->
            // Layout is already attached in factory
        },
        onRelease = { _ ->
            videoPlayer.detachFromLayout()
        },
        modifier = modifier
    )
}
