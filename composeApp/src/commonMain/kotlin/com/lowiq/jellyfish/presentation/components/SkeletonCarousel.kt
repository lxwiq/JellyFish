package com.lowiq.jellyfish.presentation.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.lowiq.jellyfish.presentation.theme.JellyFishTheme
import com.lowiq.jellyfish.presentation.theme.LocalJellyFishColors

@Composable
fun SkeletonCarousel(
    modifier: Modifier = Modifier,
    itemCount: Int = 5
) {
    val colors = LocalJellyFishColors.current
    val shapes = JellyFishTheme.shapes

    val shimmerColors = listOf(
        colors.secondary,
        colors.muted,
        colors.secondary
    )

    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer"
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(translateAnim - 500f, translateAnim - 500f),
        end = Offset(translateAnim, translateAnim)
    )

    Column(modifier = modifier) {
        // Title skeleton
        Box(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .width(120.dp)
                .height(24.dp)
                .clip(shapes.sm)
                .background(brush)
        )

        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            userScrollEnabled = false
        ) {
            items(itemCount) {
                SkeletonCard(brush = brush, shapes = shapes)
            }
        }
    }
}

@Composable
private fun SkeletonCard(
    brush: Brush,
    shapes: com.lowiq.jellyfish.presentation.theme.JellyFishShapes,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.width(160.dp)
    ) {
        // Image skeleton
        Box(
            modifier = Modifier
                .width(160.dp)
                .height(90.dp)
                .clip(shapes.default)
                .background(brush)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Title skeleton
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(16.dp)
                .clip(shapes.sm)
                .background(brush)
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Subtitle skeleton
        Box(
            modifier = Modifier
                .width(100.dp)
                .height(14.dp)
                .clip(shapes.sm)
                .background(brush)
        )
    }
}
