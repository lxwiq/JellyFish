package com.lowiq.jellyfish.domain.cast

interface CastController {
    fun play()
    fun pause()
    fun seekTo(positionMs: Long)
    fun setVolume(volume: Float)
    fun setPlaybackSpeed(speed: Float)
    fun selectAudioTrack(index: Int)
    fun selectSubtitleTrack(index: Int)
    fun disableSubtitles()
    fun stop()
}
