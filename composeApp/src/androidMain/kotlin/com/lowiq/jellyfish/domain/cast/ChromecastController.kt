package com.lowiq.jellyfish.domain.cast

import com.google.android.gms.cast.framework.media.RemoteMediaClient

class ChromecastController(
    private val remoteMediaClient: RemoteMediaClient
) : CastController {

    override fun play() {
        remoteMediaClient.play()
    }

    override fun pause() {
        remoteMediaClient.pause()
    }

    override fun seekTo(positionMs: Long) {
        remoteMediaClient.seek(positionMs)
    }

    override fun setVolume(volume: Float) {
        remoteMediaClient.setStreamVolume(volume.toDouble())
    }

    override fun setPlaybackSpeed(speed: Float) {
        remoteMediaClient.setPlaybackRate(speed.toDouble())
    }

    override fun selectAudioTrack(index: Int) {
        val tracks = remoteMediaClient.mediaStatus?.mediaInfo?.mediaTracks ?: return
        val audioTrack = tracks.getOrNull(index) ?: return
        remoteMediaClient.setActiveMediaTracks(longArrayOf(audioTrack.id))
    }

    override fun selectSubtitleTrack(index: Int) {
        val tracks = remoteMediaClient.mediaStatus?.mediaInfo?.mediaTracks ?: return
        val subtitleTrack = tracks.getOrNull(index) ?: return
        remoteMediaClient.setActiveMediaTracks(longArrayOf(subtitleTrack.id))
    }

    override fun disableSubtitles() {
        remoteMediaClient.setActiveMediaTracks(longArrayOf())
    }

    override fun stop() {
        remoteMediaClient.stop()
    }
}
