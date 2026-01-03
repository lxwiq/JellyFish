package com.lowiq.jellyfish.presentation.screens.cast

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.lowiq.jellyfish.domain.cast.CastManager
import com.lowiq.jellyfish.domain.cast.CastMediaInfo
import com.lowiq.jellyfish.domain.cast.CastPlaybackState
import com.lowiq.jellyfish.domain.cast.CastState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class CastControlEvent {
    data object NavigateBack : CastControlEvent()
    data class ResumeLocalPlayback(val positionMs: Long) : CastControlEvent()
}

data class CastControlState(
    val deviceName: String = "",
    val mediaInfo: CastMediaInfo? = null,
    val playbackState: CastPlaybackState = CastPlaybackState(),
    val queue: List<CastMediaInfo> = emptyList(),
    val currentQueueIndex: Int = -1,
    val isConnected: Boolean = false
)

class CastControlScreenModel(
    private val castManager: CastManager
) : ScreenModel {

    private val _events = MutableSharedFlow<CastControlEvent>()
    val events: SharedFlow<CastControlEvent> = _events

    val state: StateFlow<CastControlState> = castManager.castState
        .map { castState ->
            when (castState) {
                is CastState.Connected -> CastControlState(
                    deviceName = castState.device.name,
                    mediaInfo = castState.mediaInfo,
                    playbackState = castState.playbackState,
                    queue = castManager.getQueue(),
                    isConnected = true
                )
                else -> CastControlState()
            }
        }
        .stateIn(
            scope = screenModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = CastControlState()
        )

    fun play() {
        castManager.controller?.play()
    }

    fun pause() {
        castManager.controller?.pause()
    }

    fun seekTo(positionMs: Long) {
        castManager.controller?.seekTo(positionMs)
    }

    fun seekForward() {
        val currentPos = state.value.playbackState.positionMs
        castManager.controller?.seekTo(currentPos + 10_000)
    }

    fun seekBackward() {
        val currentPos = state.value.playbackState.positionMs
        castManager.controller?.seekTo((currentPos - 10_000).coerceAtLeast(0))
    }

    fun setVolume(volume: Float) {
        castManager.controller?.setVolume(volume)
    }

    fun setPlaybackSpeed(speed: Float) {
        castManager.controller?.setPlaybackSpeed(speed)
    }

    fun selectSubtitleTrack(index: Int?) {
        if (index == null) {
            castManager.controller?.disableSubtitles()
        } else {
            castManager.controller?.selectSubtitleTrack(index)
        }
    }

    fun selectAudioTrack(index: Int) {
        castManager.controller?.selectAudioTrack(index)
    }

    fun playNext() {
        castManager.playNext()
    }

    fun playPrevious() {
        castManager.playPrevious()
    }

    fun playQueueItem(index: Int) {
        val queue = castManager.getQueue()
        if (index in queue.indices) {
            screenModelScope.launch {
                castManager.loadMedia(queue[index])
            }
        }
    }

    fun removeFromQueue(index: Int) {
        // Note: Would need to add removeFromQueue to CastManager
    }

    fun stopCasting(resumeLocally: Boolean = false) {
        screenModelScope.launch {
            val currentPosition = state.value.playbackState.positionMs
            castManager.disconnect()

            if (resumeLocally) {
                _events.emit(CastControlEvent.ResumeLocalPlayback(currentPosition))
            } else {
                _events.emit(CastControlEvent.NavigateBack)
            }
        }
    }
}
