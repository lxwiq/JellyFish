# SettingsScreen Implementation Plan

> **For Claude:** REQUIRED SUB-SKILL: Use superpowers:executing-plans to implement this plan task-by-task.

**Goal:** Implement a comprehensive settings screen with account, playback, storage, about, and admin sections.

**Architecture:** ScreenModel pattern with repositories. Admin section conditionally visible based on user.isAdmin.

**Tech Stack:** Compose Multiplatform, Voyager, Koin, DataStore, Jellyfin SDK

---

### Task 1: Add isAdmin to User Model

**Files:**
- Modify: `composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/domain/model/User.kt`
- Modify: `composeApp/src/androidMain/kotlin/com/lowiq/jellyfish/data/remote/JellyfinDataSourceImpl.kt`
- Modify: `composeApp/src/jvmMain/kotlin/com/lowiq/jellyfish/data/remote/JellyfinDataSourceImpl.kt`

**Step 1: Update User model**

```kotlin
// domain/model/User.kt
data class User(
    val id: String,
    val name: String,
    val serverId: String,
    val isAdmin: Boolean = false
)
```

**Step 2: Update JellyfinDataSourceImpl.getCurrentUser() - Android**

```kotlin
override suspend fun getCurrentUser(serverUrl: String, token: String): Result<User> =
    withContext(Dispatchers.IO) {
        runCatching {
            val api = createApi(serverUrl, token)
            val user by api.userApi.getCurrentUser()
            User(
                id = user.id.toString(),
                name = user.name ?: "",
                serverId = user.serverId ?: "",
                isAdmin = user.policy?.isAdministrator == true
            )
        }
    }
```

**Step 3: Update JellyfinDataSourceImpl.getCurrentUser() - JVM**

Same change as Android.

**Step 4: Commit**

```bash
git add -A && git commit -m "feat(settings): add isAdmin to User model"
```

---

### Task 2: Extend UserPreferencesStorage with Playback Preferences

**Files:**
- Modify: `composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/data/local/UserPreferencesStorage.kt`

**Step 1: Add new preference keys and methods**

```kotlin
class UserPreferencesStorage(private val dataStore: DataStore<Preferences>) {

    private companion object {
        val KEY_DISPLAY_MODE = stringPreferencesKey("display_mode")
        val KEY_STREAMING_QUALITY = stringPreferencesKey("streaming_quality")
        val KEY_PREFERRED_AUDIO_LANG = stringPreferencesKey("preferred_audio_language")
        val KEY_PREFERRED_SUBTITLE_LANG = stringPreferencesKey("preferred_subtitle_language")
    }

    // Existing
    fun getDisplayMode(): Flow<DisplayMode> = dataStore.data.map { prefs ->
        val value = prefs[KEY_DISPLAY_MODE]
        value?.let {
            try { DisplayMode.valueOf(it) } catch (_: Exception) { DisplayMode.POSTER }
        } ?: DisplayMode.POSTER
    }

    suspend fun setDisplayMode(mode: DisplayMode) {
        dataStore.edit { it[KEY_DISPLAY_MODE] = mode.name }
    }

    // New: Streaming quality
    fun getStreamingQuality(): Flow<String> = dataStore.data.map { prefs ->
        prefs[KEY_STREAMING_QUALITY] ?: "Auto"
    }

    suspend fun setStreamingQuality(quality: String) {
        dataStore.edit { it[KEY_STREAMING_QUALITY] = quality }
    }

    // New: Audio language
    fun getPreferredAudioLanguage(): Flow<String> = dataStore.data.map { prefs ->
        prefs[KEY_PREFERRED_AUDIO_LANG] ?: ""
    }

    suspend fun setPreferredAudioLanguage(language: String) {
        dataStore.edit { it[KEY_PREFERRED_AUDIO_LANG] = language }
    }

    // New: Subtitle language
    fun getPreferredSubtitleLanguage(): Flow<String> = dataStore.data.map { prefs ->
        prefs[KEY_PREFERRED_SUBTITLE_LANG] ?: ""
    }

    suspend fun setPreferredSubtitleLanguage(language: String) {
        dataStore.edit { it[KEY_PREFERRED_SUBTITLE_LANG] = language }
    }
}
```

**Step 2: Commit**

```bash
git add -A && git commit -m "feat(settings): add playback preferences to UserPreferencesStorage"
```

---

### Task 3: Add Admin API Methods to JellyfinDataSource

**Files:**
- Modify: `composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/data/remote/JellyfinDataSource.kt`

**Step 1: Add admin method signatures**

Add to the interface:

```kotlin
// Admin: Users
suspend fun getUsers(serverUrl: String, token: String): Result<List<AdminUser>>
suspend fun createUser(serverUrl: String, token: String, username: String, password: String): Result<AdminUser>
suspend fun deleteUser(serverUrl: String, token: String, userId: String): Result<Unit>

// Admin: Libraries
suspend fun refreshLibrary(serverUrl: String, token: String): Result<Unit>

// Admin: Logs
suspend fun getServerLogs(serverUrl: String, token: String, limit: Int = 100): Result<List<LogEntry>>

// Admin: Tasks
suspend fun getScheduledTasks(serverUrl: String, token: String): Result<List<ScheduledTask>>
suspend fun runTask(serverUrl: String, token: String, taskId: String): Result<Unit>
```

**Step 2: Create admin models in domain/model**

Create `composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/domain/model/AdminModels.kt`:

```kotlin
package com.lowiq.jellyfish.domain.model

data class AdminUser(
    val id: String,
    val name: String,
    val isAdmin: Boolean,
    val isDisabled: Boolean
)

data class LogEntry(
    val timestamp: Long,
    val severity: String,
    val message: String
)

data class ScheduledTask(
    val id: String,
    val name: String,
    val description: String,
    val state: TaskState,
    val lastExecutionResult: String?
)

enum class TaskState {
    IDLE, RUNNING, CANCELLING
}
```

**Step 3: Commit**

```bash
git add -A && git commit -m "feat(settings): add admin API interface and models"
```

---

### Task 4: Implement Admin API in JellyfinDataSourceImpl (Android)

**Files:**
- Modify: `composeApp/src/androidMain/kotlin/com/lowiq/jellyfish/data/remote/JellyfinDataSourceImpl.kt`

**Step 1: Implement all admin methods**

```kotlin
// Admin: Users
override suspend fun getUsers(serverUrl: String, token: String): Result<List<AdminUser>> =
    withContext(Dispatchers.IO) {
        runCatching {
            val api = createApi(serverUrl, token)
            val users by api.userApi.getUsers()
            users.map { user ->
                AdminUser(
                    id = user.id.toString(),
                    name = user.name ?: "",
                    isAdmin = user.policy?.isAdministrator == true,
                    isDisabled = user.policy?.isDisabled == true
                )
            }
        }
    }

override suspend fun createUser(
    serverUrl: String,
    token: String,
    username: String,
    password: String
): Result<AdminUser> = withContext(Dispatchers.IO) {
    runCatching {
        val api = createApi(serverUrl, token)
        val user by api.userApi.createUserByName(
            org.jellyfin.sdk.model.api.CreateUserByName(
                name = username,
                password = password
            )
        )
        AdminUser(
            id = user.id.toString(),
            name = user.name ?: username,
            isAdmin = user.policy?.isAdministrator == true,
            isDisabled = user.policy?.isDisabled == true
        )
    }
}

override suspend fun deleteUser(serverUrl: String, token: String, userId: String): Result<Unit> =
    withContext(Dispatchers.IO) {
        runCatching {
            val api = createApi(serverUrl, token)
            api.userApi.deleteUser(java.util.UUID.fromString(userId))
        }
    }

// Admin: Libraries
override suspend fun refreshLibrary(serverUrl: String, token: String): Result<Unit> =
    withContext(Dispatchers.IO) {
        runCatching {
            val api = createApi(serverUrl, token)
            api.libraryApi.refreshLibrary()
        }
    }

// Admin: Logs
override suspend fun getServerLogs(
    serverUrl: String,
    token: String,
    limit: Int
): Result<List<LogEntry>> = withContext(Dispatchers.IO) {
    runCatching {
        val api = createApi(serverUrl, token)
        val response by api.systemApi.getLogEntries(limit = limit)
        response.items.orEmpty().map { entry ->
            LogEntry(
                timestamp = entry.date?.toEpochSecond(java.time.ZoneOffset.UTC)?.times(1000) ?: 0L,
                severity = entry.severity?.name ?: "INFO",
                message = entry.name ?: ""
            )
        }
    }
}

// Admin: Tasks
override suspend fun getScheduledTasks(serverUrl: String, token: String): Result<List<ScheduledTask>> =
    withContext(Dispatchers.IO) {
        runCatching {
            val api = createApi(serverUrl, token)
            val tasks by api.scheduledTasksApi.getTasks()
            tasks.map { task ->
                ScheduledTask(
                    id = task.id ?: "",
                    name = task.name ?: "",
                    description = task.description ?: "",
                    state = when (task.state) {
                        org.jellyfin.sdk.model.api.TaskState.RUNNING -> TaskState.RUNNING
                        org.jellyfin.sdk.model.api.TaskState.CANCELLING -> TaskState.CANCELLING
                        else -> TaskState.IDLE
                    },
                    lastExecutionResult = task.lastExecutionResult?.status?.name
                )
            }
        }
    }

override suspend fun runTask(serverUrl: String, token: String, taskId: String): Result<Unit> =
    withContext(Dispatchers.IO) {
        runCatching {
            val api = createApi(serverUrl, token)
            api.scheduledTasksApi.startTask(taskId = taskId)
        }
    }
```

**Step 2: Commit**

```bash
git add -A && git commit -m "feat(settings): implement admin API for Android"
```

---

### Task 5: Implement Admin API in JellyfinDataSourceImpl (JVM)

**Files:**
- Modify: `composeApp/src/jvmMain/kotlin/com/lowiq/jellyfish/data/remote/JellyfinDataSourceImpl.kt`

**Step 1: Implement same admin methods as Android**

Same implementations as Task 4 but in JVM file.

**Step 2: Commit**

```bash
git add -A && git commit -m "feat(settings): implement admin API for JVM"
```

---

### Task 6: Create AdminRepository

**Files:**
- Create: `composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/domain/repository/AdminRepository.kt`
- Create: `composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/data/repository/AdminRepositoryImpl.kt`

**Step 1: Create AdminRepository interface**

```kotlin
package com.lowiq.jellyfish.domain.repository

import com.lowiq.jellyfish.domain.model.AdminUser
import com.lowiq.jellyfish.domain.model.LogEntry
import com.lowiq.jellyfish.domain.model.ScheduledTask

interface AdminRepository {
    suspend fun getUsers(): Result<List<AdminUser>>
    suspend fun createUser(username: String, password: String): Result<AdminUser>
    suspend fun deleteUser(userId: String): Result<Unit>
    suspend fun refreshLibrary(): Result<Unit>
    suspend fun getServerLogs(limit: Int = 100): Result<List<LogEntry>>
    suspend fun getScheduledTasks(): Result<List<ScheduledTask>>
    suspend fun runTask(taskId: String): Result<Unit>
}
```

**Step 2: Create AdminRepositoryImpl**

```kotlin
package com.lowiq.jellyfish.data.repository

import com.lowiq.jellyfish.data.local.SecureStorage
import com.lowiq.jellyfish.data.local.ServerStorage
import com.lowiq.jellyfish.data.remote.JellyfinDataSource
import com.lowiq.jellyfish.domain.model.AdminUser
import com.lowiq.jellyfish.domain.model.LogEntry
import com.lowiq.jellyfish.domain.model.ScheduledTask
import com.lowiq.jellyfish.domain.repository.AdminRepository
import kotlinx.coroutines.flow.first

class AdminRepositoryImpl(
    private val jellyfinDataSource: JellyfinDataSource,
    private val serverStorage: ServerStorage,
    private val secureStorage: SecureStorage
) : AdminRepository {

    private suspend fun getServerAndToken(): Pair<String, String>? {
        val serverId = serverStorage.getActiveServerId().first() ?: return null
        val servers = serverStorage.getServers().first()
        val server = servers.find { it.id == serverId } ?: return null
        val token = secureStorage.getToken(serverId) ?: return null
        return server.url to token
    }

    override suspend fun getUsers(): Result<List<AdminUser>> {
        val (serverUrl, token) = getServerAndToken() ?: return Result.failure(Exception("Not authenticated"))
        return jellyfinDataSource.getUsers(serverUrl, token)
    }

    override suspend fun createUser(username: String, password: String): Result<AdminUser> {
        val (serverUrl, token) = getServerAndToken() ?: return Result.failure(Exception("Not authenticated"))
        return jellyfinDataSource.createUser(serverUrl, token, username, password)
    }

    override suspend fun deleteUser(userId: String): Result<Unit> {
        val (serverUrl, token) = getServerAndToken() ?: return Result.failure(Exception("Not authenticated"))
        return jellyfinDataSource.deleteUser(serverUrl, token, userId)
    }

    override suspend fun refreshLibrary(): Result<Unit> {
        val (serverUrl, token) = getServerAndToken() ?: return Result.failure(Exception("Not authenticated"))
        return jellyfinDataSource.refreshLibrary(serverUrl, token)
    }

    override suspend fun getServerLogs(limit: Int): Result<List<LogEntry>> {
        val (serverUrl, token) = getServerAndToken() ?: return Result.failure(Exception("Not authenticated"))
        return jellyfinDataSource.getServerLogs(serverUrl, token, limit)
    }

    override suspend fun getScheduledTasks(): Result<List<ScheduledTask>> {
        val (serverUrl, token) = getServerAndToken() ?: return Result.failure(Exception("Not authenticated"))
        return jellyfinDataSource.getScheduledTasks(serverUrl, token)
    }

    override suspend fun runTask(taskId: String): Result<Unit> {
        val (serverUrl, token) = getServerAndToken() ?: return Result.failure(Exception("Not authenticated"))
        return jellyfinDataSource.runTask(serverUrl, token, taskId)
    }
}
```

**Step 3: Commit**

```bash
git add -A && git commit -m "feat(settings): add AdminRepository"
```

---

### Task 7: Create SettingsState

**Files:**
- Create: `composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/presentation/screens/settings/SettingsState.kt`

**Step 1: Create state classes**

```kotlin
package com.lowiq.jellyfish.presentation.screens.settings

import com.lowiq.jellyfish.domain.model.AdminUser
import com.lowiq.jellyfish.domain.model.LogEntry
import com.lowiq.jellyfish.domain.model.ScheduledTask
import com.lowiq.jellyfish.domain.model.Server
import com.lowiq.jellyfish.domain.model.User

data class SettingsState(
    // Account
    val user: User? = null,
    val server: Server? = null,
    val isLoggingOut: Boolean = false,

    // Playback
    val streamingQuality: String = "Auto",
    val downloadQuality: String = "1080p",
    val preferredAudioLanguage: String = "",
    val preferredSubtitleLanguage: String = "",

    // Storage
    val usedStorageBytes: Long = 0,
    val storageLimitMb: Int = 10240,
    val isClearingCache: Boolean = false,

    // Admin
    val isAdmin: Boolean = false,
    val adminSection: AdminSectionState = AdminSectionState()
)

data class AdminSectionState(
    val users: List<AdminUser> = emptyList(),
    val logs: List<LogEntry> = emptyList(),
    val tasks: List<ScheduledTask> = emptyList(),
    val isLoadingUsers: Boolean = false,
    val isLoadingLogs: Boolean = false,
    val isLoadingTasks: Boolean = false,
    val isRefreshingLibrary: Boolean = false,
    val showCreateUserDialog: Boolean = false,
    val showDeleteUserDialog: AdminUser? = null
)

sealed class SettingsEvent {
    object LoggedOut : SettingsEvent()
    data class Error(val message: String) : SettingsEvent()
    data class Success(val message: String) : SettingsEvent()
}
```

**Step 2: Commit**

```bash
git add -A && git commit -m "feat(settings): add SettingsState"
```

---

### Task 8: Create SettingsScreenModel

**Files:**
- Create: `composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/presentation/screens/settings/SettingsScreenModel.kt`

**Step 1: Create ScreenModel**

```kotlin
package com.lowiq.jellyfish.presentation.screens.settings

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.lowiq.jellyfish.data.local.DownloadSettingsStorage
import com.lowiq.jellyfish.data.local.UserPreferencesStorage
import com.lowiq.jellyfish.domain.repository.AdminRepository
import com.lowiq.jellyfish.domain.repository.AuthRepository
import com.lowiq.jellyfish.domain.repository.DownloadRepository
import com.lowiq.jellyfish.domain.repository.ServerRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SettingsScreenModel(
    private val authRepository: AuthRepository,
    private val serverRepository: ServerRepository,
    private val downloadRepository: DownloadRepository,
    private val adminRepository: AdminRepository,
    private val userPreferencesStorage: UserPreferencesStorage,
    private val downloadSettingsStorage: DownloadSettingsStorage
) : ScreenModel {

    private val _state = MutableStateFlow(SettingsState())
    val state: StateFlow<SettingsState> = _state.asStateFlow()

    private val _events = MutableSharedFlow<SettingsEvent>()
    val events: SharedFlow<SettingsEvent> = _events.asSharedFlow()

    init {
        loadInitialData()
        observePreferences()
    }

    private fun loadInitialData() {
        screenModelScope.launch {
            // Load server and user
            val server = serverRepository.getActiveServer().first()
            if (server != null) {
                val user = authRepository.getCurrentUser(server.id)
                _state.update { it.copy(
                    server = server,
                    user = user,
                    isAdmin = user?.isAdmin == true
                )}
            }

            // Load storage info
            val storageInfo = downloadRepository.getStorageInfo()
            _state.update { it.copy(usedStorageBytes = storageInfo.usedBytes) }
        }
    }

    private fun observePreferences() {
        screenModelScope.launch {
            combine(
                userPreferencesStorage.getStreamingQuality(),
                userPreferencesStorage.getPreferredAudioLanguage(),
                userPreferencesStorage.getPreferredSubtitleLanguage(),
                downloadSettingsStorage.defaultQuality,
                downloadSettingsStorage.storageLimitMb
            ) { streaming, audio, subtitle, download, limit ->
                _state.update { it.copy(
                    streamingQuality = streaming,
                    preferredAudioLanguage = audio,
                    preferredSubtitleLanguage = subtitle,
                    downloadQuality = download,
                    storageLimitMb = limit
                )}
            }.collect()
        }
    }

    // Account actions
    fun logout() {
        screenModelScope.launch {
            _state.update { it.copy(isLoggingOut = true) }
            val serverId = _state.value.server?.id ?: return@launch
            authRepository.logout(serverId)
            _events.emit(SettingsEvent.LoggedOut)
        }
    }

    // Playback preferences
    fun setStreamingQuality(quality: String) {
        screenModelScope.launch {
            userPreferencesStorage.setStreamingQuality(quality)
        }
    }

    fun setDownloadQuality(quality: String) {
        screenModelScope.launch {
            downloadSettingsStorage.setDefaultQuality(quality)
        }
    }

    fun setPreferredAudioLanguage(language: String) {
        screenModelScope.launch {
            userPreferencesStorage.setPreferredAudioLanguage(language)
        }
    }

    fun setPreferredSubtitleLanguage(language: String) {
        screenModelScope.launch {
            userPreferencesStorage.setPreferredSubtitleLanguage(language)
        }
    }

    // Storage actions
    fun setStorageLimit(limitMb: Int) {
        screenModelScope.launch {
            downloadSettingsStorage.setStorageLimitMb(limitMb)
        }
    }

    fun clearCache() {
        screenModelScope.launch {
            _state.update { it.copy(isClearingCache = true) }
            // Clear completed downloads
            downloadRepository.clearCompletedDownloads()
            val storageInfo = downloadRepository.getStorageInfo()
            _state.update { it.copy(
                isClearingCache = false,
                usedStorageBytes = storageInfo.usedBytes
            )}
            _events.emit(SettingsEvent.Success("Cache vidé"))
        }
    }

    // Admin actions
    fun loadUsers() {
        screenModelScope.launch {
            _state.update { it.copy(adminSection = it.adminSection.copy(isLoadingUsers = true)) }
            adminRepository.getUsers()
                .onSuccess { users ->
                    _state.update { it.copy(adminSection = it.adminSection.copy(
                        users = users,
                        isLoadingUsers = false
                    ))}
                }
                .onFailure { error ->
                    _state.update { it.copy(adminSection = it.adminSection.copy(isLoadingUsers = false)) }
                    _events.emit(SettingsEvent.Error(error.message ?: "Erreur"))
                }
        }
    }

    fun showCreateUserDialog(show: Boolean) {
        _state.update { it.copy(adminSection = it.adminSection.copy(showCreateUserDialog = show)) }
    }

    fun createUser(username: String, password: String) {
        screenModelScope.launch {
            adminRepository.createUser(username, password)
                .onSuccess {
                    _state.update { it.copy(adminSection = it.adminSection.copy(showCreateUserDialog = false)) }
                    loadUsers()
                    _events.emit(SettingsEvent.Success("Utilisateur créé"))
                }
                .onFailure { error ->
                    _events.emit(SettingsEvent.Error(error.message ?: "Erreur"))
                }
        }
    }

    fun showDeleteUserDialog(user: com.lowiq.jellyfish.domain.model.AdminUser?) {
        _state.update { it.copy(adminSection = it.adminSection.copy(showDeleteUserDialog = user)) }
    }

    fun deleteUser(userId: String) {
        screenModelScope.launch {
            adminRepository.deleteUser(userId)
                .onSuccess {
                    _state.update { it.copy(adminSection = it.adminSection.copy(showDeleteUserDialog = null)) }
                    loadUsers()
                    _events.emit(SettingsEvent.Success("Utilisateur supprimé"))
                }
                .onFailure { error ->
                    _events.emit(SettingsEvent.Error(error.message ?: "Erreur"))
                }
        }
    }

    fun refreshLibrary() {
        screenModelScope.launch {
            _state.update { it.copy(adminSection = it.adminSection.copy(isRefreshingLibrary = true)) }
            adminRepository.refreshLibrary()
                .onSuccess {
                    _state.update { it.copy(adminSection = it.adminSection.copy(isRefreshingLibrary = false)) }
                    _events.emit(SettingsEvent.Success("Scan des bibliothèques lancé"))
                }
                .onFailure { error ->
                    _state.update { it.copy(adminSection = it.adminSection.copy(isRefreshingLibrary = false)) }
                    _events.emit(SettingsEvent.Error(error.message ?: "Erreur"))
                }
        }
    }

    fun loadLogs() {
        screenModelScope.launch {
            _state.update { it.copy(adminSection = it.adminSection.copy(isLoadingLogs = true)) }
            adminRepository.getServerLogs()
                .onSuccess { logs ->
                    _state.update { it.copy(adminSection = it.adminSection.copy(
                        logs = logs,
                        isLoadingLogs = false
                    ))}
                }
                .onFailure { error ->
                    _state.update { it.copy(adminSection = it.adminSection.copy(isLoadingLogs = false)) }
                    _events.emit(SettingsEvent.Error(error.message ?: "Erreur"))
                }
        }
    }

    fun loadTasks() {
        screenModelScope.launch {
            _state.update { it.copy(adminSection = it.adminSection.copy(isLoadingTasks = true)) }
            adminRepository.getScheduledTasks()
                .onSuccess { tasks ->
                    _state.update { it.copy(adminSection = it.adminSection.copy(
                        tasks = tasks,
                        isLoadingTasks = false
                    ))}
                }
                .onFailure { error ->
                    _state.update { it.copy(adminSection = it.adminSection.copy(isLoadingTasks = false)) }
                    _events.emit(SettingsEvent.Error(error.message ?: "Erreur"))
                }
        }
    }

    fun runTask(taskId: String) {
        screenModelScope.launch {
            adminRepository.runTask(taskId)
                .onSuccess {
                    _events.emit(SettingsEvent.Success("Tâche lancée"))
                    loadTasks()
                }
                .onFailure { error ->
                    _events.emit(SettingsEvent.Error(error.message ?: "Erreur"))
                }
        }
    }
}
```

**Step 2: Commit**

```bash
git add -A && git commit -m "feat(settings): add SettingsScreenModel"
```

---

### Task 9: Create SettingsScreen UI

**Files:**
- Create: `composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/presentation/screens/settings/SettingsScreen.kt`

**Step 1: Create screen with all sections**

Full Compose UI with:
- Header with back button
- Account section (user info, logout)
- Playback section (quality dropdowns, language inputs)
- Storage section (usage display, limit slider, clear cache)
- About section (version, licenses link)
- Admin section (conditionally visible) with expandable subsections

Use shadcn theme colors from LocalJellyFishColors.

**Step 2: Commit**

```bash
git add -A && git commit -m "feat(settings): add SettingsScreen UI"
```

---

### Task 10: Register in Koin and Wire Navigation

**Files:**
- Modify: `composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/di/AppModule.kt`
- Modify: `composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/presentation/screens/home/HomeScreenModel.kt`
- Modify: `composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/presentation/screens/home/HomeScreen.kt`

**Step 1: Register in Koin**

```kotlin
// In AppModule
single<AdminRepository> { AdminRepositoryImpl(get(), get(), get()) }
factory { SettingsScreenModel(get(), get(), get(), get(), get(), get()) }
```

**Step 2: Add NavigateToSettings event in HomeScreenModel**

```kotlin
sealed class HomeEvent {
    // ... existing events
    object NavigateToSettings : HomeEvent()
}

// In onNavigationItemSelected
fun onNavigationItemSelected(index: Int) {
    screenModelScope.launch {
        when (index) {
            1 -> _events.emit(HomeEvent.NavigateToSearch)
            3 -> _events.emit(HomeEvent.NavigateToDownloads)
            4 -> _events.emit(HomeEvent.NavigateToSettings)
        }
    }
}
```

**Step 3: Handle in HomeScreen**

```kotlin
is HomeEvent.NavigateToSettings -> navigator.push(SettingsScreen())
```

**Step 4: Commit**

```bash
git add -A && git commit -m "feat(settings): register in Koin and wire navigation"
```

---

### Task 11: Build and Verify

**Step 1: Run build**

```bash
./gradlew :composeApp:assembleDebug
```

**Step 2: Fix any compilation errors**

**Step 3: Update TODO doc**

Mark SettingsScreen as implemented in `docs/plans/TODO-next-features.md`

**Step 4: Commit**

```bash
git add -A && git commit -m "docs: mark SettingsScreen as implemented"
```
