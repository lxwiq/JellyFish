package com.lowiq.jellyfish.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lowiq.jellyfish.presentation.theme.LocalJellyFishColors

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun GenreBadges(
    genres: List<String>,
    modifier: Modifier = Modifier
) {
    val colors = LocalJellyFishColors.current

    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        genres.forEach { genre ->
            Text(
                text = genre,
                fontSize = 12.sp,
                color = colors.secondaryForeground,
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(colors.secondary)
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            )
        }
    }
}
