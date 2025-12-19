package com.lowiq.jellyfish.presentation.navigation

import cafe.adriel.voyager.navigator.Navigator
import com.lowiq.jellyfish.domain.model.MediaType
import com.lowiq.jellyfish.presentation.screens.detail.EpisodeDetailScreen
import com.lowiq.jellyfish.presentation.screens.detail.MovieDetailScreen
import com.lowiq.jellyfish.presentation.screens.detail.SeriesDetailScreen

fun navigateToDetail(navigator: Navigator, itemId: String, type: MediaType) {
    when (type) {
        MediaType.MOVIE -> navigator.push(MovieDetailScreen(itemId))
        MediaType.SERIES -> navigator.push(SeriesDetailScreen(itemId))
        MediaType.EPISODE -> navigator.push(EpisodeDetailScreen(itemId))
        else -> { /* No navigation for music/other yet */ }
    }
}
