package com.lowiq.jellyfish.domain.player

sealed class PlaybackState {
    data object Idle : PlaybackState()
    data object Buffering : PlaybackState()
    data class Playing(
        val positionMs: Long,
        val durationMs: Long,
        val playbackSpeed: Float = 1f
    ) : PlaybackState()
    data class Paused(
        val positionMs: Long,
        val durationMs: Long
    ) : PlaybackState()
    data class Error(
        val message: String,
        val canRetry: Boolean = true
    ) : PlaybackState()
}
