package com.lowiq.jellyfish.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade

@Composable
fun MediaCard(
    title: String,
    subtitle: String? = null,
    imageUrl: String? = null,
    progress: Float? = null,
    isPoster: Boolean = false,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Poster: vertical (2:3 ratio), Episode: horizontal (16:9 ratio)
    val cardWidth = if (isPoster) 120.dp else 160.dp
    val imageHeight = if (isPoster) 180.dp else 90.dp
    // Fixed height for text area to ensure consistent card heights
    val textAreaHeight = if (isPoster) 56.dp else 56.dp

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
            if (imageUrl != null) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalPlatformContext.current)
                        .data(imageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = title,
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

            // Progress bar overlay
            progress?.let {
                LinearProgressIndicator(
                    progress = { it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(3.dp)
                        .align(Alignment.BottomCenter),
                    color = Color(0xFFFAFAFA),
                    trackColor = Color(0xFF27272A)
                )
            }
        }

        // Text area with fixed height for consistent card sizes
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(textAreaHeight)
                .padding(top = 8.dp)
        ) {
            // Title
            Text(
                text = title,
                fontSize = 14.sp,
                color = Color(0xFFFAFAFA),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
            )

            // Subtitle (if provided)
            subtitle?.let {
                Text(
                    text = it,
                    fontSize = 12.sp,
                    color = Color(0xFFA1A1AA),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 2.dp)
                )
            }
        }
    }
}
