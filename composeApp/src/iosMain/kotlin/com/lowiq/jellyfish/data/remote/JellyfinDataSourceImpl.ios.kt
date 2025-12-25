package com.lowiq.jellyfish.data.remote

import com.lowiq.jellyfish.domain.model.User

/**
 * iOS implementation of JellyfinDataSource.
 * Note: The Jellyfin SDK is JVM-only, so iOS functionality is limited.
 * This is a stub implementation that needs to be replaced with a proper
 * Ktor-based implementation for full iOS support.
 */
class JellyfinDataSourceImpl : JellyfinDataSource {
    override suspend fun getServerInfo(serverUrl: String): Result<ServerInfo> =
        Result.failure(NotImplementedError("iOS implementation pending"))

    override suspend fun login(serverUrl: String, username: String, password: String): Result<AuthResult> =
        Result.failure(NotImplementedError("iOS implementation pending"))

    override suspend fun initiateQuickConnect(serverUrl: String): Result<QuickConnectResult> =
        Result.failure(NotImplementedError("iOS implementation pending"))

    override suspend fun checkQuickConnect(serverUrl: String, secret: String): Result<QuickConnectStatus> =
        Result.failure(NotImplementedError("iOS implementation pending"))

    override suspend fun logout(serverUrl: String, token: String) {}

    override suspend fun getCurrentUser(serverUrl: String, token: String): Result<User> =
        Result.failure(NotImplementedError("iOS implementation pending"))

    override suspend fun getUserLibraries(serverUrl: String, token: String, userId: String): Result<List<Library>> =
        Result.failure(NotImplementedError("iOS implementation pending"))

    override suspend fun getLibraryItems(serverUrl: String, token: String, userId: String, libraryId: String, limit: Int): Result<List<MediaItem>> =
        Result.failure(NotImplementedError("iOS implementation pending"))

    override suspend fun getLatestItems(serverUrl: String, token: String, userId: String, limit: Int): Result<List<MediaItem>> =
        Result.failure(NotImplementedError("iOS implementation pending"))

    override suspend fun getResumeItems(serverUrl: String, token: String, userId: String, limit: Int): Result<List<MediaItem>> =
        Result.failure(NotImplementedError("iOS implementation pending"))

    override suspend fun getFavoriteItems(serverUrl: String, token: String, userId: String, limit: Int): Result<List<MediaItem>> =
        Result.failure(NotImplementedError("iOS implementation pending"))

    override suspend fun getLatestMovies(serverUrl: String, token: String, userId: String, limit: Int): Result<List<MediaItem>> =
        Result.failure(NotImplementedError("iOS implementation pending"))

    override suspend fun getLatestSeries(serverUrl: String, token: String, userId: String, limit: Int): Result<List<MediaItem>> =
        Result.failure(NotImplementedError("iOS implementation pending"))

    override suspend fun getLatestMusic(serverUrl: String, token: String, userId: String, limit: Int): Result<List<MediaItem>> =
        Result.failure(NotImplementedError("iOS implementation pending"))

    override suspend fun getNextUpEpisodes(serverUrl: String, token: String, userId: String, limit: Int): Result<List<MediaItem>> =
        Result.failure(NotImplementedError("iOS implementation pending"))

    override suspend fun getLibraryItemsFiltered(
        serverUrl: String,
        token: String,
        userId: String,
        libraryId: String,
        limit: Int,
        startIndex: Int,
        sortBy: String,
        sortOrder: String,
        genres: List<String>?,
        years: List<Int>?,
        isPlayed: Boolean?,
        isFavorite: Boolean?
    ): Result<LibraryItemsResponse> =
        Result.failure(NotImplementedError("iOS implementation pending"))

    override suspend fun getLibraryFilters(serverUrl: String, token: String, userId: String, libraryId: String): Result<LibraryFilters> =
        Result.failure(NotImplementedError("iOS implementation pending"))

    override suspend fun getItemDetails(serverUrl: String, token: String, userId: String, itemId: String): Result<ItemDetails> =
        Result.failure(NotImplementedError("iOS implementation pending"))

    override suspend fun getItemCast(serverUrl: String, token: String, userId: String, itemId: String): Result<List<PersonInfo>> =
        Result.failure(NotImplementedError("iOS implementation pending"))

    override suspend fun getSimilarItems(serverUrl: String, token: String, userId: String, itemId: String, limit: Int): Result<List<MediaItem>> =
        Result.failure(NotImplementedError("iOS implementation pending"))

    override suspend fun getSeriesSeasons(serverUrl: String, token: String, userId: String, seriesId: String): Result<List<SeasonInfo>> =
        Result.failure(NotImplementedError("iOS implementation pending"))

    override suspend fun getSeasonEpisodes(serverUrl: String, token: String, userId: String, seriesId: String, seasonNumber: Int): Result<List<EpisodeInfo>> =
        Result.failure(NotImplementedError("iOS implementation pending"))

    override suspend fun toggleFavorite(serverUrl: String, token: String, userId: String, itemId: String, isFavorite: Boolean): Result<Unit> =
        Result.failure(NotImplementedError("iOS implementation pending"))

    override suspend fun toggleWatched(serverUrl: String, token: String, userId: String, itemId: String, isWatched: Boolean): Result<Unit> =
        Result.failure(NotImplementedError("iOS implementation pending"))

    override suspend fun getStreamInfo(serverUrl: String, token: String, userId: String, itemId: String): Result<StreamInfo> =
        Result.failure(NotImplementedError("iOS implementation pending"))

    override suspend fun reportPlaybackStart(serverUrl: String, token: String, itemId: String, mediaSourceId: String, playSessionId: String): Result<Unit> =
        Result.success(Unit)

    override suspend fun reportPlaybackProgress(serverUrl: String, token: String, progress: PlaybackProgressInfo): Result<Unit> =
        Result.success(Unit)

    override suspend fun reportPlaybackStopped(serverUrl: String, token: String, itemId: String, mediaSourceId: String, positionTicks: Long, playSessionId: String): Result<Unit> =
        Result.success(Unit)

    override suspend fun getMediaSources(serverUrl: String, token: String, userId: String, itemId: String): Result<List<MediaSourceInfo>> =
        Result.failure(NotImplementedError("iOS implementation pending"))

    override fun getTranscodingDownloadUrl(serverUrl: String, token: String, itemId: String, bitrate: Int): String {
        return "$serverUrl/Videos/$itemId/stream.mp4" +
            "?static=false" +
            "&mediaSourceId=$itemId" +
            "&videoBitRate=$bitrate" +
            "&audioBitRate=192000" +
            "&videoCodec=h264" +
            "&audioCodec=aac" +
            "&api_key=$token"
    }

    override fun getDirectDownloadUrl(serverUrl: String, token: String, itemId: String): String {
        return "$serverUrl/Items/$itemId/Download?api_key=$token"
    }

    override suspend fun canUserTranscode(serverUrl: String, token: String): Boolean {
        return true // iOS: assume transcoding allowed, implement properly later
    }
}
