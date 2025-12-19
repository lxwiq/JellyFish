package com.lowiq.jellyfish.presentation.screens.library

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ViewList
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.ViewModule
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.lowiq.jellyfish.domain.model.DisplayMode
import com.lowiq.jellyfish.domain.model.Library
import com.lowiq.jellyfish.domain.model.MediaItem
import com.lowiq.jellyfish.presentation.components.FilterBar
import com.lowiq.jellyfish.presentation.navigation.navigateToDetail
import com.lowiq.jellyfish.presentation.theme.JellyFishTheme
import com.lowiq.jellyfish.presentation.theme.LocalJellyFishColors
import org.koin.core.parameter.parametersOf

class LibraryScreen(private val library: Library) : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val colors = LocalJellyFishColors.current
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

        var showDisplayModeMenu by remember { mutableStateOf(false) }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = library.name,
                            color = colors.foreground
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { navigator.pop() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = colors.foreground
                            )
                        }
                    },
                    actions = {
                        Box {
                            IconButton(onClick = { showDisplayModeMenu = true }) {
                                Icon(
                                    imageVector = when (state.displayMode) {
                                        DisplayMode.POSTER -> Icons.Default.ViewModule
                                        DisplayMode.GRID -> Icons.Default.GridView
                                        DisplayMode.LIST -> Icons.AutoMirrored.Filled.ViewList
                                    },
                                    contentDescription = "Display mode",
                                    tint = colors.foreground
                                )
                            }
                            DropdownMenu(
                                expanded = showDisplayModeMenu,
                                onDismissRequest = { showDisplayModeMenu = false },
                                containerColor = colors.secondary
                            ) {
                                DropdownMenuItem(
                                    text = { Text("Posters", color = colors.foreground) },
                                    leadingIcon = {
                                        Icon(
                                            Icons.Default.ViewModule,
                                            contentDescription = null,
                                            tint = colors.foreground
                                        )
                                    },
                                    onClick = {
                                        screenModel.updateDisplayMode(DisplayMode.POSTER)
                                        showDisplayModeMenu = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Grille", color = colors.foreground) },
                                    leadingIcon = {
                                        Icon(
                                            Icons.Default.GridView,
                                            contentDescription = null,
                                            tint = colors.foreground
                                        )
                                    },
                                    onClick = {
                                        screenModel.updateDisplayMode(DisplayMode.GRID)
                                        showDisplayModeMenu = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Liste", color = colors.foreground) },
                                    leadingIcon = {
                                        Icon(
                                            Icons.AutoMirrored.Filled.ViewList,
                                            contentDescription = null,
                                            tint = colors.foreground
                                        )
                                    },
                                    onClick = {
                                        screenModel.updateDisplayMode(DisplayMode.LIST)
                                        showDisplayModeMenu = false
                                    }
                                )
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = colors.background
                    )
                )
            },
            containerColor = colors.background
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
                            CircularProgressIndicator(color = colors.foreground)
                        }
                    }
                    state.error != null && state.items.isEmpty() -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = state.error ?: "An error occurred",
                                color = colors.mutedForeground
                            )
                        }
                    }
                    else -> {
                        when (state.displayMode) {
                            DisplayMode.LIST -> {
                                val listState = rememberLazyListState()

                                // Infinite scroll for list
                                val shouldLoadMoreList by remember {
                                    derivedStateOf {
                                        val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()
                                            ?: return@derivedStateOf false
                                        val totalItems = listState.layoutInfo.totalItemsCount
                                        lastVisibleItem.index >= totalItems - 6
                                    }
                                }

                                LaunchedEffect(shouldLoadMoreList) {
                                    if (shouldLoadMoreList && !state.isLoading && !state.isLoadingMore && state.hasMoreItems) {
                                        screenModel.loadMore()
                                    }
                                }

                                LazyColumn(
                                    state = listState,
                                    contentPadding = PaddingValues(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(12.dp),
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    items(
                                        items = state.items,
                                        key = { it.id }
                                    ) { item ->
                                        LibraryListItem(
                                            item = item,
                                            onClick = { navigateToDetail(navigator, item.id, item.type) }
                                        )
                                    }

                                    if (state.isLoadingMore) {
                                        item {
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(16.dp),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                CircularProgressIndicator(
                                                    color = colors.foreground,
                                                    modifier = Modifier.size(32.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                            }

                            DisplayMode.POSTER, DisplayMode.GRID -> {
                                val columns = when (state.displayMode) {
                                    DisplayMode.POSTER -> GridCells.Adaptive(minSize = 110.dp)
                                    else -> GridCells.Adaptive(minSize = 150.dp)
                                }

                                LazyVerticalGrid(
                                    state = gridState,
                                    columns = columns,
                                    contentPadding = PaddingValues(16.dp),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                                    verticalArrangement = Arrangement.spacedBy(16.dp),
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    items(
                                        items = state.items,
                                        key = { it.id }
                                    ) { item ->
                                        when (state.displayMode) {
                                            DisplayMode.POSTER -> LibraryPosterItem(
                                                item = item,
                                                onClick = { navigateToDetail(navigator, item.id, item.type) }
                                            )
                                            else -> LibraryGridItem(
                                                item = item,
                                                onClick = { navigateToDetail(navigator, item.id, item.type) }
                                            )
                                        }
                                    }

                                    if (state.isLoadingMore) {
                                        item(span = { GridItemSpan(maxLineSpan) }) {
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(16.dp),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                CircularProgressIndicator(
                                                    color = colors.foreground,
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
    }
}

@Composable
private fun LibraryGridItem(
    item: MediaItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalJellyFishColors.current
    val shapes = JellyFishTheme.shapes

    Column(
        modifier = modifier.clickable(onClick = onClick)
    ) {
        // Poster image (2:3 ratio)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .clip(shapes.default)
                .background(colors.secondary),
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
                    tint = colors.mutedForeground,
                    modifier = Modifier.size(48.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Title
        Text(
            text = item.title,
            fontSize = 14.sp,
            color = colors.foreground,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )

        // Subtitle
        item.subtitle?.let { subtitle ->
            Text(
                text = subtitle,
                fontSize = 12.sp,
                color = colors.mutedForeground,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(top = 2.dp)
            )
        }
    }
}

@Composable
private fun LibraryPosterItem(
    item: MediaItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalJellyFishColors.current
    val shapes = JellyFishTheme.shapes

    Column(
        modifier = modifier.clickable(onClick = onClick)
    ) {
        // Poster image (2:3 aspect ratio)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(2f / 3f)
                .clip(shapes.default)
                .background(colors.secondary),
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
                    tint = colors.mutedForeground,
                    modifier = Modifier.size(48.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Title only for poster mode
        Text(
            text = item.title,
            fontSize = 12.sp,
            color = colors.foreground,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun LibraryListItem(
    item: MediaItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalJellyFishColors.current
    val shapes = JellyFishTheme.shapes

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
            .clip(shapes.default)
            .background(colors.secondary)
            .clickable(onClick = onClick)
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Poster thumbnail
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .aspectRatio(2f / 3f)
                .clip(shapes.sm)
                .background(colors.background),
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
                    tint = colors.mutedForeground,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        // Text content
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = item.title,
                fontSize = 14.sp,
                color = colors.foreground,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            item.subtitle?.let { subtitle ->
                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = colors.mutedForeground,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}
