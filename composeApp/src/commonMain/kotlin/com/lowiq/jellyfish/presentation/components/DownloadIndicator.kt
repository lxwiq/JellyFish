package com.lowiq.jellyfish.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lowiq.jellyfish.presentation.theme.LocalJellyFishColors

@Composable
fun DownloadIndicator(
    activeCount: Int,
    averageProgress: Float,
    modifier: Modifier = Modifier,
    size: Dp = 40.dp,
    strokeWidth: Dp = 3.dp
) {
    if (activeCount == 0) return

    val colors = LocalJellyFishColors.current
    val progressColor = colors.primary
    val trackColor = colors.muted
    val badgeColor = colors.destructive

    Box(
        modifier = modifier
            .size(size)
            .semantics {
                contentDescription = "$activeCount téléchargement${if (activeCount > 1) "s" else ""} en cours, ${(averageProgress * 100).toInt()}%"
            }
    ) {
        // Circular progress arc
        Canvas(modifier = Modifier.size(size)) {
            val stroke = strokeWidth.toPx()
            val diameter = size.toPx() - stroke
            val topLeft = Offset(stroke / 2, stroke / 2)

            // Track (background circle)
            drawArc(
                color = trackColor,
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = topLeft,
                size = Size(diameter, diameter),
                style = Stroke(width = stroke, cap = StrokeCap.Round)
            )

            // Progress arc
            drawArc(
                color = progressColor,
                startAngle = -90f,
                sweepAngle = 360f * averageProgress.coerceIn(0f, 1f),
                useCenter = false,
                topLeft = topLeft,
                size = Size(diameter, diameter),
                style = Stroke(width = stroke, cap = StrokeCap.Round)
            )
        }

        // Badge with count
        Box(
            modifier = Modifier
                .size(16.dp)
                .align(Alignment.TopEnd)
                .offset(x = 2.dp, y = (-2).dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.size(16.dp)) {
                drawCircle(color = badgeColor)
            }
            Text(
                text = if (activeCount > 9) "9+" else activeCount.toString(),
                color = Color.White,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
