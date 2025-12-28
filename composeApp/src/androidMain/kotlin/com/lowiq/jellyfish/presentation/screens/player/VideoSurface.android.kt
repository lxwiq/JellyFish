package com.lowiq.jellyfish.presentation.screens.player

import android.app.Activity
import android.content.pm.ActivityInfo
import android.os.Build
import android.view.WindowInsets
import android.view.WindowInsetsController
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

    // Enable immersive mode to hide system bars during video playback
    DisposableEffect(Unit) {
        val activity = context as? Activity
        val window = activity?.window
        val controller = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window?.insetsController
        } else {
            null
        }

        // Hide system bars (status bar + navigation bar)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            controller?.hide(WindowInsets.Type.systemBars())
            controller?.systemBarsBehavior =
                WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }

        onDispose {
            // Restore system bars when leaving video player
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                controller?.show(WindowInsets.Type.systemBars())
            }
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
