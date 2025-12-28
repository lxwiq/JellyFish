package com.lowiq.jellyfish.data.remote

import com.lowiq.jellyfish.domain.model.AdminUser
import com.lowiq.jellyfish.domain.model.LogEntry
import com.lowiq.jellyfish.domain.model.ScheduledTask
import com.lowiq.jellyfish.domain.model.TaskState
import com.lowiq.jellyfish.domain.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jellyfin.sdk.Jellyfin
import org.jellyfin.sdk.api.client.ApiClient
import org.jellyfin.sdk.api.client.extensions.authenticateUserByName
import org.jellyfin.sdk.api.client.extensions.authenticateWithQuickConnect
import org.jellyfin.sdk.api.client.extensions.itemsApi
import org.jellyfin.sdk.api.client.extensions.libraryApi
import org.jellyfin.sdk.api.client.extensions.mediaInfoApi
import org.jellyfin.sdk.api.client.extensions.playStateApi
import org.jellyfin.sdk.api.client.extensions.quickConnectApi
import org.jellyfin.sdk.api.client.extensions.scheduledTasksApi
import org.jellyfin.sdk.api.client.extensions.sessionApi
import org.jellyfin.sdk.api.client.extensions.systemApi
import org.jellyfin.sdk.api.client.extensions.tvShowsApi
import org.jellyfin.sdk.api.client.extensions.userApi
import org.jellyfin.sdk.api.client.extensions.userLibraryApi
import org.jellyfin.sdk.api.client.extensions.userViewsApi
import org.jellyfin.sdk.createJellyfin
import org.jellyfin.sdk.model.ClientInfo
import org.jellyfin.sdk.model.DeviceInfo
import org.jellyfin.sdk.model.UUID
import org.jellyfin.sdk.model.api.BaseItemDto
import org.jellyfin.sdk.model.api.BaseItemKind
import org.jellyfin.sdk.model.api.ImageType
import java.net.InetAddress

class JellyfinDataSourceImpl : JellyfinDataSource {

    private val jellyfin: Jellyfin by lazy {
        val hostname = try {
            InetAddress.getLocalHost().hostName
        } catch (e: Exception) {
            "Desktop"
        }

        // Generate a stable device ID based on hostname and user
        val deviceId = java.util.UUID.nameUUIDFromBytes(
            "${System.getProperty("user.name")}@$hostname".toByteArray()
        ).toString()

        createJellyfin {
            clientInfo = ClientInfo(
                name = "JellyFish",
                version = "1.0.0"
            )
            deviceInfo = DeviceInfo(
                id = deviceId,
                name = "$hostname (Desktop)"
            )
        }
    }

    private fun createApi(serverUrl: String, token: String? = null): ApiClient {
        return jellyfin.createApi(
            baseUrl = serverUrl,
            accessToken = token
        )
    }

    override suspend fun getServerInfo(serverUrl: String): Result<ServerInfo> = withContext(Dispatchers.IO) {
        runCatching {
            val api = createApi(serverUrl)
            val info by api.systemApi.getPublicSystemInfo()
            ServerInfo(
                id = info.id ?: "",
                name = info.serverName ?: "Jellyfin Server",
                version = info.version ?: ""
            )
        }
    }

    override suspend fun login(
        serverUrl: String,
        username: String,
        password: String
    ): Result<AuthResult> = withContext(Dispatchers.IO) {
        runCatching {
            val api = createApi(serverUrl)
            val authResult by api.userApi.authenticateUserByName(
                username = username,
                password = password
            )
            AuthResult(
                userId = authResult.user?.id?.toString() ?: "",
                username = authResult.user?.name ?: username,
                token = authResult.accessToken ?: "",
                serverId = authResult.serverId ?: ""
            )
        }
    }

    override suspend fun initiateQuickConnect(serverUrl: String): Result<QuickConnectResult> =
        withContext(Dispatchers.IO) {
            runCatching {
                val api = createApi(serverUrl)
                val state by api.quickConnectApi.initiateQuickConnect()
                QuickConnectResult(
                    secret = state.secret ?: "",
                    code = state.code ?: ""
                )
            }
        }

    override suspend fun checkQuickConnect(
        serverUrl: String,
        secret: String
    ): Result<QuickConnectStatus> = withContext(Dispatchers.IO) {
        runCatching {
            val api = createApi(serverUrl)
            val state by api.quickConnectApi.getQuickConnectState(secret = secret)

            if (state.authenticated == true) {
                val authResult by api.userApi.authenticateWithQuickConnect(secret = secret)
                QuickConnectStatus.Authenticated(
                    token = authResult.accessToken ?: "",
                    userId = authResult.user?.id?.toString() ?: ""
                )
            } else {
                QuickConnectStatus.Waiting
            }
        }
    }

    override suspend fun logout(serverUrl: String, token: String) {
        withContext(Dispatchers.IO) {
            runCatching {
                val api = createApi(serverUrl, token)
                api.sessionApi.reportSessionEnded()
            }
        }
    }

    override suspend fun getCurrentUser(serverUrl: String, token: String): Result<User> =
        withContext(Dispatchers.IO) {
            runCatching {
                val api = createApi(serverUrl, token)
                val user by api.userApi.getCurrentUser()
                User(
                    id = user.id.toString(),
                    name = user.name ?: "",
                    serverId = user.serverId ?: "",
                    isAdmin = user.policy?.isAdministrator == true
                )
            }
        }

    override suspend fun getUserLibraries(
        serverUrl: String,
        token: String,
        userId: String
    ): Result<List<Library>> = withContext(Dispatchers.IO) {
        runCatching {
            val api = createApi(serverUrl, token)
            val response by api.userViewsApi.getUserViews(
                userId = java.util.UUID.fromString(userId)
            )
            response.items.orEmpty().map { item ->
                val imageUrl = item.imageTags?.get(ImageType.PRIMARY)?.let { tag ->
                    "$serverUrl/Items/${item.id}/Images/Primary?tag=$tag"
                }
                Library(
                    id = item.id.toString(),
                    name = item.name ?: "",
                    type = item.collectionType?.toString() ?: "",
                    imageUrl = imageUrl
                )
            }
        }
    }

    override suspend fun getLibraryItems(
        serverUrl: String,
        token: String,
        userId: String,
        libraryId: String,
        limit: Int
    ): Result<List<MediaItem>> = withContext(Dispatchers.IO) {
        runCatching {
            val api = createApi(serverUrl, token)
            val response by api.itemsApi.getItems(
                userId = java.util.UUID.fromString(userId),
                parentId = java.util.UUID.fromString(libraryId),
                limit = limit,
                sortBy = listOf(org.jellyfin.sdk.model.api.ItemSortBy.DATE_CREATED),
                sortOrder = listOf(org.jellyfin.sdk.model.api.SortOrder.DESCENDING),
                recursive = true,
                enableImages = true
            )
            response.items.orEmpty().map { it.toMediaItem(serverUrl) }
        }
    }

    override suspend fun getLatestItems(
        serverUrl: String,
        token: String,
        userId: String,
        limit: Int
    ): Result<List<MediaItem>> = withContext(Dispatchers.IO) {
        runCatching {
            val api = createApi(serverUrl, token)
            val items by api.userLibraryApi.getLatestMedia(
                userId = java.util.UUID.fromString(userId),
                limit = limit
            )
            items.map { it.toMediaItem(serverUrl) }
        }
    }

    override suspend fun getResumeItems(
        serverUrl: String,
        token: String,
        userId: String,
        limit: Int
    ): Result<List<MediaItem>> = withContext(Dispatchers.IO) {
        runCatching {
            val api = createApi(serverUrl, token)
            val response by api.itemsApi.getResumeItems(
                userId = java.util.UUID.fromString(userId),
                limit = limit,
                enableUserData = true,
                enableImages = true
            )
            response.items.orEmpty().map { it.toMediaItem(serverUrl, forceBackdrop = true) }
        }
    }

    override suspend fun getFavoriteItems(
        serverUrl: String,
        token: String,
        userId: String,
        limit: Int
    ): Result<List<MediaItem>> = withContext(Dispatchers.IO) {
        runCatching {
            val api = createApi(serverUrl, token)
            val response by api.itemsApi.getItems(
                userId = java.util.UUID.fromString(userId),
                isFavorite = true,
                limit = limit,
                recursive = true,
                enableUserData = true,
                enableImages = true
            )
            response.items.orEmpty().map { it.toMediaItem(serverUrl) }
        }
    }

    override suspend fun getLatestMovies(
        serverUrl: String,
        token: String,
        userId: String,
        limit: Int
    ): Result<List<MediaItem>> = withContext(Dispatchers.IO) {
        runCatching {
            val api = createApi(serverUrl, token)
            val response by api.itemsApi.getItems(
                userId = java.util.UUID.fromString(userId),
                limit = limit,
                includeItemTypes = listOf(BaseItemKind.MOVIE),
                sortBy = listOf(org.jellyfin.sdk.model.api.ItemSortBy.DATE_CREATED),
                sortOrder = listOf(org.jellyfin.sdk.model.api.SortOrder.DESCENDING),
                recursive = true,
                enableImages = true
            )
            response.items.orEmpty().map { it.toMediaItem(serverUrl) }
        }
    }

    override suspend fun getLatestSeries(
        serverUrl: String,
        token: String,
        userId: String,
        limit: Int
    ): Result<List<MediaItem>> = withContext(Dispatchers.IO) {
        runCatching {
            val api = createApi(serverUrl, token)
            val response by api.itemsApi.getItems(
                userId = java.util.UUID.fromString(userId),
                limit = limit,
                includeItemTypes = listOf(BaseItemKind.SERIES),
                sortBy = listOf(org.jellyfin.sdk.model.api.ItemSortBy.DATE_CREATED),
                sortOrder = listOf(org.jellyfin.sdk.model.api.SortOrder.DESCENDING),
                recursive = true,
                enableImages = true
            )
            response.items.orEmpty().map { it.toMediaItem(serverUrl) }
        }
    }

    override suspend fun getLatestMusic(
        serverUrl: String,
        token: String,
        userId: String,
        limit: Int
    ): Result<List<MediaItem>> = withContext(Dispatchers.IO) {
        runCatching {
            val api = createApi(serverUrl, token)
            val response by api.itemsApi.getItems(
                userId = java.util.UUID.fromString(userId),
                limit = limit,
                includeItemTypes = listOf(BaseItemKind.MUSIC_ALBUM),
                sortBy = listOf(org.jellyfin.sdk.model.api.ItemSortBy.DATE_CREATED),
                sortOrder = listOf(org.jellyfin.sdk.model.api.SortOrder.DESCENDING),
                recursive = true,
                enableImages = true
            )
            response.items.orEmpty().map { it.toMediaItem(serverUrl) }
        }
    }

    override suspend fun getNextUpEpisodes(
        serverUrl: String,
        token: String,
        userId: String,
        limit: Int
    ): Result<List<MediaItem>> = withContext(Dispatchers.IO) {
        runCatching {
            val api = createApi(serverUrl, token)
            val response by api.tvShowsApi.getNextUp(
                userId = java.util.UUID.fromString(userId),
                limit = limit,
                enableUserData = true,
                enableImages = true
            )
            response.items.orEmpty().map { it.toMediaItem(serverUrl) }
        }
    }

    override suspend fun searchItems(
        serverUrl: String,
        token: String,
        userId: String,
        query: String,
        limit: Int
    ): Result<List<MediaItem>> = withContext(Dispatchers.IO) {
        runCatching {
            val api = createApi(serverUrl, token)
            val response by api.itemsApi.getItems(
                userId = java.util.UUID.fromString(userId),
                searchTerm = query,
                limit = limit,
                recursive = true,
                enableImages = true,
                includeItemTypes = listOf(
                    BaseItemKind.MOVIE,
                    BaseItemKind.SERIES,
                    BaseItemKind.EPISODE,
                    BaseItemKind.MUSIC_ALBUM,
                    BaseItemKind.AUDIO
                )
            )
            response.items.orEmpty().map { it.toMediaItem(serverUrl) }
        }
    }

    override suspend fun getLibraryItemsFiltered(
        serverUrl: String,
        token: String,
        userId: String,
        libraryId: String,
        libraryType: String?,
        limit: Int,
        startIndex: Int,
        sortBy: String,
        sortOrder: String,
        genres: List<String>?,
        years: List<Int>?,
        isPlayed: Boolean?,
        isFavorite: Boolean?
    ): Result<LibraryItemsResponse> = withContext(Dispatchers.IO) {
        runCatching {
            val api = createApi(serverUrl, token)

            // Map sortBy string to ItemSortBy enum
            val sortByEnum = when (sortBy) {
                "SortName" -> org.jellyfin.sdk.model.api.ItemSortBy.SORT_NAME
                "DateCreated" -> org.jellyfin.sdk.model.api.ItemSortBy.DATE_CREATED
                "ProductionYear" -> org.jellyfin.sdk.model.api.ItemSortBy.PRODUCTION_YEAR
                "CommunityRating" -> org.jellyfin.sdk.model.api.ItemSortBy.COMMUNITY_RATING
                else -> org.jellyfin.sdk.model.api.ItemSortBy.DATE_CREATED
            }

            // Map sortOrder string to SortOrder enum
            val sortOrderEnum = when (sortOrder) {
                "Ascending" -> org.jellyfin.sdk.model.api.SortOrder.ASCENDING
                "Descending" -> org.jellyfin.sdk.model.api.SortOrder.DESCENDING
                else -> org.jellyfin.sdk.model.api.SortOrder.DESCENDING
            }

            // Filter by item type based on library type
            val includeItemTypes = when (libraryType?.lowercase()) {
                "movies" -> listOf(BaseItemKind.MOVIE)
                "tvshows" -> listOf(BaseItemKind.SERIES)
                "music" -> listOf(BaseItemKind.MUSIC_ALBUM)
                else -> null
            }

            val response by api.itemsApi.getItems(
                userId = java.util.UUID.fromString(userId),
                parentId = java.util.UUID.fromString(libraryId),
                limit = limit,
                startIndex = startIndex,
                sortBy = listOf(sortByEnum),
                sortOrder = listOf(sortOrderEnum),
                genres = genres,
                years = years?.map { it },
                isPlayed = isPlayed,
                isFavorite = isFavorite,
                includeItemTypes = includeItemTypes,
                recursive = true,
                enableImages = true,
                enableUserData = true
            )

            LibraryItemsResponse(
                items = response.items.orEmpty().map { it.toMediaItem(serverUrl) },
                totalCount = response.totalRecordCount ?: 0
            )
        }
    }

    override suspend fun getLibraryFilters(
        serverUrl: String,
        token: String,
        userId: String,
        libraryId: String
    ): Result<LibraryFilters> = withContext(Dispatchers.IO) {
        runCatching {
            val api = createApi(serverUrl, token)

            // Get all items from the library to extract unique genres and years
            val response by api.itemsApi.getItems(
                userId = java.util.UUID.fromString(userId),
                parentId = java.util.UUID.fromString(libraryId),
                recursive = true
            )

            // Extract unique genres
            val genres = response.items.orEmpty()
                .flatMap { it.genres.orEmpty() }
                .distinct()
                .sorted()

            // Extract unique years
            val years = response.items.orEmpty()
                .mapNotNull { it.productionYear }
                .distinct()
                .sorted()

            LibraryFilters(
                genres = genres,
                years = years
            )
        }
    }

    override suspend fun getItemDetails(
        serverUrl: String,
        token: String,
        userId: String,
        itemId: String
    ): Result<ItemDetails> = withContext(Dispatchers.IO) {
        runCatching {
            val api = createApi(serverUrl, token)
            val item by api.userLibraryApi.getItem(
                userId = java.util.UUID.fromString(userId),
                itemId = java.util.UUID.fromString(itemId)
            )

            val backdropUrl = item.backdropImageTags?.firstOrNull()?.let { tag ->
                "$serverUrl/Items/${item.id}/Images/Backdrop?tag=$tag"
            }

            val posterUrl = item.imageTags?.get(ImageType.PRIMARY)?.let { tag ->
                "$serverUrl/Items/${item.id}/Images/Primary?tag=$tag"
            }

            val year = item.productionYear?.toString()

            val genres = item.genres.orEmpty()
            val studios = item.studios.orEmpty().mapNotNull { it.name }

            val trailerUrl = item.remoteTrailers?.firstOrNull()?.url

            ItemDetails(
                id = item.id.toString(),
                name = item.name ?: "",
                type = item.type?.toString() ?: "",
                overview = item.overview,
                backdropUrl = backdropUrl,
                posterUrl = posterUrl,
                year = year,
                runtime = item.runTimeTicks,
                communityRating = item.communityRating,
                genres = genres,
                studios = studios,
                seriesId = item.seriesId?.toString(),
                seriesName = item.seriesName,
                seasonNumber = item.parentIndexNumber,
                episodeNumber = item.indexNumber,
                trailerUrl = trailerUrl,
                isFavorite = item.userData?.isFavorite ?: false,
                isPlayed = item.userData?.played ?: false,
                playbackPositionTicks = item.userData?.playbackPositionTicks
            )
        }
    }

    override suspend fun getItemCast(
        serverUrl: String,
        token: String,
        userId: String,
        itemId: String
    ): Result<List<PersonInfo>> = withContext(Dispatchers.IO) {
        runCatching {
            val api = createApi(serverUrl, token)
            val item by api.userLibraryApi.getItem(
                userId = java.util.UUID.fromString(userId),
                itemId = java.util.UUID.fromString(itemId)
            )

            item.people.orEmpty().map { person ->
                val imageUrl = person.primaryImageTag?.let { tag ->
                    "$serverUrl/Items/${person.id}/Images/Primary?tag=$tag"
                }

                PersonInfo(
                    id = person.id.toString(),
                    name = person.name ?: "",
                    role = person.role,
                    imageUrl = imageUrl
                )
            }
        }
    }

    override suspend fun getSimilarItems(
        serverUrl: String,
        token: String,
        userId: String,
        itemId: String,
        limit: Int
    ): Result<List<MediaItem>> = withContext(Dispatchers.IO) {
        runCatching {
            val api = createApi(serverUrl, token)
            // Get the item first to retrieve its genres for similarity matching
            val item by api.userLibraryApi.getItem(
                userId = java.util.UUID.fromString(userId),
                itemId = java.util.UUID.fromString(itemId)
            )

            // Use genres from the item to find similar items
            val response by api.itemsApi.getItems(
                userId = java.util.UUID.fromString(userId),
                limit = limit,
                genres = item.genres?.take(2), // Use first 2 genres for matching
                includeItemTypes = item.type?.let { listOf(it) },
                recursive = true,
                enableImages = true
            )

            // Filter out the current item and map to MediaItem
            response.items.orEmpty()
                .filter { it.id != item.id }
                .map { it.toMediaItem(serverUrl) }
        }
    }

    override suspend fun getSeriesSeasons(
        serverUrl: String,
        token: String,
        userId: String,
        seriesId: String
    ): Result<List<SeasonInfo>> = withContext(Dispatchers.IO) {
        runCatching {
            val api = createApi(serverUrl, token)
            val response by api.tvShowsApi.getSeasons(
                seriesId = java.util.UUID.fromString(seriesId),
                userId = java.util.UUID.fromString(userId)
            )

            response.items.orEmpty().map { season ->
                val imageUrl = season.imageTags?.get(ImageType.PRIMARY)?.let { tag ->
                    "$serverUrl/Items/${season.id}/Images/Primary?tag=$tag"
                }

                SeasonInfo(
                    id = season.id.toString(),
                    name = season.name ?: "Season",
                    number = season.indexNumber ?: 0,
                    episodeCount = season.childCount ?: 0,
                    imageUrl = imageUrl
                )
            }.sortedBy { it.number }
        }
    }

    override suspend fun getSeasonEpisodes(
        serverUrl: String,
        token: String,
        userId: String,
        seriesId: String,
        seasonNumber: Int
    ): Result<List<EpisodeInfo>> = withContext(Dispatchers.IO) {
        runCatching {
            val api = createApi(serverUrl, token)
            val response by api.tvShowsApi.getEpisodes(
                seriesId = java.util.UUID.fromString(seriesId),
                userId = java.util.UUID.fromString(userId),
                season = seasonNumber
            )

            response.items.orEmpty().map { episode ->
                val imageUrl = episode.imageTags?.get(ImageType.PRIMARY)?.let { tag ->
                    "$serverUrl/Items/${episode.id}/Images/Primary?tag=$tag"
                }

                EpisodeInfo(
                    id = episode.id.toString(),
                    name = episode.name ?: "Episode",
                    overview = episode.overview,
                    seasonNumber = episode.parentIndexNumber ?: seasonNumber,
                    episodeNumber = episode.indexNumber ?: 0,
                    communityRating = episode.communityRating,
                    imageUrl = imageUrl,
                    isPlayed = episode.userData?.played ?: false,
                    playbackPositionTicks = episode.userData?.playbackPositionTicks,
                    runTimeTicks = episode.runTimeTicks
                )
            }.sortedBy { it.episodeNumber }
        }
    }

    override suspend fun toggleFavorite(
        serverUrl: String,
        token: String,
        userId: String,
        itemId: String,
        isFavorite: Boolean
    ): Result<Unit> = withContext(Dispatchers.IO) {
        runCatching {
            val api = createApi(serverUrl, token)
            if (isFavorite) {
                api.userLibraryApi.markFavoriteItem(
                    userId = java.util.UUID.fromString(userId),
                    itemId = java.util.UUID.fromString(itemId)
                )
            } else {
                api.userLibraryApi.unmarkFavoriteItem(
                    userId = java.util.UUID.fromString(userId),
                    itemId = java.util.UUID.fromString(itemId)
                )
            }
            Unit
        }
    }

    override suspend fun toggleWatched(
        serverUrl: String,
        token: String,
        userId: String,
        itemId: String,
        isWatched: Boolean
    ): Result<Unit> = withContext(Dispatchers.IO) {
        runCatching {
            val api = createApi(serverUrl, token)
            if (isWatched) {
                api.playStateApi.markPlayedItem(
                    itemId = java.util.UUID.fromString(itemId),
                    userId = java.util.UUID.fromString(userId)
                )
            } else {
                api.playStateApi.markUnplayedItem(
                    itemId = java.util.UUID.fromString(itemId),
                    userId = java.util.UUID.fromString(userId)
                )
            }
            Unit
        }
    }

    override suspend fun getStreamInfo(
        serverUrl: String,
        token: String,
        userId: String,
        itemId: String
    ): Result<StreamInfo> = withContext(Dispatchers.IO) {
        runCatching {
            val api = createApi(serverUrl, token)
            val playSessionId = java.util.UUID.randomUUID().toString()

            val playbackInfo by api.mediaInfoApi.getPlaybackInfo(
                itemId = java.util.UUID.fromString(itemId),
                userId = java.util.UUID.fromString(userId)
            )

            val mediaSource = playbackInfo.mediaSources?.firstOrNull()
            val mediaSourceId = mediaSource?.id ?: itemId
            val supportsDirectPlay = mediaSource?.supportsDirectPlay ?: false

            // Extract subtitle streams
            val subtitleStreams = mediaSource?.mediaStreams
                ?.filter { it.type == org.jellyfin.sdk.model.api.MediaStreamType.SUBTITLE }
                ?.mapIndexed { index, stream ->
                    val codec = stream.codec ?: "srt"
                    val extension = when (codec.lowercase()) {
                        "ass", "ssa" -> "ass"
                        "vtt", "webvtt" -> "vtt"
                        else -> "srt"
                    }
                    SubtitleStreamInfo(
                        index = stream.index ?: index,
                        language = stream.language,
                        title = stream.displayTitle ?: stream.title ?: stream.language ?: "Subtitle ${index + 1}",
                        codec = codec,
                        isExternal = stream.isExternal ?: false,
                        isDefault = stream.isDefault ?: false,
                        isForced = stream.isForced ?: false,
                        deliveryUrl = "$serverUrl/Videos/$itemId/$mediaSourceId/Subtitles/${stream.index ?: index}/Stream.$extension?api_key=$token"
                    )
                } ?: emptyList()

            // Direct play URL
            val directPlayUrl = "$serverUrl/Videos/$itemId/stream" +
                "?static=true" +
                "&mediaSourceId=$mediaSourceId" +
                "&playSessionId=$playSessionId" +
                "&api_key=$token"

            // HLS transcoding URL
            val transcodingUrl = "$serverUrl/Videos/$itemId/master.m3u8" +
                "?mediaSourceId=$mediaSourceId" +
                "&playSessionId=$playSessionId" +
                "&videoCodec=h264" +
                "&audioCodec=aac,mp3" +
                "&audioBitRate=192000" +
                "&maxAudioChannels=6" +
                "&api_key=$token"

            StreamInfo(
                directPlayUrl = directPlayUrl,
                transcodingUrl = transcodingUrl,
                mediaSourceId = mediaSourceId,
                playSessionId = playSessionId,
                supportsDirectPlay = supportsDirectPlay,
                subtitleStreams = subtitleStreams
            )
        }
    }

    override suspend fun reportPlaybackStart(
        serverUrl: String,
        token: String,
        itemId: String,
        mediaSourceId: String,
        playSessionId: String
    ): Result<Unit> = Result.success(Unit)

    override suspend fun reportPlaybackProgress(
        serverUrl: String,
        token: String,
        progress: PlaybackProgressInfo
    ): Result<Unit> = Result.success(Unit)

    override suspend fun reportPlaybackStopped(
        serverUrl: String,
        token: String,
        itemId: String,
        mediaSourceId: String,
        positionTicks: Long,
        playSessionId: String
    ): Result<Unit> = Result.success(Unit)

    override suspend fun getMediaSources(
        serverUrl: String,
        token: String,
        userId: String,
        itemId: String
    ): Result<List<MediaSourceInfo>> = withContext(Dispatchers.IO) {
        runCatching {
            val api = createApi(serverUrl, token)
            val playbackInfo by api.mediaInfoApi.getPlaybackInfo(
                itemId = java.util.UUID.fromString(itemId),
                userId = java.util.UUID.fromString(userId)
            )
            playbackInfo.mediaSources.orEmpty().map { source ->
                MediaSourceInfo(
                    id = source.id ?: itemId,
                    name = source.name ?: "Original",
                    bitrate = source.bitrate,
                    size = source.size,
                    container = source.container
                )
            }
        }
    }

    override fun getTranscodingDownloadUrl(
        serverUrl: String,
        token: String,
        itemId: String,
        bitrate: Int
    ): String {
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

    override suspend fun canUserTranscode(serverUrl: String, token: String): Boolean =
        withContext(Dispatchers.IO) {
            try {
                val api = createApi(serverUrl, token)
                val user by api.userApi.getCurrentUser()
                user.policy?.enableVideoPlaybackTranscoding == true
            } catch (e: Exception) {
                false
            }
        }

    private fun BaseItemDto.toMediaItem(serverUrl: String, forceBackdrop: Boolean = false): MediaItem {
        // Episodes: use Backdrop (horizontal), Movies/Series: use Primary (poster)
        // forceBackdrop=true always uses horizontal images (e.g., Continue Watching)
        val isEpisode = type == BaseItemKind.EPISODE
        val useBackdrop = forceBackdrop || isEpisode
        val imageUrl = if (useBackdrop) {
            // Prefer Backdrop (horizontal), fallback to Primary
            backdropImageTags?.firstOrNull()?.let { tag ->
                "$serverUrl/Items/$id/Images/Backdrop?tag=$tag"
            } ?: imageTags?.get(ImageType.PRIMARY)?.let { tag ->
                "$serverUrl/Items/$id/Images/Primary?tag=$tag"
            }
        } else {
            // For movies/series: prefer Primary (poster), fallback to Backdrop
            imageTags?.get(ImageType.PRIMARY)?.let { tag ->
                "$serverUrl/Items/$id/Images/Primary?tag=$tag"
            } ?: backdropImageTags?.firstOrNull()?.let { tag ->
                "$serverUrl/Items/$id/Images/Backdrop?tag=$tag"
            }
        }
        return MediaItem(
            id = id.toString(),
            name = name ?: "",
            type = type?.toString() ?: "",
            seriesName = seriesName,
            seasonNumber = parentIndexNumber,
            episodeNumber = indexNumber,
            imageUrl = imageUrl,
            playbackPositionTicks = userData?.playbackPositionTicks,
            runTimeTicks = runTimeTicks,
            dateCreated = premiereDate?.toString()
        )
    }

    // Admin: Users
    override suspend fun getUsers(serverUrl: String, token: String): Result<List<AdminUser>> =
        withContext(Dispatchers.IO) {
            runCatching {
                val api = createApi(serverUrl, token)
                val users by api.userApi.getUsers()
                users.map { user ->
                    AdminUser(
                        id = user.id.toString(),
                        name = user.name ?: "",
                        isAdmin = user.policy?.isAdministrator == true,
                        isDisabled = user.policy?.isDisabled == true
                    )
                }
            }
        }

    override suspend fun createUser(
        serverUrl: String,
        token: String,
        username: String,
        password: String
    ): Result<AdminUser> = withContext(Dispatchers.IO) {
        runCatching {
            val api = createApi(serverUrl, token)
            val user by api.userApi.createUserByName(
                org.jellyfin.sdk.model.api.CreateUserByName(
                    name = username,
                    password = password
                )
            )
            AdminUser(
                id = user.id.toString(),
                name = user.name ?: username,
                isAdmin = user.policy?.isAdministrator == true,
                isDisabled = user.policy?.isDisabled == true
            )
        }
    }

    override suspend fun deleteUser(serverUrl: String, token: String, userId: String): Result<Unit> =
        withContext(Dispatchers.IO) {
            runCatching {
                val api = createApi(serverUrl, token)
                api.userApi.deleteUser(java.util.UUID.fromString(userId))
            }.map { }
        }

    // Admin: Libraries
    override suspend fun refreshLibrary(serverUrl: String, token: String): Result<Unit> =
        withContext(Dispatchers.IO) {
            runCatching {
                val api = createApi(serverUrl, token)
                api.libraryApi.refreshLibrary()
            }.map { }
        }

    // Admin: Logs - Note: getLogEntries not available in current SDK, returning empty list
    override suspend fun getServerLogs(
        serverUrl: String,
        token: String,
        limit: Int
    ): Result<List<LogEntry>> = withContext(Dispatchers.IO) {
        Result.success(emptyList())
    }

    // Admin: Tasks
    override suspend fun getScheduledTasks(serverUrl: String, token: String): Result<List<ScheduledTask>> =
        withContext(Dispatchers.IO) {
            runCatching {
                val api = createApi(serverUrl, token)
                val tasks by api.scheduledTasksApi.getTasks()
                tasks.map { task ->
                    ScheduledTask(
                        id = task.id ?: "",
                        name = task.name ?: "",
                        description = task.description ?: "",
                        state = when (task.state) {
                            org.jellyfin.sdk.model.api.TaskState.RUNNING -> TaskState.RUNNING
                            org.jellyfin.sdk.model.api.TaskState.CANCELLING -> TaskState.CANCELLING
                            else -> TaskState.IDLE
                        },
                        lastExecutionResult = task.lastExecutionResult?.status?.name
                    )
                }
            }
        }

    override suspend fun runTask(serverUrl: String, token: String, taskId: String): Result<Unit> =
        withContext(Dispatchers.IO) {
            runCatching {
                val api = createApi(serverUrl, token)
                api.scheduledTasksApi.startTask(taskId = taskId)
            }.map { }
        }
}
