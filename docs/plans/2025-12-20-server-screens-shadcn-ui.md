# Server Connection Screens - shadcn/ui Redesign

> **For Claude:** REQUIRED SUB-SKILL: Use superpowers:executing-plans to implement this plan task-by-task.

**Goal:** Refactor the 4 server connection screens (ServerList, AddServer, Login, QuickConnect) to use the JellyFish shadcn/ui theme with proper styling and add password visibility toggle.

**Architecture:** Replace Material3 default styling with JellyFishTheme tokens (colors, dimensions, shapes). Create reusable styled components. Add passwordVisible state to LoginScreenModel.

**Tech Stack:** Compose Multiplatform, JellyFishTheme, Material3 with custom styling

---

## Task 1: Add Password Visibility State to LoginScreenModel

**Files:**
- Modify: `composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/presentation/screens/login/LoginScreenModel.kt`

**Step 1: Add passwordVisible to LoginState**

```kotlin
data class LoginState(
    val username: String = "",
    val password: String = "",
    val passwordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val usernameError: String? = null,
    val passwordError: String? = null
)
```

**Step 2: Add toggle function to LoginScreenModel**

Add after `updatePassword` function:

```kotlin
fun togglePasswordVisibility() {
    _state.update { it.copy(passwordVisible = !it.passwordVisible) }
}
```

**Step 3: Build to verify**

Run: `./gradlew :composeApp:compileCommonMainKotlinMetadata`
Expected: BUILD SUCCESSFUL

**Step 4: Commit**

```bash
git add -A && git commit -m "feat(login): add password visibility state"
```

---

## Task 2: Refactor LoginScreen with shadcn/ui styling

**Files:**
- Modify: `composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/presentation/screens/login/LoginScreen.kt`

**Step 1: Replace entire LoginScreen.kt content**

```kotlin
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
                    text = "Connexion",
                    style = MaterialTheme.typography.headlineMedium,
                    color = colors.foreground
                )

                Spacer(modifier = Modifier.height(dimensions.spacing2))

                Text(
                    text = "Connectez-vous à votre compte",
                    style = MaterialTheme.typography.bodyMedium,
                    color = colors.mutedForeground
                )

                Spacer(modifier = Modifier.height(dimensions.spacing8))

                // Username field
                StyledTextField(
                    value = state.username,
                    onValueChange = { screenModel.updateUsername(it) },
                    label = "Nom d'utilisateur",
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
                    label = "Mot de passe",
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
                        Text("Se connecter")
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
                        text = "ou",
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
                    Text("Quick Connect")
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
                            contentDescription = if (passwordVisible) "Hide password" else "Show password",
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
```

**Step 2: Build to verify**

Run: `./gradlew :composeApp:compileCommonMainKotlinMetadata`
Expected: BUILD SUCCESSFUL

**Step 3: Commit**

```bash
git add -A && git commit -m "feat(login): redesign with shadcn/ui theme and password toggle"
```

---

## Task 3: Refactor AddServerScreen with shadcn/ui styling

**Files:**
- Modify: `composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/presentation/screens/addserver/AddServerScreen.kt`

**Step 1: Replace entire AddServerScreen.kt content**

```kotlin
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
```

**Step 2: Build to verify**

Run: `./gradlew :composeApp:compileCommonMainKotlinMetadata`
Expected: BUILD SUCCESSFUL

**Step 3: Commit**

```bash
git add -A && git commit -m "feat(addserver): redesign with shadcn/ui theme"
```

---

## Task 4: Refactor ServerListScreen with shadcn/ui styling

**Files:**
- Modify: `composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/presentation/screens/serverlist/ServerListScreen.kt`

**Step 1: Replace entire ServerListScreen.kt content**

```kotlin
package com.lowiq.jellyfish.presentation.screens.serverlist

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.lowiq.jellyfish.domain.model.Server
import com.lowiq.jellyfish.presentation.screens.addserver.AddServerScreen
import com.lowiq.jellyfish.presentation.screens.login.LoginScreen
import com.lowiq.jellyfish.presentation.theme.JellyFishTheme

class ServerListScreen : Screen {

    @Composable
    override fun Content() {
        val screenModel = koinScreenModel<ServerListScreenModel>()
        val state by screenModel.state.collectAsState()
        val navigator = LocalNavigator.currentOrThrow
        val colors = JellyFishTheme.colors
        val dimensions = JellyFishTheme.dimensions
        val shapes = JellyFishTheme.shapes

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colors.background)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Header
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(dimensions.spacing6)
                ) {
                    Text(
                        text = "Mes serveurs",
                        style = MaterialTheme.typography.headlineMedium,
                        color = colors.foreground,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                // Content
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                ) {
                    when {
                        state.isLoading -> {
                            CircularProgressIndicator(
                                modifier = Modifier.align(Alignment.Center),
                                color = colors.primary
                            )
                        }
                        state.servers.isEmpty() -> {
                            EmptyServerList(
                                onAddServerClick = { navigator.push(AddServerScreen()) }
                            )
                        }
                        else -> {
                            ServerList(
                                servers = state.servers,
                                onServerClick = { server ->
                                    screenModel.selectServer(server.id)
                                    navigator.push(LoginScreen(server))
                                },
                                onServerDelete = { server ->
                                    screenModel.deleteServer(server.id)
                                }
                            )
                        }
                    }
                }
            }

            // FAB
            FloatingActionButton(
                onClick = { navigator.push(AddServerScreen()) },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(dimensions.spacing6),
                containerColor = colors.primary,
                contentColor = colors.primaryForeground,
                shape = shapes.md
            ) {
                Icon(Icons.Default.Add, contentDescription = "Ajouter un serveur")
            }
        }
    }
}

@Composable
private fun EmptyServerList(
    onAddServerClick: () -> Unit
) {
    val colors = JellyFishTheme.colors
    val dimensions = JellyFishTheme.dimensions
    val shapes = JellyFishTheme.shapes

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(dimensions.spacing8),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Large server icon
        Surface(
            color = colors.secondary,
            shape = shapes.full,
            modifier = Modifier.size(80.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Default.Dns,
                    contentDescription = null,
                    tint = colors.mutedForeground,
                    modifier = Modifier.size(40.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(dimensions.spacing6))

        Text(
            text = "Aucun serveur",
            style = MaterialTheme.typography.titleLarge,
            color = colors.foreground
        )

        Spacer(modifier = Modifier.height(dimensions.spacing2))

        Text(
            text = "Ajoutez votre premier serveur Jellyfin",
            style = MaterialTheme.typography.bodyMedium,
            color = colors.mutedForeground
        )

        Spacer(modifier = Modifier.height(dimensions.spacing8))

        Button(
            onClick = onAddServerClick,
            modifier = Modifier.height(dimensions.buttonHeightLg),
            shape = shapes.button,
            colors = ButtonDefaults.buttonColors(
                containerColor = colors.primary,
                contentColor = colors.primaryForeground
            )
        ) {
            Icon(
                Icons.Default.Add,
                contentDescription = null,
                modifier = Modifier.size(dimensions.iconSize)
            )
            Spacer(modifier = Modifier.width(dimensions.spacing2))
            Text("Ajouter un serveur")
        }
    }
}

@Composable
private fun ServerList(
    servers: List<Server>,
    onServerClick: (Server) -> Unit,
    onServerDelete: (Server) -> Unit
) {
    val dimensions = JellyFishTheme.dimensions

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(dimensions.spacing4),
        verticalArrangement = Arrangement.spacedBy(dimensions.spacing3)
    ) {
        items(
            items = servers,
            key = { it.id }
        ) { server ->
            ServerListItem(
                server = server,
                onClick = { onServerClick(server) },
                onDelete = { onServerDelete(server) }
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ServerListItem(
    server: Server,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    val colors = JellyFishTheme.colors
    val dimensions = JellyFishTheme.dimensions
    val shapes = JellyFishTheme.shapes
    var showDeleteDialog by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = onClick,
                onLongClick = { showDeleteDialog = true }
            ),
        color = colors.card,
        shape = shapes.md
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensions.spacing4),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Server icon
            Surface(
                color = colors.secondary,
                shape = shapes.full,
                modifier = Modifier.size(dimensions.avatarLg)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Dns,
                        contentDescription = null,
                        tint = colors.mutedForeground,
                        modifier = Modifier.size(dimensions.iconSizeLg)
                    )
                }
            }

            Spacer(modifier = Modifier.width(dimensions.spacing4))

            // Server info
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = server.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = colors.foreground,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = server.url,
                    style = MaterialTheme.typography.bodySmall,
                    color = colors.mutedForeground,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                server.username?.let { username ->
                    Text(
                        text = username,
                        style = MaterialTheme.typography.bodySmall,
                        color = colors.mutedForeground
                    )
                }
            }

            // Delete button
            IconButton(
                onClick = { showDeleteDialog = true }
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Supprimer",
                    tint = colors.destructive
                )
            }
        }
    }

    if (showDeleteDialog) {
        DeleteServerDialog(
            serverName = server.name,
            onConfirm = {
                onDelete()
                showDeleteDialog = false
            },
            onDismiss = { showDeleteDialog = false }
        )
    }
}

@Composable
private fun DeleteServerDialog(
    serverName: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    val colors = JellyFishTheme.colors
    val shapes = JellyFishTheme.shapes

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = colors.card,
        shape = shapes.lg,
        title = {
            Text(
                text = "Supprimer le serveur",
                color = colors.foreground
            )
        },
        text = {
            Text(
                text = "Voulez-vous vraiment supprimer \"$serverName\" ?",
                color = colors.mutedForeground
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Supprimer", color = colors.destructive)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Annuler", color = colors.foreground)
            }
        }
    )
}
```

**Step 2: Build to verify**

Run: `./gradlew :composeApp:compileCommonMainKotlinMetadata`
Expected: BUILD SUCCESSFUL

**Step 3: Commit**

```bash
git add -A && git commit -m "feat(serverlist): redesign with shadcn/ui theme"
```

---

## Task 5: Refactor QuickConnectScreen with shadcn/ui styling

**Files:**
- Modify: `composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/presentation/screens/quickconnect/QuickConnectScreen.kt`

**Step 1: Replace entire QuickConnectScreen.kt content**

```kotlin
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
```

**Step 2: Build to verify**

Run: `./gradlew :composeApp:compileCommonMainKotlinMetadata`
Expected: BUILD SUCCESSFUL

**Step 3: Commit**

```bash
git add -A && git commit -m "feat(quickconnect): redesign with shadcn/ui theme"
```

---

## Task 6: Final Build and Test

**Step 1: Full Android build**

Run: `./gradlew :composeApp:assembleDebug`
Expected: BUILD SUCCESSFUL

**Step 2: Run Desktop to visually verify**

Run: `./gradlew :composeApp:run`
Expected: App launches, navigate through all 4 screens to verify styling

**Step 3: Final commit if any adjustments**

If adjustments needed, commit them with appropriate message.
