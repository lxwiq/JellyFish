package com.lowiq.jellyfish.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.lowiq.jellyfish.domain.model.PendingPlaybackSync
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlaybackSyncStorage(private val dataStore: DataStore<Preferences>) {

    private val pendingSyncsKey = stringPreferencesKey("pending_playback_syncs")

    fun getPendingSyncs(): Flow<List<PendingPlaybackSync>> {
        return dataStore.data.map { prefs ->
            val data = prefs[pendingSyncsKey] ?: return@map emptyList()
            parseSyncs(data)
        }
    }

    suspend fun addPendingSync(sync: PendingPlaybackSync) {
        dataStore.edit { prefs ->
            val existing = prefs[pendingSyncsKey]?.let { parseSyncs(it) } ?: emptyList()
            // Replace existing sync for same item
            val updated = existing.filter { it.itemId != sync.itemId } + sync
            prefs[pendingSyncsKey] = serializeSyncs(updated)
        }
    }

    suspend fun removePendingSync(syncId: String) {
        dataStore.edit { prefs ->
            val existing = prefs[pendingSyncsKey]?.let { parseSyncs(it) } ?: emptyList()
            val updated = existing.filter { it.id != syncId }
            prefs[pendingSyncsKey] = serializeSyncs(updated)
        }
    }

    suspend fun clearAllSyncs() {
        dataStore.edit { prefs ->
            prefs.remove(pendingSyncsKey)
        }
    }

    private fun serializeSyncs(syncs: List<PendingPlaybackSync>): String {
        return syncs.joinToString("\n") { s ->
            "${s.id}||${s.itemId}||${s.serverId}||${s.positionTicks}||${s.playedPercentage}||${s.timestamp}"
        }
    }

    private fun parseSyncs(data: String): List<PendingPlaybackSync> {
        if (data.isBlank()) return emptyList()
        return data.split("\n").mapNotNull { entry ->
            val parts = entry.split("||")
            if (parts.size >= 6) {
                PendingPlaybackSync(
                    id = parts[0],
                    itemId = parts[1],
                    serverId = parts[2],
                    positionTicks = parts[3].toLongOrNull() ?: 0,
                    playedPercentage = parts[4].toFloatOrNull() ?: 0f,
                    timestamp = parts[5].toLongOrNull() ?: 0
                )
            } else null
        }
    }
}
