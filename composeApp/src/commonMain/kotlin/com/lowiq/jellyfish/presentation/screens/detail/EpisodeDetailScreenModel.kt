package com.lowiq.jellyfish.presentation.screens.detail

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.lowiq.jellyfish.domain.model.CastMember
import com.lowiq.jellyfish.domain.model.Episode
import com.lowiq.jellyfish.domain.repository.MediaRepository
import com.lowiq.jellyfish.domain.repository.ServerRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed class EpisodeDetailEvent {
    data class NavigateToEpisode(val episodeId: String) : EpisodeDetailEvent()
    data class NavigateToSeries(val seriesId: String) : EpisodeDetailEvent()
}

data class EpisodeDetailState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val title: String = "",
    val overview: String? = null,
    val thumbnailUrl: String? = null,
    val seasonNumber: Int = 0,
    val episodeNumber: Int = 0,
    val totalEpisodes: Int = 0,
    val runtime: String? = null,
    val rating: Float? = null,
    val seriesId: String = "",
    val seriesName: String = "",
    val guestStars: List<CastMember> = emptyList(),
    val seasonEpisodes: List<Episode> = emptyList(),
    val previousEpisodeId: String? = null,
    val nextEpisodeId: String? = null,
    val isFavorite: Boolean = false,
    val isWatched: Boolean = false,
    val progress: Float? = null
)

class EpisodeDetailScreenModel(
    private val itemId: String,
    private val serverRepository: ServerRepository,
    private val mediaRepository: MediaRepository
) : ScreenModel {

    private val _state = MutableStateFlow(EpisodeDetailState())
    val state = _state.asStateFlow()

    private val _events = MutableSharedFlow<EpisodeDetailEvent>()
    val events = _events.asSharedFlow()

    private var currentServerId: String? = null

    init {
        loadDetails(itemId)
    }

    private fun loadDetails(episodeId: String) {
        screenModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            val server = serverRepository.getActiveServer()
                .filterNotNull()
                .first()
            currentServerId = server.id

            mediaRepository.getEpisodeDetails(server.id, episodeId)
                .onSuccess { details ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            title = details.title,
                            overview = details.overview,
                            thumbnailUrl = details.thumbnailUrl,
                            seasonNumber = details.seasonNumber,
                            episodeNumber = details.episodeNumber,
                            totalEpisodes = details.seasonEpisodes.size,
                            runtime = details.runtime,
                            rating = details.rating,
                            seriesId = details.seriesId,
                            seriesName = details.seriesName,
                            guestStars = details.guestStars,
                            seasonEpisodes = details.seasonEpisodes,
                            previousEpisodeId = details.previousEpisodeId,
                            nextEpisodeId = details.nextEpisodeId,
                            isFavorite = details.isFavorite,
                            isWatched = details.isWatched,
                            progress = details.progress
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

    fun navigateToPreviousEpisode() {
        val prevId = _state.value.previousEpisodeId
        if (prevId != null) {
            loadDetails(prevId)
        }
    }

    fun navigateToNextEpisode() {
        val nextId = _state.value.nextEpisodeId
        if (nextId != null) {
            loadDetails(nextId)
        }
    }

    fun navigateToEpisode(episodeId: String) {
        loadDetails(episodeId)
    }

    fun navigateToSeries() {
        screenModelScope.launch {
            _events.emit(EpisodeDetailEvent.NavigateToSeries(_state.value.seriesId))
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
        // TODO: Implement playback
    }

    fun onDownload() {
        // TODO: Implement download
    }

    fun onShare() {
        // TODO: Implement share
    }
}
