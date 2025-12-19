package com.lowiq.jellyfish.domain.repository

import com.lowiq.jellyfish.domain.model.ActivityItem
import com.lowiq.jellyfish.domain.model.MediaItem

interface MediaRepository {
    suspend fun getActivityFeed(serverId: String): Result<List<ActivityItem>>
    suspend fun getLatestItems(serverId: String): Result<List<ActivityItem>>
    suspend fun getResumeItems(serverId: String): Result<List<ActivityItem>>
    suspend fun getFavoriteItems(serverId: String): Result<List<ActivityItem>>

    // New methods using MediaItem domain model
    suspend fun getContinueWatching(serverId: String): Result<List<MediaItem>>
    suspend fun getLatestMovies(serverId: String): Result<List<MediaItem>>
    suspend fun getLatestSeries(serverId: String): Result<List<MediaItem>>
    suspend fun getLatestMusic(serverId: String): Result<List<MediaItem>>
    suspend fun getFavorites(serverId: String): Result<List<MediaItem>>
    suspend fun getNextUp(serverId: String): Result<List<MediaItem>>
}
