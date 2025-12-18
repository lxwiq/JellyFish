package com.lowiq.jellyfish.presentation.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * Shadcn-inspired dark color palette
 */
data class JellyFishColors(
    val background: Color = Color(0xFF09090B),
    val surface: Color = Color(0xFF18181B),
    val border: Color = Color(0xFF27272A),
    val textPrimary: Color = Color(0xFFFAFAFA),
    val textSecondary: Color = Color(0xFFA1A1AA),
    val textTertiary: Color = Color(0xFF71717A)
)

val LocalJellyFishColors = staticCompositionLocalOf { JellyFishColors() }
