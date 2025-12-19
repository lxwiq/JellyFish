package com.lowiq.jellyfish.presentation.screens.player

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.lowiq.jellyfish.presentation.screens.player.components.PlayerControls
import com.lowiq.jellyfish.presentation.screens.player.components.PlayerSettingsSheet
import com.lowiq.jellyfish.presentation.screens.player.components.TrackSelectorSheet
import com.lowiq.jellyfish.presentation.theme.LocalJellyFishColors
import org.koin.core.parameter.parametersOf

data class VideoPlayerScreen(
    val itemId: String,
    val title: String,
    val subtitle: String? = null,
    val startPositionMs: Long = 0,
    // Offline playback support
    val offlineFilePath: String? = null,
    val downloadId: String? = null
) : Screen {

    @Composable
    override fun Content() {
        val colors = LocalJellyFishColors.current
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = koinScreenModel<VideoPlayerScreenModel> {
            parametersOf(itemId, title, subtitle, startPositionMs, offlineFilePath, downloadId)
        }
        val state by screenModel.state.collectAsState()

        LaunchedEffect(Unit) {
            screenModel.events.collect { event ->
                when (event) {
                    is VideoPlayerEvent.NavigateBack -> navigator.pop()
                    is VideoPlayerEvent.PlayNextEpisode -> {
                        navigator.replace(
                            VideoPlayerScreen(
                                itemId = event.episodeId,
                                title = "",
                                subtitle = null
                            )
                        )
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            // Video surface (platform-specific)
            VideoSurface(
                videoPlayer = screenModel.videoPlayer,
                modifier = Modifier.fillMaxSize()
            )

            // Loading indicator
            if (state.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = colors.primary)
                }
            }

            // Error message
            state.error?.let { error ->
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = error,
                        color = colors.destructive
                    )
                }
            }

            // Controls overlay
            if (!state.isLoading && state.error == null) {
                PlayerControls(
                    title = state.title,
                    subtitle = state.subtitle,
                    playbackState = state.playbackState,
                    isVisible = state.controlsVisible,
                    onToggleVisibility = screenModel::onToggleControls,
                    onBackClick = screenModel::onBackPressed,
                    onPlayPauseClick = screenModel::onPlayPauseClick,
                    onSeekForward = screenModel::onSeekForward,
                    onSeekBackward = screenModel::onSeekBackward,
                    onSeekTo = screenModel::onSeekTo,
                    onSettingsClick = screenModel::onShowSettings,
                    onAudioSubtitlesClick = screenModel::onShowTrackSelector
                )
            }

            // Resume dialog
            if (state.showResumeDialog) {
                ResumeDialog(
                    positionMs = state.resumePositionMs,
                    onResume = screenModel::onResumeFromPosition,
                    onStartOver = screenModel::onStartFromBeginning
                )
            }

            // Track selector sheet
            if (state.showTrackSelector) {
                TrackSelectorSheet(
                    audioTracks = state.audioTracks,
                    subtitleTracks = state.subtitleTracks,
                    onSelectAudioTrack = screenModel::onSelectAudioTrack,
                    onSelectSubtitleTrack = screenModel::onSelectSubtitleTrack,
                    onDisableSubtitles = screenModel::onDisableSubtitles,
                    onDismiss = screenModel::onHideTrackSelector
                )
            }

            // Settings sheet
            if (state.showSettingsSheet) {
                PlayerSettingsSheet(
                    qualityOptions = state.qualityOptions,
                    currentSpeed = state.playbackSpeed,
                    onSelectQuality = screenModel::onSelectQuality,
                    onSelectSpeed = screenModel::onSetPlaybackSpeed,
                    onDismiss = screenModel::onHideSettings
                )
            }
        }
    }
}

@Composable
private fun ResumeDialog(
    positionMs: Long,
    onResume: () -> Unit,
    onStartOver: () -> Unit
) {
    val colors = LocalJellyFishColors.current
    val formattedTime = formatResumeTime(positionMs)

    AlertDialog(
        onDismissRequest = onStartOver,
        title = { Text("Resume Playback?") },
        text = { Text("Resume at $formattedTime?") },
        confirmButton = {
            TextButton(onClick = onResume) {
                Text("Resume", color = colors.primary)
            }
        },
        dismissButton = {
            TextButton(onClick = onStartOver) {
                Text("From Beginning")
            }
        },
        containerColor = colors.card
    )
}

private fun formatResumeTime(ms: Long): String {
    val totalSeconds = ms / 1000
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60

    return if (hours > 0) {
        "$hours:${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}"
    } else {
        "$minutes:${seconds.toString().padStart(2, '0')}"
    }
}

@Composable
expect fun VideoSurface(
    videoPlayer: com.lowiq.jellyfish.domain.player.VideoPlayer,
    modifier: Modifier
)
