package com.lowiq.jellyfish.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.lowiq.jellyfish.domain.model.MediaItem

@Composable
fun MediaGrid(
    items: List<MediaItem>,
    isLoading: Boolean,
    isLoadingMore: Boolean,
    onLoadMore: () -> Unit,
    onItemClick: (MediaItem) -> Unit,
    modifier: Modifier = Modifier
) {
    val gridState = rememberLazyGridState()

    // Detect when user scrolls near bottom to trigger load more
    val shouldLoadMore by remember {
        derivedStateOf {
            val layoutInfo = gridState.layoutInfo
            val totalItemsCount = layoutInfo.totalItemsCount
            val lastVisibleItemIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0

            // Trigger load more when user is within 6 items of the end
            lastVisibleItemIndex >= totalItemsCount - 6 && !isLoadingMore && totalItemsCount > 0
        }
    }

    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore) {
            onLoadMore()
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        // Show centered loading indicator when initially loading
        if (isLoading && items.isEmpty()) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(48.dp),
                color = Color(0xFFFAFAFA)
            )
        } else {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 120.dp),
                state = gridState,
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(
                    items = items,
                    key = { it.id }
                ) { item ->
                    MediaGridItem(
                        item = item,
                        onClick = { onItemClick(item) }
                    )
                }

                // Loading more indicator at bottom
                if (isLoadingMore) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(32.dp),
                                color = Color(0xFFFAFAFA)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MediaGridItem(
    item: MediaItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cardWidth = 120.dp
    val imageHeight = 180.dp  // 2:3 aspect ratio for posters

    Column(
        modifier = modifier
            .width(cardWidth)
            .clickable(onClick = onClick)
    ) {
        // Image container
        Box(
            modifier = Modifier
                .width(cardWidth)
                .height(imageHeight)
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFF27272A)),
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
                // Placeholder icon when no image
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = null,
                    tint = Color(0xFFA1A1AA),
                    modifier = Modifier.size(48.dp)
                )
            }
        }

        // Title
        Text(
            text = item.title,
            fontSize = 14.sp,
            color = Color(0xFFFAFAFA),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        )

        // Subtitle (if provided)
        item.subtitle?.let {
            Text(
                text = it,
                fontSize = 12.sp,
                color = Color(0xFFA1A1AA),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
            )
        }
    }
}
