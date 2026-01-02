package com.lowiq.jellyfish.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lowiq.jellyfish.presentation.theme.LocalJellyFishColors
import jellyfish.composeapp.generated.resources.Res
import jellyfish.composeapp.generated.resources.action_add_to_favorites
import jellyfish.composeapp.generated.resources.action_download
import jellyfish.composeapp.generated.resources.action_mark_unwatched
import jellyfish.composeapp.generated.resources.action_mark_watched
import jellyfish.composeapp.generated.resources.action_remove_from_favorites
import jellyfish.composeapp.generated.resources.action_share
import jellyfish.composeapp.generated.resources.common_play
import org.jetbrains.compose.resources.stringResource

@Composable
fun ActionButtonsRow(
    onPlay: () -> Unit,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    isWatched: Boolean,
    onToggleWatched: () -> Unit,
    onDownload: () -> Unit,
    onShare: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalJellyFishColors.current

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Play button - Primary style
        Button(
            onClick = onPlay,
            colors = ButtonDefaults.buttonColors(
                containerColor = colors.primary,
                contentColor = colors.primaryForeground
            ),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(stringResource(Res.string.common_play))
        }

        // Favorite button - Outline style
        OutlinedButton(
            onClick = onToggleFavorite,
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = if (isFavorite) colors.destructive else colors.foreground
            ),
            contentPadding = PaddingValues(12.dp),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.size(44.dp)
        ) {
            Icon(
                imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = stringResource(if (isFavorite) Res.string.action_remove_from_favorites else Res.string.action_add_to_favorites),
                modifier = Modifier.size(20.dp)
            )
        }

        // Watched button - Outline style
        OutlinedButton(
            onClick = onToggleWatched,
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = if (isWatched) colors.primary else colors.foreground
            ),
            contentPadding = PaddingValues(12.dp),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.size(44.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = stringResource(if (isWatched) Res.string.action_mark_unwatched else Res.string.action_mark_watched),
                modifier = Modifier.size(20.dp)
            )
        }

        // Download button - Outline style
        OutlinedButton(
            onClick = onDownload,
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = colors.foreground
            ),
            contentPadding = PaddingValues(12.dp),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.size(44.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Download,
                contentDescription = stringResource(Res.string.action_download),
                modifier = Modifier.size(20.dp)
            )
        }

        // Share button - Outline style
        OutlinedButton(
            onClick = onShare,
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = colors.foreground
            ),
            contentPadding = PaddingValues(12.dp),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.size(44.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Share,
                contentDescription = stringResource(Res.string.action_share),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
