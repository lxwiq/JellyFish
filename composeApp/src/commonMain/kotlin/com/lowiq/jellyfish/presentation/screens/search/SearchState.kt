package com.lowiq.jellyfish.presentation.screens.search

import com.lowiq.jellyfish.domain.model.MediaItem

data class SearchState(
    val query: String = "",
    val results: List<MediaItem> = emptyList(),
    val history: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val hasSearched: Boolean = false,
    val error: String? = null
)
