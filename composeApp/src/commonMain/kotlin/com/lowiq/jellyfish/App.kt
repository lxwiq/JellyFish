package com.lowiq.jellyfish

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import com.lowiq.jellyfish.di.appModule
import com.lowiq.jellyfish.di.dataModule
import com.lowiq.jellyfish.di.domainModule
import com.lowiq.jellyfish.presentation.screens.home.HomeScreen
import com.lowiq.jellyfish.presentation.theme.JellyFishTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinApplication

@Composable
@Preview
fun App() {
    KoinApplication(application = {
        modules(appModule, dataModule, domainModule)
    }) {
        JellyFishTheme {
            Surface(modifier = Modifier.fillMaxSize()) {
                Navigator(HomeScreen()) { navigator ->
                    SlideTransition(navigator)
                }
            }
        }
    }
}