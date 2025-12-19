package com.lowiq.jellyfish.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SearchHistoryStorage(private val dataStore: DataStore<Preferences>) {

    private val historyKey = stringPreferencesKey("search_history")
    private val maxHistorySize = 10

    fun getHistory(): Flow<List<String>> {
        return dataStore.data.map { prefs ->
            prefs[historyKey]?.split("||")?.filter { it.isNotBlank() } ?: emptyList()
        }
    }

    suspend fun addToHistory(query: String) {
        if (query.isBlank()) return
        dataStore.edit { prefs ->
            val existing = prefs[historyKey]?.split("||")?.filter { it.isNotBlank() } ?: emptyList()
            val updated = listOf(query) + existing.filter { it != query }
            prefs[historyKey] = updated.take(maxHistorySize).joinToString("||")
        }
    }

    suspend fun removeFromHistory(query: String) {
        dataStore.edit { prefs ->
            val existing = prefs[historyKey]?.split("||")?.filter { it.isNotBlank() } ?: emptyList()
            prefs[historyKey] = existing.filter { it != query }.joinToString("||")
        }
    }

    suspend fun clearHistory() {
        dataStore.edit { prefs ->
            prefs.remove(historyKey)
        }
    }
}
