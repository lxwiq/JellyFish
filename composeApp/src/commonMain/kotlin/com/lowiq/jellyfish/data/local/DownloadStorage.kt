package com.lowiq.jellyfish.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.lowiq.jellyfish.domain.model.Download
import com.lowiq.jellyfish.domain.model.DownloadStatus
import com.lowiq.jellyfish.util.currentTimeMillis
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DownloadStorage(private val dataStore: DataStore<Preferences>) {

    private val downloadsKey = stringPreferencesKey("downloads_data")
    private val pendingSyncsKey = stringPreferencesKey("pending_syncs_data")

    fun getAllDownloads(): Flow<List<Download>> {
        return dataStore.data.map { prefs ->
            val data = prefs[downloadsKey] ?: return@map emptyList()
            parseDownloads(data)
        }
    }

    suspend fun saveDownload(download: Download) {
        dataStore.edit { prefs ->
            val existing = prefs[downloadsKey]?.let { parseDownloads(it) } ?: emptyList()
            val updated = existing.filter { it.id != download.id } + download
            prefs[downloadsKey] = serializeDownloads(updated)
        }
    }

    suspend fun removeDownload(downloadId: String) {
        dataStore.edit { prefs ->
            val existing = prefs[downloadsKey]?.let { parseDownloads(it) } ?: emptyList()
            val updated = existing.filter { it.id != downloadId }
            prefs[downloadsKey] = serializeDownloads(updated)
        }
    }

    suspend fun updateProgress(downloadId: String, progress: Float, downloadedBytes: Long) {
        dataStore.edit { prefs ->
            val existing = prefs[downloadsKey]?.let { parseDownloads(it) } ?: emptyList()
            val updated = existing.map {
                if (it.id == downloadId) it.copy(progress = progress, downloadedBytes = downloadedBytes)
                else it
            }
            prefs[downloadsKey] = serializeDownloads(updated)
        }
    }

    suspend fun updateStatus(downloadId: String, status: DownloadStatus, filePath: String? = null, errorMessage: String? = null) {
        dataStore.edit { prefs ->
            val existing = prefs[downloadsKey]?.let { parseDownloads(it) } ?: emptyList()
            val now = currentTimeMillis()
            val updated = existing.map {
                if (it.id == downloadId) it.copy(
                    status = status,
                    filePath = filePath ?: it.filePath,
                    completedAt = if (status == DownloadStatus.COMPLETED) now else it.completedAt,
                    errorMessage = errorMessage
                )
                else it
            }
            prefs[downloadsKey] = serializeDownloads(updated)
        }
    }

    suspend fun updatePlaybackPosition(downloadId: String, positionMs: Long) {
        dataStore.edit { prefs ->
            val existing = prefs[downloadsKey]?.let { parseDownloads(it) } ?: emptyList()
            val now = currentTimeMillis()
            val updated = existing.map {
                if (it.id == downloadId) it.copy(
                    lastPlayedPositionMs = positionMs,
                    lastPlayedAt = now
                )
                else it
            }
            prefs[downloadsKey] = serializeDownloads(updated)
        }
    }

    suspend fun clearAll() {
        dataStore.edit { prefs ->
            prefs.remove(downloadsKey)
        }
    }

    private fun serializeDownloads(downloads: List<Download>): String {
        return downloads.joinToString("\n\n") { d ->
            listOf(
                d.id, d.itemId, d.serverId, d.title, d.subtitle.orEmpty(),
                d.imageUrl.orEmpty(), d.quality, d.bitrate.toString(), d.status.name,
                d.progress.toString(), d.totalBytes.toString(), d.downloadedBytes.toString(),
                d.filePath.orEmpty(), d.createdAt.toString(), d.completedAt?.toString().orEmpty(),
                d.errorMessage.orEmpty(),
                d.lastPlayedPositionMs.toString(), d.lastPlayedAt?.toString().orEmpty()
            ).joinToString("||")
        }
    }

    private fun parseDownloads(data: String): List<Download> {
        if (data.isBlank()) return emptyList()
        return data.split("\n\n").mapNotNull { entry ->
            val parts = entry.split("||")
            if (parts.size >= 14) {
                Download(
                    id = parts[0],
                    itemId = parts[1],
                    serverId = parts[2],
                    title = parts[3],
                    subtitle = parts[4].takeIf { it.isNotEmpty() },
                    imageUrl = parts[5].takeIf { it.isNotEmpty() },
                    quality = parts[6],
                    bitrate = parts[7].toIntOrNull() ?: 0,
                    status = DownloadStatus.valueOf(parts[8]),
                    progress = parts[9].toFloatOrNull() ?: 0f,
                    totalBytes = parts[10].toLongOrNull() ?: 0,
                    downloadedBytes = parts[11].toLongOrNull() ?: 0,
                    filePath = parts[12].takeIf { it.isNotEmpty() },
                    createdAt = parts[13].toLongOrNull() ?: 0,
                    completedAt = parts.getOrNull(14)?.toLongOrNull(),
                    errorMessage = parts.getOrNull(15)?.takeIf { it.isNotEmpty() },
                    lastPlayedPositionMs = parts.getOrNull(16)?.toLongOrNull() ?: 0,
                    lastPlayedAt = parts.getOrNull(17)?.toLongOrNull()
                )
            } else null
        }
    }
}
