package com.lowiq.jellyfish.domain.player

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

actual class VideoPlayer {
    private val _playbackState = MutableStateFlow<PlaybackState>(PlaybackState.Idle)
    actual val playbackState: StateFlow<PlaybackState> = _playbackState

    private val _audioTracks = MutableStateFlow<List<AudioTrack>>(emptyList())
    actual val audioTracks: StateFlow<List<AudioTrack>> = _audioTracks

    private val _subtitleTracks = MutableStateFlow<List<SubtitleTrack>>(emptyList())
    actual val subtitleTracks: StateFlow<List<SubtitleTrack>> = _subtitleTracks

    private val _qualityOptions = MutableStateFlow<List<QualityOption>>(emptyList())
    actual val qualityOptions: StateFlow<List<QualityOption>> = _qualityOptions

    actual fun initialize() {
        // TODO: Implement with AVPlayer
    }

    actual fun release() {}

    actual fun play(url: String, headers: Map<String, String>, startPositionMs: Long) {
        _playbackState.value = PlaybackState.Error("iOS playback not yet implemented", canRetry = false)
    }

    actual fun pause() {}
    actual fun resume() {}
    actual fun stop() {}
    actual fun seekTo(positionMs: Long) {}
    actual fun seekForward(ms: Long) {}
    actual fun seekBackward(ms: Long) {}
    actual fun setPlaybackSpeed(speed: Float) {}
    actual fun selectAudioTrack(index: Int) {}
    actual fun selectSubtitleTrack(index: Int) {}
    actual fun disableSubtitles() {}
    actual fun addExternalSubtitle(url: String, name: String?) {}
    actual fun selectQuality(index: Int) {}
}
