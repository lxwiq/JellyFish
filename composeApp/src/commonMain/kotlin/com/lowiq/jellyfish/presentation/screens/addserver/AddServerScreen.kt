package com.lowiq.jellyfish.presentation.screens.addserver

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.lowiq.jellyfish.presentation.screens.login.LoginScreen

class AddServerScreen : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = koinScreenModel<AddServerScreenModel>()
        val state by screenModel.state.collectAsState()

        val snackbarHostState = remember { SnackbarHostState() }

        // Collect events
        LaunchedEffect(Unit) {
            screenModel.events.collect { event ->
                when (event) {
                    is AddServerEvent.ServerAdded -> {
                        navigator.push(LoginScreen(event.server))
                    }
                    is AddServerEvent.NetworkError -> {
                        snackbarHostState.showSnackbar(
                            message = event.message,
                            duration = SnackbarDuration.Short
                        )
                    }
                }
            }
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Add Server") },
                    navigationIcon = {
                        IconButton(onClick = { navigator.pop() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    }
                )
            },
            snackbarHost = { SnackbarHost(snackbarHostState) }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Spacer(modifier = Modifier.height(32.dp))

                OutlinedTextField(
                    value = state.url,
                    onValueChange = { screenModel.updateUrl(it) },
                    label = { Text("Server URL") },
                    placeholder = { Text("https://jellyfin.example.com") },
                    isError = state.error != null,
                    enabled = !state.isLoading,
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                // Show inline error below TextField
                if (state.error != null) {
                    Text(
                        text = state.error!!,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Button(
                    onClick = { screenModel.submitServer() },
                    enabled = !state.isLoading,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (state.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Text("Connect")
                    }
                }
            }
        }
    }
}
