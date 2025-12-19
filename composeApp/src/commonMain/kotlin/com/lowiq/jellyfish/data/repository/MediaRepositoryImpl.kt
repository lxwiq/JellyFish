package com.lowiq.jellyfish.data.repository

import com.lowiq.jellyfish.data.local.MediaCache
import com.lowiq.jellyfish.data.local.SecureStorage
import com.lowiq.jellyfish.data.local.ServerStorage
import com.lowiq.jellyfish.data.remote.JellyfinDataSource
import com.lowiq.jellyfish.data.remote.Library as DataLibrary
import com.lowiq.jellyfish.data.remote.MediaItem as DataMediaItem
import com.lowiq.jellyfish.domain.model.ActivityItem
import com.lowiq.jellyfish.domain.model.ActivityType
import com.lowiq.jellyfish.domain.model.Library
import com.lowiq.jellyfish.domain.model.MediaItem
import com.lowiq.jellyfish.domain.repository.HomeMediaData
import com.lowiq.jellyfish.domain.repository.MediaRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first

class MediaRepositoryImpl(
    private val jellyfinDataSource: JellyfinDataSource,
    private val serverStorage: ServerStorage,
    private val secureStorage: SecureStorage,
    private val mediaCache: MediaCache
) : MediaRepository {

    override suspend fun getActivityFeed(serverId: String): Result<List<ActivityItem>> {
        return runCatching {
            val latest = getLatestItems(serverId).getOrDefault(emptyList())
            val resume = getResumeItems(serverId).getOrDefault(emptyList())
            val favorites = getFavoriteItems(serverId).getOrDefault(emptyList())

            // Combine all items, remove duplicates, and take top 50
            (resume + latest + favorites)
                .distinctBy { it.id }
                .take(50)
        }
    }

    override suspend fun getLatestItems(serverId: String): Result<List<ActivityItem>> {
        val serverAndToken = getServerAndToken(serverId)
        if (serverAndToken == null) {
            println("DEBUG: getLatestItems - server or token not found for serverId: $serverId")
            return Result.success(emptyList())
        }
        val (server, token) = serverAndToken
        val userId = server.userId ?: return Result.success(emptyList())

        return jellyfinDataSource.getLatestItems(server.url, token, userId)
            .map { items ->
                println("DEBUG: getLatestItems - got ${items.size} items")
                items.map { it.toActivityItem(ActivityType.ADDED) }
            }
    }

    override suspend fun getResumeItems(serverId: String): Result<List<ActivityItem>> {
        val (server, token) = getServerAndToken(serverId) ?: return Result.success(emptyList())
        val userId = server.userId ?: return Result.success(emptyList())

        return jellyfinDataSource.getResumeItems(server.url, token, userId)
            .map { items ->
                items.map { it.toActivityItem(ActivityType.RESUME) }
            }
    }

    override suspend fun getFavoriteItems(serverId: String): Result<List<ActivityItem>> {
        val (server, token) = getServerAndToken(serverId) ?: return Result.success(emptyList())
        val userId = server.userId ?: return Result.success(emptyList())

        return jellyfinDataSource.getFavoriteItems(server.url, token, userId)
            .map { items ->
                items.map { it.toActivityItem(ActivityType.FAVORITE) }
            }
    }

    override suspend fun getLibraries(serverId: String): Result<List<Library>> {
        val (server, token) = getServerAndToken(serverId) ?: return Result.success(emptyList())
        val userId = server.userId ?: return Result.success(emptyList())

        return jellyfinDataSource.getUserLibraries(server.url, token, userId)
            .map { libraries ->
                libraries.map { it.toDomainLibrary() }
            }
    }

    override suspend fun getLibraryItems(
        serverId: String,
        libraryId: String,
        limit: Int
    ): Result<List<MediaItem>> {
        val (server, token) = getServerAndToken(serverId) ?: return Result.success(emptyList())
        val userId = server.userId ?: return Result.success(emptyList())

        return jellyfinDataSource.getLibraryItems(server.url, token, userId, libraryId, limit)
            .map { items -> items.map { it.toDomainMediaItem() } }
    }

    private fun DataLibrary.toDomainLibrary() = Library(
        id = id,
        name = name,
        type = type,
        imageUrl = imageUrl
    )

    private suspend fun getServerAndToken(serverId: String): Pair<com.lowiq.jellyfish.domain.model.Server, String>? {
        val servers = serverStorage.getServers().first()
        val server = servers.find { it.id == serverId } ?: return null
        val token = secureStorage.getToken(serverId) ?: return null
        return server to token
    }

    override suspend fun getContinueWatching(serverId: String): Result<List<MediaItem>> {
        val (server, token) = getServerAndToken(serverId) ?: return Result.success(emptyList())
        val userId = server.userId ?: return Result.success(emptyList())

        return jellyfinDataSource.getResumeItems(server.url, token, userId)
            .map { items -> items.map { it.toDomainMediaItem(forceBackdrop = true) } }
    }

    override suspend fun getLatestMovies(serverId: String): Result<List<MediaItem>> {
        val (server, token) = getServerAndToken(serverId) ?: return Result.success(emptyList())
        val userId = server.userId ?: return Result.success(emptyList())

        return jellyfinDataSource.getLatestMovies(server.url, token, userId, limit = 10)
            .map { items -> items.map { it.toDomainMediaItem() } }
    }

    override suspend fun getLatestSeries(serverId: String): Result<List<MediaItem>> {
        val (server, token) = getServerAndToken(serverId) ?: return Result.success(emptyList())
        val userId = server.userId ?: return Result.success(emptyList())

        return jellyfinDataSource.getLatestSeries(server.url, token, userId, limit = 10)
            .map { items -> items.map { it.toDomainMediaItem() } }
    }

    override suspend fun getLatestMusic(serverId: String): Result<List<MediaItem>> {
        val (server, token) = getServerAndToken(serverId) ?: return Result.success(emptyList())
        val userId = server.userId ?: return Result.success(emptyList())

        return jellyfinDataSource.getLatestMusic(server.url, token, userId, limit = 10)
            .map { items -> items.map { it.toDomainMediaItem() } }
    }

    override suspend fun getFavorites(serverId: String): Result<List<MediaItem>> {
        val (server, token) = getServerAndToken(serverId) ?: return Result.success(emptyList())
        val userId = server.userId ?: return Result.success(emptyList())

        return jellyfinDataSource.getFavoriteItems(server.url, token, userId, limit = 10)
            .map { items -> items.map { it.toDomainMediaItem() } }
    }

    override suspend fun getNextUp(serverId: String): Result<List<MediaItem>> {
        val (server, token) = getServerAndToken(serverId) ?: return Result.success(emptyList())
        val userId = server.userId ?: return Result.success(emptyList())

        return jellyfinDataSource.getNextUpEpisodes(server.url, token, userId, limit = 10)
            .map { items -> items.map { it.toDomainMediaItem() } }
    }

    private fun DataMediaItem.toActivityItem(type: ActivityType): ActivityItem {
        val subtitle = when {
            this.type == "Episode" && seriesName != null -> {
                val episodeInfo = "S${seasonNumber ?: 1} E${episodeNumber ?: 1}"
                "$seriesName • $episodeInfo"
            }
            else -> this.type
        }

        val progress = if (playbackPositionTicks != null && runTimeTicks != null && runTimeTicks > 0) {
            (playbackPositionTicks.toFloat() / runTimeTicks.toFloat()).coerceIn(0f, 1f)
        } else null

        val timestamp = dateCreated?.let { parseAndFormatTimestamp(it) } ?: ""

        return ActivityItem(
            id = id,
            title = name,
            subtitle = subtitle,
            timestamp = timestamp,
            imageUrl = imageUrl,
            progress = progress,
            type = type
        )
    }

    private fun DataMediaItem.toDomainMediaItem(forceBackdrop: Boolean = false): MediaItem {
        val subtitle = when {
            this.type == "Episode" && seriesName != null -> {
                val episodeInfo = "S${seasonNumber ?: 1} E${episodeNumber ?: 1}"
                "$seriesName • $episodeInfo"
            }
            else -> this.type
        }

        val progress = if (playbackPositionTicks != null && runTimeTicks != null && runTimeTicks > 0) {
            (playbackPositionTicks.toFloat() / runTimeTicks.toFloat()).coerceIn(0f, 1f)
        } else null

        // Movies and Series use poster (vertical), Episodes use backdrop (horizontal)
        // forceBackdrop=true means always use horizontal (e.g., Continue Watching)
        val isPoster = if (forceBackdrop) false else this.type != "Episode"

        return MediaItem(
            id = id,
            title = name,
            subtitle = subtitle,
            imageUrl = imageUrl,
            progress = progress,
            isPoster = isPoster
        )
    }

    private fun parseAndFormatTimestamp(isoTimestamp: String): String {
        // Simple parsing for ISO 8601 timestamps like "2024-12-18T10:30:00.000Z"
        return try {
            // For now, just return a simple relative time
            // In a real app, you'd parse the ISO timestamp and calculate the difference
            "Recently"
        } catch (e: Exception) {
            ""
        }
    }

    /**
     * Converts a timestamp (milliseconds since epoch) to a relative time string
     */
    private fun formatRelativeTime(timestampMillis: Long): String {
        val now = System.currentTimeMillis()
        val diff = now - timestampMillis
        val seconds = diff / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24

        return when {
            seconds < 60 -> "Just now"
            minutes < 60 -> "$minutes ${if (minutes == 1L) "minute" else "minutes"} ago"
            hours < 24 -> "$hours ${if (hours == 1L) "hour" else "hours"} ago"
            days < 7 -> "$days ${if (days == 1L) "day" else "days"} ago"
            days < 30 -> "${days / 7} ${if (days / 7 == 1L) "week" else "weeks"} ago"
            days < 365 -> "${days / 30} ${if (days / 30 == 1L) "month" else "months"} ago"
            else -> "${days / 365} ${if (days / 365 == 1L) "year" else "years"} ago"
        }
    }

    override fun getCachedHomeData(serverId: String): Flow<HomeMediaData> {
        val flows = listOf(
            mediaCache.getCachedItems(serverId, MediaCache.CATEGORY_CONTINUE_WATCHING),
            mediaCache.getCachedItems(serverId, MediaCache.CATEGORY_LATEST_MOVIES),
            mediaCache.getCachedItems(serverId, MediaCache.CATEGORY_LATEST_SERIES),
            mediaCache.getCachedItems(serverId, MediaCache.CATEGORY_LATEST_MUSIC),
            mediaCache.getCachedItems(serverId, MediaCache.CATEGORY_FAVORITES),
            mediaCache.getCachedItems(serverId, MediaCache.CATEGORY_NEXT_UP)
        )
        return combine(flows) { results ->
            HomeMediaData(
                continueWatching = results[0],
                latestMovies = results[1],
                latestSeries = results[2],
                latestMusic = results[3],
                favorites = results[4],
                nextUp = results[5]
            )
        }
    }

    override suspend fun refreshHomeData(serverId: String): Result<HomeMediaData> = coroutineScope {
        runCatching {
            // Fetch all categories in parallel
            val continueWatchingDeferred = async { getContinueWatching(serverId) }
            val latestMoviesDeferred = async { getLatestMovies(serverId) }
            val latestSeriesDeferred = async { getLatestSeries(serverId) }
            val latestMusicDeferred = async { getLatestMusic(serverId) }
            val favoritesDeferred = async { getFavorites(serverId) }
            val nextUpDeferred = async { getNextUp(serverId) }

            val continueWatching = continueWatchingDeferred.await().getOrElse { emptyList() }
            val latestMovies = latestMoviesDeferred.await().getOrElse { emptyList() }
            val latestSeries = latestSeriesDeferred.await().getOrElse { emptyList() }
            val latestMusic = latestMusicDeferred.await().getOrElse { emptyList() }
            val favorites = favoritesDeferred.await().getOrElse { emptyList() }
            val nextUp = nextUpDeferred.await().getOrElse { emptyList() }

            // Cache all results
            mediaCache.cacheItems(serverId, MediaCache.CATEGORY_CONTINUE_WATCHING, continueWatching)
            mediaCache.cacheItems(serverId, MediaCache.CATEGORY_LATEST_MOVIES, latestMovies)
            mediaCache.cacheItems(serverId, MediaCache.CATEGORY_LATEST_SERIES, latestSeries)
            mediaCache.cacheItems(serverId, MediaCache.CATEGORY_LATEST_MUSIC, latestMusic)
            mediaCache.cacheItems(serverId, MediaCache.CATEGORY_FAVORITES, favorites)
            mediaCache.cacheItems(serverId, MediaCache.CATEGORY_NEXT_UP, nextUp)

            HomeMediaData(
                continueWatching = continueWatching,
                latestMovies = latestMovies,
                latestSeries = latestSeries,
                latestMusic = latestMusic,
                favorites = favorites,
                nextUp = nextUp
            )
        }
    }
}
