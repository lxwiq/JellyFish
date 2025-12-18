package com.lowiq.jellyfish.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color

private val JellyfinPurple = Color(0xFF00A4DC)
private val JellyfinPurpleDark = Color(0xFF0088B9)

private val jellyFishColors = JellyFishColors()

private val DarkColorScheme = darkColorScheme(
    primary = JellyfinPurple,
    onPrimary = Color.White,
    primaryContainer = JellyfinPurpleDark,
    secondary = JellyfinPurple,
    background = jellyFishColors.background,
    surface = jellyFishColors.surface,
    onBackground = jellyFishColors.textPrimary,
    onSurface = jellyFishColors.textPrimary,
    outline = jellyFishColors.border
)

private val LightColorScheme = lightColorScheme(
    primary = JellyfinPurple,
    onPrimary = Color.White,
    primaryContainer = JellyfinPurpleDark,
    secondary = JellyfinPurple,
    background = Color(0xFFF5F5F5),
    surface = Color.White,
    onBackground = Color.Black,
    onSurface = Color.Black
)

@Composable
fun JellyFishTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    CompositionLocalProvider(LocalJellyFishColors provides jellyFishColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            content = content
        )
    }
}
