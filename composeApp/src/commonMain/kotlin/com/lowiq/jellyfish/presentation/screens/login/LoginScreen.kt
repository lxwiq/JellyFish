package com.lowiq.jellyfish.presentation.screens.login

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.lowiq.jellyfish.domain.model.Server
import com.lowiq.jellyfish.presentation.screens.home.HomeScreen
import com.lowiq.jellyfish.presentation.screens.quickconnect.QuickConnectScreen
import kotlinx.coroutines.launch
import org.koin.core.parameter.parametersOf

data class LoginScreen(val server: Server) : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = koinScreenModel<LoginScreenModel> { parametersOf(server) }
        val state by screenModel.state.collectAsState()

        val snackbarHostState = remember { SnackbarHostState() }
        val scope = rememberCoroutineScope()

        // Collect events
        LaunchedEffect(Unit) {
            screenModel.events.collect { event ->
                when (event) {
                    is LoginEvent.LoginSuccess -> {
                        navigator.replaceAll(HomeScreen())
                    }
                    is LoginEvent.NetworkError -> {
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
                    title = { Text(screenModel.serverName) },
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
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Username field
                OutlinedTextField(
                    value = state.username,
                    onValueChange = { screenModel.updateUsername(it) },
                    label = { Text("Username") },
                    isError = state.usernameError != null,
                    supportingText = state.usernameError?.let { { Text(it) } },
                    enabled = !state.isLoading,
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Password field
                OutlinedTextField(
                    value = state.password,
                    onValueChange = { screenModel.updatePassword(it) },
                    label = { Text("Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    isError = state.passwordError != null,
                    supportingText = state.passwordError?.let { { Text(it) } },
                    enabled = !state.isLoading,
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Sign In button
                Button(
                    onClick = { screenModel.login() },
                    enabled = !state.isLoading,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (state.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Text("Sign In")
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Quick Connect button
                TextButton(
                    onClick = { navigator.push(QuickConnectScreen(server)) },
                    enabled = !state.isLoading
                ) {
                    Text("Use Quick Connect")
                }
            }
        }
    }
}
