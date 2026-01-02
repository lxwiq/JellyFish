package com.lowiq.jellyfish.presentation.screens.login

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.lowiq.jellyfish.domain.model.Server
import com.lowiq.jellyfish.presentation.screens.home.HomeScreen
import com.lowiq.jellyfish.presentation.screens.quickconnect.QuickConnectScreen
import com.lowiq.jellyfish.presentation.theme.JellyFishTheme
import jellyfish.composeapp.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import org.koin.core.parameter.parametersOf

data class LoginScreen(val server: Server) : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = koinScreenModel<LoginScreenModel> { parametersOf(server) }
        val state by screenModel.state.collectAsState()
        val colors = JellyFishTheme.colors
        val dimensions = JellyFishTheme.dimensions
        val shapes = JellyFishTheme.shapes

        val snackbarHostState = remember { SnackbarHostState() }

        LaunchedEffect(Unit) {
            screenModel.events.collect { event ->
                when (event) {
                    is LoginEvent.LoginSuccess -> navigator.replaceAll(HomeScreen())
                    is LoginEvent.NetworkError -> snackbarHostState.showSnackbar(
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
                    contentDescription = stringResource(Res.string.common_back),
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
                            imageVector = Icons.Default.Person,
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
                    text = stringResource(Res.string.login_title),
                    style = MaterialTheme.typography.headlineMedium,
                    color = colors.foreground
                )

                Spacer(modifier = Modifier.height(dimensions.spacing2))

                Text(
                    text = stringResource(Res.string.login_subtitle),
                    style = MaterialTheme.typography.bodyMedium,
                    color = colors.mutedForeground
                )

                Spacer(modifier = Modifier.height(dimensions.spacing8))

                // Username field
                StyledTextField(
                    value = state.username,
                    onValueChange = { screenModel.updateUsername(it) },
                    label = stringResource(Res.string.login_username_label),
                    leadingIcon = Icons.Default.Person,
                    isError = state.usernameError != null,
                    errorMessage = state.usernameError,
                    enabled = !state.isLoading
                )

                Spacer(modifier = Modifier.height(dimensions.spacing4))

                // Password field
                StyledTextField(
                    value = state.password,
                    onValueChange = { screenModel.updatePassword(it) },
                    label = stringResource(Res.string.login_password_label),
                    leadingIcon = Icons.Default.Lock,
                    isPassword = true,
                    passwordVisible = state.passwordVisible,
                    onTogglePasswordVisibility = { screenModel.togglePasswordVisibility() },
                    isError = state.passwordError != null,
                    errorMessage = state.passwordError,
                    enabled = !state.isLoading
                )

                Spacer(modifier = Modifier.height(dimensions.spacing6))

                // Sign In button
                Button(
                    onClick = { screenModel.login() },
                    enabled = !state.isLoading,
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
                        Text(stringResource(Res.string.login_sign_in_button))
                    }
                }

                Spacer(modifier = Modifier.height(dimensions.spacing4))

                // Divider with "ou"
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    HorizontalDivider(
                        modifier = Modifier.weight(1f),
                        color = colors.border
                    )
                    Text(
                        text = stringResource(Res.string.common_or),
                        modifier = Modifier.padding(horizontal = dimensions.spacing4),
                        style = MaterialTheme.typography.bodySmall,
                        color = colors.mutedForeground
                    )
                    HorizontalDivider(
                        modifier = Modifier.weight(1f),
                        color = colors.border
                    )
                }

                Spacer(modifier = Modifier.height(dimensions.spacing4))

                // Quick Connect button
                OutlinedButton(
                    onClick = { navigator.push(QuickConnectScreen(server)) },
                    enabled = !state.isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(dimensions.buttonHeightLg),
                    shape = shapes.button,
                    border = ButtonDefaults.outlinedButtonBorder(enabled = !state.isLoading).copy(
                        brush = SolidColor(colors.border)
                    ),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = colors.foreground
                    )
                ) {
                    Text(stringResource(Res.string.login_quick_connect_button))
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

@Composable
private fun StyledTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    leadingIcon: ImageVector,
    modifier: Modifier = Modifier,
    isPassword: Boolean = false,
    passwordVisible: Boolean = false,
    onTogglePasswordVisibility: (() -> Unit)? = null,
    isError: Boolean = false,
    errorMessage: String? = null,
    enabled: Boolean = true
) {
    val colors = JellyFishTheme.colors
    val dimensions = JellyFishTheme.dimensions
    val shapes = JellyFishTheme.shapes

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = colors.foreground,
            modifier = Modifier.padding(bottom = dimensions.spacing2)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensions.inputHeight + dimensions.spacing2)
                .background(colors.card, shapes.input)
                .border(
                    width = dimensions.borderWidth,
                    color = if (isError) colors.destructive else colors.input,
                    shape = shapes.input
                )
                .padding(horizontal = dimensions.spacing4)
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = null,
                    tint = colors.mutedForeground,
                    modifier = Modifier.size(dimensions.iconSize)
                )

                BasicTextField(
                    value = value,
                    onValueChange = onValueChange,
                    enabled = enabled,
                    singleLine = true,
                    textStyle = MaterialTheme.typography.bodyMedium.copy(
                        color = colors.foreground
                    ),
                    cursorBrush = SolidColor(colors.primary),
                    visualTransformation = if (isPassword && !passwordVisible) {
                        PasswordVisualTransformation()
                    } else {
                        VisualTransformation.None
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = dimensions.spacing3)
                )

                if (isPassword && onTogglePasswordVisibility != null) {
                    val passwordContentDescription = if (passwordVisible) {
                        stringResource(Res.string.login_password_hide)
                    } else {
                        stringResource(Res.string.login_password_show)
                    }
                    IconButton(
                        onClick = onTogglePasswordVisibility,
                        modifier = Modifier.size(dimensions.iconSizeLg)
                    ) {
                        Icon(
                            imageVector = if (passwordVisible) {
                                Icons.Default.VisibilityOff
                            } else {
                                Icons.Default.Visibility
                            },
                            contentDescription = passwordContentDescription,
                            tint = colors.mutedForeground,
                            modifier = Modifier.size(dimensions.iconSize)
                        )
                    }
                }
            }
        }

        if (isError && errorMessage != null) {
            Text(
                text = errorMessage,
                style = MaterialTheme.typography.bodySmall,
                color = colors.destructive,
                modifier = Modifier.padding(top = dimensions.spacing1)
            )
        }
    }
}
