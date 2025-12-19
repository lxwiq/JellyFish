package com.lowiq.jellyfish.presentation.screens.home

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.lowiq.jellyfish.domain.model.MediaItem
import com.lowiq.jellyfish.domain.repository.AuthRepository
import com.lowiq.jellyfish.domain.repository.MediaRepository
import com.lowiq.jellyfish.domain.repository.ServerRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.async

data class HomeState(
    val username: String = "",
    val serverName: String = "",
    val continueWatching: List<MediaItem> = emptyList(),
    val latestMovies: List<MediaItem> = emptyList(),
    val latestSeries: List<MediaItem> = emptyList(),
    val latestMusic: List<MediaItem> = emptyList(),
    val favorites: List<MediaItem> = emptyList(),
    val nextUp: List<MediaItem> = emptyList(),
    val selectedNavIndex: Int = 0,
    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val error: String? = null
)

sealed class HomeEvent {
    object LoggedOut : HomeEvent()
    object NavigateToServerList : HomeEvent()
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
                    val user = authRepository.getCurrentUser(server.id)
                    _state.update {
                        it.copy(
                            username = user?.name ?: server.username ?: "User",
                            serverName = server.name,
                            isLoading = false
                        )
                    }
                    loadAllCategories()
                }
        }
    }

    private fun loadAllCategories() {
        val serverId = currentServerId ?: return
        screenModelScope.launch {
            _state.update { it.copy(error = null) }

            try {
                // Load all categories in parallel
                val continueWatchingDeferred = async { mediaRepository.getContinueWatching(serverId) }
                val latestMoviesDeferred = async { mediaRepository.getLatestMovies(serverId) }
                val latestSeriesDeferred = async { mediaRepository.getLatestSeries(serverId) }
                val latestMusicDeferred = async { mediaRepository.getLatestMusic(serverId) }
                val favoritesDeferred = async { mediaRepository.getFavorites(serverId) }
                val nextUpDeferred = async { mediaRepository.getNextUp(serverId) }

                // Wait for all results
                val continueWatchingResult = continueWatchingDeferred.await()
                val latestMoviesResult = latestMoviesDeferred.await()
                val latestSeriesResult = latestSeriesDeferred.await()
                val latestMusicResult = latestMusicDeferred.await()
                val favoritesResult = favoritesDeferred.await()
                val nextUpResult = nextUpDeferred.await()

                // Update state with successful results, using empty list for failures
                _state.update {
                    it.copy(
                        continueWatching = continueWatchingResult.getOrElse { emptyList() },
                        latestMovies = latestMoviesResult.getOrElse { emptyList() },
                        latestSeries = latestSeriesResult.getOrElse { emptyList() },
                        latestMusic = latestMusicResult.getOrElse { emptyList() },
                        favorites = favoritesResult.getOrElse { emptyList() },
                        nextUp = nextUpResult.getOrElse { emptyList() },
                        isRefreshing = false
                    )
                }

                println("DEBUG: Loaded all categories successfully")
            } catch (e: Exception) {
                println("DEBUG: Failed to load categories: ${e.message}")
                e.printStackTrace()
                _state.update { it.copy(isRefreshing = false, error = e.message) }
            }
        }
    }

    fun refresh() {
        _state.update { it.copy(isRefreshing = true) }
        loadAllCategories()
    }

    fun onNavigationItemSelected(index: Int) {
        _state.update { it.copy(selectedNavIndex = index) }
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
