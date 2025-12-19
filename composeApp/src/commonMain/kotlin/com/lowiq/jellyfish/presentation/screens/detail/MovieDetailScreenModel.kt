package com.lowiq.jellyfish.presentation.screens.detail

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.lowiq.jellyfish.domain.model.CastMember
import com.lowiq.jellyfish.domain.model.MediaItem
import com.lowiq.jellyfish.domain.repository.MediaRepository
import com.lowiq.jellyfish.domain.repository.ServerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

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
    val isWatched: Boolean = false
)

class MovieDetailScreenModel(
    private val itemId: String,
    private val serverRepository: ServerRepository,
    private val mediaRepository: MediaRepository
) : ScreenModel {

    private val _state = MutableStateFlow(MovieDetailState())
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

            mediaRepository.getMovieDetails(server.id, itemId)
                .onSuccess { details ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            title = details.title,
                            overview = details.overview,
                            backdropUrl = details.backdropUrl,
                            year = details.year,
                            runtime = details.runtime,
                            rating = details.rating,
                            genres = details.genres,
                            studio = details.studio,
                            cast = details.cast,
                            similarItems = details.similarItems,
                            trailerUrl = details.trailerUrl,
                            isFavorite = details.isFavorite,
                            isWatched = details.isWatched
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
        // TODO: Implement playback
    }

    fun onDownload() {
        // TODO: Implement download
    }

    fun onShare() {
        // TODO: Implement share
    }
}
