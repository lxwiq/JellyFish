package com.lowiq.jellyfish.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.lowiq.jellyfish.domain.model.DisplayMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferencesStorage(private val dataStore: DataStore<Preferences>) {

    private val displayModeKey = stringPreferencesKey("display_mode")

    fun getDisplayMode(): Flow<DisplayMode> = dataStore.data.map { prefs ->
        val value = prefs[displayModeKey]
        value?.let {
            try { DisplayMode.valueOf(it) } catch (_: Exception) { DisplayMode.POSTER }
        } ?: DisplayMode.POSTER
    }

    suspend fun setDisplayMode(mode: DisplayMode) {
        dataStore.edit { prefs ->
            prefs[displayModeKey] = mode.name
        }
    }
}
