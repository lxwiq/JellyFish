package com.lowiq.jellyfish.presentation.screens.detail

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.lowiq.jellyfish.domain.model.CastMember
import com.lowiq.jellyfish.domain.model.Episode
import com.lowiq.jellyfish.domain.model.MediaItem
import com.lowiq.jellyfish.domain.model.Season
import com.lowiq.jellyfish.domain.repository.MediaRepository
import com.lowiq.jellyfish.domain.repository.ServerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SeriesDetailState(
    val isLoading: Boolean = true,
    val isLoadingEpisodes: Boolean = false,
    val error: String? = null,
    val title: String = "",
    val overview: String? = null,
    val backdropUrl: String? = null,
    val year: String? = null,
    val seasonCount: Int = 0,
    val rating: Float? = null,
    val genres: List<String> = emptyList(),
    val studio: String? = null,
    val cast: List<CastMember> = emptyList(),
    val seasons: List<Season> = emptyList(),
    val selectedSeason: Int = 1,
    val episodes: List<Episode> = emptyList(),
    val similarItems: List<MediaItem> = emptyList(),
    val isFavorite: Boolean = false,
    val isWatched: Boolean = false
)

class SeriesDetailScreenModel(
    private val itemId: String,
    private val serverRepository: ServerRepository,
    private val mediaRepository: MediaRepository
) : ScreenModel {

    private val _state = MutableStateFlow(SeriesDetailState())
    val state = _state.asStateFlow()

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

            mediaRepository.getSeriesDetails(server.id, itemId)
                .onSuccess { details ->
                    val firstSeason = details.seasons.firstOrNull()?.number ?: 1
                    _state.update {
                        it.copy(
                            isLoading = false,
                            title = details.title,
                            overview = details.overview,
                            backdropUrl = details.backdropUrl,
                            year = details.year,
                            seasonCount = details.seasonCount,
                            rating = details.rating,
                            genres = details.genres,
                            studio = details.studio,
                            cast = details.cast,
                            seasons = details.seasons,
                            selectedSeason = firstSeason,
                            similarItems = details.similarItems,
                            isFavorite = details.isFavorite,
                            isWatched = details.isWatched
                        )
                    }
                    // Load episodes for first season
                    loadEpisodes(server.id, firstSeason)
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

    private fun loadEpisodes(serverId: String, seasonNumber: Int) {
        screenModelScope.launch {
            _state.update { it.copy(isLoadingEpisodes = true) }

            mediaRepository.getSeasonEpisodes(serverId, itemId, seasonNumber)
                .onSuccess { episodes ->
                    _state.update {
                        it.copy(
                            isLoadingEpisodes = false,
                            episodes = episodes
                        )
                    }
                }
                .onFailure {
                    _state.update { it.copy(isLoadingEpisodes = false) }
                }
        }
    }

    fun selectSeason(seasonNumber: Int) {
        val serverId = currentServerId ?: return
        _state.update { it.copy(selectedSeason = seasonNumber) }
        loadEpisodes(serverId, seasonNumber)
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
