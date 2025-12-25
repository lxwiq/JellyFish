package com.lowiq.jellyfish.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.lowiq.jellyfish.presentation.theme.JellyFishTheme
import com.lowiq.jellyfish.presentation.theme.LocalJellyFishColors

data class NavigationItem(
    val icon: ImageVector,
    val contentDescription: String
)

private const val DOWNLOADS_INDEX = 3

private val navigationItems = listOf(
    NavigationItem(Icons.Default.Home, "Home"),
    NavigationItem(Icons.Default.Search, "Search"),
    NavigationItem(Icons.Default.Star, "Favorites"),
    NavigationItem(Icons.Default.Download, "Downloads"),
    NavigationItem(Icons.Default.Settings, "Settings")
)

@Composable
fun NavigationRail(
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit,
    activeDownloadCount: Int = 0,
    downloadProgress: Float = 0f,
    modifier: Modifier = Modifier
) {
    val shapes = JellyFishTheme.shapes
    Column(
        modifier = modifier
            .width(56.dp)
            .fillMaxHeight()
            .background(MaterialTheme.colorScheme.surface),
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Top),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        navigationItems.forEachIndexed { index, item ->
            if (index == DOWNLOADS_INDEX && activeDownloadCount > 0) {
                DownloadNavigationItem(
                    icon = item.icon,
                    contentDescription = item.contentDescription,
                    isSelected = index == selectedIndex,
                    onClick = { onItemSelected(index) },
                    shapes = shapes,
                    activeCount = activeDownloadCount,
                    progress = downloadProgress
                )
            } else {
                NavigationRailItem(
                    icon = item.icon,
                    contentDescription = item.contentDescription,
                    isSelected = index == selectedIndex,
                    onClick = { onItemSelected(index) },
                    shapes = shapes
                )
            }
        }
    }
}

@Composable
private fun NavigationRailItem(
    icon: ImageVector,
    contentDescription: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    shapes: com.lowiq.jellyfish.presentation.theme.JellyFishShapes,
    modifier: Modifier = Modifier
) {
    val colors = LocalJellyFishColors.current

    Box(
        modifier = modifier
            .size(56.dp)
            .clip(shapes.default)
            .background(
                if (isSelected) colors.secondary else Color.Transparent
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = if (isSelected) colors.foreground else colors.mutedForeground,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
private fun DownloadNavigationItem(
    icon: ImageVector,
    contentDescription: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    shapes: com.lowiq.jellyfish.presentation.theme.JellyFishShapes,
    activeCount: Int,
    progress: Float,
    modifier: Modifier = Modifier
) {
    val colors = LocalJellyFishColors.current

    Box(
        modifier = modifier
            .size(56.dp)
            .clip(shapes.default)
            .background(if (isSelected) colors.secondary else Color.Transparent)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Box(contentAlignment = Alignment.Center) {
            DownloadIndicator(
                activeCount = activeCount,
                averageProgress = progress,
                size = 40.dp,
                strokeWidth = 3.dp
            )
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
                tint = if (isSelected) colors.foreground else colors.mutedForeground,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
