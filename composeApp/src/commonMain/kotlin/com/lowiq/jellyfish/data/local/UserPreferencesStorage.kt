package com.lowiq.jellyfish.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.lowiq.jellyfish.domain.model.DisplayMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferencesStorage(private val dataStore: DataStore<Preferences>) {

    private companion object {
        val KEY_DISPLAY_MODE = stringPreferencesKey("display_mode")
        val KEY_STREAMING_QUALITY = stringPreferencesKey("streaming_quality")
        val KEY_PREFERRED_AUDIO_LANG = stringPreferencesKey("preferred_audio_language")
        val KEY_PREFERRED_SUBTITLE_LANG = stringPreferencesKey("preferred_subtitle_language")
        val KEY_USER_LANGUAGE = stringPreferencesKey("user_language")
    }

    // Existing: Display mode
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

    // User language preference (for app UI)
    fun getLanguage(): Flow<String> = dataStore.data.map { prefs ->
        prefs[KEY_USER_LANGUAGE] ?: "system"
    }

    suspend fun setLanguage(code: String) {
        dataStore.edit { it[KEY_USER_LANGUAGE] = code }
    }
}
