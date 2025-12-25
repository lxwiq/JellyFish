package com.lowiq.jellyfish.presentation.screens.detail

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.lowiq.jellyfish.data.local.DownloadSettingsStorage
import com.lowiq.jellyfish.domain.model.CastMember
import com.lowiq.jellyfish.domain.model.MediaItem
import com.lowiq.jellyfish.domain.model.QualityOption
import com.lowiq.jellyfish.domain.repository.DownloadRepository
import com.lowiq.jellyfish.domain.repository.MediaRepository
import com.lowiq.jellyfish.domain.repository.ServerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

data class MovieDetailState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val title: String = "",
    val overview: String? = null,
    val backdropUrl: String? = null,
    val year: String? = null,
    val runtime: String? = null,
    val rating: Float? = null,
    val genres: List<String> = emptyList(),
    val studio: String? = null,
    val cast: List<CastMember> = emptyList(),
    val similarItems: List<MediaItem> = emptyList(),
    val trailerUrl: String? = null,
    val isFavorite: Boolean = false,
    val isWatched: Boolean = false,
    val showQualityDialog: Boolean = false,
    val availableQualities: List<QualityOption> = emptyList(),
    val isLoadingQualities: Boolean = false,
    val posterUrl: String? = null,
    val playbackPositionMs: Long = 0
)

sealed class MovieDetailEvent {
    data class PlayVideo(
        val itemId: String,
        val title: String,
        val startPositionMs: Long
    ) : MovieDetailEvent()
    object NavigateToDownloads : MovieDetailEvent()
}

class MovieDetailScreenModel(
    private val itemId: String,
    private val serverRepository: ServerRepository,
    private val mediaRepository: MediaRepository,
    private val downloadRepository: DownloadRepository,
    private val downloadSettings: DownloadSettingsStorage
) : ScreenModel {

    private val _state = MutableStateFlow(MovieDetailState())
    val state = _state.asStateFlow()

    private val _events = MutableSharedFlow<MovieDetailEvent>()
    val events = _events.asSharedFlow()

    private var currentServerId: String? = null

    init {
        loadDetails()
    }

    private fun loadDetails() {
        screenModelScope.launch {
            val server = serverRepository.getActiveServer()
                .filterNotNull()
                .first()
            currentServerId = server.id

            mediaRepository.getMovieDetails(server.id, itemId)
                .onSuccess { details ->
                    // Convert ticks to milliseconds (1 tick = 100 nanoseconds, so 10,000 ticks = 1 ms)
                    val positionMs = details.playbackPositionTicks?.div(10_000) ?: 0
                    _state.update {
                        it.copy(
                            isLoading = false,
                            title = details.title,
                            overview = details.overview,
                            backdropUrl = details.backdropUrl,
                            posterUrl = details.posterUrl,
                            year = details.year,
                            runtime = details.runtime,
                            rating = details.rating,
                            genres = details.genres,
                            studio = details.studio,
                            cast = details.cast,
                            similarItems = details.similarItems,
                            trailerUrl = details.trailerUrl,
                            isFavorite = details.isFavorite,
                            isWatched = details.isWatched,
                            playbackPositionMs = positionMs
                        )
                    }
                }
                .onFailure { e ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = e.message
                        )
                    }
                }
        }
    }

    fun toggleFavorite() {
        val serverId = currentServerId ?: return
        val newValue = !_state.value.isFavorite

        screenModelScope.launch {
            mediaRepository.toggleFavorite(serverId, itemId, newValue)
                .onSuccess {
                    _state.update { it.copy(isFavorite = newValue) }
                }
        }
    }

    fun toggleWatched() {
        val serverId = currentServerId ?: return
        val newValue = !_state.value.isWatched

        screenModelScope.launch {
            mediaRepository.toggleWatched(serverId, itemId, newValue)
                .onSuccess {
                    _state.update { it.copy(isWatched = newValue) }
                }
        }
    }

    fun onPlay() {
        screenModelScope.launch {
            _events.emit(
                MovieDetailEvent.PlayVideo(
                    itemId = itemId,
                    title = _state.value.title,
                    startPositionMs = _state.value.playbackPositionMs
                )
            )
        }
    }

    fun onDownload() {
        screenModelScope.launch {
            val alwaysAsk = downloadSettings.alwaysAskQuality.first()

            _state.update { it.copy(isLoadingQualities = true) }

            val serverId = currentServerId ?: return@launch
            downloadRepository.getAvailableQualities(serverId, itemId)
                .onSuccess { qualities ->
                    if (alwaysAsk || qualities.isEmpty()) {
                        _state.update {
                            it.copy(
                                showQualityDialog = true,
                                availableQualities = qualities,
                                isLoadingQualities = false
                            )
                        }
                    } else {
                        val defaultQuality = downloadSettings.defaultQuality.first()
                        val quality = qualities.find { it.label == defaultQuality } ?: qualities.first()
                        startDownload(quality)
                    }
                }
                .onFailure {
                    _state.update { it.copy(isLoadingQualities = false) }
                }
        }
    }

    fun onQualitySelected(quality: QualityOption, dontAskAgain: Boolean) {
        screenModelScope.launch {
            if (dontAskAgain) {
                downloadSettings.setDefaultQuality(quality.label)
                downloadSettings.setAlwaysAskQuality(false)
            }
            startDownload(quality)
            _state.update { it.copy(showQualityDialog = false) }
        }
    }

    fun dismissQualityDialog() {
        _state.update { it.copy(showQualityDialog = false) }
    }

    private suspend fun startDownload(quality: QualityOption) {
        val serverId = currentServerId ?: return
        val state = _state.value

        downloadRepository.startDownload(
            serverId = serverId,
            itemId = itemId,
            title = state.title,
            subtitle = null,
            imageUrl = state.posterUrl,
            quality = quality
        )

        _events.emit(MovieDetailEvent.NavigateToDownloads)
    }

    fun onShare() {
        // TODO: Implement share
    }
}
