package com.lowiq.jellyfish

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import com.lowiq.jellyfish.presentation.screens.splash.SplashScreen
import com.lowiq.jellyfish.presentation.theme.JellyFishTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

private val BackgroundColor = Color(0xFF09090B)

@Composable
@Preview
fun App() {
    JellyFishTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding(),
            color = BackgroundColor
        ) {
            Navigator(SplashScreen()) { navigator ->
                SlideTransition(navigator)
            }
        }
    }
}