package com.lowiq.jellyfish.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.lowiq.jellyfish.domain.model.Episode
import com.lowiq.jellyfish.presentation.theme.LocalJellyFishColors

@Composable
fun EpisodeCard(
    episode: Episode,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalJellyFishColors.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(90.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(colors.card)
            .clickable(onClick = onClick)
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Thumbnail (16:9)
        Box(
            modifier = Modifier
                .width(120.dp)
                .fillMaxHeight()
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

        // Text content
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center
        ) {
            // Episode number and title
            Text(
                text = "${episode.episodeNumber}. ${episode.title}",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = colors.foreground,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            // Duration
            episode.runtime?.let { runtime ->
                Text(
                    text = runtime,
                    fontSize = 12.sp,
                    color = colors.mutedForeground,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }

            // Description
            episode.overview?.let { overview ->
                Text(
                    text = overview,
                    fontSize = 12.sp,
                    color = colors.mutedForeground,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}
