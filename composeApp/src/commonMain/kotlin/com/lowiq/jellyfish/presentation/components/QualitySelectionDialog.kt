package com.lowiq.jellyfish.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.lowiq.jellyfish.domain.model.QualityOption
import com.lowiq.jellyfish.presentation.theme.LocalJellyFishColors
import jellyfish.composeapp.generated.resources.Res
import jellyfish.composeapp.generated.resources.common_cancel
import jellyfish.composeapp.generated.resources.quality_dialog_title
import jellyfish.composeapp.generated.resources.quality_dont_ask
import jellyfish.composeapp.generated.resources.quality_download
import jellyfish.composeapp.generated.resources.quality_recommended
import org.jetbrains.compose.resources.stringResource

@Composable
fun QualitySelectionDialog(
    qualities: List<QualityOption>,
    recommendedQuality: String = "1080p",
    onDismiss: () -> Unit,
    onConfirm: (QualityOption, Boolean) -> Unit
) {
    val colors = LocalJellyFishColors.current
    var selectedQuality by remember { mutableStateOf(qualities.find { it.label == recommendedQuality } ?: qualities.firstOrNull()) }
    var dontAskAgain by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(Res.string.quality_dialog_title)) },
        text = {
            Column {
                qualities.forEach { quality ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = selectedQuality == quality,
                                onClick = { selectedQuality = quality },
                                role = Role.RadioButton
                            )
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedQuality == quality,
                            onClick = null
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(quality.label)
                                if (quality.label == recommendedQuality) {
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        stringResource(Res.string.quality_recommended),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = colors.primary
                                    )
                                }
                            }
                            if (quality.estimatedSizeBytes > 0) {
                                Text(
                                    "~${formatBytes(quality.estimatedSizeBytes)}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = colors.mutedForeground
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { dontAskAgain = !dontAskAgain },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = dontAskAgain,
                        onCheckedChange = { dontAskAgain = it }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        stringResource(Res.string.quality_dont_ask, selectedQuality?.label ?: ""),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    selectedQuality?.let { onConfirm(it, dontAskAgain) }
                },
                enabled = selectedQuality != null
            ) {
                Text(stringResource(Res.string.quality_download))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(Res.string.common_cancel))
            }
        }
    )
}

private fun formatBytes(bytes: Long): String {
    return when {
        bytes >= 1_000_000_000 -> {
            val gb = bytes / 1_000_000_000.0
            "${(gb * 10).toLong() / 10.0} Go"
        }
        bytes >= 1_000_000 -> "${bytes / 1_000_000} Mo"
        else -> "${bytes / 1_000} Ko"
    }
}
