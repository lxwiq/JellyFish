package com.lowiq.jellyfish.presentation.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.lowiq.jellyfish.domain.model.MediaType
import com.lowiq.jellyfish.presentation.components.AppHeader
import com.lowiq.jellyfish.presentation.components.AppScaffold
import com.lowiq.jellyfish.presentation.components.LibraryCards
import com.lowiq.jellyfish.presentation.components.MediaCarousel
import com.lowiq.jellyfish.presentation.components.MediaCarouselItem
import com.lowiq.jellyfish.presentation.components.SkeletonCarousel
import com.lowiq.jellyfish.presentation.navigation.navigateToDetail
import com.lowiq.jellyfish.presentation.screens.downloads.DownloadsScreen
import com.lowiq.jellyfish.presentation.screens.library.LibraryScreen
import com.lowiq.jellyfish.presentation.screens.search.SearchScreen
import com.lowiq.jellyfish.presentation.screens.serverlist.ServerListScreen
import com.lowiq.jellyfish.presentation.screens.settings.SettingsScreen
import com.lowiq.jellyfish.presentation.theme.LocalJellyFishColors

class HomeScreen : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val colors = LocalJellyFishColors.current
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = koinScreenModel<HomeScreenModel>()
        val state by screenModel.state.collectAsState()

        LaunchedEffect(Unit) {
            screenModel.events.collect { event ->
                when (event) {
                    is HomeEvent.LoggedOut -> {
                        navigator.replaceAll(ServerListScreen())
                    }
                    is HomeEvent.NavigateToServerList -> {
                        navigator.push(ServerListScreen())
                    }
                    is HomeEvent.NavigateToDownloads -> {
                        navigator.push(DownloadsScreen())
                    }
                    is HomeEvent.NavigateToSearch -> {
                        navigator.push(SearchScreen())
                    }
                    is HomeEvent.NavigateToSettings -> {
                        navigator.push(SettingsScreen())
                    }
                }
            }
        }

        AppScaffold(
            selectedIndex = state.selectedNavIndex,
            onNavigationItemSelected = { screenModel.onNavigationItemSelected(it) },
            modifier = Modifier.background(colors.background)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                AppHeader(
                    username = state.username,
                    onSwitchServer = { screenModel.switchServer() },
                    onLogout = { screenModel.logout() }
                )

                if (state.libraries.isNotEmpty()) {
                    LibraryCards(
                        libraries = state.libraries,
                        onLibraryClick = { library ->
                            navigator.push(LibraryScreen(library))
                        },
                        modifier = Modifier.padding(vertical = 12.dp)
                    )
                }

                PullToRefreshBox(
                    isRefreshing = state.isRefreshing,
                    onRefresh = { screenModel.refresh() },
                    modifier = Modifier.fillMaxSize()
                ) {
                    if (state.isLoading) {
                        // Show skeletons while initial loading
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(colors.background),
                            verticalArrangement = Arrangement.spacedBy(24.dp)
                        ) {
                            items(4) {
                                SkeletonCarousel()
                            }
                        }
                    } else if (state.error != null && !state.isCategoriesLoading) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Error: ${state.error}",
                                color = colors.destructive
                            )
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(colors.background),
                            verticalArrangement = Arrangement.spacedBy(24.dp)
                        ) {
                            // Show skeletons while categories are loading
                            if (state.isCategoriesLoading) {
                                items(4) {
                                    SkeletonCarousel()
                                }
                            } else {
                                // Continue Watching
                                if (state.continueWatching.isNotEmpty()) {
                                    item {
                                        MediaCarousel(
                                            title = "Continue Watching",
                                            items = state.continueWatching.map { it.toCarouselItem() },
                                            onItemClick = { id, type -> navigateToDetail(navigator, id, type) }
                                        )
                                    }
                                }

                                // Latest Movies
                                if (state.latestMovies.isNotEmpty()) {
                                    item {
                                        MediaCarousel(
                                            title = "Latest Movies",
                                            items = state.latestMovies.map { it.toCarouselItem() },
                                            onItemClick = { id, type -> navigateToDetail(navigator, id, type) }
                                        )
                                    }
                                }

                                // Latest Series
                                if (state.latestSeries.isNotEmpty()) {
                                    item {
                                        MediaCarousel(
                                            title = "Latest Series",
                                            items = state.latestSeries.map { it.toCarouselItem() },
                                            onItemClick = { id, type -> navigateToDetail(navigator, id, type) }
                                        )
                                    }
                                }

                                // Latest Music
                                if (state.latestMusic.isNotEmpty()) {
                                    item {
                                        MediaCarousel(
                                            title = "Latest Music",
                                            items = state.latestMusic.map { it.toCarouselItem() },
                                            onItemClick = { id, type -> navigateToDetail(navigator, id, type) }
                                        )
                                    }
                                }

                                // Other library sections (books, photos, etc.)
                                items(
                                    items = state.librarySections,
                                    key = { it.library.id }
                                ) { section ->
                                    MediaCarousel(
                                        title = section.library.name,
                                        items = section.items.map { it.toCarouselItem() },
                                        onItemClick = { id, type -> navigateToDetail(navigator, id, type) }
                                    )
                                }

                                // Favorites
                                if (state.favorites.isNotEmpty()) {
                                    item {
                                        MediaCarousel(
                                            title = "Favorites",
                                            items = state.favorites.map { it.toCarouselItem() },
                                            onItemClick = { id, type -> navigateToDetail(navigator, id, type) }
                                        )
                                    }
                                }

                                // Next Up
                                if (state.nextUp.isNotEmpty()) {
                                    item {
                                        MediaCarousel(
                                            title = "Next Up",
                                            items = state.nextUp.map { it.toCarouselItem() },
                                            onItemClick = { id, type -> navigateToDetail(navigator, id, type) }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun com.lowiq.jellyfish.domain.model.MediaItem.toCarouselItem() = MediaCarouselItem(
    id = id,
    title = title,
    subtitle = subtitle,
    imageUrl = imageUrl,
    progress = progress,
    isPoster = isPoster,
    type = type
)
