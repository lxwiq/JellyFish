package com.lowiq.jellyfish.presentation.screens.settings

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.lowiq.jellyfish.data.local.DownloadSettingsStorage
import com.lowiq.jellyfish.data.local.UserPreferencesStorage
import com.lowiq.jellyfish.domain.repository.AdminRepository
import com.lowiq.jellyfish.domain.repository.AuthRepository
import com.lowiq.jellyfish.domain.repository.DownloadRepository
import com.lowiq.jellyfish.domain.repository.ServerRepository
import com.lowiq.jellyfish.presentation.locale.customAppLocale
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
            val server = serverRepository.getActiveServer().first()
            if (server != null) {
                val user = authRepository.getCurrentUser(server.id)
                _state.update { it.copy(
                    server = server,
                    user = user,
                    isAdmin = user?.isAdmin == true
                )}
            }

            val storageInfo = downloadRepository.getStorageInfo()
            _state.update { it.copy(usedStorageBytes = storageInfo.usedBytes) }
        }
    }

    private fun observePreferences() {
        screenModelScope.launch {
            userPreferencesStorage.getStreamingQuality().collect { quality ->
                _state.update { it.copy(streamingQuality = quality) }
            }
        }
        screenModelScope.launch {
            userPreferencesStorage.getPreferredAudioLanguage().collect { lang ->
                _state.update { it.copy(preferredAudioLanguage = lang) }
            }
        }
        screenModelScope.launch {
            userPreferencesStorage.getPreferredSubtitleLanguage().collect { lang ->
                _state.update { it.copy(preferredSubtitleLanguage = lang) }
            }
        }
        screenModelScope.launch {
            downloadSettingsStorage.defaultQuality.collect { quality ->
                _state.update { it.copy(downloadQuality = quality) }
            }
        }
        screenModelScope.launch {
            downloadSettingsStorage.storageLimitMb.collect { limit ->
                _state.update { it.copy(storageLimitMb = limit) }
            }
        }
        screenModelScope.launch {
            userPreferencesStorage.getLanguage().collect { language ->
                _state.update { it.copy(userLanguage = language) }
                // Sync global locale state with stored preference
                customAppLocale = if (language == "system") null else language
            }
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

    fun setUserLanguage(code: String) {
        screenModelScope.launch {
            userPreferencesStorage.setLanguage(code)
            // Update global locale state - null means "system", otherwise specific language code
            customAppLocale = if (code == "system") null else code
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
            // Get all completed downloads and delete them
            downloadRepository.getCompletedDownloads().first().forEach { download ->
                downloadRepository.deleteDownload(download.id)
            }
            val storageInfo = downloadRepository.getStorageInfo()
            _state.update { it.copy(
                isClearingCache = false,
                usedStorageBytes = storageInfo.usedBytes
            )}
            _events.emit(SettingsEvent.Success("Cache vidé"))
        }
    }

    fun showDeleteAllDownloadsDialog() {
        _state.update { it.copy(showDeleteAllDownloadsDialog = true) }
    }

    fun hideDeleteAllDownloadsDialog() {
        _state.update { it.copy(showDeleteAllDownloadsDialog = false) }
    }

    fun deleteAllDownloads() {
        screenModelScope.launch {
            _state.update { it.copy(isDeletingAllDownloads = true, showDeleteAllDownloadsDialog = false) }
            val result = downloadRepository.deleteAllDownloads()
            val storageInfo = downloadRepository.getStorageInfo()
            _state.update { it.copy(
                isDeletingAllDownloads = false,
                usedStorageBytes = storageInfo.usedBytes
            )}
            val freedMb = result.freedBytes / (1024 * 1024)
            _events.emit(SettingsEvent.Success("${result.deletedCount} téléchargement(s) supprimé(s), ${freedMb} Mo libérés"))
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
