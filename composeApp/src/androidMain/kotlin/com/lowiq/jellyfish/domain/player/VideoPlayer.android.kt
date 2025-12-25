package com.lowiq.jellyfish.domain.player

import android.content.Context
import android.net.Uri
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.videolan.libvlc.LibVLC
import org.videolan.libvlc.Media
import org.videolan.libvlc.MediaPlayer
import org.videolan.libvlc.interfaces.IVLCVout
import org.videolan.libvlc.util.VLCVideoLayout

actual class VideoPlayer(
    private val context: Context
) {
    private var libVLC: LibVLC? = null
    private var mediaPlayer: MediaPlayer? = null
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
    private var videoLayout: VLCVideoLayout? = null

    actual fun initialize() {
        if (libVLC != null) return

        val options = arrayListOf(
            "--aout=opensles",
            "--audio-time-stretch",
            "--network-caching=1500",
            "--file-caching=300",
            "--live-caching=1500",
            "--http-reconnect",
            "--avcodec-skiploopfilter=0"
        )

        libVLC = LibVLC(context, options)
        mediaPlayer = MediaPlayer(libVLC).apply {
            setEventListener { event ->
                when (event.type) {
                    MediaPlayer.Event.Opening -> {
                        _playbackState.value = PlaybackState.Buffering
                    }
                    MediaPlayer.Event.Playing -> {
                        updatePlaybackState()
                        startPositionUpdates()
                    }
                    MediaPlayer.Event.Paused -> {
                        updatePlaybackState()
                    }
                    MediaPlayer.Event.Stopped, MediaPlayer.Event.EndReached -> {
                        positionUpdateJob?.cancel()
                        _playbackState.value = PlaybackState.Idle
                    }
                    MediaPlayer.Event.EncounteredError -> {
                        positionUpdateJob?.cancel()
                        _playbackState.value = PlaybackState.Error(
                            message = "Playback error",
                            canRetry = true
                        )
                    }
                    MediaPlayer.Event.ESAdded, MediaPlayer.Event.ESSelected -> {
                        updateTracks()
                    }
                    MediaPlayer.Event.Buffering -> {
                        if (event.buffering < 100f) {
                            _playbackState.value = PlaybackState.Buffering
                        }
                    }
                }
            }
        }
    }

    actual fun release() {
        positionUpdateJob?.cancel()
        videoLayout?.let { mediaPlayer?.detachViews() }
        mediaPlayer?.release()
        libVLC?.release()
        mediaPlayer = null
        libVLC = null
        videoLayout = null
        _playbackState.value = PlaybackState.Idle
    }

    actual fun play(url: String, headers: Map<String, String>, startPositionMs: Long) {
        val vlc = libVLC ?: return
        val player = mediaPlayer ?: return

        val media = Media(vlc, Uri.parse(url)).apply {
            // Add HTTP headers if present
            headers.forEach { (key, value) ->
                when (key.lowercase()) {
                    "authorization" -> addOption(":http-auth=$value")
                    "user-agent" -> addOption(":http-user-agent=$value")
                    else -> addOption(":http-header=$key: $value")
                }
            }
            // Enable hardware decoding
            setHWDecoderEnabled(true, false)
        }

        player.media = media
        media.release()
        player.play()

        if (startPositionMs > 0) {
            player.time = startPositionMs
        }
    }

    actual fun pause() {
        mediaPlayer?.pause()
    }

    actual fun resume() {
        mediaPlayer?.play()
    }

    actual fun stop() {
        positionUpdateJob?.cancel()
        mediaPlayer?.stop()
        _playbackState.value = PlaybackState.Idle
    }

    actual fun seekTo(positionMs: Long) {
        mediaPlayer?.time = positionMs
    }

    actual fun seekForward(ms: Long) {
        val player = mediaPlayer ?: return
        val newPosition = (player.time + ms).coerceAtMost(player.length)
        player.time = newPosition
    }

    actual fun seekBackward(ms: Long) {
        val player = mediaPlayer ?: return
        val newPosition = (player.time - ms).coerceAtLeast(0)
        player.time = newPosition
    }

    actual fun setPlaybackSpeed(speed: Float) {
        currentPlaybackSpeed = speed
        mediaPlayer?.rate = speed
    }

    actual fun selectAudioTrack(index: Int) {
        val tracks = mediaPlayer?.audioTracks ?: return
        if (index in tracks.indices) {
            mediaPlayer?.audioTrack = tracks[index].id
            updateTracks()
        }
    }

    actual fun selectSubtitleTrack(index: Int) {
        val tracks = mediaPlayer?.spuTracks ?: return
        if (index in tracks.indices) {
            mediaPlayer?.spuTrack = tracks[index].id
            updateTracks()
        }
    }

    actual fun disableSubtitles() {
        mediaPlayer?.spuTrack = -1
        updateTracks()
    }

    actual fun addExternalSubtitle(url: String, name: String?) {
        val player = mediaPlayer ?: return
        try {
            player.addSlave(org.videolan.libvlc.interfaces.IMedia.Slave.Type.Subtitle, url, true)
            // Update tracks after adding external subtitle
            scope.launch {
                delay(500) // Give VLC time to load the subtitle
                updateTracks()
            }
        } catch (e: Exception) {
            // Failed to add subtitle
        }
    }

    actual fun selectQuality(index: Int) {
        // VLC handles quality automatically via adaptive streaming
        // No manual quality selection needed
    }

    fun attachToLayout(layout: VLCVideoLayout) {
        videoLayout = layout
        mediaPlayer?.attachViews(layout, null, false, false)
    }

    fun detachFromLayout() {
        mediaPlayer?.detachViews()
        videoLayout = null
    }

    private fun updatePlaybackState() {
        val player = mediaPlayer ?: return
        val position = player.time.coerceAtLeast(0)
        val duration = player.length.coerceAtLeast(0)

        _playbackState.value = if (player.isPlaying) {
            PlaybackState.Playing(
                positionMs = position,
                durationMs = duration,
                playbackSpeed = currentPlaybackSpeed
            )
        } else {
            PlaybackState.Paused(
                positionMs = position,
                durationMs = duration
            )
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

    private fun updateTracks() {
        val player = mediaPlayer ?: return

        // Audio tracks
        val currentAudioTrack = player.audioTrack
        val audioList = player.audioTracks?.mapIndexed { index, track ->
            AudioTrack(
                index = index,
                language = null,
                label = track.name ?: "Audio ${index + 1}",
                codec = null,
                channels = null,
                isSelected = track.id == currentAudioTrack
            )
        } ?: emptyList()
        _audioTracks.value = audioList

        // Subtitle tracks
        val currentSpuTrack = player.spuTrack
        val subtitleList = player.spuTracks?.mapIndexed { index, track ->
            SubtitleTrack(
                index = index,
                language = null,
                label = track.name ?: "Subtitle ${index + 1}",
                isSelected = track.id == currentSpuTrack
            )
        } ?: emptyList()
        _subtitleTracks.value = subtitleList

        // VLC doesn't expose quality options for HLS in the same way
        _qualityOptions.value = emptyList()
    }
}
