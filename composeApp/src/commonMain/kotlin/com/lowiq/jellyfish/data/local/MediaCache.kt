package com.lowiq.jellyfish.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.lowiq.jellyfish.domain.model.MediaItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MediaCache(private val dataStore: DataStore<Preferences>) {

    private fun cacheKey(serverId: String, category: String) =
        stringPreferencesKey("media_${serverId}_$category")

    fun getCachedItems(serverId: String, category: String): Flow<List<MediaItem>> {
        return dataStore.data.map { prefs ->
            val json = prefs[cacheKey(serverId, category)]
            if (json.isNullOrBlank()) emptyList() else parseItems(json)
        }
    }

    suspend fun cacheItems(serverId: String, category: String, items: List<MediaItem>) {
        dataStore.edit { prefs ->
            prefs[cacheKey(serverId, category)] = serializeItems(items)
        }
    }

    suspend fun clearCache(serverId: String) {
        dataStore.edit { prefs ->
            val keysToRemove = prefs.asMap().keys.filter {
                it.name.startsWith("media_${serverId}_")
            }
            keysToRemove.forEach { prefs.remove(it) }
        }
    }

    private fun serializeItems(items: List<MediaItem>): String {
        return items.joinToString(separator = "|||") { item ->
            listOf(
                item.id,
                item.title,
                item.subtitle.orEmpty(),
                item.imageUrl.orEmpty(),
                item.progress?.toString().orEmpty(),
                item.isPoster.toString()
            ).joinToString("::")
        }
    }

    private fun parseItems(data: String): List<MediaItem> {
        if (data.isBlank()) return emptyList()
        return data.split("|||").mapNotNull { entry ->
            val parts = entry.split("::")
            if (parts.size >= 2) {
                MediaItem(
                    id = parts[0],
                    title = parts[1],
                    subtitle = parts.getOrNull(2)?.takeIf { it.isNotEmpty() },
                    imageUrl = parts.getOrNull(3)?.takeIf { it.isNotEmpty() },
                    progress = parts.getOrNull(4)?.takeIf { it.isNotEmpty() }?.toFloatOrNull(),
                    isPoster = parts.getOrNull(5)?.toBooleanStrictOrNull() ?: false
                )
            } else null
        }
    }

    companion object {
        const val CATEGORY_CONTINUE_WATCHING = "continue_watching"
        const val CATEGORY_LATEST_MOVIES = "latest_movies"
        const val CATEGORY_LATEST_SERIES = "latest_series"
        const val CATEGORY_LATEST_MUSIC = "latest_music"
        const val CATEGORY_FAVORITES = "favorites"
        const val CATEGORY_NEXT_UP = "next_up"
    }
}
