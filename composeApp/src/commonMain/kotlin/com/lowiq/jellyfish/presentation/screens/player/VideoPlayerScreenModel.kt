package com.lowiq.jellyfish.presentation.screens.player

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.lowiq.jellyfish.data.remote.PlaybackProgressInfo
import com.lowiq.jellyfish.domain.player.PlaybackState
import com.lowiq.jellyfish.domain.player.VideoPlayer
import com.lowiq.jellyfish.domain.repository.MediaRepository
import com.lowiq.jellyfish.domain.repository.ServerRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

sealed class VideoPlayerEvent {
    data object NavigateBack : VideoPlayerEvent()
    data class PlayNextEpisode(val episodeId: String) : VideoPlayerEvent()
}

class VideoPlayerScreenModel(
    private val itemId: String,
    private val itemTitle: String,
    private val itemSubtitle: String?,
    private val startPositionMs: Long,
    val videoPlayer: VideoPlayer,
    private val serverRepository: ServerRepository,
    private val mediaRepository: MediaRepository
) : ScreenModel {

    private val _state = MutableStateFlow(VideoPlayerState(
        title = itemTitle,
        subtitle = itemSubtitle,
        resumePositionMs = startPositionMs
    ))
    val state = _state.asStateFlow()

    private val _events = MutableSharedFlow<VideoPlayerEvent>()
    val events = _events.asSharedFlow()

    private var currentServerId: String? = null
    private var mediaSourceId: String? = null
    private var playSessionId: String? = null
    private var progressReportJob: Job? = null
    private var controlsHideJob: Job? = null
    private var streamUrl: String? = null
    private var streamHeaders: Map<String, String> = emptyMap()

    init {
        videoPlayer.initialize()
        observePlaybackState()
        observeTracks()
        loadStreamInfo()
    }

    private fun observePlaybackState() {
        screenModelScope.launch {
            videoPlayer.playbackState.collect { playbackState ->
                _state.update { it.copy(playbackState = playbackState) }

                // Check for next episode notification (30s before end)
                if (playbackState is PlaybackState.Playing) {
                    val remainingMs = playbackState.durationMs - playbackState.positionMs
                    if (remainingMs in 1..30_000 && _state.value.nextEpisode != null) {
                        _state.update { it.copy(showNextEpisodeCard = true) }
                    }
                }
            }
        }
    }

    private fun observeTracks() {
        screenModelScope.launch {
            videoPlayer.audioTracks.collect { tracks ->
                _state.update { it.copy(audioTracks = tracks) }
            }
        }
        screenModelScope.launch {
            videoPlayer.subtitleTracks.collect { tracks ->
                _state.update { it.copy(subtitleTracks = tracks) }
            }
        }
        screenModelScope.launch {
            videoPlayer.qualityOptions.collect { options ->
                _state.update { it.copy(qualityOptions = options) }
            }
        }
    }

    private fun loadStreamInfo() {
        screenModelScope.launch {
            val server = serverRepository.getActiveServer()
                .filterNotNull()
                .first()
            currentServerId = server.id

            mediaRepository.getStreamInfo(server.id, itemId)
                .onSuccess { streamInfo ->
                    mediaSourceId = streamInfo.mediaSourceId
                    playSessionId = streamInfo.playSessionId

                    streamUrl = if (streamInfo.supportsDirectPlay) {
                        streamInfo.directPlayUrl
                    } else {
                        streamInfo.transcodingUrl ?: streamInfo.directPlayUrl
                    }

                    streamHeaders = mapOf(
                        "X-Emby-Token" to (server.token ?: "")
                    )

                    _state.update { it.copy(isLoading = false) }

                    // Show resume dialog if we have a start position
                    if (startPositionMs > 0) {
                        _state.update { it.copy(showResumeDialog = true) }
                    } else {
                        startPlayback(streamUrl!!, streamHeaders, 0)
                    }
                }
                .onFailure { e ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = e.message ?: "Failed to load stream info"
                        )
                    }
                }
        }
    }

    fun onResumeFromPosition() {
        _state.update { it.copy(showResumeDialog = false) }
        streamUrl?.let { url ->
            startPlayback(url, streamHeaders, startPositionMs)
        }
    }

    fun onStartFromBeginning() {
        _state.update { it.copy(showResumeDialog = false) }
        streamUrl?.let { url ->
            startPlayback(url, streamHeaders, 0)
        }
    }

    private fun startPlayback(url: String, headers: Map<String, String>, positionMs: Long) {
        videoPlayer.play(url, headers, positionMs)
        reportPlaybackStart()
        startProgressReporting()
    }

    private fun reportPlaybackStart() {
        val serverId = currentServerId ?: return
        val sourceId = mediaSourceId ?: return
        val sessionId = playSessionId ?: return

        screenModelScope.launch {
            mediaRepository.reportPlaybackStart(serverId, itemId, sourceId, sessionId)
        }
    }

    private fun startProgressReporting() {
        progressReportJob?.cancel()
        progressReportJob = screenModelScope.launch {
            while (isActive) {
                delay(10_000) // Report every 10 seconds
                reportProgress()
            }
        }
    }

    private fun reportProgress() {
        val serverId = currentServerId ?: return
        val sourceId = mediaSourceId ?: return
        val sessionId = playSessionId ?: return
        val state = _state.value.playbackState

        val (positionMs, isPaused) = when (state) {
            is PlaybackState.Playing -> state.positionMs to false
            is PlaybackState.Paused -> state.positionMs to true
            else -> return
        }

        screenModelScope.launch {
            mediaRepository.reportPlaybackProgress(
                serverId,
                PlaybackProgressInfo(
                    itemId = itemId,
                    mediaSourceId = sourceId,
                    positionTicks = positionMs * 10_000, // Convert ms to ticks
                    isPaused = isPaused,
                    playSessionId = sessionId
                )
            )
        }
    }

    fun onPlayPauseClick() {
        when (_state.value.playbackState) {
            is PlaybackState.Playing -> videoPlayer.pause()
            is PlaybackState.Paused -> videoPlayer.resume()
            else -> {}
        }
        resetControlsHideTimer()
    }

    fun onSeekTo(positionMs: Long) {
        videoPlayer.seekTo(positionMs)
        resetControlsHideTimer()
    }

    fun onSeekForward() {
        videoPlayer.seekForward()
        resetControlsHideTimer()
    }

    fun onSeekBackward() {
        videoPlayer.seekBackward()
        resetControlsHideTimer()
    }

    fun onSetPlaybackSpeed(speed: Float) {
        videoPlayer.setPlaybackSpeed(speed)
        _state.update { it.copy(playbackSpeed = speed) }
    }

    fun onSelectAudioTrack(index: Int) {
        videoPlayer.selectAudioTrack(index)
    }

    fun onSelectSubtitleTrack(index: Int) {
        videoPlayer.selectSubtitleTrack(index)
    }

    fun onDisableSubtitles() {
        videoPlayer.disableSubtitles()
    }

    fun onSelectQuality(index: Int) {
        videoPlayer.selectQuality(index)
    }

    fun onToggleControls() {
        _state.update { it.copy(controlsVisible = !it.controlsVisible) }
        if (_state.value.controlsVisible) {
            resetControlsHideTimer()
        }
    }

    fun onShowTrackSelector() {
        _state.update { it.copy(showTrackSelector = true) }
    }

    fun onHideTrackSelector() {
        _state.update { it.copy(showTrackSelector = false) }
    }

    fun onShowSettings() {
        _state.update { it.copy(showSettingsSheet = true) }
    }

    fun onHideSettings() {
        _state.update { it.copy(showSettingsSheet = false) }
    }

    fun onBackPressed() {
        stopPlayback()
        screenModelScope.launch {
            _events.emit(VideoPlayerEvent.NavigateBack)
        }
    }

    fun onPlayNextEpisode() {
        val nextEp = _state.value.nextEpisode ?: return
        stopPlayback()
        screenModelScope.launch {
            _events.emit(VideoPlayerEvent.PlayNextEpisode(nextEp.id))
        }
    }

    fun onDismissNextEpisodeCard() {
        _state.update { it.copy(showNextEpisodeCard = false) }
    }

    private fun resetControlsHideTimer() {
        controlsHideJob?.cancel()
        controlsHideJob = screenModelScope.launch {
            delay(4000)
            _state.update { it.copy(controlsVisible = false) }
        }
    }

    private fun stopPlayback() {
        progressReportJob?.cancel()

        val serverId = currentServerId ?: return
        val sourceId = mediaSourceId ?: return
        val sessionId = playSessionId ?: return
        val state = _state.value.playbackState

        val positionMs = when (state) {
            is PlaybackState.Playing -> state.positionMs
            is PlaybackState.Paused -> state.positionMs
            else -> 0
        }

        screenModelScope.launch {
            mediaRepository.reportPlaybackStopped(
                serverId,
                itemId,
                sourceId,
                positionMs * 10_000,
                sessionId
            )
        }

        videoPlayer.stop()
    }

    override fun onDispose() {
        stopPlayback()
        videoPlayer.release()
    }
}
