package com.lowiq.jellyfish

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import com.lowiq.jellyfish.data.local.UserPreferencesStorage
import com.lowiq.jellyfish.presentation.locale.AppLocaleProvider
import com.lowiq.jellyfish.presentation.locale.customAppLocale
import com.lowiq.jellyfish.presentation.screens.splash.SplashScreen
import com.lowiq.jellyfish.presentation.theme.JellyFishTheme
import kotlinx.coroutines.flow.first
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject

@Composable
@Preview
fun App() {
    // Initialize locale from stored preferences
    val userPreferencesStorage: UserPreferencesStorage = koinInject()
    LaunchedEffect(Unit) {
        val storedLanguage = userPreferencesStorage.getLanguage().first()
        customAppLocale = if (storedLanguage == "system") null else storedLanguage
    }

    AppLocaleProvider {
        JellyFishTheme {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                Box(modifier = Modifier.safeDrawingPadding()) {
                    Navigator(SplashScreen()) { navigator ->
                        SlideTransition(navigator)
                    }
                }
            }
        }
    }
}