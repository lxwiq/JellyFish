package com.lowiq.jellyfish.presentation.screens.cast

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CastConnected
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.lowiq.jellyfish.presentation.screens.cast.components.CastMediaArtwork
import com.lowiq.jellyfish.presentation.screens.cast.components.CastPlaybackControls
import com.lowiq.jellyfish.presentation.screens.cast.components.CastQueueView
import com.lowiq.jellyfish.presentation.screens.cast.components.CastTrackSelector
import kotlinx.coroutines.flow.collectLatest

class CastControlScreen : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = koinScreenModel<CastControlScreenModel>()
        val state by screenModel.state.collectAsState()

        LaunchedEffect(Unit) {
            screenModel.events.collectLatest { event ->
                when (event) {
                    is CastControlEvent.NavigateBack -> navigator.pop()
                    is CastControlEvent.ResumeLocalPlayback -> {
                        navigator.pop()
                    }
                }
            }
        }

        LaunchedEffect(state.isConnected) {
            if (!state.isConnected && state.deviceName.isEmpty()) {
                navigator.pop()
            }
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text("Cast Control")
                    },
                    navigationIcon = {
                        IconButton(onClick = { navigator.pop() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    },
                    actions = {
                        Icon(
                            imageVector = Icons.Filled.CastConnected,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        TextButton(onClick = { screenModel.stopCasting() }) {
                            Text("Disconnect")
                        }
                    }
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
            ) {
                CastMediaArtwork(
                    mediaInfo = state.mediaInfo,
                    deviceName = state.deviceName
                )

                Spacer(modifier = Modifier.height(24.dp))

                CastPlaybackControls(
                    playbackState = state.playbackState,
                    hasQueue = state.queue.size > 1,
                    onPlay = screenModel::play,
                    onPause = screenModel::pause,
                    onSeek = screenModel::seekTo,
                    onSeekForward = screenModel::seekForward,
                    onSeekBackward = screenModel::seekBackward,
                    onVolumeChange = screenModel::setVolume,
                    onPlayNext = screenModel::playNext,
                    onPlayPrevious = screenModel::playPrevious
                )

                Spacer(modifier = Modifier.height(24.dp))

                state.mediaInfo?.let { mediaInfo ->
                    CastTrackSelector(
                        subtitleTracks = mediaInfo.subtitleTracks,
                        audioTracks = mediaInfo.audioTracks,
                        currentSubtitleIndex = state.playbackState.currentSubtitleTrackIndex,
                        currentAudioIndex = state.playbackState.currentAudioTrackIndex,
                        currentSpeed = state.playbackState.playbackSpeed,
                        onSubtitleSelected = screenModel::selectSubtitleTrack,
                        onAudioSelected = screenModel::selectAudioTrack,
                        onSpeedSelected = screenModel::setPlaybackSpeed
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                CastQueueView(
                    queue = state.queue,
                    currentIndex = state.currentQueueIndex,
                    onItemClick = screenModel::playQueueItem,
                    onRemoveItem = screenModel::removeFromQueue
                )

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}
