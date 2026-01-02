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

    // App preferences
    val userLanguage: String = "system",

    // Storage
    val usedStorageBytes: Long = 0,
    val storageLimitMb: Int = 10240,
    val isClearingCache: Boolean = false,
    val showDeleteAllDownloadsDialog: Boolean = false,
    val isDeletingAllDownloads: Boolean = false,

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
