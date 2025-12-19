package com.lowiq.jellyfish.presentation.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * Shadcn/ui dark color palette
 * Based on OKLCH color space conversions to hex values
 */
data class JellyFishColors(
    // Background colors
    val background: Color = Color(0xFF0D0B14),        // oklch(0.129 0.042 264.695)
    val foreground: Color = Color(0xFFF8F8FC),        // oklch(0.984 0.003 247.858)

    // Surface colors
    val card: Color = Color(0xFF1C1A27),              // oklch(0.208 0.042 265.755)
    val cardForeground: Color = Color(0xFFF8F8FC),    // oklch(0.984 0.003 247.858)
    val popover: Color = Color(0xFF1C1A27),           // oklch(0.208 0.042 265.755)
    val popoverForeground: Color = Color(0xFFF8F8FC), // oklch(0.984 0.003 247.858)

    // Primary colors
    val primary: Color = Color(0xFFE8E8ED),           // oklch(0.929 0.013 255.508)
    val primaryForeground: Color = Color(0xFF1C1A27), // oklch(0.208 0.042 265.755)

    // Secondary colors
    val secondary: Color = Color(0xFF2A2835),         // oklch(0.279 0.041 260.031)
    val secondaryForeground: Color = Color(0xFFF8F8FC), // oklch(0.984 0.003 247.858)

    // Muted colors
    val muted: Color = Color(0xFF2A2835),             // oklch(0.279 0.041 260.031)
    val mutedForeground: Color = Color(0xFF9895A8),   // oklch(0.704 0.04 256.788)

    // Accent colors
    val accent: Color = Color(0xFF2A2835),            // oklch(0.279 0.041 260.031)
    val accentForeground: Color = Color(0xFFF8F8FC),  // oklch(0.984 0.003 247.858)

    // Destructive colors
    val destructive: Color = Color(0xFFE54D4D),       // oklch(0.704 0.191 22.216)

    // Border and input
    val border: Color = Color(0x1AFFFFFF),            // oklch(1 0 0 / 10%)
    val input: Color = Color(0x26FFFFFF),             // oklch(1 0 0 / 15%)
    val ring: Color = Color(0xFF7A7592)               // oklch(0.551 0.027 264.364)
)

val LocalJellyFishColors = staticCompositionLocalOf { JellyFishColors() }
