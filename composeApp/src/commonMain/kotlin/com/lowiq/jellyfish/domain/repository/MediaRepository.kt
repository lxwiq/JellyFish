package com.lowiq.jellyfish.domain.repository

import com.lowiq.jellyfish.domain.model.ActivityItem

interface MediaRepository {
    suspend fun getActivityFeed(serverId: String): Result<List<ActivityItem>>
    suspend fun getLatestItems(serverId: String): Result<List<ActivityItem>>
    suspend fun getResumeItems(serverId: String): Result<List<ActivityItem>>
    suspend fun getFavoriteItems(serverId: String): Result<List<ActivityItem>>
}
