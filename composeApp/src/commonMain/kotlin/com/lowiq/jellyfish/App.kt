package com.lowiq.jellyfish

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import com.lowiq.jellyfish.presentation.screens.splash.SplashScreen
import com.lowiq.jellyfish.presentation.theme.JellyFishTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    JellyFishTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            Navigator(SplashScreen()) { navigator ->
                SlideTransition(navigator)
            }
        }
    }
}