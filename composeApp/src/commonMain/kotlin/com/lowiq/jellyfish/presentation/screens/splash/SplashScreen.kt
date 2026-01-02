package com.lowiq.jellyfish.presentation.screens.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.lowiq.jellyfish.domain.usecase.CheckSessionUseCase
import com.lowiq.jellyfish.presentation.screens.home.HomeScreen
import com.lowiq.jellyfish.presentation.screens.serverlist.ServerListScreen
import org.koin.compose.koinInject

class SplashScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val checkSessionUseCase: CheckSessionUseCase = koinInject()

        LaunchedEffect(Unit) {
            when (val sessionState = checkSessionUseCase()) {
                is CheckSessionUseCase.SessionState.Authenticated -> {
                    navigator.replaceAll(HomeScreen())
                }
                is CheckSessionUseCase.SessionState.HasServers -> {
                    navigator.replaceAll(ServerListScreen())
                }
                is CheckSessionUseCase.SessionState.NoServers -> {
                    navigator.replaceAll(ServerListScreen())
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF0F0915)),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
