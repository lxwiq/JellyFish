package com.lowiq.jellyfish.presentation.screens.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
import com.lowiq.jellyfish.presentation.components.ActionButtonsRow
import com.lowiq.jellyfish.presentation.components.CastCarousel
import com.lowiq.jellyfish.presentation.theme.LocalJellyFishColors
import org.koin.core.parameter.parametersOf

class EpisodeDetailScreen(private val itemId: String) : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val colors = LocalJellyFishColors.current
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = koinScreenModel<EpisodeDetailScreenModel> { parametersOf(itemId) }
        val state by screenModel.state.collectAsState()

        // Handle navigation events
        LaunchedEffect(Unit) {
            screenModel.events.collect { event ->
                when (event) {
                    is EpisodeDetailEvent.NavigateToSeries -> {
                        navigator.push(SeriesDetailScreen(event.seriesId))
                    }
                    is EpisodeDetailEvent.NavigateToEpisode -> {
                        // Events are handled by reloading in the same screen
                    }
                }
            }
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        if (!state.isLoading && state.seriesName.isNotEmpty()) {
                            Text(
                                text = state.seriesName,
                                color = colors.foreground,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.clickable { screenModel.navigateToSeries() }
                            )
                        }
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
                        IconButton(onClick = { /* TODO: Show menu */ }) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "More options",
                                tint = colors.foreground
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = colors.background
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
                            .padding(paddingValues)
                    ) {
                        // Large 16:9 thumbnail with play overlay
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(220.dp)
                                .padding(16.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(colors.secondary),
                            contentAlignment = Alignment.Center
                        ) {
                            if (state.thumbnailUrl != null) {
                                AsyncImage(
                                    model = ImageRequest.Builder(LocalPlatformContext.current)
                                        .data(state.thumbnailUrl)
                                        .crossfade(true)
                                        .build(),
                                    contentDescription = state.title,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = null,
                                    tint = colors.mutedForeground,
                                    modifier = Modifier.size(64.dp)
                                )
                            }

                            // Play overlay button
                            Box(
                                modifier = Modifier
                                    .size(60.dp)
                                    .clip(RoundedCornerShape(30.dp))
                                    .background(colors.primary.copy(alpha = 0.9f))
                                    .clickable { screenModel.onPlay() },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = "Play",
                                    tint = colors.primaryForeground,
                                    modifier = Modifier.size(36.dp)
                                )
                            }

                            // Progress bar overlay at bottom
                            state.progress?.let { progress ->
                                if (progress > 0f) {
                                    LinearProgressIndicator(
                                        progress = { progress },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(4.dp)
                                            .align(Alignment.BottomCenter),
                                        color = colors.primary,
                                        trackColor = colors.secondary.copy(alpha = 0.5f)
                                    )
                                }
                            }
                        }

                        // Content
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                        ) {
                            // Episode title: "S{season} E{episode} • {title}"
                            // Skip redundant title if it's just "Episode X"
                            val displayTitle = if (state.title.equals("Episode ${state.episodeNumber}", ignoreCase = true) ||
                                state.title.equals("Episode", ignoreCase = true)) {
                                "S${state.seasonNumber} E${state.episodeNumber}"
                            } else {
                                "S${state.seasonNumber} E${state.episodeNumber} • ${state.title}"
                            }
                            Text(
                                text = displayTitle,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = colors.foreground
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            // Metadata row: runtime + rating
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                state.runtime?.let { runtime ->
                                    Text(
                                        text = runtime,
                                        fontSize = 14.sp,
                                        color = colors.mutedForeground
                                    )
                                }
                                state.rating?.let { rating ->
                                    if (state.runtime != null) {
                                        Text(text = "•", color = colors.mutedForeground)
                                    }
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

                            // Episode navigation row
                            Spacer(modifier = Modifier.height(24.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Previous button
                                OutlinedButton(
                                    onClick = { screenModel.navigateToPreviousEpisode() },
                                    enabled = state.previousEpisodeId != null,
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        contentColor = colors.foreground,
                                        disabledContentColor = colors.mutedForeground
                                    ),
                                    shape = RoundedCornerShape(8.dp),
                                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp)
                                ) {
                                    Text("◀ Previous")
                                }

                                // Current episode indicator
                                Text(
                                    text = "${state.episodeNumber} / ${state.totalEpisodes}",
                                    fontSize = 14.sp,
                                    color = colors.mutedForeground,
                                    fontWeight = FontWeight.Medium
                                )

                                // Next button
                                OutlinedButton(
                                    onClick = { screenModel.navigateToNextEpisode() },
                                    enabled = state.nextEpisodeId != null,
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        contentColor = colors.foreground,
                                        disabledContentColor = colors.mutedForeground
                                    ),
                                    shape = RoundedCornerShape(8.dp),
                                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp)
                                ) {
                                    Text("Next ▶")
                                }
                            }
                        }

                        // Guest stars carousel
                        if (state.guestStars.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(24.dp))
                            CastCarousel(
                                title = "Guest Stars",
                                cast = state.guestStars,
                                onPersonClick = { /* TODO: Navigate to person */ }
                            )
                        }

                        // "More from Season {n}" episode carousel
                        if (state.seasonEpisodes.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(24.dp))
                            Column {
                                Text(
                                    text = "More from Season ${state.seasonNumber}",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = colors.foreground,
                                    modifier = Modifier.padding(horizontal = 16.dp)
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                LazyRow(
                                    contentPadding = PaddingValues(horizontal = 16.dp),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    items(state.seasonEpisodes) { episode ->
                                        EpisodeSmallCard(
                                            episode = episode,
                                            isSelected = episode.id == itemId,
                                            onClick = { screenModel.navigateToEpisode(episode.id) }
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(32.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun EpisodeSmallCard(
    episode: com.lowiq.jellyfish.domain.model.Episode,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalJellyFishColors.current

    Column(
        modifier = modifier
            .width(160.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSelected) colors.secondary else colors.card)
            .clickable(onClick = onClick)
            .padding(8.dp)
    ) {
        // Thumbnail (16:9)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(colors.secondary),
            contentAlignment = Alignment.Center
        ) {
            if (episode.thumbnailUrl != null) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalPlatformContext.current)
                        .data(episode.thumbnailUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = episode.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = null,
                    tint = colors.mutedForeground,
                    modifier = Modifier.size(32.dp)
                )
            }

            // Progress bar overlay at bottom
            episode.progress?.let { progress ->
                if (progress > 0f) {
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(3.dp)
                            .align(Alignment.BottomCenter),
                        color = colors.primary,
                        trackColor = colors.secondary.copy(alpha = 0.5f)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Episode number and title
        Text(
            text = "${episode.episodeNumber}. ${episode.title}",
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = colors.foreground,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )

        // Runtime
        episode.runtime?.let { runtime ->
            Text(
                text = runtime,
                fontSize = 11.sp,
                color = colors.mutedForeground,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}
