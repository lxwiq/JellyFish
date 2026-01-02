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
import jellyfish.composeapp.generated.resources.*
import org.jetbrains.compose.resources.stringResource

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
                        text = stringResource(Res.string.server_list_title),
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

            // FAB - only show when there are servers (empty state has its own button)
            if (!state.isLoading && state.servers.isNotEmpty()) {
                FloatingActionButton(
                    onClick = { navigator.push(AddServerScreen()) },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(dimensions.spacing6),
                    containerColor = colors.primary,
                    contentColor = colors.primaryForeground,
                    shape = shapes.md
                ) {
                    Icon(Icons.Default.Add, contentDescription = stringResource(Res.string.server_list_add_button))
                }
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
            text = stringResource(Res.string.server_list_empty_title),
            style = MaterialTheme.typography.titleLarge,
            color = colors.foreground
        )

        Spacer(modifier = Modifier.height(dimensions.spacing2))

        Text(
            text = stringResource(Res.string.server_list_empty_subtitle),
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
            Text(stringResource(Res.string.server_list_add_button))
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
                    contentDescription = stringResource(Res.string.server_list_delete_content_description),
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
                text = stringResource(Res.string.server_delete_dialog_title),
                color = colors.foreground
            )
        },
        text = {
            Text(
                text = stringResource(Res.string.server_delete_dialog_message, serverName),
                color = colors.mutedForeground
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(stringResource(Res.string.common_delete), color = colors.destructive)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(Res.string.common_cancel), color = colors.foreground)
            }
        }
    )
}
