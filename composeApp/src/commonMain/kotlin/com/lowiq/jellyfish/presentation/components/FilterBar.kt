package com.lowiq.jellyfish.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lowiq.jellyfish.domain.model.SortOption
import com.lowiq.jellyfish.presentation.theme.JellyFishTheme
import com.lowiq.jellyfish.presentation.theme.LocalJellyFishColors

@Composable
fun FilterBar(
    sortOption: SortOption,
    onSortChanged: (SortOption) -> Unit,
    availableGenres: List<String>,
    selectedGenre: String?,
    onGenreChanged: (String?) -> Unit,
    availableYears: List<Int>,
    selectedYear: Int?,
    onYearChanged: (Int?) -> Unit,
    showWatched: Boolean?,  // null = all, true = watched only, false = unwatched only
    onWatchedChanged: (Boolean?) -> Unit,
    showFavoritesOnly: Boolean,
    onFavoritesChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Sort dropdown
        item {
            FilterDropdown(
                label = "Sort: ${sortOption.displayName}",
                options = SortOption.entries.map { it.displayName },
                onOptionSelected = { index ->
                    onSortChanged(SortOption.entries[index])
                }
            )
        }

        // Genre dropdown
        item {
            FilterDropdown(
                label = selectedGenre ?: "All Genres",
                options = listOf("All Genres") + availableGenres,
                onOptionSelected = { index ->
                    onGenreChanged(if (index == 0) null else availableGenres[index - 1])
                }
            )
        }

        // Year dropdown
        item {
            FilterDropdown(
                label = selectedYear?.toString() ?: "All Years",
                options = listOf("All Years") + availableYears.map { it.toString() },
                onOptionSelected = { index ->
                    onYearChanged(if (index == 0) null else availableYears[index - 1])
                }
            )
        }

        // Watched toggle chips
        item {
            FilterChip(
                name = "All",
                isSelected = showWatched == null,
                onClick = { onWatchedChanged(null) }
            )
        }
        item {
            FilterChip(
                name = "Watched",
                isSelected = showWatched == true,
                onClick = { onWatchedChanged(true) }
            )
        }
        item {
            FilterChip(
                name = "Unwatched",
                isSelected = showWatched == false,
                onClick = { onWatchedChanged(false) }
            )
        }

        // Favorites toggle
        item {
            FilterChip(
                name = "Favorites",
                isSelected = showFavoritesOnly,
                onClick = { onFavoritesChanged(!showFavoritesOnly) }
            )
        }
    }
}

@Composable
private fun FilterDropdown(
    label: String,
    options: List<String>,
    onOptionSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalJellyFishColors.current
    val shapes = JellyFishTheme.shapes
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        // Dropdown trigger
        Box(
            modifier = Modifier
                .clip(shapes.full)
                .background(colors.secondary)
                .clickable { expanded = true }
                .padding(horizontal = 16.dp, vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            androidx.compose.foundation.layout.Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = label,
                    fontSize = 14.sp,
                    color = colors.foreground
                )
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    tint = colors.foreground
                )
            }
        }

        // Dropdown menu
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .background(colors.secondary)
                .width(200.dp)
        ) {
            options.forEachIndexed { index, option ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = option,
                            fontSize = 14.sp,
                            color = colors.foreground
                        )
                    },
                    onClick = {
                        onOptionSelected(index)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun FilterChip(
    name: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalJellyFishColors.current
    val shapes = JellyFishTheme.shapes
    val backgroundColor = if (isSelected) colors.foreground else colors.secondary
    val textColor = if (isSelected) colors.background else colors.foreground

    Text(
        text = name,
        fontSize = 14.sp,
        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
        color = textColor,
        modifier = modifier
            .clip(shapes.full)
            .background(backgroundColor)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    )
}
