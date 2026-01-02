package com.lowiq.jellyfish.presentation.screens.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil3.compose.AsyncImage
import com.lowiq.jellyfish.domain.model.MediaItem
import com.lowiq.jellyfish.presentation.navigation.navigateToDetail
import com.lowiq.jellyfish.presentation.theme.LocalJellyFishColors
import org.jetbrains.compose.resources.stringResource
import jellyfish.composeapp.generated.resources.Res
import jellyfish.composeapp.generated.resources.*

class SearchScreen : Screen {

    @Composable
    override fun Content() {
        val colors = LocalJellyFishColors.current
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = koinScreenModel<SearchScreenModel>()
        val state by screenModel.state.collectAsState()
        val focusRequester = remember { FocusRequester() }

        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colors.background)
        ) {
            // Search bar
            SearchBar(
                query = state.query,
                onQueryChange = screenModel::onQueryChange,
                onClear = screenModel::clearQuery,
                isLoading = state.isLoading,
                focusRequester = focusRequester
            )

            when {
                state.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = colors.primary)
                    }
                }

                state.error != null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = state.error ?: "Error",
                            color = colors.destructive
                        )
                    }
                }

                state.query.isBlank() && state.history.isNotEmpty() -> {
                    SearchHistory(
                        history = state.history,
                        onItemClick = screenModel::onHistoryItemClick,
                        onRemoveItem = screenModel::onRemoveHistoryItem,
                        onClearAll = screenModel::onClearHistory
                    )
                }

                state.hasSearched && state.results.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(Res.string.search_no_results),
                            color = colors.mutedForeground
                        )
                    }
                }

                state.results.isNotEmpty() -> {
                    SearchResults(
                        results = state.results,
                        onItemClick = { item -> navigateToDetail(navigator, item.id, item.type) }
                    )
                }

                else -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                Icons.Default.Search,
                                contentDescription = null,
                                modifier = Modifier.size(64.dp),
                                tint = colors.mutedForeground
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = stringResource(Res.string.search_hint),
                                color = colors.mutedForeground
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onClear: () -> Unit,
    isLoading: Boolean,
    focusRequester: FocusRequester
) {
    val colors = LocalJellyFishColors.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(colors.card)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            Icons.Default.Search,
            contentDescription = null,
            tint = colors.mutedForeground
        )

        Spacer(modifier = Modifier.width(12.dp))

        BasicTextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier
                .weight(1f)
                .focusRequester(focusRequester),
            textStyle = MaterialTheme.typography.bodyLarge.copy(color = colors.foreground),
            cursorBrush = SolidColor(colors.primary),
            singleLine = true,
            decorationBox = { innerTextField ->
                Box {
                    if (query.isEmpty()) {
                        Text(
                            stringResource(Res.string.search_placeholder),
                            color = colors.mutedForeground
                        )
                    }
                    innerTextField()
                }
            }
        )

        if (query.isNotEmpty()) {
            IconButton(onClick = onClear) {
                Icon(
                    Icons.Default.Clear,
                    contentDescription = stringResource(Res.string.common_clear),
                    tint = colors.mutedForeground
                )
            }
        }
    }
}

@Composable
private fun SearchHistory(
    history: List<String>,
    onItemClick: (String) -> Unit,
    onRemoveItem: (String) -> Unit,
    onClearAll: () -> Unit
) {
    val colors = LocalJellyFishColors.current

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                stringResource(Res.string.search_recent),
                style = MaterialTheme.typography.titleSmall,
                color = colors.mutedForeground
            )
            TextButton(onClick = onClearAll) {
                Text(stringResource(Res.string.search_clear_history), color = colors.primary)
            }
        }

        LazyColumn {
            items(history) { query ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onItemClick(query) }
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.History,
                        contentDescription = null,
                        tint = colors.mutedForeground
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        query,
                        modifier = Modifier.weight(1f),
                        color = colors.foreground
                    )
                    IconButton(onClick = { onRemoveItem(query) }) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = stringResource(Res.string.search_remove_content_description),
                            tint = colors.mutedForeground
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SearchResults(
    results: List<MediaItem>,
    onItemClick: (MediaItem) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 120.dp),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(results, key = { it.id }) { item ->
            SearchResultItem(
                item = item,
                onClick = { onItemClick(item) }
            )
        }
    }
}

@Composable
private fun SearchResultItem(
    item: MediaItem,
    onClick: () -> Unit
) {
    val colors = LocalJellyFishColors.current

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
    ) {
        AsyncImage(
            model = item.imageUrl,
            contentDescription = item.title,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(2f / 3f)
                .clip(RoundedCornerShape(8.dp))
                .background(colors.card),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = item.title,
            style = MaterialTheme.typography.bodySmall,
            color = colors.foreground,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )

        item.subtitle?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.labelSmall,
                color = colors.mutedForeground,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
