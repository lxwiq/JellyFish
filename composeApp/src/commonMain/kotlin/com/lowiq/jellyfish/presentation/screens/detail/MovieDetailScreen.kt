package com.lowiq.jellyfish.presentation.screens.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Star
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
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
import com.lowiq.jellyfish.domain.model.MediaType
import com.lowiq.jellyfish.presentation.components.ActionButtonsRow
import com.lowiq.jellyfish.presentation.components.CastCarousel
import com.lowiq.jellyfish.presentation.components.GenreBadges
import com.lowiq.jellyfish.presentation.components.MediaCarousel
import com.lowiq.jellyfish.presentation.components.MediaCarouselItem
import com.lowiq.jellyfish.presentation.components.QualitySelectionDialog
import com.lowiq.jellyfish.presentation.screens.downloads.DownloadsScreen
import com.lowiq.jellyfish.presentation.screens.player.VideoPlayerScreen
import com.lowiq.jellyfish.presentation.theme.LocalJellyFishColors
import org.koin.core.parameter.parametersOf

class MovieDetailScreen(private val itemId: String) : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val colors = LocalJellyFishColors.current
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = koinScreenModel<MovieDetailScreenModel> { parametersOf(itemId) }
        val state by screenModel.state.collectAsState()

        LaunchedEffect(Unit) {
            screenModel.events.collect { event ->
                when (event) {
                    is MovieDetailEvent.PlayVideo -> {
                        navigator.push(
                            VideoPlayerScreen(
                                itemId = event.itemId,
                                title = event.title,
                                startPositionMs = event.startPositionMs
                            )
                        )
                    }
                    is MovieDetailEvent.NavigateToDownloads -> {
                        navigator.push(DownloadsScreen())
                    }
                }
            }
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { },
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
                        IconButton(onClick = { /* TODO: Show menu */ }) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "More options",
                                tint = colors.foreground
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent
                    )
                )
            },
            containerColor = colors.background
        ) { paddingValues ->
            when {
                state.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = colors.foreground)
                    }
                }
                state.error != null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = state.error ?: "An error occurred",
                            color = colors.destructive
                        )
                    }
                }
                else -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    ) {
                        // Backdrop with gradient
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(280.dp)
                        ) {
                            if (state.backdropUrl != null) {
                                AsyncImage(
                                    model = ImageRequest.Builder(LocalPlatformContext.current)
                                        .data(state.backdropUrl)
                                        .crossfade(true)
                                        .build(),
                                    contentDescription = state.title,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                            // Gradient overlay
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        Brush.verticalGradient(
                                            colors = listOf(
                                                Color.Transparent,
                                                colors.background
                                            ),
                                            startY = 100f
                                        )
                                    )
                            )
                        }

                        // Content
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                        ) {
                            // Title
                            Text(
                                text = state.title,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = colors.foreground
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            // Metadata row
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                state.year?.let { year ->
                                    Text(
                                        text = year,
                                        fontSize = 14.sp,
                                        color = colors.mutedForeground
                                    )
                                }
                                state.runtime?.let { runtime ->
                                    Text(text = "•", color = colors.mutedForeground)
                                    Text(
                                        text = runtime,
                                        fontSize = 14.sp,
                                        color = colors.mutedForeground
                                    )
                                }
                                state.rating?.let { rating ->
                                    Text(text = "•", color = colors.mutedForeground)
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = null,
                                        tint = Color(0xFFFFD700),
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        text = String.format("%.1f", rating),
                                        fontSize = 14.sp,
                                        color = colors.mutedForeground
                                    )
                                }
                            }

                            // Genres
                            if (state.genres.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(12.dp))
                                GenreBadges(genres = state.genres)
                            }

                            // Studio
                            state.studio?.let { studio ->
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = studio,
                                    fontSize = 14.sp,
                                    color = colors.mutedForeground
                                )
                            }

                            // Action buttons
                            Spacer(modifier = Modifier.height(16.dp))
                            ActionButtonsRow(
                                onPlay = { screenModel.onPlay() },
                                isFavorite = state.isFavorite,
                                onToggleFavorite = { screenModel.toggleFavorite() },
                                isWatched = state.isWatched,
                                onToggleWatched = { screenModel.toggleWatched() },
                                onDownload = { screenModel.onDownload() },
                                onShare = { screenModel.onShare() }
                            )

                            // Overview
                            state.overview?.let { overview ->
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = overview,
                                    fontSize = 14.sp,
                                    color = colors.foreground,
                                    lineHeight = 22.sp
                                )
                            }
                        }

                        // Cast section
                        if (state.cast.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(24.dp))
                            CastCarousel(
                                title = "Cast",
                                cast = state.cast,
                                onPersonClick = { /* TODO: Navigate to person */ }
                            )
                        }

                        // Similar movies section
                        if (state.similarItems.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(24.dp))
                            MediaCarousel(
                                title = "Similar Movies",
                                items = state.similarItems.map { item ->
                                    MediaCarouselItem(
                                        id = item.id,
                                        title = item.title,
                                        subtitle = item.subtitle,
                                        imageUrl = item.imageUrl,
                                        progress = item.progress,
                                        isPoster = item.isPoster,
                                        type = item.type
                                    )
                                },
                                onItemClick = { id, type ->
                                    navigator.push(MovieDetailScreen(id))
                                }
                            )
                        }

                        Spacer(modifier = Modifier.height(32.dp))
                    }

                    if (state.showQualityDialog && state.availableQualities.isNotEmpty()) {
                        QualitySelectionDialog(
                            qualities = state.availableQualities,
                            onDismiss = { screenModel.dismissQualityDialog() },
                            onConfirm = { quality, dontAsk -> screenModel.onQualitySelected(quality, dontAsk) }
                        )
                    }
                }
            }
        }
    }
}
