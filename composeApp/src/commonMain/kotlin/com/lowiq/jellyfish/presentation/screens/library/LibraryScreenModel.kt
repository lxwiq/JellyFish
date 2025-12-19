package com.lowiq.jellyfish.presentation.screens.library

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.lowiq.jellyfish.data.local.UserPreferencesStorage
import com.lowiq.jellyfish.domain.model.DisplayMode
import com.lowiq.jellyfish.domain.model.Library
import com.lowiq.jellyfish.domain.model.LibraryFilters
import com.lowiq.jellyfish.domain.model.MediaItem
import com.lowiq.jellyfish.domain.model.SortOption
import com.lowiq.jellyfish.domain.repository.MediaRepository
import com.lowiq.jellyfish.domain.repository.ServerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LibraryState(
    val library: Library,
    val items: List<MediaItem> = emptyList(),
    val isLoading: Boolean = true,
    val isLoadingMore: Boolean = false,
    val hasMoreItems: Boolean = true,
    val error: String? = null,

    // Display mode
    val displayMode: DisplayMode = DisplayMode.POSTER,

    // Filters
    val sortBy: SortOption = SortOption.DATE_ADDED,
    val selectedGenre: String? = null,
    val selectedYear: Int? = null,
    val showWatchedOnly: Boolean? = null, // null = all, true = watched, false = unwatched
    val showFavoritesOnly: Boolean = false,

    // Available filter options
    val availableGenres: List<String> = emptyList(),
    val availableYears: List<Int> = emptyList()
)

class LibraryScreenModel(
    private val library: Library,
    private val mediaRepository: MediaRepository,
    private val serverRepository: ServerRepository,
    private val userPreferencesStorage: UserPreferencesStorage
) : ScreenModel {

    private val _state = MutableStateFlow(LibraryState(library = library))
    val state: StateFlow<LibraryState> = _state.asStateFlow()

    private var currentOffset = 0
    private val pageSize = 20

    init {
        loadDisplayMode()
        loadInitialData()
    }

    private fun loadDisplayMode() {
        screenModelScope.launch {
            userPreferencesStorage.getDisplayMode().collect { mode ->
                _state.update { it.copy(displayMode = mode) }
            }
        }
    }

    private fun loadInitialData() {
        screenModelScope.launch {
            val server = serverRepository.getActiveServer().first() ?: return@launch
            val serverId = server.id

            _state.update { it.copy(isLoading = true, error = null) }

            // Load filters and initial items in parallel
            launch {
                mediaRepository.getLibraryFilters(serverId, library.id)
                    .onSuccess { filters ->
                        _state.update {
                            it.copy(
                                availableGenres = filters.genres,
                                availableYears = filters.years
                            )
                        }
                    }
            }

            loadItems(serverId, reset = true)
        }
    }

    private suspend fun loadItems(serverId: String, reset: Boolean) {
        if (reset) {
            currentOffset = 0
            _state.update { it.copy(items = emptyList(), hasMoreItems = true) }
        }

        val currentState = _state.value

        mediaRepository.getLibraryItemsPaginated(
            serverId = serverId,
            libraryId = library.id,
            limit = pageSize,
            offset = currentOffset,
            sortBy = currentState.sortBy,
            genres = currentState.selectedGenre?.let { listOf(it) },
            years = currentState.selectedYear?.let { listOf(it) },
            isWatched = currentState.showWatchedOnly,
            isFavorite = if (currentState.showFavoritesOnly) true else null
        ).onSuccess { result ->
            currentOffset += result.items.size
            _state.update {
                it.copy(
                    items = if (reset) result.items else it.items + result.items,
                    isLoading = false,
                    isLoadingMore = false,
                    hasMoreItems = result.hasMore,
                    error = null
                )
            }
        }.onFailure { error ->
            _state.update {
                it.copy(
                    isLoading = false,
                    isLoadingMore = false,
                    error = error.message ?: "Failed to load items"
                )
            }
        }
    }

    fun loadMore() {
        val currentState = _state.value
        if (currentState.isLoadingMore || !currentState.hasMoreItems) return

        screenModelScope.launch {
            val server = serverRepository.getActiveServer().first() ?: return@launch
            _state.update { it.copy(isLoadingMore = true) }
            loadItems(server.id, reset = false)
        }
    }

    fun updateSort(sortOption: SortOption) {
        if (_state.value.sortBy == sortOption) return
        _state.update { it.copy(sortBy = sortOption) }
        reloadWithFilters()
    }

    fun updateGenre(genre: String?) {
        if (_state.value.selectedGenre == genre) return
        _state.update { it.copy(selectedGenre = genre) }
        reloadWithFilters()
    }

    fun updateYear(year: Int?) {
        if (_state.value.selectedYear == year) return
        _state.update { it.copy(selectedYear = year) }
        reloadWithFilters()
    }

    fun updateWatchedFilter(watched: Boolean?) {
        if (_state.value.showWatchedOnly == watched) return
        _state.update { it.copy(showWatchedOnly = watched) }
        reloadWithFilters()
    }

    fun updateFavoritesFilter(favoritesOnly: Boolean) {
        if (_state.value.showFavoritesOnly == favoritesOnly) return
        _state.update { it.copy(showFavoritesOnly = favoritesOnly) }
        reloadWithFilters()
    }

    fun updateDisplayMode(mode: DisplayMode) {
        if (_state.value.displayMode == mode) return
        screenModelScope.launch {
            userPreferencesStorage.setDisplayMode(mode)
        }
    }

    private fun reloadWithFilters() {
        screenModelScope.launch {
            val server = serverRepository.getActiveServer().first() ?: return@launch
            _state.update { it.copy(isLoading = true) }
            loadItems(server.id, reset = true)
        }
    }
}
