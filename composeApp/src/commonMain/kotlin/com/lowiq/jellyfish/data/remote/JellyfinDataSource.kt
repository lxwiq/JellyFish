package com.lowiq.jellyfish.data.remote

import com.lowiq.jellyfish.domain.model.User

interface JellyfinDataSource {
    suspend fun getServerInfo(serverUrl: String): Result<ServerInfo>
    suspend fun login(serverUrl: String, username: String, password: String): Result<AuthResult>
    suspend fun initiateQuickConnect(serverUrl: String): Result<QuickConnectResult>
    suspend fun checkQuickConnect(serverUrl: String, secret: String): Result<QuickConnectStatus>
    suspend fun logout(serverUrl: String, token: String)
    suspend fun getCurrentUser(serverUrl: String, token: String): Result<User>
    suspend fun getLatestItems(serverUrl: String, token: String, limit: Int = 20): Result<List<MediaItem>>
    suspend fun getResumeItems(serverUrl: String, token: String, userId: String, limit: Int = 10): Result<List<MediaItem>>
    suspend fun getFavoriteItems(serverUrl: String, token: String, userId: String, limit: Int = 20): Result<List<MediaItem>>
    suspend fun getLatestMovies(serverUrl: String, token: String, limit: Int = 20): Result<List<MediaItem>>
    suspend fun getLatestSeries(serverUrl: String, token: String, limit: Int = 20): Result<List<MediaItem>>
    suspend fun getLatestMusic(serverUrl: String, token: String, limit: Int = 20): Result<List<MediaItem>>
    suspend fun getNextUpEpisodes(serverUrl: String, token: String, userId: String, limit: Int = 20): Result<List<MediaItem>>
}

data class ServerInfo(
    val id: String,
    val name: String,
    val version: String
)

data class AuthResult(
    val userId: String,
    val username: String,
    val token: String,
    val serverId: String
)

data class QuickConnectResult(
    val secret: String,
    val code: String
)

sealed class QuickConnectStatus {
    object Waiting : QuickConnectStatus()
    data class Authenticated(val token: String, val userId: String) : QuickConnectStatus()
    object Expired : QuickConnectStatus()
}

data class MediaItem(
    val id: String,
    val name: String,
    val type: String, // Movie, Episode, Series, etc.
    val seriesName: String?, // For episodes
    val seasonNumber: Int?,
    val episodeNumber: Int?,
    val imageUrl: String?,
    val playbackPositionTicks: Long?,
    val runTimeTicks: Long?,
    val dateCreated: String?
)
