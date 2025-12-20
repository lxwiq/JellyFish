package com.lowiq.jellyfish.presentation.screens.addserver

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Language
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.lowiq.jellyfish.presentation.screens.login.LoginScreen
import com.lowiq.jellyfish.presentation.theme.JellyFishTheme

class AddServerScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = koinScreenModel<AddServerScreenModel>()
        val state by screenModel.state.collectAsState()
        val colors = JellyFishTheme.colors
        val dimensions = JellyFishTheme.dimensions
        val shapes = JellyFishTheme.shapes

        val snackbarHostState = remember { SnackbarHostState() }

        LaunchedEffect(Unit) {
            screenModel.events.collect { event ->
                when (event) {
                    is AddServerEvent.ServerAdded -> navigator.push(LoginScreen(event.server))
                    is AddServerEvent.NetworkError -> snackbarHostState.showSnackbar(
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
                // Title
                Text(
                    text = "Ajouter un serveur",
                    style = MaterialTheme.typography.headlineMedium,
                    color = colors.foreground
                )

                Spacer(modifier = Modifier.height(dimensions.spacing2))

                Text(
                    text = "Entrez l'adresse de votre serveur Jellyfin",
                    style = MaterialTheme.typography.bodyMedium,
                    color = colors.mutedForeground
                )

                Spacer(modifier = Modifier.height(dimensions.spacing8))

                // URL field
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Adresse du serveur",
                        style = MaterialTheme.typography.bodySmall,
                        color = colors.foreground,
                        modifier = Modifier.padding(bottom = dimensions.spacing2)
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(dimensions.buttonHeightLg)
                            .background(colors.card, shapes.input)
                            .border(
                                width = dimensions.borderWidth,
                                color = if (state.error != null) colors.destructive else colors.input,
                                shape = shapes.input
                            )
                            .padding(horizontal = dimensions.spacing4)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxSize(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Language,
                                contentDescription = null,
                                tint = colors.mutedForeground,
                                modifier = Modifier.size(dimensions.iconSize)
                            )

                            BasicTextField(
                                value = state.url,
                                onValueChange = { screenModel.updateUrl(it) },
                                enabled = !state.isLoading,
                                singleLine = true,
                                textStyle = MaterialTheme.typography.bodyMedium.copy(
                                    color = colors.foreground
                                ),
                                cursorBrush = SolidColor(colors.primary),
                                decorationBox = { innerTextField ->
                                    Box {
                                        if (state.url.isEmpty()) {
                                            Text(
                                                text = "https://jellyfin.exemple.com",
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = colors.mutedForeground
                                            )
                                        }
                                        innerTextField()
                                    }
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(horizontal = dimensions.spacing3)
                            )
                        }
                    }

                    if (state.error != null) {
                        Text(
                            text = state.error!!,
                            style = MaterialTheme.typography.bodySmall,
                            color = colors.destructive,
                            modifier = Modifier.padding(top = dimensions.spacing1)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(dimensions.spacing8))

                // Continue button
                Button(
                    onClick = { screenModel.submitServer() },
                    enabled = !state.isLoading && state.url.isNotBlank(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(dimensions.buttonHeightLg),
                    shape = shapes.button,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colors.primary,
                        contentColor = colors.primaryForeground
                    )
                ) {
                    if (state.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(dimensions.iconSize),
                            color = colors.primaryForeground,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text("Continuer")
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
