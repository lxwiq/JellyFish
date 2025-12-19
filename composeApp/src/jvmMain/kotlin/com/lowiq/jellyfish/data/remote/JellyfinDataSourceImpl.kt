package com.lowiq.jellyfish.data.remote

import com.lowiq.jellyfish.domain.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jellyfin.sdk.Jellyfin
import org.jellyfin.sdk.api.client.ApiClient
import org.jellyfin.sdk.api.client.extensions.authenticateUserByName
import org.jellyfin.sdk.api.client.extensions.authenticateWithQuickConnect
import org.jellyfin.sdk.api.client.extensions.itemsApi
import org.jellyfin.sdk.api.client.extensions.quickConnectApi
import org.jellyfin.sdk.api.client.extensions.sessionApi
import org.jellyfin.sdk.api.client.extensions.systemApi
import org.jellyfin.sdk.api.client.extensions.tvShowsApi
import org.jellyfin.sdk.api.client.extensions.userApi
import org.jellyfin.sdk.api.client.extensions.userLibraryApi
import org.jellyfin.sdk.api.client.extensions.userViewsApi
import org.jellyfin.sdk.createJellyfin
import org.jellyfin.sdk.model.ClientInfo
import org.jellyfin.sdk.model.UUID
import org.jellyfin.sdk.model.api.BaseItemDto
import org.jellyfin.sdk.model.api.BaseItemKind
import org.jellyfin.sdk.model.api.ImageType

class JellyfinDataSourceImpl : JellyfinDataSource {

    private val jellyfin: Jellyfin by lazy {
        createJellyfin {
            clientInfo = ClientInfo(
                name = "JellyFish",
                version = "1.0.0"
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
                    serverId = user.serverId ?: ""
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
}
