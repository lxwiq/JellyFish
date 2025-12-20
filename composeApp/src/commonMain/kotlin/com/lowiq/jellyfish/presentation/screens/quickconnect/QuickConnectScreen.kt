package com.lowiq.jellyfish.presentation.screens.quickconnect

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.lowiq.jellyfish.domain.model.Server
import com.lowiq.jellyfish.presentation.screens.home.HomeScreen
import com.lowiq.jellyfish.presentation.theme.JellyFishTheme
import org.koin.core.parameter.parametersOf

data class QuickConnectScreen(val server: Server) : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = koinScreenModel<QuickConnectScreenModel> { parametersOf(server) }
        val state by screenModel.state.collectAsState()
        val colors = JellyFishTheme.colors
        val dimensions = JellyFishTheme.dimensions
        val shapes = JellyFishTheme.shapes

        val snackbarHostState = remember { SnackbarHostState() }

        DisposableEffect(Unit) {
            onDispose { screenModel.cancel() }
        }

        LaunchedEffect(Unit) {
            screenModel.events.collect { event ->
                when (event) {
                    is QuickConnectEvent.AuthSuccess -> navigator.replaceAll(HomeScreen())
                    is QuickConnectEvent.NetworkError -> snackbarHostState.showSnackbar(
                        message = event.message,
                        duration = SnackbarDuration.Short
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colors.background)
        ) {
            // Back button
            IconButton(
                onClick = { navigator.pop() },
                modifier = Modifier
                    .padding(dimensions.spacing4)
                    .align(Alignment.TopStart)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = colors.foreground
                )
            }

            // Main content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = dimensions.spacing6),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Server badge
                Surface(
                    color = colors.secondary,
                    shape = shapes.full,
                    modifier = Modifier.padding(bottom = dimensions.spacing6)
                ) {
                    Row(
                        modifier = Modifier.padding(
                            horizontal = dimensions.spacing4,
                            vertical = dimensions.spacing2
                        ),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(dimensions.spacing2)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Dns,
                            contentDescription = null,
                            tint = colors.mutedForeground,
                            modifier = Modifier.size(dimensions.iconSizeSm)
                        )
                        Text(
                            text = screenModel.serverName,
                            style = MaterialTheme.typography.bodySmall,
                            color = colors.mutedForeground
                        )
                    }
                }

                // Title
                Text(
                    text = "Quick Connect",
                    style = MaterialTheme.typography.headlineMedium,
                    color = colors.foreground
                )

                Spacer(modifier = Modifier.height(dimensions.spacing8))

                when {
                    state.isInitializing -> {
                        CircularProgressIndicator(color = colors.primary)
                        Spacer(modifier = Modifier.height(dimensions.spacing4))
                        Text(
                            text = "Initialisation...",
                            style = MaterialTheme.typography.bodyMedium,
                            color = colors.mutedForeground
                        )
                    }

                    state.error != null -> {
                        Text(
                            text = state.error ?: "Une erreur est survenue",
                            style = MaterialTheme.typography.bodyLarge,
                            color = colors.destructive,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(dimensions.spacing6))
                        Button(
                            onClick = { screenModel.retry() },
                            shape = shapes.button,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = colors.primary,
                                contentColor = colors.primaryForeground
                            )
                        ) {
                            Text("Réessayer")
                        }
                    }

                    state.code != null -> {
                        // Code card
                        Surface(
                            color = colors.card,
                            shape = shapes.lg,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(dimensions.spacing8),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = state.code ?: "",
                                    style = MaterialTheme.typography.displayLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = colors.primary,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(dimensions.spacing6))

                        Text(
                            text = "Entrez ce code sur votre serveur Jellyfin",
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            color = colors.mutedForeground
                        )

                        Spacer(modifier = Modifier.height(dimensions.spacing8))

                        if (state.isPolling) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(dimensions.iconSizeXl),
                                color = colors.primary,
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.height(dimensions.spacing4))
                            Text(
                                text = "En attente d'autorisation...",
                                style = MaterialTheme.typography.bodySmall,
                                color = colors.mutedForeground
                            )
                        }
                    }
                }
            }

            // Snackbar
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}
