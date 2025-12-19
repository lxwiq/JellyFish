package com.lowiq.jellyfish.domain.repository

import com.lowiq.jellyfish.domain.model.ActivityItem
import com.lowiq.jellyfish.domain.model.Library
import com.lowiq.jellyfish.domain.model.MediaItem
import kotlinx.coroutines.flow.Flow

data class HomeMediaData(
    val continueWatching: List<MediaItem> = emptyList(),
    val latestMovies: List<MediaItem> = emptyList(),
    val latestSeries: List<MediaItem> = emptyList(),
    val latestMusic: List<MediaItem> = emptyList(),
    val favorites: List<MediaItem> = emptyList(),
    val nextUp: List<MediaItem> = emptyList()
)

interface MediaRepository {
    suspend fun getActivityFeed(serverId: String): Result<List<ActivityItem>>
    suspend fun getLatestItems(serverId: String): Result<List<ActivityItem>>
    suspend fun getResumeItems(serverId: String): Result<List<ActivityItem>>
    suspend fun getFavoriteItems(serverId: String): Result<List<ActivityItem>>

    // Libraries
    suspend fun getLibraries(serverId: String): Result<List<Library>>
    suspend fun getLibraryItems(serverId: String, libraryId: String, limit: Int = 20): Result<List<MediaItem>>

    // New methods using MediaItem domain model
    suspend fun getContinueWatching(serverId: String): Result<List<MediaItem>>
    suspend fun getLatestMovies(serverId: String): Result<List<MediaItem>>
    suspend fun getLatestSeries(serverId: String): Result<List<MediaItem>>
    suspend fun getLatestMusic(serverId: String): Result<List<MediaItem>>
    suspend fun getFavorites(serverId: String): Result<List<MediaItem>>
    suspend fun getNextUp(serverId: String): Result<List<MediaItem>>

    // Cached data methods
    fun getCachedHomeData(serverId: String): Flow<HomeMediaData>
    suspend fun refreshHomeData(serverId: String): Result<HomeMediaData>
}
