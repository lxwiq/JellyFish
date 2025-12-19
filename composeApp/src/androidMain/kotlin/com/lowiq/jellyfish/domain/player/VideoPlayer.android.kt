package com.lowiq.jellyfish.domain.player

import android.content.Context
import androidx.annotation.OptIn
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.TrackSelectionOverride
import androidx.media3.common.Tracks
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.hls.HlsMediaSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

@OptIn(UnstableApi::class)
actual class VideoPlayer(
    private val context: Context
) {
    private var exoPlayer: ExoPlayer? = null
    private val scope = CoroutineScope(Dispatchers.Main)
    private var positionUpdateJob: Job? = null

    private val _playbackState = MutableStateFlow<PlaybackState>(PlaybackState.Idle)
    actual val playbackState: StateFlow<PlaybackState> = _playbackState.asStateFlow()

    private val _audioTracks = MutableStateFlow<List<AudioTrack>>(emptyList())
    actual val audioTracks: StateFlow<List<AudioTrack>> = _audioTracks.asStateFlow()

    private val _subtitleTracks = MutableStateFlow<List<SubtitleTrack>>(emptyList())
    actual val subtitleTracks: StateFlow<List<SubtitleTrack>> = _subtitleTracks.asStateFlow()

    private val _qualityOptions = MutableStateFlow<List<QualityOption>>(emptyList())
    actual val qualityOptions: StateFlow<List<QualityOption>> = _qualityOptions.asStateFlow()

    private var currentPlaybackSpeed = 1f

    actual fun initialize() {
        if (exoPlayer != null) return

        exoPlayer = ExoPlayer.Builder(context).build().apply {
            addListener(object : Player.Listener {
                override fun onPlaybackStateChanged(state: Int) {
                    updatePlaybackState()
                }

                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    updatePlaybackState()
                }

                override fun onPlayerError(error: PlaybackException) {
                    _playbackState.value = PlaybackState.Error(
                        message = error.message ?: "Playback error",
                        canRetry = true
                    )
                }

                override fun onTracksChanged(tracks: Tracks) {
                    updateTracks(tracks)
                }
            })
        }
    }

    actual fun release() {
        positionUpdateJob?.cancel()
        exoPlayer?.release()
        exoPlayer = null
        _playbackState.value = PlaybackState.Idle
    }

    actual fun play(url: String, headers: Map<String, String>, startPositionMs: Long) {
        val player = exoPlayer ?: return

        val dataSourceFactory = DefaultHttpDataSource.Factory()
            .setDefaultRequestProperties(headers)

        val mediaSource = if (url.contains(".m3u8")) {
            HlsMediaSource.Factory(dataSourceFactory)
                .createMediaSource(MediaItem.fromUri(url))
        } else {
            ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(MediaItem.fromUri(url))
        }

        player.setMediaSource(mediaSource)
        player.prepare()

        if (startPositionMs > 0) {
            player.seekTo(startPositionMs)
        }

        player.play()
        startPositionUpdates()
    }

    actual fun pause() {
        exoPlayer?.pause()
    }

    actual fun resume() {
        exoPlayer?.play()
    }

    actual fun stop() {
        positionUpdateJob?.cancel()
        exoPlayer?.stop()
        _playbackState.value = PlaybackState.Idle
    }

    actual fun seekTo(positionMs: Long) {
        exoPlayer?.seekTo(positionMs)
    }

    actual fun seekForward(ms: Long) {
        val player = exoPlayer ?: return
        val newPosition = (player.currentPosition + ms).coerceAtMost(player.duration)
        player.seekTo(newPosition)
    }

    actual fun seekBackward(ms: Long) {
        val player = exoPlayer ?: return
        val newPosition = (player.currentPosition - ms).coerceAtLeast(0)
        player.seekTo(newPosition)
    }

    actual fun setPlaybackSpeed(speed: Float) {
        currentPlaybackSpeed = speed
        exoPlayer?.setPlaybackSpeed(speed)
    }

    actual fun selectAudioTrack(index: Int) {
        val player = exoPlayer ?: return
        val tracks = player.currentTracks

        tracks.groups.forEachIndexed { groupIndex, group ->
            if (group.type == C.TRACK_TYPE_AUDIO) {
                for (i in 0 until group.length) {
                    if (i == index) {
                        player.trackSelectionParameters = player.trackSelectionParameters
                            .buildUpon()
                            .setOverrideForType(
                                TrackSelectionOverride(group.mediaTrackGroup, listOf(i))
                            )
                            .build()
                        updateTracks(player.currentTracks)
                        return
                    }
                }
            }
        }
    }

    actual fun selectSubtitleTrack(index: Int) {
        val player = exoPlayer ?: return
        val tracks = player.currentTracks

        tracks.groups.forEachIndexed { groupIndex, group ->
            if (group.type == C.TRACK_TYPE_TEXT) {
                for (i in 0 until group.length) {
                    if (i == index) {
                        player.trackSelectionParameters = player.trackSelectionParameters
                            .buildUpon()
                            .setOverrideForType(
                                TrackSelectionOverride(group.mediaTrackGroup, listOf(i))
                            )
                            .build()
                        updateTracks(player.currentTracks)
                        return
                    }
                }
            }
        }
    }

    actual fun disableSubtitles() {
        val player = exoPlayer ?: return
        player.trackSelectionParameters = player.trackSelectionParameters
            .buildUpon()
            .setTrackTypeDisabled(C.TRACK_TYPE_TEXT, true)
            .build()
        updateTracks(player.currentTracks)
    }

    actual fun selectQuality(index: Int) {
        val player = exoPlayer ?: return
        val tracks = player.currentTracks

        tracks.groups.forEachIndexed { groupIndex, group ->
            if (group.type == C.TRACK_TYPE_VIDEO) {
                for (i in 0 until group.length) {
                    if (i == index) {
                        player.trackSelectionParameters = player.trackSelectionParameters
                            .buildUpon()
                            .setOverrideForType(
                                TrackSelectionOverride(group.mediaTrackGroup, listOf(i))
                            )
                            .build()
                        return
                    }
                }
            }
        }
    }

    fun getExoPlayer(): ExoPlayer? = exoPlayer

    private fun updatePlaybackState() {
        val player = exoPlayer ?: return

        _playbackState.value = when {
            player.playbackState == Player.STATE_BUFFERING -> PlaybackState.Buffering
            player.playbackState == Player.STATE_ENDED -> PlaybackState.Paused(
                positionMs = player.duration,
                durationMs = player.duration
            )
            player.isPlaying -> PlaybackState.Playing(
                positionMs = player.currentPosition,
                durationMs = player.duration.coerceAtLeast(0),
                playbackSpeed = currentPlaybackSpeed
            )
            player.playbackState == Player.STATE_READY -> PlaybackState.Paused(
                positionMs = player.currentPosition,
                durationMs = player.duration.coerceAtLeast(0)
            )
            else -> PlaybackState.Idle
        }
    }

    private fun startPositionUpdates() {
        positionUpdateJob?.cancel()
        positionUpdateJob = scope.launch {
            while (isActive) {
                updatePlaybackState()
                delay(1000)
            }
        }
    }

    private fun updateTracks(tracks: Tracks) {
        val audioList = mutableListOf<AudioTrack>()
        val subtitleList = mutableListOf<SubtitleTrack>()
        val qualityList = mutableListOf<QualityOption>()

        var audioIndex = 0
        var subtitleIndex = 0
        var videoIndex = 0

        tracks.groups.forEach { group ->
            when (group.type) {
                C.TRACK_TYPE_AUDIO -> {
                    for (i in 0 until group.length) {
                        val format = group.getTrackFormat(i)
                        audioList.add(
                            AudioTrack(
                                index = audioIndex++,
                                language = format.language,
                                label = format.label ?: format.language ?: "Track ${audioIndex}",
                                codec = format.codecs,
                                channels = format.channelCount,
                                isSelected = group.isTrackSelected(i)
                            )
                        )
                    }
                }
                C.TRACK_TYPE_TEXT -> {
                    for (i in 0 until group.length) {
                        val format = group.getTrackFormat(i)
                        subtitleList.add(
                            SubtitleTrack(
                                index = subtitleIndex++,
                                language = format.language,
                                label = format.label ?: format.language ?: "Subtitle ${subtitleIndex}",
                                isSelected = group.isTrackSelected(i)
                            )
                        )
                    }
                }
                C.TRACK_TYPE_VIDEO -> {
                    for (i in 0 until group.length) {
                        val format = group.getTrackFormat(i)
                        val height = format.height
                        val label = when {
                            height >= 2160 -> "4K"
                            height >= 1080 -> "1080p"
                            height >= 720 -> "720p"
                            height >= 480 -> "480p"
                            else -> "${height}p"
                        }
                        qualityList.add(
                            QualityOption(
                                index = videoIndex++,
                                label = label,
                                bitrate = format.bitrate.toLong(),
                                isSelected = group.isTrackSelected(i)
                            )
                        )
                    }
                }
            }
        }

        _audioTracks.value = audioList
        _subtitleTracks.value = subtitleList
        _qualityOptions.value = qualityList
    }
}
