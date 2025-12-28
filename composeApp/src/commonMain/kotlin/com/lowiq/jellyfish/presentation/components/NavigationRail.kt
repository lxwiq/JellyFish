package com.lowiq.jellyfish.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.lowiq.jellyfish.domain.model.Library
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
    expanded: Boolean = false,
    libraries: List<Library> = emptyList(),
    onLibraryClick: ((Library) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val shapes = JellyFishTheme.shapes
    val colors = LocalJellyFishColors.current
    val railWidth = if (expanded) 200.dp else 56.dp

    Column(
        modifier = modifier
            .width(railWidth)
            .fillMaxHeight()
            .background(MaterialTheme.colorScheme.surface),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = if (expanded) Alignment.Start else Alignment.CenterHorizontally
    ) {
        // Navigation items
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = if (expanded) Alignment.Start else Alignment.CenterHorizontally
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
                        progress = downloadProgress,
                        expanded = expanded,
                        label = item.contentDescription
                    )
                } else {
                    NavigationRailItem(
                        icon = item.icon,
                        contentDescription = item.contentDescription,
                        isSelected = index == selectedIndex,
                        onClick = { onItemSelected(index) },
                        shapes = shapes,
                        expanded = expanded,
                        label = item.contentDescription
                    )
                }
            }

            // Libraries section (only when expanded)
            if (expanded && libraries.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Libraries",
                    style = MaterialTheme.typography.labelSmall,
                    color = colors.mutedForeground,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )

                libraries.forEach { library ->
                    LibraryItem(
                        library = library,
                        onClick = { onLibraryClick?.invoke(library) },
                        shapes = shapes
                    )
                }
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
    expanded: Boolean = false,
    label: String = "",
    modifier: Modifier = Modifier
) {
    val colors = LocalJellyFishColors.current

    if (expanded) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .height(48.dp)
                .padding(horizontal = 8.dp)
                .clip(shapes.default)
                .background(if (isSelected) colors.secondary else Color.Transparent)
                .clickable(onClick = onClick)
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
                tint = if (isSelected) colors.foreground else colors.mutedForeground,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = if (isSelected) colors.foreground else colors.mutedForeground,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    } else {
        Box(
            modifier = modifier
                .size(56.dp)
                .clip(shapes.default)
                .background(if (isSelected) colors.secondary else Color.Transparent)
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
    expanded: Boolean = false,
    label: String = "",
    modifier: Modifier = Modifier
) {
    val colors = LocalJellyFishColors.current

    if (expanded) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .height(48.dp)
                .padding(horizontal = 8.dp)
                .clip(shapes.default)
                .background(if (isSelected) colors.secondary else Color.Transparent)
                .clickable(onClick = onClick)
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(contentAlignment = Alignment.Center) {
                DownloadIndicator(
                    activeCount = activeCount,
                    averageProgress = progress,
                    size = 32.dp,
                    strokeWidth = 2.dp
                )
                Icon(
                    imageVector = icon,
                    contentDescription = contentDescription,
                    tint = if (isSelected) colors.foreground else colors.mutedForeground,
                    modifier = Modifier.size(16.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = if (isSelected) colors.foreground else colors.mutedForeground,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    } else {
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
}

@Composable
private fun LibraryItem(
    library: Library,
    onClick: () -> Unit,
    shapes: com.lowiq.jellyfish.presentation.theme.JellyFishShapes,
    modifier: Modifier = Modifier
) {
    val colors = LocalJellyFishColors.current
    val icon = getLibraryIcon(library.type)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(40.dp)
            .padding(horizontal = 8.dp)
            .clip(shapes.default)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = library.name,
            tint = colors.mutedForeground,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = library.name,
            style = MaterialTheme.typography.bodySmall,
            color = colors.mutedForeground,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

private fun getLibraryIcon(type: String): ImageVector {
    return when (type.lowercase()) {
        "movies" -> Icons.Default.Movie
        "tvshows" -> Icons.Default.Tv
        "music" -> Icons.Default.MusicNote
        else -> Icons.Default.Folder
    }
}
