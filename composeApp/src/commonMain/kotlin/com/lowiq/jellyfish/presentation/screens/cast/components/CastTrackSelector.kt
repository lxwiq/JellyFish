package com.lowiq.jellyfish.presentation.screens.cast.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ClosedCaption
import androidx.compose.material.icons.filled.ClosedCaptionOff
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lowiq.jellyfish.domain.cast.CastAudioTrack
import com.lowiq.jellyfish.domain.cast.CastSubtitleTrack

@Composable
fun CastTrackSelector(
    subtitleTracks: List<CastSubtitleTrack>,
    audioTracks: List<CastAudioTrack>,
    currentSubtitleIndex: Int?,
    currentAudioIndex: Int?,
    currentSpeed: Float,
    onSubtitleSelected: (Int?) -> Unit,
    onAudioSelected: (Int) -> Unit,
    onSpeedSelected: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        if (subtitleTracks.isNotEmpty()) {
            TrackSelectorButton(
                icon = {
                    Icon(
                        imageVector = if (currentSubtitleIndex != null)
                            Icons.Filled.ClosedCaption
                        else
                            Icons.Filled.ClosedCaptionOff,
                        contentDescription = "Subtitles"
                    )
                },
                label = "Subtitles",
                items = listOf("Off") + subtitleTracks.map { it.name },
                selectedIndex = (currentSubtitleIndex ?: -1) + 1,
                onItemSelected = { index ->
                    onSubtitleSelected(if (index == 0) null else index - 1)
                }
            )
        }

        if (audioTracks.size > 1) {
            TrackSelectorButton(
                icon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                        contentDescription = "Audio"
                    )
                },
                label = "Audio",
                items = audioTracks.map { it.name },
                selectedIndex = currentAudioIndex ?: 0,
                onItemSelected = onAudioSelected
            )
        }

        val speeds = listOf(0.5f, 0.75f, 1f, 1.25f, 1.5f, 2f)
        TrackSelectorButton(
            icon = {
                Icon(
                    imageVector = Icons.Filled.Speed,
                    contentDescription = "Speed"
                )
            },
            label = "${currentSpeed}x",
            items = speeds.map { "${it}x" },
            selectedIndex = speeds.indexOf(currentSpeed).coerceAtLeast(0),
            onItemSelected = { index -> onSpeedSelected(speeds[index]) }
        )
    }
}

@Composable
private fun TrackSelectorButton(
    icon: @Composable () -> Unit,
    label: String,
    items: List<String>,
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            modifier = Modifier.clickable { expanded = true },
            color = MaterialTheme.colorScheme.surfaceVariant,
            shape = MaterialTheme.shapes.medium
        ) {
            Column(
                modifier = Modifier.padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                icon()
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            items.forEachIndexed { index, item ->
                DropdownMenuItem(
                    text = { Text(item) },
                    onClick = {
                        onItemSelected(index)
                        expanded = false
                    },
                    leadingIcon = if (index == selectedIndex) {
                        { Icon(Icons.Filled.Check, contentDescription = null) }
                    } else null
                )
            }
        }
    }
}
