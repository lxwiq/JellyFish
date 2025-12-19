package com.lowiq.jellyfish.presentation.screens.search

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.lowiq.jellyfish.data.local.SearchHistoryStorage
import com.lowiq.jellyfish.domain.repository.MediaRepository
import com.lowiq.jellyfish.domain.repository.ServerRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchScreenModel(
    private val serverRepository: ServerRepository,
    private val mediaRepository: MediaRepository,
    private val searchHistoryStorage: SearchHistoryStorage
) : ScreenModel {

    private val _state = MutableStateFlow(SearchState())
    val state = _state.asStateFlow()

    private var searchJob: Job? = null
    private var currentServerId: String? = null

    init {
        loadHistory()
        loadServerId()
    }

    private fun loadServerId() {
        screenModelScope.launch {
            serverRepository.getActiveServer()
                .filterNotNull()
                .first()
                .let { server ->
                    currentServerId = server.id
                }
        }
    }

    private fun loadHistory() {
        screenModelScope.launch {
            searchHistoryStorage.getHistory().collect { history ->
                _state.update { it.copy(history = history) }
            }
        }
    }

    fun onQueryChange(query: String) {
        _state.update { it.copy(query = query, error = null) }

        searchJob?.cancel()

        if (query.isBlank()) {
            _state.update { it.copy(results = emptyList(), hasSearched = false, isLoading = false) }
            return
        }

        searchJob = screenModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            delay(300) // Debounce
            performSearch(query)
        }
    }

    fun onHistoryItemClick(query: String) {
        _state.update { it.copy(query = query) }
        searchJob?.cancel()
        searchJob = screenModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            performSearch(query)
        }
    }

    fun onRemoveHistoryItem(query: String) {
        screenModelScope.launch {
            searchHistoryStorage.removeFromHistory(query)
        }
    }

    fun onClearHistory() {
        screenModelScope.launch {
            searchHistoryStorage.clearHistory()
        }
    }

    private suspend fun performSearch(query: String) {
        val serverId = currentServerId ?: return

        mediaRepository.search(serverId, query)
            .onSuccess { results ->
                _state.update {
                    it.copy(
                        results = results,
                        isLoading = false,
                        hasSearched = true,
                        error = null
                    )
                }
                searchHistoryStorage.addToHistory(query)
            }
            .onFailure { e ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        hasSearched = true,
                        error = e.message ?: "Search failed"
                    )
                }
            }
    }

    fun clearQuery() {
        _state.update { it.copy(query = "", results = emptyList(), hasSearched = false) }
        searchJob?.cancel()
    }
}
