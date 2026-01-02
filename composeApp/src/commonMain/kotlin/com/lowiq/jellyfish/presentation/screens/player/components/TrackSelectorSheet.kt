package com.lowiq.jellyfish.presentation.screens.player.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lowiq.jellyfish.domain.player.AudioTrack
import com.lowiq.jellyfish.domain.player.SubtitleTrack
import com.lowiq.jellyfish.presentation.theme.LocalJellyFishColors
import jellyfish.composeapp.generated.resources.Res
import jellyfish.composeapp.generated.resources.player_audio
import jellyfish.composeapp.generated.resources.player_subtitles
import jellyfish.composeapp.generated.resources.player_subtitles_off
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackSelectorSheet(
    audioTracks: List<AudioTrack>,
    subtitleTracks: List<SubtitleTrack>,
    onSelectAudioTrack: (Int) -> Unit,
    onSelectSubtitleTrack: (Int) -> Unit,
    onDisableSubtitles: () -> Unit,
    onDismiss: () -> Unit
) {
    val colors = LocalJellyFishColors.current
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = colors.card
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Audio section
            if (audioTracks.isNotEmpty()) {
                Text(
                    text = stringResource(Res.string.player_audio),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = colors.foreground
                )
                Spacer(modifier = Modifier.height(12.dp))

                audioTracks.forEach { track ->
                    TrackRow(
                        label = track.label,
                        subtitle = buildString {
                            track.codec?.let { append(it.uppercase()) }
                            track.channels?.let {
                                if (isNotEmpty()) append(" • ")
                                append(when(it) {
                                    1 -> "Mono"
                                    2 -> "Stereo"
                                    6 -> "5.1"
                                    8 -> "7.1"
                                    else -> "$it ch"
                                })
                            }
                        },
                        isSelected = track.isSelected,
                        onClick = { onSelectAudioTrack(track.index) }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(color = colors.border)
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Subtitles section
            Text(
                text = stringResource(Res.string.player_subtitles),
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = colors.foreground
            )
            Spacer(modifier = Modifier.height(12.dp))

            // Off option
            val noSubtitleSelected = subtitleTracks.none { it.isSelected }
            TrackRow(
                label = stringResource(Res.string.player_subtitles_off),
                subtitle = null,
                isSelected = noSubtitleSelected,
                onClick = onDisableSubtitles
            )

            subtitleTracks.forEach { track ->
                TrackRow(
                    label = track.label,
                    subtitle = track.language,
                    isSelected = track.isSelected,
                    onClick = { onSelectSubtitleTrack(track.index) }
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun TrackRow(
    label: String,
    subtitle: String?,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val colors = LocalJellyFishColors.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = isSelected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(
                selectedColor = colors.primary,
                unselectedColor = colors.mutedForeground
            )
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                fontSize = 16.sp,
                color = colors.foreground
            )
            subtitle?.let {
                if (it.isNotEmpty()) {
                    Text(
                        text = it,
                        fontSize = 14.sp,
                        color = colors.mutedForeground
                    )
                }
            }
        }
        if (isSelected) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = colors.primary
            )
        }
    }
}
