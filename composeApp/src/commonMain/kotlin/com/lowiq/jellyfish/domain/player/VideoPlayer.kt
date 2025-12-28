package com.lowiq.jellyfish.domain.player

import kotlinx.coroutines.flow.StateFlow

expect class VideoPlayer {
    val playbackState: StateFlow<PlaybackState>
    val audioTracks: StateFlow<List<AudioTrack>>
    val subtitleTracks: StateFlow<List<SubtitleTrack>>
    val qualityOptions: StateFlow<List<QualityOption>>

    fun initialize()
    fun release()

    fun play(url: String, headers: Map<String, String> = emptyMap(), startPositionMs: Long = 0)
    fun pause()
    fun resume()
    fun stop()

    fun seekTo(positionMs: Long)
    fun seekForward(ms: Long = 10_000)
    fun seekBackward(ms: Long = 10_000)

    fun setPlaybackSpeed(speed: Float)
    fun selectAudioTrack(index: Int)
    fun selectSubtitleTrack(index: Int)
    fun disableSubtitles()
    fun selectQuality(index: Int)
    fun addExternalSubtitle(url: String, name: String? = null)
    fun setScaleMode(mode: VideoScaleMode)
}
