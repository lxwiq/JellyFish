package com.lowiq.jellyfish.domain.cast

data class CastPlaybackState(
    val isPlaying: Boolean = false,
    val positionMs: Long = 0,
    val durationMs: Long = 0,
    val volume: Float = 1f,
    val playbackSpeed: Float = 1f,
    val currentAudioTrackIndex: Int? = null,
    val currentSubtitleTrackIndex: Int? = null,
    val isBuffering: Boolean = false
)

sealed class CastState {
    data object Disconnected : CastState()
    data object Connecting : CastState()
    data class Connected(
        val device: CastDevice,
        val mediaInfo: CastMediaInfo? = null,
        val playbackState: CastPlaybackState = CastPlaybackState()
    ) : CastState()
    data class Error(val message: String) : CastState()
}
