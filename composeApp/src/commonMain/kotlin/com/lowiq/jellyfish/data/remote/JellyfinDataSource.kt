package com.lowiq.jellyfish.data.remote

import com.lowiq.jellyfish.domain.model.AdminUser
import com.lowiq.jellyfish.domain.model.LogEntry
import com.lowiq.jellyfish.domain.model.ScheduledTask
import com.lowiq.jellyfish.domain.model.User

interface JellyfinDataSource {
    suspend fun getServerInfo(serverUrl: String): Result<ServerInfo>
    suspend fun login(serverUrl: String, username: String, password: String): Result<AuthResult>
    suspend fun initiateQuickConnect(serverUrl: String): Result<QuickConnectResult>
    suspend fun checkQuickConnect(serverUrl: String, secret: String): Result<QuickConnectStatus>
    suspend fun logout(serverUrl: String, token: String)
    suspend fun getCurrentUser(serverUrl: String, token: String): Result<User>
    suspend fun getUserLibraries(serverUrl: String, token: String, userId: String): Result<List<Library>>
    suspend fun getLibraryItems(serverUrl: String, token: String, userId: String, libraryId: String, limit: Int = 20): Result<List<MediaItem>>
    suspend fun getLatestItems(serverUrl: String, token: String, userId: String, limit: Int = 20): Result<List<MediaItem>>
    suspend fun getResumeItems(serverUrl: String, token: String, userId: String, limit: Int = 10): Result<List<MediaItem>>
    suspend fun getFavoriteItems(serverUrl: String, token: String, userId: String, limit: Int = 20): Result<List<MediaItem>>
    suspend fun getLatestMovies(serverUrl: String, token: String, userId: String, limit: Int = 20): Result<List<MediaItem>>
    suspend fun getLatestSeries(serverUrl: String, token: String, userId: String, limit: Int = 20): Result<List<MediaItem>>
    suspend fun getLatestMusic(serverUrl: String, token: String, userId: String, limit: Int = 20): Result<List<MediaItem>>
    suspend fun getNextUpEpisodes(serverUrl: String, token: String, userId: String, limit: Int = 20): Result<List<MediaItem>>
    suspend fun searchItems(
        serverUrl: String,
        token: String,
        userId: String,
        query: String,
        limit: Int = 30
    ): Result<List<MediaItem>>
    suspend fun getLibraryItemsFiltered(
        serverUrl: String,
        token: String,
        userId: String,
        libraryId: String,
        libraryType: String? = null,
        limit: Int = 20,
        startIndex: Int = 0,
        sortBy: String = "DateCreated",
        sortOrder: String = "Descending",
        genres: List<String>? = null,
        years: List<Int>? = null,
        isPlayed: Boolean? = null,
        isFavorite: Boolean? = null
    ): Result<LibraryItemsResponse>
    suspend fun getLibraryFilters(
        serverUrl: String,
        token: String,
        userId: String,
        libraryId: String
    ): Result<LibraryFilters>
    suspend fun getItemDetails(serverUrl: String, token: String, userId: String, itemId: String): Result<ItemDetails>
    suspend fun getItemCast(serverUrl: String, token: String, userId: String, itemId: String): Result<List<PersonInfo>>
    suspend fun getSimilarItems(serverUrl: String, token: String, userId: String, itemId: String, limit: Int = 10): Result<List<MediaItem>>
    suspend fun getSeriesSeasons(serverUrl: String, token: String, userId: String, seriesId: String): Result<List<SeasonInfo>>
    suspend fun getSeasonEpisodes(serverUrl: String, token: String, userId: String, seriesId: String, seasonNumber: Int): Result<List<EpisodeInfo>>
    suspend fun toggleFavorite(serverUrl: String, token: String, userId: String, itemId: String, isFavorite: Boolean): Result<Unit>
    suspend fun toggleWatched(serverUrl: String, token: String, userId: String, itemId: String, isWatched: Boolean): Result<Unit>
    suspend fun getStreamInfo(
        serverUrl: String,
        token: String,
        userId: String,
        itemId: String
    ): Result<StreamInfo>

    suspend fun reportPlaybackStart(
        serverUrl: String,
        token: String,
        itemId: String,
        mediaSourceId: String,
        playSessionId: String
    ): Result<Unit>

    suspend fun reportPlaybackProgress(
        serverUrl: String,
        token: String,
        progress: PlaybackProgressInfo
    ): Result<Unit>

    suspend fun reportPlaybackStopped(
        serverUrl: String,
        token: String,
        itemId: String,
        mediaSourceId: String,
        positionTicks: Long,
        playSessionId: String
    ): Result<Unit>

    suspend fun getMediaSources(serverUrl: String, token: String, userId: String, itemId: String): Result<List<MediaSourceInfo>>
    fun getTranscodingDownloadUrl(serverUrl: String, token: String, itemId: String, bitrate: Int): String
    fun getDirectDownloadUrl(serverUrl: String, token: String, itemId: String): String
    suspend fun canUserTranscode(serverUrl: String, token: String): Boolean

    // Admin: Users
    suspend fun getUsers(serverUrl: String, token: String): Result<List<AdminUser>>
    suspend fun createUser(serverUrl: String, token: String, username: String, password: String): Result<AdminUser>
    suspend fun deleteUser(serverUrl: String, token: String, userId: String): Result<Unit>

    // Admin: Libraries
    suspend fun refreshLibrary(serverUrl: String, token: String): Result<Unit>

    // Admin: Logs
    suspend fun getServerLogs(serverUrl: String, token: String, limit: Int = 100): Result<List<LogEntry>>

    // Admin: Tasks
    suspend fun getScheduledTasks(serverUrl: String, token: String): Result<List<ScheduledTask>>
    suspend fun runTask(serverUrl: String, token: String, taskId: String): Result<Unit>
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

data class Library(
    val id: String,
    val name: String,
    val type: String, // movies, tvshows, music, etc.
    val imageUrl: String?
)

data class LibraryItemsResponse(
    val items: List<MediaItem>,
    val totalCount: Int
)

data class LibraryFilters(
    val genres: List<String>,
    val years: List<Int>
)

data class ItemDetails(
    val id: String,
    val name: String,
    val type: String,
    val overview: String?,
    val backdropUrl: String?,
    val posterUrl: String?,
    val year: String?,
    val runtime: Long?, // in ticks
    val communityRating: Float?,
    val genres: List<String>,
    val studios: List<String>,
    val seriesId: String?,
    val seriesName: String?,
    val seasonNumber: Int?,
    val episodeNumber: Int?,
    val trailerUrl: String?,
    val isFavorite: Boolean,
    val isPlayed: Boolean,
    val playbackPositionTicks: Long?
)

data class PersonInfo(
    val id: String,
    val name: String,
    val role: String?,
    val imageUrl: String?
)

data class SeasonInfo(
    val id: String,
    val name: String,
    val number: Int,
    val episodeCount: Int,
    val imageUrl: String?
)

data class EpisodeInfo(
    val id: String,
    val name: String,
    val overview: String?,
    val seasonNumber: Int,
    val episodeNumber: Int,
    val communityRating: Float?,
    val imageUrl: String?,
    val isPlayed: Boolean,
    val playbackPositionTicks: Long?,
    val runTimeTicks: Long?
)

data class SubtitleStreamInfo(
    val index: Int,
    val language: String?,
    val title: String?,
    val codec: String,
    val isExternal: Boolean,
    val isDefault: Boolean,
    val isForced: Boolean,
    val deliveryUrl: String?
)

data class StreamInfo(
    val directPlayUrl: String,
    val transcodingUrl: String?,
    val mediaSourceId: String,
    val playSessionId: String,
    val supportsDirectPlay: Boolean,
    val subtitleStreams: List<SubtitleStreamInfo> = emptyList()
)

data class PlaybackProgressInfo(
    val itemId: String,
    val mediaSourceId: String,
    val positionTicks: Long,
    val isPaused: Boolean,
    val playSessionId: String
)

data class MediaSourceInfo(
    val id: String,
    val name: String,
    val bitrate: Int?,
    val size: Long?,
    val container: String?
)
