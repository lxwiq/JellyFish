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
import com.lowiq.jellyfish.domain.player.QualityOption
import com.lowiq.jellyfish.presentation.theme.LocalJellyFishColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerSettingsSheet(
    qualityOptions: List<QualityOption>,
    currentSpeed: Float,
    onSelectQuality: (Int) -> Unit,
    onSelectSpeed: (Float) -> Unit,
    onDismiss: () -> Unit
) {
    val colors = LocalJellyFishColors.current
    val sheetState = rememberModalBottomSheetState()

    val speedOptions = listOf(0.5f, 0.75f, 1f, 1.25f, 1.5f, 2f)

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
            // Quality section
            if (qualityOptions.isNotEmpty()) {
                Text(
                    text = "Quality",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = colors.foreground
                )
                Spacer(modifier = Modifier.height(12.dp))

                qualityOptions.forEach { option ->
                    SettingsRow(
                        label = option.label,
                        subtitle = option.bitrate?.let { "${it / 1_000_000} Mbps" },
                        isSelected = option.isSelected,
                        onClick = { onSelectQuality(option.index) }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(color = colors.border)
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Playback speed section
            Text(
                text = "Playback Speed",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = colors.foreground
            )
            Spacer(modifier = Modifier.height(12.dp))

            speedOptions.forEach { speed ->
                SettingsRow(
                    label = when (speed) {
                        1f -> "Normal"
                        else -> "${speed}x"
                    },
                    subtitle = null,
                    isSelected = currentSpeed == speed,
                    onClick = { onSelectSpeed(speed) }
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun SettingsRow(
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
                Text(
                    text = it,
                    fontSize = 14.sp,
                    color = colors.mutedForeground
                )
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
