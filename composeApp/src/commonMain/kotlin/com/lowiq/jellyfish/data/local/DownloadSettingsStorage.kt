package com.lowiq.jellyfish.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DownloadSettingsStorage(private val dataStore: DataStore<Preferences>) {

    private companion object {
        val KEY_DEFAULT_QUALITY = stringPreferencesKey("download_default_quality")
        val KEY_ALWAYS_ASK_QUALITY = booleanPreferencesKey("download_always_ask_quality")
        val KEY_PARALLEL_DOWNLOADS = intPreferencesKey("download_parallel_count")
        val KEY_AUTO_CLEANUP = booleanPreferencesKey("download_auto_cleanup")
        val KEY_STORAGE_LIMIT_MB = intPreferencesKey("download_storage_limit_mb")
    }

    val defaultQuality: Flow<String> = dataStore.data.map { prefs ->
        prefs[KEY_DEFAULT_QUALITY] ?: "1080p"
    }

    val alwaysAskQuality: Flow<Boolean> = dataStore.data.map { prefs ->
        prefs[KEY_ALWAYS_ASK_QUALITY] ?: true
    }

    val parallelDownloads: Flow<Int> = dataStore.data.map { prefs ->
        prefs[KEY_PARALLEL_DOWNLOADS] ?: 2
    }

    val autoCleanup: Flow<Boolean> = dataStore.data.map { prefs ->
        prefs[KEY_AUTO_CLEANUP] ?: false
    }

    val storageLimitMb: Flow<Int> = dataStore.data.map { prefs ->
        prefs[KEY_STORAGE_LIMIT_MB] ?: 10240 // 10 GB
    }

    suspend fun setDefaultQuality(quality: String) {
        dataStore.edit { it[KEY_DEFAULT_QUALITY] = quality }
    }

    suspend fun setAlwaysAskQuality(ask: Boolean) {
        dataStore.edit { it[KEY_ALWAYS_ASK_QUALITY] = ask }
    }

    suspend fun setParallelDownloads(count: Int) {
        dataStore.edit { it[KEY_PARALLEL_DOWNLOADS] = count.coerceIn(1, 5) }
    }

    suspend fun setAutoCleanup(enabled: Boolean) {
        dataStore.edit { it[KEY_AUTO_CLEANUP] = enabled }
    }

    suspend fun setStorageLimitMb(limitMb: Int) {
        dataStore.edit { it[KEY_STORAGE_LIMIT_MB] = limitMb }
    }
}
