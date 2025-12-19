package com.lowiq.jellyfish.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable

private val jellyFishColors = JellyFishColors()
private val jellyFishDimensions = JellyFishDimensions()
private val jellyFishShapes = JellyFishShapes(jellyFishDimensions)

private val DarkColorScheme = darkColorScheme(
    // Primary colors - used for prominent UI elements
    primary = jellyFishColors.primary,
    onPrimary = jellyFishColors.primaryForeground,
    primaryContainer = jellyFishColors.accent,
    onPrimaryContainer = jellyFishColors.accentForeground,

    // Secondary colors - used for less prominent UI elements
    secondary = jellyFishColors.secondary,
    onSecondary = jellyFishColors.secondaryForeground,
    secondaryContainer = jellyFishColors.muted,
    onSecondaryContainer = jellyFishColors.mutedForeground,

    // Tertiary colors
    tertiary = jellyFishColors.accent,
    onTertiary = jellyFishColors.accentForeground,

    // Background colors
    background = jellyFishColors.background,
    onBackground = jellyFishColors.foreground,

    // Surface colors
    surface = jellyFishColors.card,
    onSurface = jellyFishColors.cardForeground,
    surfaceVariant = jellyFishColors.muted,
    onSurfaceVariant = jellyFishColors.mutedForeground,

    // Error colors
    error = jellyFishColors.destructive,
    onError = jellyFishColors.foreground,

    // Other colors
    outline = jellyFishColors.border,
    outlineVariant = jellyFishColors.input
)

private val LightColorScheme = lightColorScheme(
    // For now, we only support dark theme
    // Light theme can be added later if needed
    primary = jellyFishColors.primary,
    onPrimary = jellyFishColors.primaryForeground,
    background = jellyFishColors.background,
    onBackground = jellyFishColors.foreground,
    surface = jellyFishColors.card,
    onSurface = jellyFishColors.cardForeground
)

@Composable
fun JellyFishTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    CompositionLocalProvider(
        LocalJellyFishColors provides jellyFishColors,
        LocalJellyFishDimensions provides jellyFishDimensions,
        LocalJellyFishShapes provides jellyFishShapes
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            content = content
        )
    }
}

/**
 * Accessor object for JellyFish theme values.
 * Use this to access colors, dimensions, and shapes in composables.
 *
 * Example:
 * ```
 * val colors = JellyFishTheme.colors
 * val dimensions = JellyFishTheme.dimensions
 * val shapes = JellyFishTheme.shapes
 * ```
 */
object JellyFishTheme {
    val colors: JellyFishColors
        @Composable
        @ReadOnlyComposable
        get() = LocalJellyFishColors.current

    val dimensions: JellyFishDimensions
        @Composable
        @ReadOnlyComposable
        get() = LocalJellyFishDimensions.current

    val shapes: JellyFishShapes
        @Composable
        @ReadOnlyComposable
        get() = LocalJellyFishShapes.current
}
