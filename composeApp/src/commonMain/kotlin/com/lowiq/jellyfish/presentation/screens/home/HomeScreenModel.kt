package com.lowiq.jellyfish.presentation.screens.home

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.lowiq.jellyfish.domain.model.Library
import com.lowiq.jellyfish.domain.model.MediaItem
import com.lowiq.jellyfish.domain.repository.AuthRepository
import com.lowiq.jellyfish.domain.repository.HomeMediaData
import com.lowiq.jellyfish.domain.repository.MediaRepository
import com.lowiq.jellyfish.domain.repository.ServerRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class LibrarySection(
    val library: Library,
    val items: List<MediaItem>
)

data class HomeState(
    val username: String = "",
    val serverName: String = "",
    val libraries: List<Library> = emptyList(),
    val librarySections: List<LibrarySection> = emptyList(),
    val continueWatching: List<MediaItem> = emptyList(),
    val latestMovies: List<MediaItem> = emptyList(),
    val latestSeries: List<MediaItem> = emptyList(),
    val latestMusic: List<MediaItem> = emptyList(),
    val favorites: List<MediaItem> = emptyList(),
    val nextUp: List<MediaItem> = emptyList(),
    val selectedNavIndex: Int = 0,
    val isLoading: Boolean = true,
    val isCategoriesLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val error: String? = null
)

sealed class HomeEvent {
    object LoggedOut : HomeEvent()
    object NavigateToServerList : HomeEvent()
    object NavigateToDownloads : HomeEvent()
    object NavigateToSearch : HomeEvent()
    object NavigateToSettings : HomeEvent()
}

class HomeScreenModel(
    private val serverRepository: ServerRepository,
    private val authRepository: AuthRepository,
    private val mediaRepository: MediaRepository
) : ScreenModel {

    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    private val _events = MutableSharedFlow<HomeEvent>()
    val events = _events.asSharedFlow()

    private var currentServerId: String? = null

    init {
        loadUserInfo()
    }

    private fun loadUserInfo() {
        screenModelScope.launch {
            serverRepository.getActiveServer()
                .filterNotNull()
                .first()
                .let { server ->
                    currentServerId = server.id
                    _state.update {
                        it.copy(
                            username = server.username ?: "User",
                            serverName = server.name,
                            isLoading = false
                        )
                    }
                    // Load cached data first, then refresh from network
                    loadCachedData(server.id)
                    refreshFromNetwork(server.id)
                }
        }
    }

    private fun loadCachedData(serverId: String) {
        screenModelScope.launch {
            mediaRepository.getCachedHomeData(serverId)
                .first()
                .let { cached ->
                    // Only update if we have cached data
                    if (cached.hasData()) {
                        _state.update {
                            it.copy(
                                continueWatching = cached.continueWatching,
                                latestMovies = cached.latestMovies,
                                latestSeries = cached.latestSeries,
                                latestMusic = cached.latestMusic,
                                favorites = cached.favorites,
                                nextUp = cached.nextUp,
                                isCategoriesLoading = false
                            )
                        }
                    }
                }
        }
    }

    private fun refreshFromNetwork(serverId: String) {
        screenModelScope.launch {
            _state.update { it.copy(error = null) }

            // Load libraries
            val librariesResult = mediaRepository.getLibraries(serverId)
            val libraries = librariesResult.getOrElse { emptyList() }
            _state.update { it.copy(libraries = libraries) }

            // Load items for "other" libraries (not movies, tvshows, music)
            val defaultTypes = setOf("movies", "tvshows", "music")
            val otherLibraries = libraries.filter { it.type.lowercase() !in defaultTypes }
            val sections = otherLibraries.mapNotNull { library ->
                mediaRepository.getLibraryItems(serverId, library.id, limit = 10)
                    .getOrNull()
                    ?.takeIf { it.isNotEmpty() }
                    ?.let { items -> LibrarySection(library, items) }
            }
            _state.update { it.copy(librarySections = sections) }

            // Load standard media data
            mediaRepository.refreshHomeData(serverId)
                .onSuccess { data ->
                    _state.update {
                        it.copy(
                            continueWatching = data.continueWatching,
                            latestMovies = data.latestMovies,
                            latestSeries = data.latestSeries,
                            latestMusic = data.latestMusic,
                            favorites = data.favorites,
                            nextUp = data.nextUp,
                            isCategoriesLoading = false,
                            isRefreshing = false
                        )
                    }
                }
                .onFailure { e ->
                    _state.update {
                        it.copy(
                            isCategoriesLoading = false,
                            isRefreshing = false,
                            error = e.message
                        )
                    }
                }
        }
    }

    private fun HomeMediaData.hasData(): Boolean {
        return continueWatching.isNotEmpty() ||
                latestMovies.isNotEmpty() ||
                latestSeries.isNotEmpty() ||
                latestMusic.isNotEmpty() ||
                favorites.isNotEmpty() ||
                nextUp.isNotEmpty()
    }

    fun refresh() {
        val serverId = currentServerId ?: return
        _state.update { it.copy(isRefreshing = true) }
        refreshFromNetwork(serverId)
    }

    fun onNavigationItemSelected(index: Int) {
        _state.update { it.copy(selectedNavIndex = index) }

        screenModelScope.launch {
            when (index) {
                1 -> _events.emit(HomeEvent.NavigateToSearch)
                3 -> _events.emit(HomeEvent.NavigateToDownloads)
                4 -> _events.emit(HomeEvent.NavigateToSettings)
            }
        }
    }

    fun switchServer() {
        screenModelScope.launch {
            _events.emit(HomeEvent.NavigateToServerList)
        }
    }

    fun logout() {
        screenModelScope.launch {
            serverRepository.getActiveServer()
                .filterNotNull()
                .first()
                .let { server ->
                    authRepository.logout(server.id)
                    _events.emit(HomeEvent.LoggedOut)
                }
        }
    }
}
