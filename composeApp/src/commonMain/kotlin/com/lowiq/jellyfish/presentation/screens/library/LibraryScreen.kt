package com.lowiq.jellyfish.presentation.screens.library

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.lowiq.jellyfish.domain.model.Library
import com.lowiq.jellyfish.domain.model.MediaItem
import com.lowiq.jellyfish.presentation.components.FilterBar
import org.koin.core.parameter.parametersOf

private val BackgroundColor = Color(0xFF09090B)
private val SurfaceColor = Color(0xFF27272A)
private val TextColor = Color(0xFFFAFAFA)
private val SubtleTextColor = Color(0xFFA1A1AA)

class LibraryScreen(private val library: Library) : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = koinScreenModel<LibraryScreenModel> { parametersOf(library) }
        val state by screenModel.state.collectAsState()

        val gridState = rememberLazyGridState()

        // Infinite scroll detection
        val shouldLoadMore by remember {
            derivedStateOf {
                val lastVisibleItem = gridState.layoutInfo.visibleItemsInfo.lastOrNull()
                    ?: return@derivedStateOf false
                val totalItems = gridState.layoutInfo.totalItemsCount
                lastVisibleItem.index >= totalItems - 6
            }
        }

        LaunchedEffect(shouldLoadMore) {
            if (shouldLoadMore && !state.isLoading && !state.isLoadingMore && state.hasMoreItems) {
                screenModel.loadMore()
            }
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = library.name,
                            color = TextColor
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { navigator.pop() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = TextColor
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = BackgroundColor
                    )
                )
            },
            containerColor = BackgroundColor
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // Filter bar
                FilterBar(
                    sortOption = state.sortBy,
                    onSortChanged = { screenModel.updateSort(it) },
                    availableGenres = state.availableGenres,
                    selectedGenre = state.selectedGenre,
                    onGenreChanged = { screenModel.updateGenre(it) },
                    availableYears = state.availableYears,
                    selectedYear = state.selectedYear,
                    onYearChanged = { screenModel.updateYear(it) },
                    showWatched = state.showWatchedOnly,
                    onWatchedChanged = { screenModel.updateWatchedFilter(it) },
                    showFavoritesOnly = state.showFavoritesOnly,
                    onFavoritesChanged = { screenModel.updateFavoritesFilter(it) },
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                when {
                    state.isLoading && state.items.isEmpty() -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = TextColor)
                        }
                    }
                    state.error != null && state.items.isEmpty() -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = state.error ?: "An error occurred",
                                color = SubtleTextColor
                            )
                        }
                    }
                    else -> {
                        LazyVerticalGrid(
                            state = gridState,
                            columns = GridCells.Adaptive(minSize = 120.dp),
                            contentPadding = PaddingValues(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(
                                items = state.items,
                                key = { it.id }
                            ) { item ->
                                LibraryGridItem(
                                    item = item,
                                    onClick = { /* TODO: Navigate to detail */ }
                                )
                            }

                            // Loading indicator at bottom
                            if (state.isLoadingMore) {
                                item(span = { GridItemSpan(maxLineSpan) }) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        CircularProgressIndicator(
                                            color = TextColor,
                                            modifier = Modifier.size(32.dp)
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

@Composable
private fun LibraryGridItem(
    item: MediaItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.clickable(onClick = onClick)
    ) {
        // Poster image (2:3 ratio)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(SurfaceColor),
            contentAlignment = Alignment.Center
        ) {
            if (item.imageUrl != null) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalPlatformContext.current)
                        .data(item.imageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = item.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = null,
                    tint = SubtleTextColor,
                    modifier = Modifier.size(48.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Title
        Text(
            text = item.title,
            fontSize = 14.sp,
            color = TextColor,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )

        // Subtitle
        item.subtitle?.let { subtitle ->
            Text(
                text = subtitle,
                fontSize = 12.sp,
                color = SubtleTextColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(top = 2.dp)
            )
        }
    }
}
