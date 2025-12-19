package com.lowiq.jellyfish.presentation.screens.player.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lowiq.jellyfish.presentation.theme.LocalJellyFishColors

@Composable
fun PlayerProgressBar(
    positionMs: Long,
    durationMs: Long,
    onSeekTo: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalJellyFishColors.current
    val progress = if (durationMs > 0) positionMs.toFloat() / durationMs.toFloat() else 0f

    var barWidth by remember { mutableFloatStateOf(0f) }
    var isDragging by remember { mutableStateOf(false) }
    var dragProgress by remember { mutableFloatStateOf(progress) }

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = formatTime(if (isDragging) (dragProgress * durationMs).toLong() else positionMs),
            color = Color.White,
            fontSize = 12.sp
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 12.dp)
                .height(20.dp)
                .onSizeChanged { barWidth = it.width.toFloat() }
                .pointerInput(Unit) {
                    detectTapGestures { offset ->
                        val newProgress = (offset.x / barWidth).coerceIn(0f, 1f)
                        onSeekTo((newProgress * durationMs).toLong())
                    }
                }
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        onDragStart = { isDragging = true },
                        onDragEnd = {
                            onSeekTo((dragProgress * durationMs).toLong())
                            isDragging = false
                        },
                        onDragCancel = { isDragging = false },
                        onHorizontalDrag = { _, dragAmount ->
                            dragProgress = (dragProgress + dragAmount / barWidth).coerceIn(0f, 1f)
                        }
                    )
                },
            contentAlignment = Alignment.CenterStart
        ) {
            // Background track
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(colors.muted)
            )

            // Progress track
            Box(
                modifier = Modifier
                    .fillMaxWidth(if (isDragging) dragProgress else progress)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(colors.primary)
            )
        }

        Text(
            text = formatTime(durationMs),
            color = Color.White,
            fontSize = 12.sp
        )
    }
}

private fun formatTime(ms: Long): String {
    val totalSeconds = ms / 1000
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60

    return if (hours > 0) {
        String.format("%d:%02d:%02d", hours, minutes, seconds)
    } else {
        String.format("%d:%02d", minutes, seconds)
    }
}
