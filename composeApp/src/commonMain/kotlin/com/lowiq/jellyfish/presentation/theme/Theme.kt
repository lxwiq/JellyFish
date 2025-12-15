package com.lowiq.jellyfish.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val JellyfinPurple = Color(0xFF00A4DC)
private val JellyfinPurpleDark = Color(0xFF0088B9)

private val DarkColorScheme = darkColorScheme(
    primary = JellyfinPurple,
    onPrimary = Color.White,
    primaryContainer = JellyfinPurpleDark,
    secondary = JellyfinPurple,
    background = Color(0xFF101010),
    surface = Color(0xFF1A1A1A),
    onBackground = Color.White,
    onSurface = Color.White
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

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
