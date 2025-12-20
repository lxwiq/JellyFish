package com.lowiq.jellyfish.presentation.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.lowiq.jellyfish.domain.model.AdminUser
import com.lowiq.jellyfish.domain.model.LogEntry
import com.lowiq.jellyfish.domain.model.ScheduledTask
import com.lowiq.jellyfish.domain.model.TaskState
import com.lowiq.jellyfish.presentation.screens.serverlist.ServerListScreen
import com.lowiq.jellyfish.presentation.theme.LocalJellyFishColors
import kotlinx.coroutines.launch

class SettingsScreen : Screen {

    @Composable
    override fun Content() {
        val colors = LocalJellyFishColors.current
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = koinScreenModel<SettingsScreenModel>()
        val state by screenModel.state.collectAsState()
        val scope = rememberCoroutineScope()

        var showLogoutDialog by remember { mutableStateOf(false) }

        LaunchedEffect(Unit) {
            screenModel.events.collect { event ->
                when (event) {
                    is SettingsEvent.LoggedOut -> {
                        navigator.replaceAll(ServerListScreen())
                    }
                    is SettingsEvent.Error -> {
                        // TODO: Show snackbar with error
                    }
                    is SettingsEvent.Success -> {
                        // TODO: Show snackbar with success message
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colors.background)
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navigator.pop() }) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Retour",
                        tint = colors.foreground
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Paramètres",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.foreground
                )
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Account Section
                item {
                    SectionTitle("Compte")
                }
                item {
                    AccountSection(
                        userName = state.user?.name ?: "",
                        serverName = state.server?.name ?: "",
                        isLoggingOut = state.isLoggingOut,
                        onLogoutClick = { showLogoutDialog = true }
                    )
                }

                // Playback Section
                item {
                    SectionTitle("Lecture")
                }
                item {
                    PlaybackSection(
                        streamingQuality = state.streamingQuality,
                        onStreamingQualityChange = screenModel::setStreamingQuality,
                        downloadQuality = state.downloadQuality,
                        onDownloadQualityChange = screenModel::setDownloadQuality,
                        audioLanguage = state.preferredAudioLanguage,
                        onAudioLanguageChange = screenModel::setPreferredAudioLanguage,
                        subtitleLanguage = state.preferredSubtitleLanguage,
                        onSubtitleLanguageChange = screenModel::setPreferredSubtitleLanguage
                    )
                }

                // Storage Section
                item {
                    SectionTitle("Stockage")
                }
                item {
                    StorageSection(
                        usedStorageBytes = state.usedStorageBytes,
                        storageLimitMb = state.storageLimitMb,
                        onStorageLimitChange = screenModel::setStorageLimit,
                        isClearingCache = state.isClearingCache,
                        onClearCache = screenModel::clearCache
                    )
                }

                // About Section
                item {
                    SectionTitle("À propos")
                }
                item {
                    AboutSection()
                }

                // Admin Section (only if admin)
                if (state.isAdmin) {
                    item {
                        SectionTitle("Administration")
                    }
                    item {
                        AdminSection(
                            adminState = state.adminSection,
                            onLoadUsers = screenModel::loadUsers,
                            onShowCreateUserDialog = screenModel::showCreateUserDialog,
                            onCreateUser = screenModel::createUser,
                            onShowDeleteUserDialog = screenModel::showDeleteUserDialog,
                            onDeleteUser = screenModel::deleteUser,
                            onRefreshLibrary = screenModel::refreshLibrary,
                            onLoadLogs = screenModel::loadLogs,
                            onLoadTasks = screenModel::loadTasks,
                            onRunTask = screenModel::runTask
                        )
                    }
                }
            }
        }

        // Logout Confirmation Dialog
        if (showLogoutDialog) {
            AlertDialog(
                onDismissRequest = { showLogoutDialog = false },
                title = { Text("Déconnexion") },
                text = { Text("Voulez-vous vraiment vous déconnecter ?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showLogoutDialog = false
                            screenModel.logout()
                        }
                    ) {
                        Text("Déconnexion", color = colors.destructive)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showLogoutDialog = false }) {
                        Text("Annuler", color = colors.foreground)
                    }
                }
            )
        }
    }
}

@Composable
private fun SectionTitle(title: String) {
    val colors = LocalJellyFishColors.current
    Text(
        text = title,
        fontSize = 18.sp,
        fontWeight = FontWeight.SemiBold,
        color = colors.mutedForeground,
        modifier = Modifier.padding(top = 8.dp)
    )
}

@Composable
private fun AccountSection(
    userName: String,
    serverName: String,
    isLoggingOut: Boolean,
    onLogoutClick: () -> Unit
) {
    val colors = LocalJellyFishColors.current

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = colors.card)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // User info
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Person,
                    contentDescription = null,
                    tint = colors.mutedForeground
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = userName,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = colors.foreground
                    )
                    Text(
                        text = serverName,
                        fontSize = 14.sp,
                        color = colors.mutedForeground
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = colors.border)
            Spacer(modifier = Modifier.height(16.dp))

            // Logout button
            Button(
                onClick = onLogoutClick,
                enabled = !isLoggingOut,
                colors = ButtonDefaults.buttonColors(
                    containerColor = colors.destructive,
                    contentColor = colors.foreground
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                if (isLoggingOut) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = colors.foreground
                    )
                } else {
                    Text("Se déconnecter")
                }
            }
        }
    }
}

@Composable
private fun PlaybackSection(
    streamingQuality: String,
    onStreamingQualityChange: (String) -> Unit,
    downloadQuality: String,
    onDownloadQualityChange: (String) -> Unit,
    audioLanguage: String,
    onAudioLanguageChange: (String) -> Unit,
    subtitleLanguage: String,
    onSubtitleLanguageChange: (String) -> Unit
) {
    val colors = LocalJellyFishColors.current
    val qualityOptions = listOf("Auto", "1080p", "720p", "480p")

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = colors.card)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Streaming quality
            QualityDropdown(
                label = "Qualité streaming",
                selectedQuality = streamingQuality,
                qualityOptions = qualityOptions,
                onQualitySelected = onStreamingQualityChange
            )

            // Download quality
            QualityDropdown(
                label = "Qualité téléchargement",
                selectedQuality = downloadQuality,
                qualityOptions = qualityOptions,
                onQualitySelected = onDownloadQualityChange
            )

            // Audio language
            OutlinedTextField(
                value = audioLanguage,
                onValueChange = onAudioLanguageChange,
                label = { Text("Langue audio", color = colors.mutedForeground) },
                placeholder = { Text("fr, en", color = colors.mutedForeground) },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = colors.foreground,
                    unfocusedTextColor = colors.foreground,
                    focusedBorderColor = colors.primary,
                    unfocusedBorderColor = colors.border
                ),
                modifier = Modifier.fillMaxWidth()
            )

            // Subtitle language
            OutlinedTextField(
                value = subtitleLanguage,
                onValueChange = onSubtitleLanguageChange,
                label = { Text("Langue sous-titres", color = colors.mutedForeground) },
                placeholder = { Text("fr, en", color = colors.mutedForeground) },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = colors.foreground,
                    unfocusedTextColor = colors.foreground,
                    focusedBorderColor = colors.primary,
                    unfocusedBorderColor = colors.border
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun QualityDropdown(
    label: String,
    selectedQuality: String,
    qualityOptions: List<String>,
    onQualitySelected: (String) -> Unit
) {
    val colors = LocalJellyFishColors.current
    var expanded by remember { mutableStateOf(false) }

    Column {
        Text(
            text = label,
            fontSize = 14.sp,
            color = colors.mutedForeground
        )
        Spacer(modifier = Modifier.height(4.dp))
        Box {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colors.secondary, RoundedCornerShape(8.dp))
                    .clickable { expanded = true }
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = selectedQuality,
                    fontSize = 16.sp,
                    color = colors.foreground
                )
                Icon(
                    Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    tint = colors.mutedForeground
                )
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.background(colors.card)
            ) {
                qualityOptions.forEach { quality ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = quality,
                                color = if (quality == selectedQuality) colors.primary else colors.foreground
                            )
                        },
                        onClick = {
                            onQualitySelected(quality)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun StorageSection(
    usedStorageBytes: Long,
    storageLimitMb: Int,
    onStorageLimitChange: (Int) -> Unit,
    isClearingCache: Boolean,
    onClearCache: () -> Unit
) {
    val colors = LocalJellyFishColors.current
    val usedMb = usedStorageBytes / (1024 * 1024)
    val usedGb = usedMb / 1024f

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = colors.card)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Used storage display
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Stockage utilisé",
                    fontSize = 16.sp,
                    color = colors.foreground
                )
                Text(
                    text = "${"%.2f".format(usedGb)} GB",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = colors.primary
                )
            }

            // Storage limit slider
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Limite de stockage",
                        fontSize = 14.sp,
                        color = colors.mutedForeground
                    )
                    Text(
                        text = "${storageLimitMb / 1024} GB",
                        fontSize = 14.sp,
                        color = colors.foreground
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Slider(
                    value = storageLimitMb.toFloat(),
                    onValueChange = { onStorageLimitChange(it.toInt()) },
                    valueRange = 1024f..51200f, // 1 GB to 50 GB
                    steps = 49,
                    colors = SliderDefaults.colors(
                        thumbColor = colors.primary,
                        activeTrackColor = colors.primary,
                        inactiveTrackColor = colors.secondary
                    )
                )
            }

            HorizontalDivider(color = colors.border)

            // Clear cache button
            Button(
                onClick = onClearCache,
                enabled = !isClearingCache,
                colors = ButtonDefaults.buttonColors(
                    containerColor = colors.secondary,
                    contentColor = colors.foreground
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                if (isClearingCache) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = colors.foreground
                    )
                } else {
                    Text("Vider le cache")
                }
            }
        }
    }
}

@Composable
private fun AboutSection() {
    val colors = LocalJellyFishColors.current

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = colors.card)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // App version
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Version",
                    fontSize = 16.sp,
                    color = colors.foreground
                )
                Text(
                    text = "1.0.0",
                    fontSize = 16.sp,
                    color = colors.mutedForeground
                )
            }

            HorizontalDivider(color = colors.border)

            // Licenses link
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { /* TODO: Open licenses */ },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Licences",
                    fontSize = 16.sp,
                    color = colors.foreground
                )
                Icon(
                    Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = colors.mutedForeground
                )
            }
        }
    }
}

@Composable
private fun AdminSection(
    adminState: AdminSectionState,
    onLoadUsers: () -> Unit,
    onShowCreateUserDialog: (Boolean) -> Unit,
    onCreateUser: (String, String) -> Unit,
    onShowDeleteUserDialog: (AdminUser?) -> Unit,
    onDeleteUser: (String) -> Unit,
    onRefreshLibrary: () -> Unit,
    onLoadLogs: () -> Unit,
    onLoadTasks: () -> Unit,
    onRunTask: (String) -> Unit
) {
    val colors = LocalJellyFishColors.current

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = colors.card)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Users
            ExpandableAdminSection(
                title = "Utilisateurs",
                icon = Icons.Default.People,
                onExpand = onLoadUsers
            ) {
                if (adminState.isLoadingUsers) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(24.dp)
                            .align(Alignment.CenterHorizontally),
                        color = colors.primary
                    )
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        adminState.users.forEach { user ->
                            UserItem(
                                user = user,
                                onDeleteClick = { onShowDeleteUserDialog(user) }
                            )
                        }
                        Button(
                            onClick = { onShowCreateUserDialog(true) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = colors.secondary,
                                contentColor = colors.foreground
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Ajouter un utilisateur")
                        }
                    }
                }
            }

            HorizontalDivider(color = colors.border)

            // Libraries
            ExpandableAdminSection(
                title = "Bibliothèques",
                icon = Icons.Default.VideoLibrary
            ) {
                Button(
                    onClick = onRefreshLibrary,
                    enabled = !adminState.isRefreshingLibrary,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colors.secondary,
                        contentColor = colors.foreground
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (adminState.isRefreshingLibrary) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = colors.foreground
                        )
                    } else {
                        Icon(Icons.Default.Refresh, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Actualiser les bibliothèques")
                    }
                }
            }

            HorizontalDivider(color = colors.border)

            // Logs
            ExpandableAdminSection(
                title = "Journaux",
                icon = Icons.Default.Description,
                onExpand = onLoadLogs
            ) {
                if (adminState.isLoadingLogs) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(24.dp)
                            .align(Alignment.CenterHorizontally),
                        color = colors.primary
                    )
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 300.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        adminState.logs.take(20).forEach { log ->
                            LogItem(log = log)
                        }
                    }
                }
            }

            HorizontalDivider(color = colors.border)

            // Tasks
            ExpandableAdminSection(
                title = "Tâches",
                icon = Icons.Default.Task,
                onExpand = onLoadTasks
            ) {
                if (adminState.isLoadingTasks) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(24.dp)
                            .align(Alignment.CenterHorizontally),
                        color = colors.primary
                    )
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        adminState.tasks.forEach { task ->
                            TaskItem(
                                task = task,
                                onRunClick = { onRunTask(task.id) }
                            )
                        }
                    }
                }
            }
        }
    }

    // Create User Dialog
    if (adminState.showCreateUserDialog) {
        CreateUserDialog(
            onDismiss = { onShowCreateUserDialog(false) },
            onCreate = onCreateUser
        )
    }

    // Delete User Dialog
    adminState.showDeleteUserDialog?.let { user ->
        AlertDialog(
            onDismissRequest = { onShowDeleteUserDialog(null) },
            title = { Text("Supprimer l'utilisateur") },
            text = { Text("Voulez-vous vraiment supprimer ${user.name} ?") },
            confirmButton = {
                TextButton(
                    onClick = { onDeleteUser(user.id) }
                ) {
                    Text("Supprimer", color = LocalJellyFishColors.current.destructive)
                }
            },
            dismissButton = {
                TextButton(onClick = { onShowDeleteUserDialog(null) }) {
                    Text("Annuler", color = LocalJellyFishColors.current.foreground)
                }
            }
        )
    }
}

@Composable
private fun ExpandableAdminSection(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onExpand: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    val colors = LocalJellyFishColors.current
    var expanded by remember { mutableStateOf(false) }

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    if (!expanded && onExpand != null) {
                        onExpand()
                    }
                    expanded = !expanded
                }
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = colors.mutedForeground
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = colors.foreground
                )
            }
            Icon(
                if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                contentDescription = null,
                tint = colors.mutedForeground
            )
        }

        if (expanded) {
            Spacer(modifier = Modifier.height(8.dp))
            content()
        }
    }
}

@Composable
private fun UserItem(
    user: AdminUser,
    onDeleteClick: () -> Unit
) {
    val colors = LocalJellyFishColors.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(colors.secondary, RoundedCornerShape(8.dp))
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = user.name,
                fontSize = 16.sp,
                color = colors.foreground
            )
            if (user.isAdmin) {
                Text(
                    text = "Administrateur",
                    fontSize = 12.sp,
                    color = colors.mutedForeground
                )
            }
        }
        IconButton(onClick = onDeleteClick) {
            Icon(
                Icons.Default.Delete,
                contentDescription = "Supprimer",
                tint = colors.destructive
            )
        }
    }
}

@Composable
private fun LogItem(log: LogEntry) {
    val colors = LocalJellyFishColors.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(colors.secondary, RoundedCornerShape(4.dp))
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = log.severity,
                fontSize = 12.sp,
                color = when (log.severity) {
                    "Error" -> colors.destructive
                    "Warning" -> Color(0xFFFFBB00)
                    else -> colors.mutedForeground
                },
                fontWeight = FontWeight.Medium
            )
            Text(
                text = formatTimestamp(log.timestamp),
                fontSize = 12.sp,
                color = colors.mutedForeground
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = log.message,
            fontSize = 13.sp,
            color = colors.foreground
        )
    }
}

@Composable
private fun TaskItem(
    task: ScheduledTask,
    onRunClick: () -> Unit
) {
    val colors = LocalJellyFishColors.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(colors.secondary, RoundedCornerShape(8.dp))
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = task.name,
                fontSize = 16.sp,
                color = colors.foreground
            )
            Text(
                text = task.description,
                fontSize = 13.sp,
                color = colors.mutedForeground
            )
            task.lastExecutionResult?.let {
                Text(
                    text = "Dernier résultat: $it",
                    fontSize = 12.sp,
                    color = colors.mutedForeground
                )
            }
        }
        IconButton(
            onClick = onRunClick,
            enabled = task.state == TaskState.IDLE
        ) {
            Icon(
                Icons.Default.PlayArrow,
                contentDescription = "Exécuter",
                tint = if (task.state == TaskState.IDLE) colors.primary else colors.mutedForeground
            )
        }
    }
}

@Composable
private fun CreateUserDialog(
    onDismiss: () -> Unit,
    onCreate: (String, String) -> Unit
) {
    val colors = LocalJellyFishColors.current
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Créer un utilisateur") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Nom d'utilisateur", color = colors.mutedForeground) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = colors.foreground,
                        unfocusedTextColor = colors.foreground,
                        focusedBorderColor = colors.primary,
                        unfocusedBorderColor = colors.border
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Mot de passe", color = colors.mutedForeground) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = colors.foreground,
                        unfocusedTextColor = colors.foreground,
                        focusedBorderColor = colors.primary,
                        unfocusedBorderColor = colors.border
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (username.isNotBlank() && password.isNotBlank()) {
                        onCreate(username, password)
                    }
                },
                enabled = username.isNotBlank() && password.isNotBlank()
            ) {
                Text("Créer", color = colors.primary)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Annuler", color = colors.foreground)
            }
        }
    )
}

private fun formatTimestamp(timestamp: Long): String {
    val totalSeconds = timestamp / 1000
    val hours = (totalSeconds / 3600) % 24
    val minutes = (totalSeconds / 60) % 60
    val seconds = totalSeconds % 60
    return "${hours.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}"
}
