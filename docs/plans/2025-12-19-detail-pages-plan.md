# Detail Pages Implementation Plan

> **For Claude:** REQUIRED SUB-SKILL: Use superpowers:executing-plans to implement this plan task-by-task.

**Goal:** Implement three detail screens (Movie, Series, Episode) with shared components, API integration, and navigation from Home/Library screens.

**Architecture:** Voyager screens with ScreenModel for state management. Repository pattern with JellyfinDataSource for API calls. Shared UI components for action buttons, cast carousel, genre badges, episode cards, and season dropdown.

**Tech Stack:** Kotlin Multiplatform, Compose Multiplatform, Voyager (navigation), Koin (DI), Coil (images), Jellyfin API

---

## Task 1: Add MediaType to MediaItem Model

**Files:**
- Modify: `composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/domain/model/MediaItem.kt`
- Modify: `composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/data/repository/MediaRepositoryImpl.kt`
- Modify: `composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/presentation/components/MediaCarousel.kt`

**Step 1: Add MediaType enum and update MediaItem**

```kotlin
// domain/model/MediaItem.kt
package com.lowiq.jellyfish.domain.model

enum class MediaType {
    MOVIE,
    SERIES,
    EPISODE,
    MUSIC,
    OTHER
}

data class MediaItem(
    val id: String,
    val title: String,
    val subtitle: String?,
    val imageUrl: String?,
    val progress: Float?,
    val isPoster: Boolean = false,
    val type: MediaType = MediaType.OTHER
)
```

**Step 2: Update MediaRepositoryImpl.toDomainMediaItem()**

In `MediaRepositoryImpl.kt`, update the `toDomainMediaItem` function to map the `type` field:

```kotlin
private fun DataMediaItem.toDomainMediaItem(forceBackdrop: Boolean = false): MediaItem {
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
```

**Step 3: Update MediaCarouselItem to include type**

```kotlin
// presentation/components/MediaCarousel.kt
data class MediaCarouselItem(
    val id: String,
    val title: String,
    val subtitle: String? = null,
    val imageUrl: String? = null,
    val progress: Float? = null,
    val isPoster: Boolean = false,
    val type: MediaType = MediaType.OTHER
)
```

**Step 4: Update toCarouselItem() in HomeScreen.kt**

```kotlin
private fun com.lowiq.jellyfish.domain.model.MediaItem.toCarouselItem() = MediaCarouselItem(
    id = id,
    title = title,
    subtitle = subtitle,
    imageUrl = imageUrl,
    progress = progress,
    isPoster = isPoster,
    type = type
)
```

**Step 5: Verify build compiles**

Run: `./gradlew :composeApp:compileKotlinJvm`
Expected: BUILD SUCCESSFUL

**Step 6: Commit**

```bash
git add -A
git commit -m "feat: add MediaType enum to MediaItem model"
```

---

## Task 2: Create Detail Data Models

**Files:**
- Create: `composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/domain/model/MovieDetails.kt`
- Create: `composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/domain/model/SeriesDetails.kt`
- Create: `composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/domain/model/EpisodeDetails.kt`
- Create: `composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/domain/model/CastMember.kt`
- Create: `composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/domain/model/Season.kt`
- Create: `composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/domain/model/Episode.kt`

**Step 1: Create CastMember.kt**

```kotlin
package com.lowiq.jellyfish.domain.model

data class CastMember(
    val id: String,
    val name: String,
    val role: String?,
    val imageUrl: String?
)
```

**Step 2: Create Season.kt**

```kotlin
package com.lowiq.jellyfish.domain.model

data class Season(
    val id: String,
    val name: String,
    val number: Int,
    val episodeCount: Int,
    val imageUrl: String?
)
```

**Step 3: Create Episode.kt**

```kotlin
package com.lowiq.jellyfish.domain.model

data class Episode(
    val id: String,
    val title: String,
    val overview: String?,
    val seasonNumber: Int,
    val episodeNumber: Int,
    val runtime: String?,
    val rating: Float?,
    val thumbnailUrl: String?,
    val progress: Float?,
    val isWatched: Boolean
)
```

**Step 4: Create MovieDetails.kt**

```kotlin
package com.lowiq.jellyfish.domain.model

data class MovieDetails(
    val id: String,
    val title: String,
    val overview: String?,
    val backdropUrl: String?,
    val posterUrl: String?,
    val year: String?,
    val runtime: String?,
    val rating: Float?,
    val genres: List<String>,
    val studio: String?,
    val cast: List<CastMember>,
    val similarItems: List<MediaItem>,
    val trailerUrl: String?,
    val isFavorite: Boolean,
    val isWatched: Boolean
)
```

**Step 5: Create SeriesDetails.kt**

```kotlin
package com.lowiq.jellyfish.domain.model

data class SeriesDetails(
    val id: String,
    val title: String,
    val overview: String?,
    val backdropUrl: String?,
    val posterUrl: String?,
    val year: String?,
    val seasonCount: Int,
    val rating: Float?,
    val genres: List<String>,
    val studio: String?,
    val cast: List<CastMember>,
    val seasons: List<Season>,
    val similarItems: List<MediaItem>,
    val isFavorite: Boolean,
    val isWatched: Boolean
)
```

**Step 6: Create EpisodeDetails.kt**

```kotlin
package com.lowiq.jellyfish.domain.model

data class EpisodeDetails(
    val id: String,
    val title: String,
    val overview: String?,
    val thumbnailUrl: String?,
    val seasonNumber: Int,
    val episodeNumber: Int,
    val runtime: String?,
    val rating: Float?,
    val seriesId: String,
    val seriesName: String,
    val guestStars: List<CastMember>,
    val seasonEpisodes: List<Episode>,
    val previousEpisodeId: String?,
    val nextEpisodeId: String?,
    val isFavorite: Boolean,
    val isWatched: Boolean,
    val progress: Float?
)
```

**Step 7: Verify build compiles**

Run: `./gradlew :composeApp:compileKotlinJvm`
Expected: BUILD SUCCESSFUL

**Step 8: Commit**

```bash
git add -A
git commit -m "feat: add domain models for detail pages"
```

---

## Task 3: Add Detail Methods to JellyfinDataSource

**Files:**
- Modify: `composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/data/remote/JellyfinDataSource.kt`
- Modify: `composeApp/src/androidMain/kotlin/com/lowiq/jellyfish/data/remote/JellyfinDataSourceImpl.kt`
- Modify: `composeApp/src/jvmMain/kotlin/com/lowiq/jellyfish/data/remote/JellyfinDataSourceImpl.kt`

**Step 1: Add data classes to JellyfinDataSource.kt**

Add after existing data classes:

```kotlin
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
    val runtime: Long?,
    val communityRating: Float?,
    val imageUrl: String?,
    val isPlayed: Boolean,
    val playbackPositionTicks: Long?,
    val runTimeTicks: Long?
)
```

**Step 2: Add interface methods to JellyfinDataSource**

Add to interface:

```kotlin
suspend fun getItemDetails(serverUrl: String, token: String, userId: String, itemId: String): Result<ItemDetails>
suspend fun getItemCast(serverUrl: String, token: String, itemId: String): Result<List<PersonInfo>>
suspend fun getSimilarItems(serverUrl: String, token: String, userId: String, itemId: String, limit: Int = 10): Result<List<MediaItem>>
suspend fun getSeriesSeasons(serverUrl: String, token: String, userId: String, seriesId: String): Result<List<SeasonInfo>>
suspend fun getSeasonEpisodes(serverUrl: String, token: String, userId: String, seriesId: String, seasonNumber: Int): Result<List<EpisodeInfo>>
suspend fun toggleFavorite(serverUrl: String, token: String, userId: String, itemId: String, isFavorite: Boolean): Result<Unit>
suspend fun toggleWatched(serverUrl: String, token: String, userId: String, itemId: String, isWatched: Boolean): Result<Unit>
```

**Step 3: Implement in JellyfinDataSourceImpl.kt (Android)**

Add implementations in the Android version. Key API endpoints:

- Item details: `GET /Users/{userId}/Items/{itemId}`
- Cast: included in item details response under `People` field
- Similar: `GET /Items/{itemId}/Similar`
- Seasons: `GET /Shows/{seriesId}/Seasons`
- Episodes: `GET /Shows/{seriesId}/Episodes?seasonNumber={n}`
- Toggle favorite: `POST /Users/{userId}/FavoriteItems/{itemId}` (add) or `DELETE` (remove)
- Toggle watched: `POST /Users/{userId}/PlayedItems/{itemId}` (add) or `DELETE` (remove)

```kotlin
override suspend fun getItemDetails(
    serverUrl: String,
    token: String,
    userId: String,
    itemId: String
): Result<ItemDetails> = runCatching {
    val response = client.get("$serverUrl/Users/$userId/Items/$itemId") {
        header("X-Emby-Token", token)
    }
    val json = response.body<JsonObject>()

    val backdropTags = json["BackdropImageTags"]?.jsonArray
    val backdropUrl = if (backdropTags?.isNotEmpty() == true) {
        "$serverUrl/Items/$itemId/Images/Backdrop?tag=${backdropTags[0].jsonPrimitive.content}"
    } else null

    val posterTag = json["ImageTags"]?.jsonObject?.get("Primary")?.jsonPrimitive?.contentOrNull
    val posterUrl = posterTag?.let { "$serverUrl/Items/$itemId/Images/Primary?tag=$it" }

    val year = json["ProductionYear"]?.jsonPrimitive?.intOrNull?.toString()

    val genres = json["Genres"]?.jsonArray?.map { it.jsonPrimitive.content } ?: emptyList()
    val studios = json["Studios"]?.jsonArray?.mapNotNull {
        it.jsonObject["Name"]?.jsonPrimitive?.contentOrNull
    } ?: emptyList()

    // Check for trailer
    val remoteTrailers = json["RemoteTrailers"]?.jsonArray
    val trailerUrl = remoteTrailers?.firstOrNull()?.jsonObject?.get("Url")?.jsonPrimitive?.contentOrNull

    ItemDetails(
        id = json["Id"]!!.jsonPrimitive.content,
        name = json["Name"]!!.jsonPrimitive.content,
        type = json["Type"]!!.jsonPrimitive.content,
        overview = json["Overview"]?.jsonPrimitive?.contentOrNull,
        backdropUrl = backdropUrl,
        posterUrl = posterUrl,
        year = year,
        runtime = json["RunTimeTicks"]?.jsonPrimitive?.longOrNull,
        communityRating = json["CommunityRating"]?.jsonPrimitive?.floatOrNull,
        genres = genres,
        studios = studios,
        seriesId = json["SeriesId"]?.jsonPrimitive?.contentOrNull,
        seriesName = json["SeriesName"]?.jsonPrimitive?.contentOrNull,
        seasonNumber = json["ParentIndexNumber"]?.jsonPrimitive?.intOrNull,
        episodeNumber = json["IndexNumber"]?.jsonPrimitive?.intOrNull,
        trailerUrl = trailerUrl,
        isFavorite = json["UserData"]?.jsonObject?.get("IsFavorite")?.jsonPrimitive?.booleanOrNull ?: false,
        isPlayed = json["UserData"]?.jsonObject?.get("Played")?.jsonPrimitive?.booleanOrNull ?: false,
        playbackPositionTicks = json["UserData"]?.jsonObject?.get("PlaybackPositionTicks")?.jsonPrimitive?.longOrNull
    )
}

override suspend fun getItemCast(
    serverUrl: String,
    token: String,
    itemId: String
): Result<List<PersonInfo>> = runCatching {
    val response = client.get("$serverUrl/Items/$itemId") {
        header("X-Emby-Token", token)
        parameter("Fields", "People")
    }
    val json = response.body<JsonObject>()

    json["People"]?.jsonArray?.mapNotNull { person ->
        val personObj = person.jsonObject
        val personId = personObj["Id"]?.jsonPrimitive?.contentOrNull ?: return@mapNotNull null
        val name = personObj["Name"]?.jsonPrimitive?.contentOrNull ?: return@mapNotNull null
        val primaryImageTag = personObj["PrimaryImageTag"]?.jsonPrimitive?.contentOrNull
        val imageUrl = primaryImageTag?.let { "$serverUrl/Items/$personId/Images/Primary?tag=$it" }

        PersonInfo(
            id = personId,
            name = name,
            role = personObj["Role"]?.jsonPrimitive?.contentOrNull,
            imageUrl = imageUrl
        )
    } ?: emptyList()
}

override suspend fun getSimilarItems(
    serverUrl: String,
    token: String,
    userId: String,
    itemId: String,
    limit: Int
): Result<List<MediaItem>> = runCatching {
    val response = client.get("$serverUrl/Items/$itemId/Similar") {
        header("X-Emby-Token", token)
        parameter("userId", userId)
        parameter("limit", limit)
        parameter("Fields", "PrimaryImageAspectRatio,DateCreated")
    }
    val json = response.body<JsonObject>()

    json["Items"]?.jsonArray?.map { item ->
        val itemObj = item.jsonObject
        val id = itemObj["Id"]!!.jsonPrimitive.content
        val primaryTag = itemObj["ImageTags"]?.jsonObject?.get("Primary")?.jsonPrimitive?.contentOrNull
        val imageUrl = primaryTag?.let { "$serverUrl/Items/$id/Images/Primary?tag=$it" }

        MediaItem(
            id = id,
            name = itemObj["Name"]!!.jsonPrimitive.content,
            type = itemObj["Type"]!!.jsonPrimitive.content,
            seriesName = null,
            seasonNumber = null,
            episodeNumber = null,
            imageUrl = imageUrl,
            playbackPositionTicks = null,
            runTimeTicks = null,
            dateCreated = itemObj["DateCreated"]?.jsonPrimitive?.contentOrNull
        )
    } ?: emptyList()
}

override suspend fun getSeriesSeasons(
    serverUrl: String,
    token: String,
    userId: String,
    seriesId: String
): Result<List<SeasonInfo>> = runCatching {
    val response = client.get("$serverUrl/Shows/$seriesId/Seasons") {
        header("X-Emby-Token", token)
        parameter("userId", userId)
        parameter("Fields", "ItemCounts")
    }
    val json = response.body<JsonObject>()

    json["Items"]?.jsonArray?.mapNotNull { season ->
        val seasonObj = season.jsonObject
        val id = seasonObj["Id"]?.jsonPrimitive?.contentOrNull ?: return@mapNotNull null
        val primaryTag = seasonObj["ImageTags"]?.jsonObject?.get("Primary")?.jsonPrimitive?.contentOrNull
        val imageUrl = primaryTag?.let { "$serverUrl/Items/$id/Images/Primary?tag=$it" }

        SeasonInfo(
            id = id,
            name = seasonObj["Name"]?.jsonPrimitive?.content ?: "Season",
            number = seasonObj["IndexNumber"]?.jsonPrimitive?.intOrNull ?: 0,
            episodeCount = seasonObj["ChildCount"]?.jsonPrimitive?.intOrNull ?: 0,
            imageUrl = imageUrl
        )
    }?.sortedBy { it.number } ?: emptyList()
}

override suspend fun getSeasonEpisodes(
    serverUrl: String,
    token: String,
    userId: String,
    seriesId: String,
    seasonNumber: Int
): Result<List<EpisodeInfo>> = runCatching {
    val response = client.get("$serverUrl/Shows/$seriesId/Episodes") {
        header("X-Emby-Token", token)
        parameter("userId", userId)
        parameter("seasonNumber", seasonNumber)
        parameter("Fields", "Overview,PrimaryImageAspectRatio")
    }
    val json = response.body<JsonObject>()

    json["Items"]?.jsonArray?.mapNotNull { episode ->
        val epObj = episode.jsonObject
        val id = epObj["Id"]?.jsonPrimitive?.contentOrNull ?: return@mapNotNull null
        val primaryTag = epObj["ImageTags"]?.jsonObject?.get("Primary")?.jsonPrimitive?.contentOrNull
        val imageUrl = primaryTag?.let { "$serverUrl/Items/$id/Images/Primary?tag=$it" }

        val userData = epObj["UserData"]?.jsonObject

        EpisodeInfo(
            id = id,
            name = epObj["Name"]?.jsonPrimitive?.content ?: "Episode",
            overview = epObj["Overview"]?.jsonPrimitive?.contentOrNull,
            seasonNumber = epObj["ParentIndexNumber"]?.jsonPrimitive?.intOrNull ?: seasonNumber,
            episodeNumber = epObj["IndexNumber"]?.jsonPrimitive?.intOrNull ?: 0,
            runtime = epObj["RunTimeTicks"]?.jsonPrimitive?.longOrNull,
            communityRating = epObj["CommunityRating"]?.jsonPrimitive?.floatOrNull,
            imageUrl = imageUrl,
            isPlayed = userData?.get("Played")?.jsonPrimitive?.booleanOrNull ?: false,
            playbackPositionTicks = userData?.get("PlaybackPositionTicks")?.jsonPrimitive?.longOrNull,
            runTimeTicks = epObj["RunTimeTicks"]?.jsonPrimitive?.longOrNull
        )
    }?.sortedBy { it.episodeNumber } ?: emptyList()
}

override suspend fun toggleFavorite(
    serverUrl: String,
    token: String,
    userId: String,
    itemId: String,
    isFavorite: Boolean
): Result<Unit> = runCatching {
    if (isFavorite) {
        client.post("$serverUrl/Users/$userId/FavoriteItems/$itemId") {
            header("X-Emby-Token", token)
        }
    } else {
        client.delete("$serverUrl/Users/$userId/FavoriteItems/$itemId") {
            header("X-Emby-Token", token)
        }
    }
}

override suspend fun toggleWatched(
    serverUrl: String,
    token: String,
    userId: String,
    itemId: String,
    isWatched: Boolean
): Result<Unit> = runCatching {
    if (isWatched) {
        client.post("$serverUrl/Users/$userId/PlayedItems/$itemId") {
            header("X-Emby-Token", token)
        }
    } else {
        client.delete("$serverUrl/Users/$userId/PlayedItems/$itemId") {
            header("X-Emby-Token", token)
        }
    }
}
```

**Step 4: Copy implementations to JVM version**

Copy the same implementations to `jvmMain/.../JellyfinDataSourceImpl.kt`

**Step 5: Verify build compiles**

Run: `./gradlew :composeApp:compileKotlinJvm`
Expected: BUILD SUCCESSFUL

**Step 6: Commit**

```bash
git add -A
git commit -m "feat: add detail API methods to JellyfinDataSource"
```

---

## Task 4: Add Detail Methods to MediaRepository

**Files:**
- Modify: `composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/domain/repository/MediaRepository.kt`
- Modify: `composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/data/repository/MediaRepositoryImpl.kt`

**Step 1: Add methods to MediaRepository interface**

```kotlin
// Add to interface
suspend fun getMovieDetails(serverId: String, itemId: String): Result<MovieDetails>
suspend fun getSeriesDetails(serverId: String, itemId: String): Result<SeriesDetails>
suspend fun getEpisodeDetails(serverId: String, itemId: String): Result<EpisodeDetails>
suspend fun getSeasonEpisodes(serverId: String, seriesId: String, seasonNumber: Int): Result<List<Episode>>
suspend fun toggleFavorite(serverId: String, itemId: String, isFavorite: Boolean): Result<Boolean>
suspend fun toggleWatched(serverId: String, itemId: String, isWatched: Boolean): Result<Boolean>
```

**Step 2: Implement in MediaRepositoryImpl**

Add helper function for formatting runtime:

```kotlin
private fun formatRuntime(ticks: Long?): String? {
    if (ticks == null) return null
    val totalMinutes = ticks / 10_000_000 / 60
    val hours = totalMinutes / 60
    val minutes = totalMinutes % 60
    return when {
        hours > 0 -> "${hours}h ${minutes}min"
        else -> "${minutes}min"
    }
}
```

Implement `getMovieDetails`:

```kotlin
override suspend fun getMovieDetails(serverId: String, itemId: String): Result<MovieDetails> {
    val (server, token) = getServerAndToken(serverId)
        ?: return Result.failure(Exception("Server not found"))
    val userId = server.userId
        ?: return Result.failure(Exception("User not found"))

    return runCatching {
        val details = jellyfinDataSource.getItemDetails(server.url, token, userId, itemId).getOrThrow()
        val cast = jellyfinDataSource.getItemCast(server.url, token, itemId).getOrElse { emptyList() }
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
```

Implement `getSeriesDetails`:

```kotlin
override suspend fun getSeriesDetails(serverId: String, itemId: String): Result<SeriesDetails> {
    val (server, token) = getServerAndToken(serverId)
        ?: return Result.failure(Exception("Server not found"))
    val userId = server.userId
        ?: return Result.failure(Exception("User not found"))

    return runCatching {
        val details = jellyfinDataSource.getItemDetails(server.url, token, userId, itemId).getOrThrow()
        val cast = jellyfinDataSource.getItemCast(server.url, token, itemId).getOrElse { emptyList() }
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
```

Implement `getEpisodeDetails`:

```kotlin
override suspend fun getEpisodeDetails(serverId: String, itemId: String): Result<EpisodeDetails> {
    val (server, token) = getServerAndToken(serverId)
        ?: return Result.failure(Exception("Server not found"))
    val userId = server.userId
        ?: return Result.failure(Exception("User not found"))

    return runCatching {
        val details = jellyfinDataSource.getItemDetails(server.url, token, userId, itemId).getOrThrow()
        val guestStars = jellyfinDataSource.getItemCast(server.url, token, itemId).getOrElse { emptyList() }

        val seriesId = details.seriesId ?: throw Exception("Series ID not found")
        val seasonNumber = details.seasonNumber ?: 1
        val episodeNumber = details.episodeNumber ?: 1

        val episodes = jellyfinDataSource.getSeasonEpisodes(server.url, token, userId, seriesId, seasonNumber)
            .getOrElse { emptyList() }

        val sortedEpisodes = episodes.sortedBy { it.episodeNumber }
        val currentIndex = sortedEpisodes.indexOfFirst { it.id == itemId }
        val previousId = if (currentIndex > 0) sortedEpisodes[currentIndex - 1].id else null
        val nextId = if (currentIndex < sortedEpisodes.size - 1) sortedEpisodes[currentIndex + 1].id else null

        val progress = if (details.playbackPositionTicks != null && details.runtime != null && details.runtime > 0) {
            (details.playbackPositionTicks.toFloat() / details.runtime.toFloat()).coerceIn(0f, 1f)
        } else null

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
                val epProgress = if (ep.playbackPositionTicks != null && ep.runTimeTicks != null && ep.runTimeTicks > 0) {
                    (ep.playbackPositionTicks.toFloat() / ep.runTimeTicks.toFloat()).coerceIn(0f, 1f)
                } else null
                Episode(
                    id = ep.id,
                    title = ep.name,
                    overview = ep.overview,
                    seasonNumber = ep.seasonNumber,
                    episodeNumber = ep.episodeNumber,
                    runtime = formatRuntime(ep.runtime),
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
```

Implement `getSeasonEpisodes`:

```kotlin
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
                val progress = if (ep.playbackPositionTicks != null && ep.runTimeTicks != null && ep.runTimeTicks > 0) {
                    (ep.playbackPositionTicks.toFloat() / ep.runTimeTicks.toFloat()).coerceIn(0f, 1f)
                } else null
                Episode(
                    id = ep.id,
                    title = ep.name,
                    overview = ep.overview,
                    seasonNumber = ep.seasonNumber,
                    episodeNumber = ep.episodeNumber,
                    runtime = formatRuntime(ep.runtime),
                    rating = ep.communityRating,
                    thumbnailUrl = ep.imageUrl,
                    progress = progress,
                    isWatched = ep.isPlayed
                )
            }
        }
}
```

Implement toggle methods:

```kotlin
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
```

**Step 3: Verify build compiles**

Run: `./gradlew :composeApp:compileKotlinJvm`
Expected: BUILD SUCCESSFUL

**Step 4: Commit**

```bash
git add -A
git commit -m "feat: add detail methods to MediaRepository"
```

---

## Task 5: Create ActionButtonsRow Component

**Files:**
- Create: `composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/presentation/components/ActionButtonsRow.kt`

**Step 1: Create ActionButtonsRow.kt**

```kotlin
package com.lowiq.jellyfish.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lowiq.jellyfish.presentation.theme.LocalJellyFishColors

@Composable
fun ActionButtonsRow(
    onPlay: () -> Unit,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    isWatched: Boolean,
    onToggleWatched: () -> Unit,
    onDownload: () -> Unit,
    onShare: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalJellyFishColors.current

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Play button - Primary style
        Button(
            onClick = onPlay,
            colors = ButtonDefaults.buttonColors(
                containerColor = colors.primary,
                contentColor = colors.primaryForeground
            ),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text("Play")
        }

        // Favorite button - Outline style
        OutlinedButton(
            onClick = onToggleFavorite,
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = if (isFavorite) colors.destructive else colors.foreground
            ),
            contentPadding = PaddingValues(12.dp),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.size(44.dp)
        ) {
            Icon(
                imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = if (isFavorite) "Remove from favorites" else "Add to favorites",
                modifier = Modifier.size(20.dp)
            )
        }

        // Watched button - Outline style
        OutlinedButton(
            onClick = onToggleWatched,
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = if (isWatched) colors.primary else colors.foreground
            ),
            contentPadding = PaddingValues(12.dp),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.size(44.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = if (isWatched) "Mark as unwatched" else "Mark as watched",
                modifier = Modifier.size(20.dp)
            )
        }

        // Download button - Outline style
        OutlinedButton(
            onClick = onDownload,
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = colors.foreground
            ),
            contentPadding = PaddingValues(12.dp),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.size(44.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Download,
                contentDescription = "Download",
                modifier = Modifier.size(20.dp)
            )
        }

        // Share button - Outline style
        OutlinedButton(
            onClick = onShare,
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = colors.foreground
            ),
            contentPadding = PaddingValues(12.dp),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.size(44.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Share,
                contentDescription = "Share",
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
```

**Step 2: Verify build compiles**

Run: `./gradlew :composeApp:compileKotlinJvm`
Expected: BUILD SUCCESSFUL

**Step 3: Commit**

```bash
git add -A
git commit -m "feat: add ActionButtonsRow component"
```

---

## Task 6: Create CastCarousel Component

**Files:**
- Create: `composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/presentation/components/CastCarousel.kt`

**Step 1: Create CastCarousel.kt**

```kotlin
package com.lowiq.jellyfish.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.lowiq.jellyfish.domain.model.CastMember
import com.lowiq.jellyfish.presentation.theme.LocalJellyFishColors

@Composable
fun CastCarousel(
    title: String,
    cast: List<CastMember>,
    onPersonClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalJellyFishColors.current

    Column(modifier = modifier) {
        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = colors.foreground,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(
                items = cast,
                key = { it.id }
            ) { person ->
                CastItem(
                    person = person,
                    onClick = { onPersonClick(person.id) }
                )
            }
        }
    }
}

@Composable
private fun CastItem(
    person: CastMember,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalJellyFishColors.current

    Column(
        modifier = modifier
            .width(72.dp)
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Circular avatar
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(colors.secondary),
            contentAlignment = Alignment.Center
        ) {
            if (person.imageUrl != null) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalPlatformContext.current)
                        .data(person.imageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = person.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(56.dp)
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = colors.mutedForeground,
                    modifier = Modifier.size(28.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Name
        Text(
            text = person.name,
            fontSize = 12.sp,
            color = colors.foreground,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )

        // Role (if available)
        person.role?.let { role ->
            Text(
                text = role,
                fontSize = 10.sp,
                color = colors.mutedForeground,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )
        }
    }
}
```

**Step 2: Verify build compiles**

Run: `./gradlew :composeApp:compileKotlinJvm`
Expected: BUILD SUCCESSFUL

**Step 3: Commit**

```bash
git add -A
git commit -m "feat: add CastCarousel component"
```

---

## Task 7: Create GenreBadges Component

**Files:**
- Create: `composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/presentation/components/GenreBadges.kt`

**Step 1: Create GenreBadges.kt**

```kotlin
package com.lowiq.jellyfish.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lowiq.jellyfish.presentation.theme.LocalJellyFishColors

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun GenreBadges(
    genres: List<String>,
    modifier: Modifier = Modifier
) {
    val colors = LocalJellyFishColors.current

    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        genres.forEach { genre ->
            Text(
                text = genre,
                fontSize = 12.sp,
                color = colors.secondaryForeground,
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(colors.secondary)
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            )
        }
    }
}
```

**Step 2: Verify build compiles**

Run: `./gradlew :composeApp:compileKotlinJvm`
Expected: BUILD SUCCESSFUL

**Step 3: Commit**

```bash
git add -A
git commit -m "feat: add GenreBadges component"
```

---

## Task 8: Create EpisodeCard Component

**Files:**
- Create: `composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/presentation/components/EpisodeCard.kt`

**Step 1: Create EpisodeCard.kt**

```kotlin
package com.lowiq.jellyfish.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.lowiq.jellyfish.domain.model.Episode
import com.lowiq.jellyfish.presentation.theme.LocalJellyFishColors

@Composable
fun EpisodeCard(
    episode: Episode,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalJellyFishColors.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(90.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(colors.card)
            .clickable(onClick = onClick)
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Thumbnail (16:9)
        Box(
            modifier = Modifier
                .width(120.dp)
                .fillMaxHeight()
                .clip(RoundedCornerShape(6.dp))
                .background(colors.secondary),
            contentAlignment = Alignment.Center
        ) {
            if (episode.thumbnailUrl != null) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalPlatformContext.current)
                        .data(episode.thumbnailUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = episode.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = null,
                    tint = colors.mutedForeground,
                    modifier = Modifier.size(32.dp)
                )
            }

            // Progress bar overlay at bottom
            episode.progress?.let { progress ->
                if (progress > 0f) {
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(3.dp)
                            .align(Alignment.BottomCenter),
                        color = colors.primary,
                        trackColor = colors.secondary.copy(alpha = 0.5f)
                    )
                }
            }
        }

        // Text content
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center
        ) {
            // Episode number and title
            Text(
                text = "${episode.episodeNumber}. ${episode.title}",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = colors.foreground,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            // Duration
            episode.runtime?.let { runtime ->
                Text(
                    text = runtime,
                    fontSize = 12.sp,
                    color = colors.mutedForeground,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }

            // Description
            episode.overview?.let { overview ->
                Text(
                    text = overview,
                    fontSize = 12.sp,
                    color = colors.mutedForeground,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}
```

**Step 2: Verify build compiles**

Run: `./gradlew :composeApp:compileKotlinJvm`
Expected: BUILD SUCCESSFUL

**Step 3: Commit**

```bash
git add -A
git commit -m "feat: add EpisodeCard component"
```

---

## Task 9: Create SeasonDropdown Component

**Files:**
- Create: `composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/presentation/components/SeasonDropdown.kt`

**Step 1: Create SeasonDropdown.kt**

```kotlin
package com.lowiq.jellyfish.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lowiq.jellyfish.domain.model.Season
import com.lowiq.jellyfish.presentation.theme.LocalJellyFishColors

@Composable
fun SeasonDropdown(
    seasons: List<Season>,
    selectedSeason: Int,
    onSeasonSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalJellyFishColors.current
    var expanded by remember { mutableStateOf(false) }

    val selectedSeasonName = seasons.find { it.number == selectedSeason }?.name
        ?: "Season $selectedSeason"

    Box(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .border(1.dp, colors.border, RoundedCornerShape(8.dp))
                .background(colors.card)
                .clickable { expanded = true }
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = selectedSeasonName,
                fontSize = 14.sp,
                color = colors.foreground
            )
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "Select season",
                tint = colors.foreground
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(colors.card)
        ) {
            seasons.forEach { season ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = "${season.name} (${season.episodeCount} episodes)",
                            color = if (season.number == selectedSeason)
                                colors.primary else colors.foreground
                        )
                    },
                    onClick = {
                        onSeasonSelected(season.number)
                        expanded = false
                    }
                )
            }
        }
    }
}
```

**Step 2: Verify build compiles**

Run: `./gradlew :composeApp:compileKotlinJvm`
Expected: BUILD SUCCESSFUL

**Step 3: Commit**

```bash
git add -A
git commit -m "feat: add SeasonDropdown component"
```

---

## Task 10: Create MovieDetailScreen

**Files:**
- Create: `composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/presentation/screens/detail/MovieDetailScreen.kt`
- Create: `composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/presentation/screens/detail/MovieDetailScreenModel.kt`

**Step 1: Create MovieDetailScreenModel.kt**

```kotlin
package com.lowiq.jellyfish.presentation.screens.detail

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.lowiq.jellyfish.domain.model.CastMember
import com.lowiq.jellyfish.domain.model.MediaItem
import com.lowiq.jellyfish.domain.model.MovieDetails
import com.lowiq.jellyfish.domain.repository.MediaRepository
import com.lowiq.jellyfish.domain.repository.ServerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class MovieDetailState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val title: String = "",
    val overview: String? = null,
    val backdropUrl: String? = null,
    val year: String? = null,
    val runtime: String? = null,
    val rating: Float? = null,
    val genres: List<String> = emptyList(),
    val studio: String? = null,
    val cast: List<CastMember> = emptyList(),
    val similarItems: List<MediaItem> = emptyList(),
    val trailerUrl: String? = null,
    val isFavorite: Boolean = false,
    val isWatched: Boolean = false
)

class MovieDetailScreenModel(
    private val itemId: String,
    private val serverRepository: ServerRepository,
    private val mediaRepository: MediaRepository
) : ScreenModel {

    private val _state = MutableStateFlow(MovieDetailState())
    val state = _state.asStateFlow()

    private var currentServerId: String? = null

    init {
        loadDetails()
    }

    private fun loadDetails() {
        screenModelScope.launch {
            val server = serverRepository.getActiveServer()
                .filterNotNull()
                .first()
            currentServerId = server.id

            mediaRepository.getMovieDetails(server.id, itemId)
                .onSuccess { details ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            title = details.title,
                            overview = details.overview,
                            backdropUrl = details.backdropUrl,
                            year = details.year,
                            runtime = details.runtime,
                            rating = details.rating,
                            genres = details.genres,
                            studio = details.studio,
                            cast = details.cast,
                            similarItems = details.similarItems,
                            trailerUrl = details.trailerUrl,
                            isFavorite = details.isFavorite,
                            isWatched = details.isWatched
                        )
                    }
                }
                .onFailure { e ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = e.message
                        )
                    }
                }
        }
    }

    fun toggleFavorite() {
        val serverId = currentServerId ?: return
        val newValue = !_state.value.isFavorite

        screenModelScope.launch {
            mediaRepository.toggleFavorite(serverId, itemId, newValue)
                .onSuccess {
                    _state.update { it.copy(isFavorite = newValue) }
                }
        }
    }

    fun toggleWatched() {
        val serverId = currentServerId ?: return
        val newValue = !_state.value.isWatched

        screenModelScope.launch {
            mediaRepository.toggleWatched(serverId, itemId, newValue)
                .onSuccess {
                    _state.update { it.copy(isWatched = newValue) }
                }
        }
    }

    fun onPlay() {
        // TODO: Implement playback
    }

    fun onDownload() {
        // TODO: Implement download
    }

    fun onShare() {
        // TODO: Implement share
    }
}
```

**Step 2: Create MovieDetailScreen.kt**

```kotlin
package com.lowiq.jellyfish.presentation.screens.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.lowiq.jellyfish.domain.model.MediaType
import com.lowiq.jellyfish.presentation.components.ActionButtonsRow
import com.lowiq.jellyfish.presentation.components.CastCarousel
import com.lowiq.jellyfish.presentation.components.GenreBadges
import com.lowiq.jellyfish.presentation.components.MediaCarousel
import com.lowiq.jellyfish.presentation.components.MediaCarouselItem
import com.lowiq.jellyfish.presentation.theme.LocalJellyFishColors
import org.koin.core.parameter.parametersOf

class MovieDetailScreen(private val itemId: String) : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val colors = LocalJellyFishColors.current
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = koinScreenModel<MovieDetailScreenModel> { parametersOf(itemId) }
        val state by screenModel.state.collectAsState()

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { },
                    navigationIcon = {
                        IconButton(onClick = { navigator.pop() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = colors.foreground
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = { /* TODO: Show menu */ }) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "More options",
                                tint = colors.foreground
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent
                    )
                )
            },
            containerColor = colors.background
        ) { paddingValues ->
            when {
                state.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = colors.foreground)
                    }
                }
                state.error != null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = state.error ?: "An error occurred",
                            color = colors.destructive
                        )
                    }
                }
                else -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    ) {
                        // Backdrop with gradient
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(280.dp)
                        ) {
                            if (state.backdropUrl != null) {
                                AsyncImage(
                                    model = ImageRequest.Builder(LocalPlatformContext.current)
                                        .data(state.backdropUrl)
                                        .crossfade(true)
                                        .build(),
                                    contentDescription = state.title,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                            // Gradient overlay
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        Brush.verticalGradient(
                                            colors = listOf(
                                                Color.Transparent,
                                                colors.background
                                            ),
                                            startY = 100f
                                        )
                                    )
                            )
                        }

                        // Content
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                        ) {
                            // Title
                            Text(
                                text = state.title,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = colors.foreground
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            // Metadata row
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                state.year?.let { year ->
                                    Text(
                                        text = year,
                                        fontSize = 14.sp,
                                        color = colors.mutedForeground
                                    )
                                }
                                state.runtime?.let { runtime ->
                                    Text(text = "•", color = colors.mutedForeground)
                                    Text(
                                        text = runtime,
                                        fontSize = 14.sp,
                                        color = colors.mutedForeground
                                    )
                                }
                                state.rating?.let { rating ->
                                    Text(text = "•", color = colors.mutedForeground)
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = null,
                                        tint = Color(0xFFFFD700),
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        text = String.format("%.1f", rating),
                                        fontSize = 14.sp,
                                        color = colors.mutedForeground
                                    )
                                }
                            }

                            // Genres
                            if (state.genres.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(12.dp))
                                GenreBadges(genres = state.genres)
                            }

                            // Studio
                            state.studio?.let { studio ->
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = studio,
                                    fontSize = 14.sp,
                                    color = colors.mutedForeground
                                )
                            }

                            // Action buttons
                            Spacer(modifier = Modifier.height(16.dp))
                            ActionButtonsRow(
                                onPlay = { screenModel.onPlay() },
                                isFavorite = state.isFavorite,
                                onToggleFavorite = { screenModel.toggleFavorite() },
                                isWatched = state.isWatched,
                                onToggleWatched = { screenModel.toggleWatched() },
                                onDownload = { screenModel.onDownload() },
                                onShare = { screenModel.onShare() }
                            )

                            // Overview
                            state.overview?.let { overview ->
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = overview,
                                    fontSize = 14.sp,
                                    color = colors.foreground,
                                    lineHeight = 22.sp
                                )
                            }
                        }

                        // Cast section
                        if (state.cast.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(24.dp))
                            CastCarousel(
                                title = "Cast",
                                cast = state.cast,
                                onPersonClick = { /* TODO: Navigate to person */ }
                            )
                        }

                        // Similar movies section
                        if (state.similarItems.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(24.dp))
                            MediaCarousel(
                                title = "Similar Movies",
                                items = state.similarItems.map { item ->
                                    MediaCarouselItem(
                                        id = item.id,
                                        title = item.title,
                                        subtitle = item.subtitle,
                                        imageUrl = item.imageUrl,
                                        progress = item.progress,
                                        isPoster = item.isPoster,
                                        type = item.type
                                    )
                                },
                                onItemClick = { id ->
                                    navigator.push(MovieDetailScreen(id))
                                }
                            )
                        }

                        Spacer(modifier = Modifier.height(32.dp))
                    }
                }
            }
        }
    }
}
```

**Step 3: Register ScreenModel in AppModule.kt**

Add to `presentationModule`:

```kotlin
factory { (itemId: String) -> MovieDetailScreenModel(itemId, get(), get()) }
```

**Step 4: Verify build compiles**

Run: `./gradlew :composeApp:compileKotlinJvm`
Expected: BUILD SUCCESSFUL

**Step 5: Commit**

```bash
git add -A
git commit -m "feat: add MovieDetailScreen with ScreenModel"
```

---

## Task 11: Create SeriesDetailScreen

**Files:**
- Create: `composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/presentation/screens/detail/SeriesDetailScreen.kt`
- Create: `composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/presentation/screens/detail/SeriesDetailScreenModel.kt`

**Step 1: Create SeriesDetailScreenModel.kt**

```kotlin
package com.lowiq.jellyfish.presentation.screens.detail

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.lowiq.jellyfish.domain.model.CastMember
import com.lowiq.jellyfish.domain.model.Episode
import com.lowiq.jellyfish.domain.model.MediaItem
import com.lowiq.jellyfish.domain.model.Season
import com.lowiq.jellyfish.domain.repository.MediaRepository
import com.lowiq.jellyfish.domain.repository.ServerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SeriesDetailState(
    val isLoading: Boolean = true,
    val isLoadingEpisodes: Boolean = false,
    val error: String? = null,
    val title: String = "",
    val overview: String? = null,
    val backdropUrl: String? = null,
    val year: String? = null,
    val seasonCount: Int = 0,
    val rating: Float? = null,
    val genres: List<String> = emptyList(),
    val studio: String? = null,
    val cast: List<CastMember> = emptyList(),
    val seasons: List<Season> = emptyList(),
    val selectedSeason: Int = 1,
    val episodes: List<Episode> = emptyList(),
    val similarItems: List<MediaItem> = emptyList(),
    val isFavorite: Boolean = false,
    val isWatched: Boolean = false
)

class SeriesDetailScreenModel(
    private val itemId: String,
    private val serverRepository: ServerRepository,
    private val mediaRepository: MediaRepository
) : ScreenModel {

    private val _state = MutableStateFlow(SeriesDetailState())
    val state = _state.asStateFlow()

    private var currentServerId: String? = null

    init {
        loadDetails()
    }

    private fun loadDetails() {
        screenModelScope.launch {
            val server = serverRepository.getActiveServer()
                .filterNotNull()
                .first()
            currentServerId = server.id

            mediaRepository.getSeriesDetails(server.id, itemId)
                .onSuccess { details ->
                    val firstSeason = details.seasons.firstOrNull()?.number ?: 1
                    _state.update {
                        it.copy(
                            isLoading = false,
                            title = details.title,
                            overview = details.overview,
                            backdropUrl = details.backdropUrl,
                            year = details.year,
                            seasonCount = details.seasonCount,
                            rating = details.rating,
                            genres = details.genres,
                            studio = details.studio,
                            cast = details.cast,
                            seasons = details.seasons,
                            selectedSeason = firstSeason,
                            similarItems = details.similarItems,
                            isFavorite = details.isFavorite,
                            isWatched = details.isWatched
                        )
                    }
                    // Load episodes for first season
                    loadEpisodes(server.id, firstSeason)
                }
                .onFailure { e ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = e.message
                        )
                    }
                }
        }
    }

    private fun loadEpisodes(serverId: String, seasonNumber: Int) {
        screenModelScope.launch {
            _state.update { it.copy(isLoadingEpisodes = true) }

            mediaRepository.getSeasonEpisodes(serverId, itemId, seasonNumber)
                .onSuccess { episodes ->
                    _state.update {
                        it.copy(
                            isLoadingEpisodes = false,
                            episodes = episodes
                        )
                    }
                }
                .onFailure {
                    _state.update { it.copy(isLoadingEpisodes = false) }
                }
        }
    }

    fun selectSeason(seasonNumber: Int) {
        val serverId = currentServerId ?: return
        _state.update { it.copy(selectedSeason = seasonNumber) }
        loadEpisodes(serverId, seasonNumber)
    }

    fun toggleFavorite() {
        val serverId = currentServerId ?: return
        val newValue = !_state.value.isFavorite

        screenModelScope.launch {
            mediaRepository.toggleFavorite(serverId, itemId, newValue)
                .onSuccess {
                    _state.update { it.copy(isFavorite = newValue) }
                }
        }
    }

    fun toggleWatched() {
        val serverId = currentServerId ?: return
        val newValue = !_state.value.isWatched

        screenModelScope.launch {
            mediaRepository.toggleWatched(serverId, itemId, newValue)
                .onSuccess {
                    _state.update { it.copy(isWatched = newValue) }
                }
        }
    }

    fun onPlay() {
        // TODO: Resume or play S1E1
    }

    fun onDownload() {
        // TODO: Implement download
    }

    fun onShare() {
        // TODO: Implement share
    }
}
```

**Step 2: Create SeriesDetailScreen.kt**

Similar structure to MovieDetailScreen but with:
- Season count in metadata instead of runtime
- SeasonDropdown component
- Episode list (vertical) instead of trailer section
- Loading indicator for episodes

```kotlin
package com.lowiq.jellyfish.presentation.screens.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.lowiq.jellyfish.domain.model.MediaType
import com.lowiq.jellyfish.presentation.components.ActionButtonsRow
import com.lowiq.jellyfish.presentation.components.CastCarousel
import com.lowiq.jellyfish.presentation.components.EpisodeCard
import com.lowiq.jellyfish.presentation.components.GenreBadges
import com.lowiq.jellyfish.presentation.components.MediaCarousel
import com.lowiq.jellyfish.presentation.components.MediaCarouselItem
import com.lowiq.jellyfish.presentation.components.SeasonDropdown
import com.lowiq.jellyfish.presentation.theme.LocalJellyFishColors
import org.koin.core.parameter.parametersOf

class SeriesDetailScreen(private val itemId: String) : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val colors = LocalJellyFishColors.current
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = koinScreenModel<SeriesDetailScreenModel> { parametersOf(itemId) }
        val state by screenModel.state.collectAsState()

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { },
                    navigationIcon = {
                        IconButton(onClick = { navigator.pop() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = colors.foreground
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = { /* TODO: Show menu */ }) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "More options",
                                tint = colors.foreground
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent
                    )
                )
            },
            containerColor = colors.background
        ) { paddingValues ->
            when {
                state.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = colors.foreground)
                    }
                }
                state.error != null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = state.error ?: "An error occurred",
                            color = colors.destructive
                        )
                    }
                }
                else -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    ) {
                        // Backdrop with gradient
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(280.dp)
                        ) {
                            if (state.backdropUrl != null) {
                                AsyncImage(
                                    model = ImageRequest.Builder(LocalPlatformContext.current)
                                        .data(state.backdropUrl)
                                        .crossfade(true)
                                        .build(),
                                    contentDescription = state.title,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        Brush.verticalGradient(
                                            colors = listOf(
                                                Color.Transparent,
                                                colors.background
                                            ),
                                            startY = 100f
                                        )
                                    )
                            )
                        }

                        // Content
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                        ) {
                            Text(
                                text = state.title,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = colors.foreground
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            // Metadata
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                state.year?.let { year ->
                                    Text(
                                        text = year,
                                        fontSize = 14.sp,
                                        color = colors.mutedForeground
                                    )
                                }
                                if (state.seasonCount > 0) {
                                    Text(text = "•", color = colors.mutedForeground)
                                    Text(
                                        text = "${state.seasonCount} Season${if (state.seasonCount > 1) "s" else ""}",
                                        fontSize = 14.sp,
                                        color = colors.mutedForeground
                                    )
                                }
                                state.rating?.let { rating ->
                                    Text(text = "•", color = colors.mutedForeground)
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = null,
                                        tint = Color(0xFFFFD700),
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        text = String.format("%.1f", rating),
                                        fontSize = 14.sp,
                                        color = colors.mutedForeground
                                    )
                                }
                            }

                            if (state.genres.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(12.dp))
                                GenreBadges(genres = state.genres)
                            }

                            state.studio?.let { studio ->
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = studio,
                                    fontSize = 14.sp,
                                    color = colors.mutedForeground
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))
                            ActionButtonsRow(
                                onPlay = { screenModel.onPlay() },
                                isFavorite = state.isFavorite,
                                onToggleFavorite = { screenModel.toggleFavorite() },
                                isWatched = state.isWatched,
                                onToggleWatched = { screenModel.toggleWatched() },
                                onDownload = { screenModel.onDownload() },
                                onShare = { screenModel.onShare() }
                            )

                            state.overview?.let { overview ->
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = overview,
                                    fontSize = 14.sp,
                                    color = colors.foreground,
                                    lineHeight = 22.sp
                                )
                            }

                            // Seasons section
                            if (state.seasons.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(24.dp))
                                Text(
                                    text = "Seasons",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = colors.foreground
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                SeasonDropdown(
                                    seasons = state.seasons,
                                    selectedSeason = state.selectedSeason,
                                    onSeasonSelected = { screenModel.selectSeason(it) }
                                )
                            }

                            // Episodes
                            Spacer(modifier = Modifier.height(16.dp))
                            if (state.isLoadingEpisodes) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(32.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(
                                        color = colors.foreground,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            } else {
                                Column(
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    state.episodes.forEach { episode ->
                                        EpisodeCard(
                                            episode = episode,
                                            onClick = {
                                                navigator.push(EpisodeDetailScreen(episode.id))
                                            }
                                        )
                                    }
                                }
                            }
                        }

                        // Cast section
                        if (state.cast.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(24.dp))
                            CastCarousel(
                                title = "Cast",
                                cast = state.cast,
                                onPersonClick = { /* TODO */ }
                            )
                        }

                        // Similar series
                        if (state.similarItems.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(24.dp))
                            MediaCarousel(
                                title = "Similar Series",
                                items = state.similarItems.map { item ->
                                    MediaCarouselItem(
                                        id = item.id,
                                        title = item.title,
                                        subtitle = item.subtitle,
                                        imageUrl = item.imageUrl,
                                        progress = item.progress,
                                        isPoster = item.isPoster,
                                        type = item.type
                                    )
                                },
                                onItemClick = { id ->
                                    navigator.push(SeriesDetailScreen(id))
                                }
                            )
                        }

                        Spacer(modifier = Modifier.height(32.dp))
                    }
                }
            }
        }
    }
}
```

**Step 3: Register ScreenModel in AppModule.kt**

Add to `presentationModule`:

```kotlin
factory { (itemId: String) -> SeriesDetailScreenModel(itemId, get(), get()) }
```

**Step 4: Verify build compiles**

Run: `./gradlew :composeApp:compileKotlinJvm`
Expected: BUILD SUCCESSFUL

**Step 5: Commit**

```bash
git add -A
git commit -m "feat: add SeriesDetailScreen with ScreenModel"
```

---

## Task 12: Create EpisodeDetailScreen

**Files:**
- Create: `composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/presentation/screens/detail/EpisodeDetailScreen.kt`
- Create: `composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/presentation/screens/detail/EpisodeDetailScreenModel.kt`

**Step 1: Create EpisodeDetailScreenModel.kt**

```kotlin
package com.lowiq.jellyfish.presentation.screens.detail

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.lowiq.jellyfish.domain.model.CastMember
import com.lowiq.jellyfish.domain.model.Episode
import com.lowiq.jellyfish.domain.repository.MediaRepository
import com.lowiq.jellyfish.domain.repository.ServerRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class EpisodeDetailState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val title: String = "",
    val overview: String? = null,
    val thumbnailUrl: String? = null,
    val seasonNumber: Int = 0,
    val episodeNumber: Int = 0,
    val totalEpisodes: Int = 0,
    val runtime: String? = null,
    val rating: Float? = null,
    val seriesId: String = "",
    val seriesName: String = "",
    val guestStars: List<CastMember> = emptyList(),
    val seasonEpisodes: List<Episode> = emptyList(),
    val previousEpisodeId: String? = null,
    val nextEpisodeId: String? = null,
    val isFavorite: Boolean = false,
    val isWatched: Boolean = false,
    val progress: Float? = null
)

sealed class EpisodeDetailEvent {
    data class NavigateToEpisode(val episodeId: String) : EpisodeDetailEvent()
    data class NavigateToSeries(val seriesId: String) : EpisodeDetailEvent()
}

class EpisodeDetailScreenModel(
    private val itemId: String,
    private val serverRepository: ServerRepository,
    private val mediaRepository: MediaRepository
) : ScreenModel {

    private val _state = MutableStateFlow(EpisodeDetailState())
    val state = _state.asStateFlow()

    private val _events = MutableSharedFlow<EpisodeDetailEvent>()
    val events = _events.asSharedFlow()

    private var currentServerId: String? = null
    private var currentItemId: String = itemId

    init {
        loadDetails(itemId)
    }

    private fun loadDetails(episodeId: String) {
        currentItemId = episodeId
        screenModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            val server = serverRepository.getActiveServer()
                .filterNotNull()
                .first()
            currentServerId = server.id

            mediaRepository.getEpisodeDetails(server.id, episodeId)
                .onSuccess { details ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            title = details.title,
                            overview = details.overview,
                            thumbnailUrl = details.thumbnailUrl,
                            seasonNumber = details.seasonNumber,
                            episodeNumber = details.episodeNumber,
                            totalEpisodes = details.seasonEpisodes.size,
                            runtime = details.runtime,
                            rating = details.rating,
                            seriesId = details.seriesId,
                            seriesName = details.seriesName,
                            guestStars = details.guestStars,
                            seasonEpisodes = details.seasonEpisodes,
                            previousEpisodeId = details.previousEpisodeId,
                            nextEpisodeId = details.nextEpisodeId,
                            isFavorite = details.isFavorite,
                            isWatched = details.isWatched,
                            progress = details.progress
                        )
                    }
                }
                .onFailure { e ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = e.message
                        )
                    }
                }
        }
    }

    fun navigateToPreviousEpisode() {
        _state.value.previousEpisodeId?.let { id ->
            loadDetails(id)
        }
    }

    fun navigateToNextEpisode() {
        _state.value.nextEpisodeId?.let { id ->
            loadDetails(id)
        }
    }

    fun navigateToEpisode(episodeId: String) {
        if (episodeId != currentItemId) {
            loadDetails(episodeId)
        }
    }

    fun navigateToSeries() {
        screenModelScope.launch {
            _events.emit(EpisodeDetailEvent.NavigateToSeries(_state.value.seriesId))
        }
    }

    fun toggleFavorite() {
        val serverId = currentServerId ?: return
        val newValue = !_state.value.isFavorite

        screenModelScope.launch {
            mediaRepository.toggleFavorite(serverId, currentItemId, newValue)
                .onSuccess {
                    _state.update { it.copy(isFavorite = newValue) }
                }
        }
    }

    fun toggleWatched() {
        val serverId = currentServerId ?: return
        val newValue = !_state.value.isWatched

        screenModelScope.launch {
            mediaRepository.toggleWatched(serverId, currentItemId, newValue)
                .onSuccess {
                    _state.update { it.copy(isWatched = newValue) }
                }
        }
    }

    fun onPlay() {
        // TODO: Implement playback
    }

    fun onDownload() {
        // TODO: Implement download
    }

    fun onShare() {
        // TODO: Implement share
    }
}
```

**Step 2: Create EpisodeDetailScreen.kt**

```kotlin
package com.lowiq.jellyfish.presentation.screens.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.NavigateBefore
import androidx.compose.material.icons.automirrored.filled.NavigateNext
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.lowiq.jellyfish.presentation.components.ActionButtonsRow
import com.lowiq.jellyfish.presentation.components.CastCarousel
import com.lowiq.jellyfish.presentation.theme.LocalJellyFishColors
import org.koin.core.parameter.parametersOf

class EpisodeDetailScreen(private val itemId: String) : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val colors = LocalJellyFishColors.current
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = koinScreenModel<EpisodeDetailScreenModel> { parametersOf(itemId) }
        val state by screenModel.state.collectAsState()

        LaunchedEffect(Unit) {
            screenModel.events.collect { event ->
                when (event) {
                    is EpisodeDetailEvent.NavigateToSeries -> {
                        navigator.push(SeriesDetailScreen(event.seriesId))
                    }
                    is EpisodeDetailEvent.NavigateToEpisode -> {
                        // Handled internally via loadDetails
                    }
                }
            }
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = state.seriesName,
                            color = colors.foreground,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.clickable { screenModel.navigateToSeries() }
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { navigator.pop() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = colors.foreground
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = { /* TODO: Show menu */ }) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "More options",
                                tint = colors.foreground
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = colors.background
                    )
                )
            },
            containerColor = colors.background
        ) { paddingValues ->
            when {
                state.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = colors.foreground)
                    }
                }
                state.error != null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = state.error ?: "An error occurred",
                            color = colors.destructive
                        )
                    }
                }
                else -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                            .verticalScroll(rememberScrollState())
                    ) {
                        // Large thumbnail with play overlay
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .aspectRatio(16f / 9f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(colors.secondary)
                                .clickable { screenModel.onPlay() },
                            contentAlignment = Alignment.Center
                        ) {
                            if (state.thumbnailUrl != null) {
                                AsyncImage(
                                    model = ImageRequest.Builder(LocalPlatformContext.current)
                                        .data(state.thumbnailUrl)
                                        .crossfade(true)
                                        .build(),
                                    contentDescription = state.title,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                            // Play button overlay
                            Box(
                                modifier = Modifier
                                    .size(64.dp)
                                    .clip(CircleShape)
                                    .background(colors.primary.copy(alpha = 0.9f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = "Play",
                                    tint = colors.primaryForeground,
                                    modifier = Modifier.size(36.dp)
                                )
                            }
                        }

                        // Content
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                        ) {
                            Spacer(modifier = Modifier.height(16.dp))

                            // Episode identifier and title
                            Text(
                                text = "S${state.seasonNumber} E${state.episodeNumber} • ${state.title}",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = colors.foreground
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            // Duration and rating
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                state.runtime?.let { runtime ->
                                    Text(
                                        text = runtime,
                                        fontSize = 14.sp,
                                        color = colors.mutedForeground
                                    )
                                }
                                state.rating?.let { rating ->
                                    Text(text = "•", color = colors.mutedForeground)
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = null,
                                        tint = Color(0xFFFFD700),
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        text = String.format("%.1f", rating),
                                        fontSize = 14.sp,
                                        color = colors.mutedForeground
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))
                            ActionButtonsRow(
                                onPlay = { screenModel.onPlay() },
                                isFavorite = state.isFavorite,
                                onToggleFavorite = { screenModel.toggleFavorite() },
                                isWatched = state.isWatched,
                                onToggleWatched = { screenModel.toggleWatched() },
                                onDownload = { screenModel.onDownload() },
                                onShare = { screenModel.onShare() }
                            )

                            state.overview?.let { overview ->
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = overview,
                                    fontSize = 14.sp,
                                    color = colors.foreground,
                                    lineHeight = 22.sp
                                )
                            }

                            // Episode navigation
                            Spacer(modifier = Modifier.height(24.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                TextButton(
                                    onClick = { screenModel.navigateToPreviousEpisode() },
                                    enabled = state.previousEpisodeId != null
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.NavigateBefore,
                                        contentDescription = null,
                                        tint = if (state.previousEpisodeId != null)
                                            colors.foreground else colors.mutedForeground
                                    )
                                    Text(
                                        text = "Previous",
                                        color = if (state.previousEpisodeId != null)
                                            colors.foreground else colors.mutedForeground
                                    )
                                }

                                Text(
                                    text = "${state.episodeNumber} / ${state.totalEpisodes}",
                                    fontSize = 14.sp,
                                    color = colors.mutedForeground
                                )

                                TextButton(
                                    onClick = { screenModel.navigateToNextEpisode() },
                                    enabled = state.nextEpisodeId != null
                                ) {
                                    Text(
                                        text = "Next",
                                        color = if (state.nextEpisodeId != null)
                                            colors.foreground else colors.mutedForeground
                                    )
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.NavigateNext,
                                        contentDescription = null,
                                        tint = if (state.nextEpisodeId != null)
                                            colors.foreground else colors.mutedForeground
                                    )
                                }
                            }
                        }

                        // Guest stars
                        if (state.guestStars.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(24.dp))
                            CastCarousel(
                                title = "Guest Stars",
                                cast = state.guestStars,
                                onPersonClick = { /* TODO */ }
                            )
                        }

                        // More from this season
                        if (state.seasonEpisodes.size > 1) {
                            Spacer(modifier = Modifier.height(24.dp))
                            Text(
                                text = "More from Season ${state.seasonNumber}",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = colors.foreground,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            LazyRow(
                                contentPadding = PaddingValues(horizontal = 16.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                items(
                                    items = state.seasonEpisodes.filter { it.id != itemId },
                                    key = { it.id }
                                ) { episode ->
                                    EpisodeThumbnailCard(
                                        episode = episode,
                                        onClick = { screenModel.navigateToEpisode(episode.id) }
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(32.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun EpisodeThumbnailCard(
    episode: com.lowiq.jellyfish.domain.model.Episode,
    onClick: () -> Unit
) {
    val colors = LocalJellyFishColors.current

    Column(
        modifier = Modifier
            .width(160.dp)
            .clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f)
                .clip(RoundedCornerShape(8.dp))
                .background(colors.secondary),
            contentAlignment = Alignment.Center
        ) {
            if (episode.thumbnailUrl != null) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalPlatformContext.current)
                        .data(episode.thumbnailUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = episode.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = null,
                    tint = colors.mutedForeground,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "${episode.episodeNumber}. ${episode.title}",
            fontSize = 12.sp,
            color = colors.foreground,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}
```

**Step 3: Register ScreenModel in AppModule.kt**

Add to `presentationModule`:

```kotlin
factory { (itemId: String) -> EpisodeDetailScreenModel(itemId, get(), get()) }
```

**Step 4: Verify build compiles**

Run: `./gradlew :composeApp:compileKotlinJvm`
Expected: BUILD SUCCESSFUL

**Step 5: Commit**

```bash
git add -A
git commit -m "feat: add EpisodeDetailScreen with ScreenModel"
```

---

## Task 13: Integrate Navigation from HomeScreen and LibraryScreen

**Files:**
- Modify: `composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/presentation/screens/home/HomeScreen.kt`
- Modify: `composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/presentation/screens/library/LibraryScreen.kt`
- Modify: `composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/presentation/components/MediaCarousel.kt`

**Step 1: Update MediaCarousel to pass type in onItemClick**

Update the callback signature:

```kotlin
@Composable
fun MediaCarousel(
    title: String,
    items: List<MediaCarouselItem>,
    onItemClick: (id: String, type: MediaType) -> Unit,
    modifier: Modifier = Modifier
)
```

Update the click handler:

```kotlin
MediaCard(
    title = item.title,
    subtitle = item.subtitle,
    imageUrl = item.imageUrl,
    progress = item.progress,
    isPoster = item.isPoster,
    onClick = { onItemClick(item.id, item.type) }
)
```

**Step 2: Create navigation helper function**

Create a new file `composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/presentation/navigation/DetailNavigation.kt`:

```kotlin
package com.lowiq.jellyfish.presentation.navigation

import cafe.adriel.voyager.navigator.Navigator
import com.lowiq.jellyfish.domain.model.MediaType
import com.lowiq.jellyfish.presentation.screens.detail.EpisodeDetailScreen
import com.lowiq.jellyfish.presentation.screens.detail.MovieDetailScreen
import com.lowiq.jellyfish.presentation.screens.detail.SeriesDetailScreen

fun navigateToDetail(navigator: Navigator, itemId: String, type: MediaType) {
    when (type) {
        MediaType.MOVIE -> navigator.push(MovieDetailScreen(itemId))
        MediaType.SERIES -> navigator.push(SeriesDetailScreen(itemId))
        MediaType.EPISODE -> navigator.push(EpisodeDetailScreen(itemId))
        else -> { /* No navigation for music/other yet */ }
    }
}
```

**Step 3: Update HomeScreen to use navigation**

Replace all `onItemClick = { /* TODO */ }` with:

```kotlin
onItemClick = { id, type -> navigateToDetail(navigator, id, type) }
```

Add import:

```kotlin
import com.lowiq.jellyfish.presentation.navigation.navigateToDetail
```

**Step 4: Update LibraryScreen to use navigation**

Replace all `onClick = { /* TODO: Navigate to detail */ }` with proper navigation:

```kotlin
// In LibraryListItem, LibraryPosterItem, LibraryGridItem
onClick = { navigateToDetail(navigator, item.id, item.type) }
```

This requires passing MediaType through MediaItem. Since MediaItem already has `type`, update the click handlers.

**Step 5: Add type parameter to LibraryScreen item components**

Update `LibraryGridItem`, `LibraryPosterItem`, `LibraryListItem` to accept `onItemClick: (String, MediaType) -> Unit` and call with `item.id` and `item.type`.

**Step 6: Verify build compiles**

Run: `./gradlew :composeApp:compileKotlinJvm`
Expected: BUILD SUCCESSFUL

**Step 7: Commit**

```bash
git add -A
git commit -m "feat: integrate detail page navigation from Home and Library"
```

---

## Task 14: Run and Test

**Step 1: Run the desktop app**

Run: `./gradlew :composeApp:run`
Expected: App launches successfully

**Step 2: Manual testing checklist**

- [ ] Navigate to movie detail from home screen
- [ ] Navigate to series detail from home screen
- [ ] Navigate to episode detail from series
- [ ] Test previous/next episode navigation
- [ ] Test favorite toggle (verify API call works)
- [ ] Test watched toggle (verify API call works)
- [ ] Test back navigation
- [ ] Verify cast carousel displays
- [ ] Verify similar items carousel displays
- [ ] Verify season dropdown works
- [ ] Navigate to detail from library screen

**Step 3: Final commit**

```bash
git add -A
git commit -m "test: verify detail pages implementation"
```

---

## Summary

This plan implements:

1. **Data Layer**: New domain models, JellyfinDataSource methods, MediaRepository methods
2. **Presentation Layer**: 5 shared components + 3 detail screens with ScreenModels
3. **Navigation**: Helper function + integration in Home/Library screens

Total tasks: 14
Estimated commits: 14
