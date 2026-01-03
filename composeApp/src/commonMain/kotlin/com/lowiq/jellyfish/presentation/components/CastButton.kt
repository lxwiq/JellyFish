package com.lowiq.jellyfish.presentation.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cast
import androidx.compose.material.icons.filled.CastConnected
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import com.lowiq.jellyfish.domain.cast.CastDevice
import com.lowiq.jellyfish.domain.cast.CastState

@Composable
fun CastButton(
    castState: CastState,
    availableDevices: List<CastDevice>,
    onShowDevicePicker: () -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "cast_connecting")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "cast_alpha"
    )

    val icon = when (castState) {
        is CastState.Connected -> Icons.Filled.CastConnected
        else -> Icons.Filled.Cast
    }

    val tint = when (castState) {
        is CastState.Connected -> MaterialTheme.colorScheme.primary
        is CastState.Error -> MaterialTheme.colorScheme.error
        else -> MaterialTheme.colorScheme.onSurface
    }

    val buttonAlpha = when (castState) {
        is CastState.Connecting -> alpha
        else -> 1f
    }

    // Always show the button - the picker will show "no devices" if none available
    IconButton(
        onClick = onShowDevicePicker,
        modifier = modifier.alpha(buttonAlpha)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "Cast",
            tint = tint
        )
    }
}
