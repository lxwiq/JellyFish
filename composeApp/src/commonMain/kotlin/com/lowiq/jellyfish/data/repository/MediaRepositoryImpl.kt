package com.lowiq.jellyfish.data.repository

import com.lowiq.jellyfish.data.local.MediaCache
import com.lowiq.jellyfish.data.local.SecureStorage
import com.lowiq.jellyfish.data.local.ServerStorage
import com.lowiq.jellyfish.data.remote.JellyfinDataSource
import com.lowiq.jellyfish.data.remote.PlaybackProgressInfo
import com.lowiq.jellyfish.data.remote.StreamInfo
import com.lowiq.jellyfish.data.remote.Library as DataLibrary
import com.lowiq.jellyfish.data.remote.MediaItem as DataMediaItem
import com.lowiq.jellyfish.domain.model.ActivityItem
import com.lowiq.jellyfish.domain.model.ActivityType
import com.lowiq.jellyfish.domain.model.CastMember
import com.lowiq.jellyfish.domain.model.Episode
import com.lowiq.jellyfish.domain.model.EpisodeDetails
import com.lowiq.jellyfish.domain.model.Library
import com.lowiq.jellyfish.domain.model.MediaItem
import com.lowiq.jellyfish.domain.model.MediaType
import com.lowiq.jellyfish.domain.model.MovieDetails
import com.lowiq.jellyfish.domain.model.Season
import com.lowiq.jellyfish.domain.model.SeriesDetails
import com.lowiq.jellyfish.domain.repository.HomeMediaData
import com.lowiq.jellyfish.domain.repository.MediaRepository
import com.lowiq.jellyfish.util.currentTimeMillis
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

    companion object {
        // Jellyfin uses ticks as its time unit, where 1 tick = 100 nanoseconds
        // This constant converts ticks to seconds (10,000,000 ticks = 1 second)
        private const val TICKS_PER_SECOND = 10_000_000L
    }

    /**
     * Calculates playback progress as a percentage (0.0 to 1.0) from Jellyfin tick values.
     * Returns null if either value is null or if runtime is invalid.
     */
    private fun calculateProgress(playbackTicks: Long?, runtimeTicks: Long?): Float? {
        if (playbackTicks == null || runtimeTicks == null || runtimeTicks <= 0) return null
        return (playbackTicks.toFloat() / runtimeTicks.toFloat()).coerceIn(0f, 1f)
    }

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

    override suspend fun getLibraryItemsPaginated(
        serverId: String,
        libraryId: String,
        limit: Int,
        offset: Int,
        sortBy: com.lowiq.jellyfish.domain.model.SortOption,
        genres: List<String>?,
        years: List<Int>?,
        isWatched: Boolean?,
        isFavorite: Boolean?
    ): Result<com.lowiq.jellyfish.domain.model.PaginatedResult<MediaItem>> {
        val (server, token) = getServerAndToken(serverId) ?: return Result.success(
            com.lowiq.jellyfish.domain.model.PaginatedResult(emptyList(), 0, false)
        )
        val userId = server.userId ?: return Result.success(
            com.lowiq.jellyfish.domain.model.PaginatedResult(emptyList(), 0, false)
        )

        val sortOrder = if (sortBy == com.lowiq.jellyfish.domain.model.SortOption.NAME) "Ascending" else "Descending"

        return jellyfinDataSource.getLibraryItemsFiltered(
            serverUrl = server.url,
            token = token,
            userId = userId,
            libraryId = libraryId,
            limit = limit,
            startIndex = offset,
            sortBy = sortBy.apiValue,
            sortOrder = sortOrder,
            genres = genres,
            years = years,
            isPlayed = isWatched,
            isFavorite = isFavorite
        ).map { response ->
            com.lowiq.jellyfish.domain.model.PaginatedResult(
                items = response.items.map { it.toDomainMediaItem() },
                totalCount = response.totalCount,
                hasMore = offset + response.items.size < response.totalCount
            )
        }
    }

    override suspend fun getLibraryFilters(
        serverId: String,
        libraryId: String
    ): Result<com.lowiq.jellyfish.domain.model.LibraryFilters> {
        val (server, token) = getServerAndToken(serverId) ?: return Result.success(
            com.lowiq.jellyfish.domain.model.LibraryFilters(emptyList(), emptyList())
        )
        val userId = server.userId ?: return Result.success(
            com.lowiq.jellyfish.domain.model.LibraryFilters(emptyList(), emptyList())
        )

        return jellyfinDataSource.getLibraryFilters(server.url, token, userId, libraryId)
            .map { filters ->
                com.lowiq.jellyfish.domain.model.LibraryFilters(
                    genres = filters.genres,
                    years = filters.years
                )
            }
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
        // Extract year from dateCreated (format: "2024-12-18T10:30:00.000Z")
        val year = dateCreated?.take(4)

        val subtitle = when {
            this.type == "Episode" && seriesName != null -> {
                val episodeInfo = "S${seasonNumber ?: 1} E${episodeNumber ?: 1}"
                "$seriesName • $episodeInfo"
            }
            else -> year
        }

        val progress = if (playbackPositionTicks != null && runTimeTicks != null && runTimeTicks > 0) {
            (playbackPositionTicks.toFloat() / runTimeTicks.toFloat()).coerceIn(0f, 1f)
        } else null

        // Movies and Series use poster (vertical), Episodes use backdrop (horizontal)
        // forceBackdrop=true means always use horizontal (e.g., Continue Watching)
        val isPoster = if (forceBackdrop) false else this.type != "Episode"

        val mediaType = when (this.type) {
            "Movie" -> MediaType.MOVIE
            "Series" -> MediaType.SERIES
            "Episode" -> MediaType.EPISODE
            "Audio", "MusicAlbum" -> MediaType.MUSIC
            else -> MediaType.OTHER
        }

        return MediaItem(
            id = id,
            title = name,
            subtitle = subtitle,
            imageUrl = imageUrl,
            progress = progress,
            isPoster = isPoster,
            type = mediaType
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
        val now = currentTimeMillis()
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

    private fun formatRuntime(ticks: Long?): String? {
        if (ticks == null) return null
        val totalMinutes = ticks / TICKS_PER_SECOND / 60
        val hours = totalMinutes / 60
        val minutes = totalMinutes % 60
        return when {
            hours > 0 -> "${hours}h ${minutes}min"
            else -> "${minutes}min"
        }
    }

    override suspend fun getMovieDetails(serverId: String, itemId: String): Result<MovieDetails> {
        val (server, token) = getServerAndToken(serverId)
            ?: return Result.failure(Exception("Server not found"))
        val userId = server.userId
            ?: return Result.failure(Exception("User not found"))

        return runCatching {
            val details = jellyfinDataSource.getItemDetails(server.url, token, userId, itemId).getOrThrow()
            val cast = jellyfinDataSource.getItemCast(server.url, token, userId, itemId).getOrElse { emptyList() }
            val similar = jellyfinDataSource.getSimilarItems(server.url, token, userId, itemId).getOrElse { emptyList() }

            MovieDetails(
                id = details.id,
                title = details.name,
                overview = details.overview,
                backdropUrl = details.backdropUrl,
                posterUrl = details.posterUrl,
                year = details.year,
                runtime = formatRuntime(details.runtime),
                rating = details.communityRating,
                genres = details.genres,
                studio = details.studios.firstOrNull(),
                cast = cast.map { CastMember(it.id, it.name, it.role, it.imageUrl) },
                similarItems = similar.map { it.toDomainMediaItem() },
                trailerUrl = details.trailerUrl,
                isFavorite = details.isFavorite,
                isWatched = details.isPlayed
            )
        }
    }

    override suspend fun getSeriesDetails(serverId: String, itemId: String): Result<SeriesDetails> {
        val (server, token) = getServerAndToken(serverId)
            ?: return Result.failure(Exception("Server not found"))
        val userId = server.userId
            ?: return Result.failure(Exception("User not found"))

        return runCatching {
            val details = jellyfinDataSource.getItemDetails(server.url, token, userId, itemId).getOrThrow()
            val cast = jellyfinDataSource.getItemCast(server.url, token, userId, itemId).getOrElse { emptyList() }
            val seasons = jellyfinDataSource.getSeriesSeasons(server.url, token, userId, itemId).getOrElse { emptyList() }
            val similar = jellyfinDataSource.getSimilarItems(server.url, token, userId, itemId).getOrElse { emptyList() }

            SeriesDetails(
                id = details.id,
                title = details.name,
                overview = details.overview,
                backdropUrl = details.backdropUrl,
                posterUrl = details.posterUrl,
                year = details.year,
                seasonCount = seasons.size,
                rating = details.communityRating,
                genres = details.genres,
                studio = details.studios.firstOrNull(),
                cast = cast.map { CastMember(it.id, it.name, it.role, it.imageUrl) },
                seasons = seasons.map { Season(it.id, it.name, it.number, it.episodeCount, it.imageUrl) },
                similarItems = similar.map { it.toDomainMediaItem() },
                isFavorite = details.isFavorite,
                isWatched = details.isPlayed
            )
        }
    }

    override suspend fun getEpisodeDetails(serverId: String, itemId: String): Result<EpisodeDetails> {
        val (server, token) = getServerAndToken(serverId)
            ?: return Result.failure(Exception("Server not found"))
        val userId = server.userId
            ?: return Result.failure(Exception("User not found"))

        return runCatching {
            val details = jellyfinDataSource.getItemDetails(server.url, token, userId, itemId).getOrThrow()
            val guestStars = jellyfinDataSource.getItemCast(server.url, token, userId, itemId).getOrElse { emptyList() }

            val seriesId = details.seriesId ?: throw Exception("Series ID not found")
            val seasonNumber = details.seasonNumber ?: 1
            val episodeNumber = details.episodeNumber ?: 1

            val episodes = jellyfinDataSource.getSeasonEpisodes(server.url, token, userId, seriesId, seasonNumber)
                .getOrElse { emptyList() }

            val sortedEpisodes = episodes.sortedBy { it.episodeNumber }
            val currentIndex = sortedEpisodes.indexOfFirst { it.id == itemId }
            val previousId = if (currentIndex > 0) sortedEpisodes[currentIndex - 1].id else null
            val nextId = if (currentIndex < sortedEpisodes.size - 1) sortedEpisodes[currentIndex + 1].id else null

            val progress = calculateProgress(details.playbackPositionTicks, details.runtime)

            EpisodeDetails(
                id = details.id,
                title = details.name,
                overview = details.overview,
                thumbnailUrl = details.backdropUrl ?: details.posterUrl,
                seasonNumber = seasonNumber,
                episodeNumber = episodeNumber,
                runtime = formatRuntime(details.runtime),
                rating = details.communityRating,
                seriesId = seriesId,
                seriesName = details.seriesName ?: "Series",
                guestStars = guestStars.map { CastMember(it.id, it.name, it.role, it.imageUrl) },
                seasonEpisodes = sortedEpisodes.map { ep ->
                    val epProgress = calculateProgress(ep.playbackPositionTicks, ep.runTimeTicks)
                    Episode(
                        id = ep.id,
                        title = ep.name,
                        overview = ep.overview,
                        seasonNumber = ep.seasonNumber,
                        episodeNumber = ep.episodeNumber,
                        runtime = formatRuntime(ep.runTimeTicks),
                        rating = ep.communityRating,
                        thumbnailUrl = ep.imageUrl,
                        progress = epProgress,
                        isWatched = ep.isPlayed
                    )
                },
                previousEpisodeId = previousId,
                nextEpisodeId = nextId,
                isFavorite = details.isFavorite,
                isWatched = details.isPlayed,
                progress = progress
            )
        }
    }

    override suspend fun getSeasonEpisodes(
        serverId: String,
        seriesId: String,
        seasonNumber: Int
    ): Result<List<Episode>> {
        val (server, token) = getServerAndToken(serverId)
            ?: return Result.failure(Exception("Server not found"))
        val userId = server.userId
            ?: return Result.failure(Exception("User not found"))

        return jellyfinDataSource.getSeasonEpisodes(server.url, token, userId, seriesId, seasonNumber)
            .map { episodes ->
                episodes.map { ep ->
                    val progress = calculateProgress(ep.playbackPositionTicks, ep.runTimeTicks)
                    Episode(
                        id = ep.id,
                        title = ep.name,
                        overview = ep.overview,
                        seasonNumber = ep.seasonNumber,
                        episodeNumber = ep.episodeNumber,
                        runtime = formatRuntime(ep.runTimeTicks),
                        rating = ep.communityRating,
                        thumbnailUrl = ep.imageUrl,
                        progress = progress,
                        isWatched = ep.isPlayed
                    )
                }
            }
    }

    override suspend fun toggleFavorite(serverId: String, itemId: String, isFavorite: Boolean): Result<Boolean> {
        val (server, token) = getServerAndToken(serverId)
            ?: return Result.failure(Exception("Server not found"))
        val userId = server.userId
            ?: return Result.failure(Exception("User not found"))

        return jellyfinDataSource.toggleFavorite(server.url, token, userId, itemId, isFavorite)
            .map { isFavorite }
    }

    override suspend fun toggleWatched(serverId: String, itemId: String, isWatched: Boolean): Result<Boolean> {
        val (server, token) = getServerAndToken(serverId)
            ?: return Result.failure(Exception("Server not found"))
        val userId = server.userId
            ?: return Result.failure(Exception("User not found"))

        return jellyfinDataSource.toggleWatched(server.url, token, userId, itemId, isWatched)
            .map { isWatched }
    }

    override suspend fun getStreamInfo(serverId: String, itemId: String): Result<StreamInfo> {
        val (server, token) = getServerAndToken(serverId) ?: return Result.failure(Exception("Server not found"))
        val userId = server.userId ?: return Result.failure(Exception("User not found"))

        return jellyfinDataSource.getStreamInfo(server.url, token, userId, itemId)
    }

    override suspend fun reportPlaybackStart(
        serverId: String,
        itemId: String,
        mediaSourceId: String,
        playSessionId: String
    ): Result<Unit> {
        val (server, token) = getServerAndToken(serverId) ?: return Result.failure(Exception("Server not found"))

        return jellyfinDataSource.reportPlaybackStart(server.url, token, itemId, mediaSourceId, playSessionId)
    }

    override suspend fun reportPlaybackProgress(
        serverId: String,
        progress: PlaybackProgressInfo
    ): Result<Unit> {
        val (server, token) = getServerAndToken(serverId) ?: return Result.failure(Exception("Server not found"))

        return jellyfinDataSource.reportPlaybackProgress(server.url, token, progress)
    }

    override suspend fun reportPlaybackStopped(
        serverId: String,
        itemId: String,
        mediaSourceId: String,
        positionTicks: Long,
        playSessionId: String
    ): Result<Unit> {
        val (server, token) = getServerAndToken(serverId) ?: return Result.failure(Exception("Server not found"))

        return jellyfinDataSource.reportPlaybackStopped(server.url, token, itemId, mediaSourceId, positionTicks, playSessionId)
    }

    override suspend fun search(serverId: String, query: String): Result<List<MediaItem>> {
        val server = serverStorage.getServers().first().find { it.id == serverId }
            ?: return Result.failure(Exception("Server not found"))
        val token = secureStorage.getToken(serverId)
            ?: return Result.failure(Exception("Not authenticated"))
        val userId = server.userId ?: return Result.failure(Exception("No user ID"))

        return jellyfinDataSource.searchItems(server.url, token, userId, query)
            .map { items -> items.map { it.toDomainMediaItem() } }
    }
}
