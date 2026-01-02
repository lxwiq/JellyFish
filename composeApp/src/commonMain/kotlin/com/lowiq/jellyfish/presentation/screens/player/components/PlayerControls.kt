package com.lowiq.jellyfish.presentation.screens.player.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Forward10
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Replay10
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.AspectRatio
import androidx.compose.material.icons.outlined.Crop
import androidx.compose.material.icons.outlined.FitScreen
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lowiq.jellyfish.domain.player.PlaybackState
import com.lowiq.jellyfish.domain.player.VideoScaleMode
import com.lowiq.jellyfish.presentation.theme.LocalJellyFishColors
import jellyfish.composeapp.generated.resources.Res
import jellyfish.composeapp.generated.resources.common_back
import jellyfish.composeapp.generated.resources.player_audio_subtitles
import jellyfish.composeapp.generated.resources.player_forward
import jellyfish.composeapp.generated.resources.player_pause
import jellyfish.composeapp.generated.resources.player_play
import jellyfish.composeapp.generated.resources.player_rewind
import jellyfish.composeapp.generated.resources.player_scale_mode
import jellyfish.composeapp.generated.resources.player_settings
import org.jetbrains.compose.resources.stringResource

@Composable
fun PlayerControls(
    title: String,
    subtitle: String?,
    playbackState: PlaybackState,
    isVisible: Boolean,
    onToggleVisibility: () -> Unit,
    onBackClick: () -> Unit,
    onPlayPauseClick: () -> Unit,
    onSeekForward: () -> Unit,
    onSeekBackward: () -> Unit,
    onSeekTo: (Long) -> Unit,
    onSettingsClick: () -> Unit,
    onAudioSubtitlesClick: () -> Unit,
    scaleMode: VideoScaleMode,
    onScaleModeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalJellyFishColors.current

    Box(
        modifier = modifier
            .fillMaxSize()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onToggleVisibility
            )
    ) {
        AnimatedVisibility(
            visible = isVisible,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f))
            ) {
                // Top gradient and header
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Black.copy(alpha = 0.7f), Color.Transparent)
                            )
                        )
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(onClick = onBackClick) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = stringResource(Res.string.common_back),
                                    tint = Color.White
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = title,
                                    color = Color.White,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                subtitle?.let {
                                    Text(
                                        text = it,
                                        color = Color.White.copy(alpha = 0.7f),
                                        fontSize = 14.sp
                                    )
                                }
                            }
                        }

                        IconButton(onClick = onSettingsClick) {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = stringResource(Res.string.player_settings),
                                tint = Color.White
                            )
                        }
                    }
                }

                // Center controls
                Row(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalArrangement = Arrangement.spacedBy(32.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onSeekBackward,
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Replay10,
                            contentDescription = stringResource(Res.string.player_rewind),
                            tint = Color.White,
                            modifier = Modifier.size(36.dp)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(colors.primary)
                            .clickable(onClick = onPlayPauseClick),
                        contentAlignment = Alignment.Center
                    ) {
                        when (playbackState) {
                            is PlaybackState.Buffering -> {
                                CircularProgressIndicator(
                                    color = colors.primaryForeground,
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                            is PlaybackState.Playing -> {
                                Icon(
                                    imageVector = Icons.Default.Pause,
                                    contentDescription = stringResource(Res.string.player_pause),
                                    tint = colors.primaryForeground,
                                    modifier = Modifier.size(40.dp)
                                )
                            }
                            else -> {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = stringResource(Res.string.player_play),
                                    tint = colors.primaryForeground,
                                    modifier = Modifier.size(40.dp)
                                )
                            }
                        }
                    }

                    IconButton(
                        onClick = onSeekForward,
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Forward10,
                            contentDescription = stringResource(Res.string.player_forward),
                            tint = Color.White,
                            modifier = Modifier.size(36.dp)
                        )
                    }
                }

                // Bottom controls
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f))
                            )
                        )
                        .padding(16.dp)
                ) {
                    // Progress bar
                    val (positionMs, durationMs) = when (playbackState) {
                        is PlaybackState.Playing -> playbackState.positionMs to playbackState.durationMs
                        is PlaybackState.Paused -> playbackState.positionMs to playbackState.durationMs
                        else -> 0L to 0L
                    }

                    PlayerProgressBar(
                        positionMs = positionMs,
                        durationMs = durationMs,
                        onSeekTo = onSeekTo
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Action buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedButton(
                            onClick = onAudioSubtitlesClick,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(stringResource(Res.string.player_audio_subtitles), color = Color.White)
                        }

                        // Scale mode button
                        IconButton(onClick = onScaleModeClick) {
                            Icon(
                                imageVector = when (scaleMode) {
                                    VideoScaleMode.FIT -> Icons.Outlined.FitScreen
                                    VideoScaleMode.FILL -> Icons.Outlined.Crop
                                    VideoScaleMode.STRETCH -> Icons.Outlined.AspectRatio
                                },
                                contentDescription = stringResource(Res.string.player_scale_mode, scaleMode.displayName),
                                tint = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}
