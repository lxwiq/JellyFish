# Download Feature Implementation Plan

> **For Claude:** REQUIRED SUB-SKILL: Use superpowers:executing-plans to implement this plan task-by-task.

**Goal:** Implement offline download functionality with quality selection, progress tracking, and Jellyfin playback sync.

**Architecture:** Use DataStore for download metadata (consistent with existing patterns), expect/actual for platform-specific file I/O and notifications. Downloads managed by a DownloadManager that coordinates queue and workers.

**Tech Stack:** Kotlin Multiplatform, Compose, DataStore, Ktor (HTTP client), Koin DI, Voyager navigation

---

## Task 1: Download Domain Models

**Files:**
- Create: `composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/domain/model/Download.kt`

**Step 1: Create Download data models**

```kotlin
package com.lowiq.jellyfish.domain.model

enum class DownloadStatus {
    QUEUED,
    DOWNLOADING,
    PAUSED,
    COMPLETED,
    FAILED
}

data class Download(
    val id: String,
    val itemId: String,
    val serverId: String,
    val title: String,
    val subtitle: String? = null,
    val imageUrl: String? = null,
    val quality: String,
    val bitrate: Int,
    val status: DownloadStatus,
    val progress: Float = 0f,
    val totalBytes: Long = 0,
    val downloadedBytes: Long = 0,
    val filePath: String? = null,
    val createdAt: Long,
    val completedAt: Long? = null,
    val errorMessage: String? = null
)

data class QualityOption(
    val id: String,
    val label: String,
    val bitrate: Int,
    val estimatedSizeBytes: Long
)

data class PendingPlaybackSync(
    val id: String,
    val itemId: String,
    val serverId: String,
    val positionTicks: Long,
    val playedPercentage: Float,
    val timestamp: Long
)
```

**Step 2: Verify file compiles**

Run: `./gradlew :composeApp:compileKotlinJvm`
Expected: BUILD SUCCESSFUL

**Step 3: Commit**

```bash
git add composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/domain/model/Download.kt
git commit -m "feat(download): add domain models for download feature"
```

---

## Task 2: Download Settings Storage

**Files:**
- Create: `composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/data/local/DownloadSettingsStorage.kt`

**Step 1: Create download settings storage**

```kotlin
package com.lowiq.jellyfish.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DownloadSettingsStorage(private val dataStore: DataStore<Preferences>) {

    private companion object {
        val KEY_DEFAULT_QUALITY = stringPreferencesKey("download_default_quality")
        val KEY_ALWAYS_ASK_QUALITY = booleanPreferencesKey("download_always_ask_quality")
        val KEY_PARALLEL_DOWNLOADS = intPreferencesKey("download_parallel_count")
        val KEY_AUTO_CLEANUP = booleanPreferencesKey("download_auto_cleanup")
        val KEY_STORAGE_LIMIT_MB = intPreferencesKey("download_storage_limit_mb")
    }

    val defaultQuality: Flow<String> = dataStore.data.map { prefs ->
        prefs[KEY_DEFAULT_QUALITY] ?: "1080p"
    }

    val alwaysAskQuality: Flow<Boolean> = dataStore.data.map { prefs ->
        prefs[KEY_ALWAYS_ASK_QUALITY] ?: true
    }

    val parallelDownloads: Flow<Int> = dataStore.data.map { prefs ->
        prefs[KEY_PARALLEL_DOWNLOADS] ?: 2
    }

    val autoCleanup: Flow<Boolean> = dataStore.data.map { prefs ->
        prefs[KEY_AUTO_CLEANUP] ?: false
    }

    val storageLimitMb: Flow<Int> = dataStore.data.map { prefs ->
        prefs[KEY_STORAGE_LIMIT_MB] ?: 10240 // 10 GB
    }

    suspend fun setDefaultQuality(quality: String) {
        dataStore.edit { it[KEY_DEFAULT_QUALITY] = quality }
    }

    suspend fun setAlwaysAskQuality(ask: Boolean) {
        dataStore.edit { it[KEY_ALWAYS_ASK_QUALITY] = ask }
    }

    suspend fun setParallelDownloads(count: Int) {
        dataStore.edit { it[KEY_PARALLEL_DOWNLOADS] = count.coerceIn(1, 5) }
    }

    suspend fun setAutoCleanup(enabled: Boolean) {
        dataStore.edit { it[KEY_AUTO_CLEANUP] = enabled }
    }

    suspend fun setStorageLimitMb(limitMb: Int) {
        dataStore.edit { it[KEY_STORAGE_LIMIT_MB] = limitMb }
    }
}
```

**Step 2: Verify file compiles**

Run: `./gradlew :composeApp:compileKotlinJvm`
Expected: BUILD SUCCESSFUL

**Step 3: Commit**

```bash
git add composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/data/local/DownloadSettingsStorage.kt
git commit -m "feat(download): add download settings storage"
```

---

## Task 3: Download Metadata Storage

**Files:**
- Create: `composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/data/local/DownloadStorage.kt`

**Step 1: Create download metadata storage using DataStore**

```kotlin
package com.lowiq.jellyfish.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.lowiq.jellyfish.domain.model.Download
import com.lowiq.jellyfish.domain.model.DownloadStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DownloadStorage(private val dataStore: DataStore<Preferences>) {

    private val downloadsKey = stringPreferencesKey("downloads_data")
    private val pendingSyncsKey = stringPreferencesKey("pending_syncs_data")

    fun getAllDownloads(): Flow<List<Download>> {
        return dataStore.data.map { prefs ->
            val data = prefs[downloadsKey] ?: return@map emptyList()
            parseDownloads(data)
        }
    }

    suspend fun saveDownload(download: Download) {
        dataStore.edit { prefs ->
            val existing = prefs[downloadsKey]?.let { parseDownloads(it) } ?: emptyList()
            val updated = existing.filter { it.id != download.id } + download
            prefs[downloadsKey] = serializeDownloads(updated)
        }
    }

    suspend fun removeDownload(downloadId: String) {
        dataStore.edit { prefs ->
            val existing = prefs[downloadsKey]?.let { parseDownloads(it) } ?: emptyList()
            val updated = existing.filter { it.id != downloadId }
            prefs[downloadsKey] = serializeDownloads(updated)
        }
    }

    suspend fun updateProgress(downloadId: String, progress: Float, downloadedBytes: Long) {
        dataStore.edit { prefs ->
            val existing = prefs[downloadsKey]?.let { parseDownloads(it) } ?: emptyList()
            val updated = existing.map {
                if (it.id == downloadId) it.copy(progress = progress, downloadedBytes = downloadedBytes)
                else it
            }
            prefs[downloadsKey] = serializeDownloads(updated)
        }
    }

    suspend fun updateStatus(downloadId: String, status: DownloadStatus, filePath: String? = null, errorMessage: String? = null) {
        dataStore.edit { prefs ->
            val existing = prefs[downloadsKey]?.let { parseDownloads(it) } ?: emptyList()
            val now = System.currentTimeMillis()
            val updated = existing.map {
                if (it.id == downloadId) it.copy(
                    status = status,
                    filePath = filePath ?: it.filePath,
                    completedAt = if (status == DownloadStatus.COMPLETED) now else it.completedAt,
                    errorMessage = errorMessage
                )
                else it
            }
            prefs[downloadsKey] = serializeDownloads(updated)
        }
    }

    private fun serializeDownloads(downloads: List<Download>): String {
        return downloads.joinToString("\n\n") { d ->
            listOf(
                d.id, d.itemId, d.serverId, d.title, d.subtitle.orEmpty(),
                d.imageUrl.orEmpty(), d.quality, d.bitrate.toString(), d.status.name,
                d.progress.toString(), d.totalBytes.toString(), d.downloadedBytes.toString(),
                d.filePath.orEmpty(), d.createdAt.toString(), d.completedAt?.toString().orEmpty(),
                d.errorMessage.orEmpty()
            ).joinToString("||")
        }
    }

    private fun parseDownloads(data: String): List<Download> {
        if (data.isBlank()) return emptyList()
        return data.split("\n\n").mapNotNull { entry ->
            val parts = entry.split("||")
            if (parts.size >= 14) {
                Download(
                    id = parts[0],
                    itemId = parts[1],
                    serverId = parts[2],
                    title = parts[3],
                    subtitle = parts[4].takeIf { it.isNotEmpty() },
                    imageUrl = parts[5].takeIf { it.isNotEmpty() },
                    quality = parts[6],
                    bitrate = parts[7].toIntOrNull() ?: 0,
                    status = DownloadStatus.valueOf(parts[8]),
                    progress = parts[9].toFloatOrNull() ?: 0f,
                    totalBytes = parts[10].toLongOrNull() ?: 0,
                    downloadedBytes = parts[11].toLongOrNull() ?: 0,
                    filePath = parts[12].takeIf { it.isNotEmpty() },
                    createdAt = parts[13].toLongOrNull() ?: 0,
                    completedAt = parts.getOrNull(14)?.toLongOrNull(),
                    errorMessage = parts.getOrNull(15)?.takeIf { it.isNotEmpty() }
                )
            } else null
        }
    }
}
```

**Step 2: Verify file compiles**

Run: `./gradlew :composeApp:compileKotlinJvm`
Expected: BUILD SUCCESSFUL

**Step 3: Commit**

```bash
git add composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/data/local/DownloadStorage.kt
git commit -m "feat(download): add download metadata storage"
```

---

## Task 4: Platform File Operations (expect/actual)

**Files:**
- Create: `composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/data/local/FileManager.kt`
- Create: `composeApp/src/androidMain/kotlin/com/lowiq/jellyfish/data/local/FileManager.android.kt`
- Create: `composeApp/src/jvmMain/kotlin/com/lowiq/jellyfish/data/local/FileManager.jvm.kt`
- Create: `composeApp/src/iosMain/kotlin/com/lowiq/jellyfish/data/local/FileManager.ios.kt`

**Step 1: Create expect declaration in commonMain**

```kotlin
// composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/data/local/FileManager.kt
package com.lowiq.jellyfish.data.local

expect class FileManager {
    fun getDownloadsDirectory(): String
    fun getAvailableSpaceBytes(): Long
    fun getUsedSpaceBytes(): Long
    fun deleteFile(path: String): Boolean
    fun fileExists(path: String): Boolean
    fun getFileSize(path: String): Long
}
```

**Step 2: Create Android actual implementation**

```kotlin
// composeApp/src/androidMain/kotlin/com/lowiq/jellyfish/data/local/FileManager.android.kt
package com.lowiq.jellyfish.data.local

import android.content.Context
import java.io.File

actual class FileManager(private val context: Context) {

    actual fun getDownloadsDirectory(): String {
        val dir = File(context.filesDir, "downloads")
        if (!dir.exists()) dir.mkdirs()
        return dir.absolutePath
    }

    actual fun getAvailableSpaceBytes(): Long {
        return context.filesDir.usableSpace
    }

    actual fun getUsedSpaceBytes(): Long {
        val dir = File(getDownloadsDirectory())
        return dir.walkTopDown().filter { it.isFile }.sumOf { it.length() }
    }

    actual fun deleteFile(path: String): Boolean {
        return File(path).delete()
    }

    actual fun fileExists(path: String): Boolean {
        return File(path).exists()
    }

    actual fun getFileSize(path: String): Long {
        return File(path).length()
    }
}
```

**Step 3: Create JVM actual implementation**

```kotlin
// composeApp/src/jvmMain/kotlin/com/lowiq/jellyfish/data/local/FileManager.jvm.kt
package com.lowiq.jellyfish.data.local

import java.io.File

actual class FileManager {

    private val baseDir = File(System.getProperty("user.home"), ".jellyfish/downloads")

    actual fun getDownloadsDirectory(): String {
        if (!baseDir.exists()) baseDir.mkdirs()
        return baseDir.absolutePath
    }

    actual fun getAvailableSpaceBytes(): Long {
        return baseDir.usableSpace
    }

    actual fun getUsedSpaceBytes(): Long {
        return baseDir.walkTopDown().filter { it.isFile }.sumOf { it.length() }
    }

    actual fun deleteFile(path: String): Boolean {
        return File(path).delete()
    }

    actual fun fileExists(path: String): Boolean {
        return File(path).exists()
    }

    actual fun getFileSize(path: String): Long {
        return File(path).length()
    }
}
```

**Step 4: Create iOS actual implementation**

```kotlin
// composeApp/src/iosMain/kotlin/com/lowiq/jellyfish/data/local/FileManager.ios.kt
package com.lowiq.jellyfish.data.local

import platform.Foundation.NSFileManager
import platform.Foundation.NSSearchPathForDirectoriesInDomains
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSUserDomainMask
import platform.Foundation.NSURL
import platform.Foundation.NSFileSystemFreeSize
import platform.Foundation.NSNumber

actual class FileManager {

    private val fileManager = NSFileManager.defaultManager

    actual fun getDownloadsDirectory(): String {
        val paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, true)
        val documentsDir = paths.firstOrNull() as? String ?: ""
        val downloadsDir = "$documentsDir/downloads"
        if (!fileManager.fileExistsAtPath(downloadsDir)) {
            fileManager.createDirectoryAtPath(downloadsDir, withIntermediateDirectories = true, attributes = null, error = null)
        }
        return downloadsDir
    }

    actual fun getAvailableSpaceBytes(): Long {
        val paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, true)
        val documentsDir = paths.firstOrNull() as? String ?: return 0
        val attrs = fileManager.attributesOfFileSystemForPath(documentsDir, error = null) ?: return 0
        val freeSize = attrs[NSFileSystemFreeSize] as? NSNumber
        return freeSize?.longLongValue ?: 0
    }

    actual fun getUsedSpaceBytes(): Long {
        val dir = getDownloadsDirectory()
        var totalSize = 0L
        val contents = fileManager.contentsOfDirectoryAtPath(dir, error = null) ?: return 0
        contents.forEach { file ->
            val path = "$dir/$file"
            val attrs = fileManager.attributesOfItemAtPath(path, error = null)
            val size = (attrs?.get("NSFileSize") as? NSNumber)?.longLongValue ?: 0
            totalSize += size
        }
        return totalSize
    }

    actual fun deleteFile(path: String): Boolean {
        return fileManager.removeItemAtPath(path, error = null)
    }

    actual fun fileExists(path: String): Boolean {
        return fileManager.fileExistsAtPath(path)
    }

    actual fun getFileSize(path: String): Long {
        val attrs = fileManager.attributesOfItemAtPath(path, error = null) ?: return 0
        val size = attrs["NSFileSize"] as? NSNumber
        return size?.longLongValue ?: 0
    }
}
```

**Step 5: Verify all platforms compile**

Run: `./gradlew :composeApp:compileKotlinJvm :composeApp:compileKotlinAndroid`
Expected: BUILD SUCCESSFUL

**Step 6: Commit**

```bash
git add composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/data/local/FileManager.kt
git add composeApp/src/androidMain/kotlin/com/lowiq/jellyfish/data/local/FileManager.android.kt
git add composeApp/src/jvmMain/kotlin/com/lowiq/jellyfish/data/local/FileManager.jvm.kt
git add composeApp/src/iosMain/kotlin/com/lowiq/jellyfish/data/local/FileManager.ios.kt
git commit -m "feat(download): add platform-specific file manager"
```

---

## Task 5: Download Repository Interface

**Files:**
- Create: `composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/domain/repository/DownloadRepository.kt`

**Step 1: Create repository interface**

```kotlin
package com.lowiq.jellyfish.domain.repository

import com.lowiq.jellyfish.domain.model.Download
import com.lowiq.jellyfish.domain.model.DownloadStatus
import com.lowiq.jellyfish.domain.model.QualityOption
import kotlinx.coroutines.flow.Flow

interface DownloadRepository {
    fun getAllDownloads(): Flow<List<Download>>
    fun getActiveDownloads(): Flow<List<Download>>
    fun getCompletedDownloads(): Flow<List<Download>>
    suspend fun getDownloadByItemId(itemId: String): Download?

    suspend fun getAvailableQualities(serverId: String, itemId: String): Result<List<QualityOption>>
    suspend fun startDownload(
        serverId: String,
        itemId: String,
        title: String,
        subtitle: String?,
        imageUrl: String?,
        quality: QualityOption
    ): Result<Download>

    suspend fun pauseDownload(downloadId: String)
    suspend fun resumeDownload(downloadId: String)
    suspend fun cancelDownload(downloadId: String)
    suspend fun deleteDownload(downloadId: String)

    suspend fun getStorageInfo(): StorageInfo
}

data class StorageInfo(
    val usedBytes: Long,
    val availableBytes: Long,
    val limitBytes: Long?
)
```

**Step 2: Verify file compiles**

Run: `./gradlew :composeApp:compileKotlinJvm`
Expected: BUILD SUCCESSFUL

**Step 3: Commit**

```bash
git add composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/domain/repository/DownloadRepository.kt
git commit -m "feat(download): add download repository interface"
```

---

## Task 6: Add Download API Methods to JellyfinDataSource

**Files:**
- Modify: `composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/data/remote/JellyfinDataSource.kt`
- Modify: `composeApp/src/androidMain/kotlin/com/lowiq/jellyfish/data/remote/JellyfinDataSourceImpl.kt`
- Modify: `composeApp/src/jvmMain/kotlin/com/lowiq/jellyfish/data/remote/JellyfinDataSourceImpl.kt`

**Step 1: Add methods to interface**

Add to `JellyfinDataSource.kt` interface:

```kotlin
suspend fun getMediaSources(serverUrl: String, token: String, userId: String, itemId: String): Result<List<MediaSourceInfo>>
fun getTranscodingDownloadUrl(serverUrl: String, token: String, itemId: String, bitrate: Int): String
```

Add data class:

```kotlin
data class MediaSourceInfo(
    val id: String,
    val name: String,
    val bitrate: Int?,
    val size: Long?,
    val container: String?
)
```

**Step 2: Implement in Android JellyfinDataSourceImpl**

Add after `getStreamInfo`:

```kotlin
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
```

**Step 3: Implement in JVM JellyfinDataSourceImpl (same code)**

**Step 4: Verify compiles**

Run: `./gradlew :composeApp:compileKotlinJvm :composeApp:compileKotlinAndroid`
Expected: BUILD SUCCESSFUL

**Step 5: Commit**

```bash
git add composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/data/remote/JellyfinDataSource.kt
git add composeApp/src/androidMain/kotlin/com/lowiq/jellyfish/data/remote/JellyfinDataSourceImpl.kt
git add composeApp/src/jvmMain/kotlin/com/lowiq/jellyfish/data/remote/JellyfinDataSourceImpl.kt
git commit -m "feat(download): add media source and transcoding URL methods"
```

---

## Task 7: HTTP Download Client

**Files:**
- Create: `composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/data/remote/DownloadClient.kt`

**Step 1: Create download client using Ktor**

```kotlin
package com.lowiq.jellyfish.data.remote

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.utils.io.*
import io.ktor.utils.io.core.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

data class DownloadProgress(
    val bytesDownloaded: Long,
    val totalBytes: Long,
    val progress: Float
)

sealed class DownloadResult {
    data class Progress(val progress: DownloadProgress) : DownloadResult()
    data class Success(val filePath: String) : DownloadResult()
    data class Error(val message: String) : DownloadResult()
}

class DownloadClient(private val httpClient: HttpClient) {

    fun downloadFile(
        url: String,
        destinationPath: String,
        onWrite: suspend (ByteArray) -> Unit
    ): Flow<DownloadResult> = flow {
        try {
            httpClient.prepareGet(url).execute { response ->
                if (!response.status.isSuccess()) {
                    emit(DownloadResult.Error("HTTP ${response.status.value}: ${response.status.description}"))
                    return@execute
                }

                val contentLength = response.contentLength() ?: 0L
                var bytesDownloaded = 0L
                val channel = response.bodyAsChannel()

                while (!channel.isClosedForRead) {
                    val packet = channel.readRemaining(DEFAULT_BUFFER_SIZE.toLong())
                    while (!packet.isEmpty) {
                        val bytes = packet.readBytes()
                        onWrite(bytes)
                        bytesDownloaded += bytes.size

                        val progress = if (contentLength > 0) {
                            bytesDownloaded.toFloat() / contentLength.toFloat()
                        } else 0f

                        emit(DownloadResult.Progress(
                            DownloadProgress(
                                bytesDownloaded = bytesDownloaded,
                                totalBytes = contentLength,
                                progress = progress
                            )
                        ))
                    }
                }

                emit(DownloadResult.Success(destinationPath))
            }
        } catch (e: Exception) {
            emit(DownloadResult.Error(e.message ?: "Download failed"))
        }
    }.flowOn(Dispatchers.IO)

    companion object {
        private const val DEFAULT_BUFFER_SIZE = 8192
    }
}
```

**Step 2: Verify file compiles**

Run: `./gradlew :composeApp:compileKotlinJvm`
Expected: BUILD SUCCESSFUL

**Step 3: Commit**

```bash
git add composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/data/remote/DownloadClient.kt
git commit -m "feat(download): add HTTP download client with progress"
```

---

## Task 8: Download Repository Implementation

**Files:**
- Create: `composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/data/repository/DownloadRepositoryImpl.kt`

**Step 1: Implement download repository**

```kotlin
package com.lowiq.jellyfish.data.repository

import com.lowiq.jellyfish.data.local.DownloadSettingsStorage
import com.lowiq.jellyfish.data.local.DownloadStorage
import com.lowiq.jellyfish.data.local.FileManager
import com.lowiq.jellyfish.data.local.ServerStorage
import com.lowiq.jellyfish.data.remote.DownloadClient
import com.lowiq.jellyfish.data.remote.DownloadResult
import com.lowiq.jellyfish.data.remote.JellyfinDataSource
import com.lowiq.jellyfish.domain.model.Download
import com.lowiq.jellyfish.domain.model.DownloadStatus
import com.lowiq.jellyfish.domain.model.QualityOption
import com.lowiq.jellyfish.domain.repository.DownloadRepository
import com.lowiq.jellyfish.domain.repository.StorageInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

class DownloadRepositoryImpl(
    private val downloadStorage: DownloadStorage,
    private val downloadSettings: DownloadSettingsStorage,
    private val serverStorage: ServerStorage,
    private val fileManager: FileManager,
    private val jellyfinDataSource: JellyfinDataSource,
    private val downloadClient: DownloadClient
) : DownloadRepository {

    override fun getAllDownloads(): Flow<List<Download>> = downloadStorage.getAllDownloads()

    override fun getActiveDownloads(): Flow<List<Download>> = downloadStorage.getAllDownloads().map { downloads ->
        downloads.filter { it.status == DownloadStatus.QUEUED || it.status == DownloadStatus.DOWNLOADING }
    }

    override fun getCompletedDownloads(): Flow<List<Download>> = downloadStorage.getAllDownloads().map { downloads ->
        downloads.filter { it.status == DownloadStatus.COMPLETED }
    }

    override suspend fun getDownloadByItemId(itemId: String): Download? {
        return downloadStorage.getAllDownloads().first().find { it.itemId == itemId }
    }

    override suspend fun getAvailableQualities(serverId: String, itemId: String): Result<List<QualityOption>> {
        val server = serverStorage.getServer(serverId) ?: return Result.failure(Exception("Server not found"))
        val token = serverStorage.getToken(serverId) ?: return Result.failure(Exception("Not authenticated"))

        return jellyfinDataSource.getMediaSources(server.url, token, server.userId ?: "", itemId)
            .map { sources ->
                val original = sources.firstOrNull()
                val originalBitrate = original?.bitrate ?: 20_000_000

                listOf(
                    QualityOption("original", "Original", originalBitrate, original?.size ?: 0),
                    QualityOption("1080p", "1080p", 8_000_000, estimateSize(8_000_000, original?.size, originalBitrate)),
                    QualityOption("720p", "720p", 4_000_000, estimateSize(4_000_000, original?.size, originalBitrate)),
                    QualityOption("480p", "480p", 1_500_000, estimateSize(1_500_000, original?.size, originalBitrate))
                ).filter { it.bitrate <= originalBitrate }
            }
    }

    private fun estimateSize(targetBitrate: Int, originalSize: Long?, originalBitrate: Int): Long {
        if (originalSize == null || originalSize == 0L || originalBitrate == 0) return 0
        return (originalSize * targetBitrate.toLong() / originalBitrate)
    }

    override suspend fun startDownload(
        serverId: String,
        itemId: String,
        title: String,
        subtitle: String?,
        imageUrl: String?,
        quality: QualityOption
    ): Result<Download> {
        val server = serverStorage.getServer(serverId) ?: return Result.failure(Exception("Server not found"))
        val token = serverStorage.getToken(serverId) ?: return Result.failure(Exception("Not authenticated"))

        val downloadId = UUID.randomUUID().toString()
        val download = Download(
            id = downloadId,
            itemId = itemId,
            serverId = serverId,
            title = title,
            subtitle = subtitle,
            imageUrl = imageUrl,
            quality = quality.label,
            bitrate = quality.bitrate,
            status = DownloadStatus.QUEUED,
            totalBytes = quality.estimatedSizeBytes,
            createdAt = System.currentTimeMillis()
        )

        downloadStorage.saveDownload(download)
        return Result.success(download)
    }

    override suspend fun pauseDownload(downloadId: String) {
        downloadStorage.updateStatus(downloadId, DownloadStatus.PAUSED)
    }

    override suspend fun resumeDownload(downloadId: String) {
        downloadStorage.updateStatus(downloadId, DownloadStatus.QUEUED)
    }

    override suspend fun cancelDownload(downloadId: String) {
        val download = downloadStorage.getAllDownloads().first().find { it.id == downloadId }
        download?.filePath?.let { fileManager.deleteFile(it) }
        downloadStorage.removeDownload(downloadId)
    }

    override suspend fun deleteDownload(downloadId: String) {
        val download = downloadStorage.getAllDownloads().first().find { it.id == downloadId }
        download?.filePath?.let { fileManager.deleteFile(it) }
        downloadStorage.removeDownload(downloadId)
    }

    override suspend fun getStorageInfo(): StorageInfo {
        val limitMb = downloadSettings.storageLimitMb.first()
        val autoCleanup = downloadSettings.autoCleanup.first()

        return StorageInfo(
            usedBytes = fileManager.getUsedSpaceBytes(),
            availableBytes = fileManager.getAvailableSpaceBytes(),
            limitBytes = if (autoCleanup) limitMb.toLong() * 1024 * 1024 else null
        )
    }
}
```

**Step 2: Verify file compiles**

Run: `./gradlew :composeApp:compileKotlinJvm`
Expected: BUILD SUCCESSFUL

**Step 3: Commit**

```bash
git add composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/data/repository/DownloadRepositoryImpl.kt
git commit -m "feat(download): implement download repository"
```

---

## Task 9: Download Manager

**Files:**
- Create: `composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/domain/download/DownloadManager.kt`

**Step 1: Create download manager for queue management**

```kotlin
package com.lowiq.jellyfish.domain.download

import com.lowiq.jellyfish.data.local.DownloadSettingsStorage
import com.lowiq.jellyfish.data.local.DownloadStorage
import com.lowiq.jellyfish.data.local.FileManager
import com.lowiq.jellyfish.data.local.ServerStorage
import com.lowiq.jellyfish.data.remote.DownloadClient
import com.lowiq.jellyfish.data.remote.DownloadResult
import com.lowiq.jellyfish.data.remote.JellyfinDataSource
import com.lowiq.jellyfish.domain.model.Download
import com.lowiq.jellyfish.domain.model.DownloadStatus
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.io.File
import java.io.FileOutputStream

class DownloadManager(
    private val downloadStorage: DownloadStorage,
    private val downloadSettings: DownloadSettingsStorage,
    private val serverStorage: ServerStorage,
    private val fileManager: FileManager,
    private val jellyfinDataSource: JellyfinDataSource,
    private val downloadClient: DownloadClient
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val activeJobs = mutableMapOf<String, Job>()

    private val _downloadEvents = MutableSharedFlow<DownloadEvent>()
    val downloadEvents: SharedFlow<DownloadEvent> = _downloadEvents.asSharedFlow()

    init {
        observeDownloads()
    }

    private fun observeDownloads() {
        scope.launch {
            downloadStorage.getAllDownloads().collect { downloads ->
                val maxParallel = downloadSettings.parallelDownloads.first()
                val activeCount = activeJobs.count { it.value.isActive }
                val queued = downloads.filter { it.status == DownloadStatus.QUEUED }

                queued.take(maxParallel - activeCount).forEach { download ->
                    if (activeJobs[download.id]?.isActive != true) {
                        startDownloadJob(download)
                    }
                }
            }
        }
    }

    private fun startDownloadJob(download: Download) {
        val job = scope.launch {
            try {
                downloadStorage.updateStatus(download.id, DownloadStatus.DOWNLOADING)
                _downloadEvents.emit(DownloadEvent.Started(download.id, download.title))

                val server = serverStorage.getServer(download.serverId) ?: throw Exception("Server not found")
                val token = serverStorage.getToken(download.serverId) ?: throw Exception("Not authenticated")

                val url = jellyfinDataSource.getTranscodingDownloadUrl(
                    serverUrl = server.url,
                    token = token,
                    itemId = download.itemId,
                    bitrate = download.bitrate
                )

                val fileName = "${download.itemId}_${download.quality}.mp4"
                val filePath = "${fileManager.getDownloadsDirectory()}/$fileName"
                val file = File(filePath)
                val outputStream = FileOutputStream(file)

                downloadClient.downloadFile(url, filePath) { bytes ->
                    outputStream.write(bytes)
                }.collect { result ->
                    when (result) {
                        is DownloadResult.Progress -> {
                            downloadStorage.updateProgress(
                                download.id,
                                result.progress.progress,
                                result.progress.bytesDownloaded
                            )
                            _downloadEvents.emit(DownloadEvent.Progress(
                                download.id,
                                result.progress.progress,
                                result.progress.bytesDownloaded,
                                result.progress.totalBytes
                            ))
                        }
                        is DownloadResult.Success -> {
                            outputStream.close()
                            downloadStorage.updateStatus(download.id, DownloadStatus.COMPLETED, filePath)
                            _downloadEvents.emit(DownloadEvent.Completed(download.id, download.title))
                        }
                        is DownloadResult.Error -> {
                            outputStream.close()
                            file.delete()
                            downloadStorage.updateStatus(download.id, DownloadStatus.FAILED, errorMessage = result.message)
                            _downloadEvents.emit(DownloadEvent.Failed(download.id, download.title, result.message))
                        }
                    }
                }
            } catch (e: CancellationException) {
                downloadStorage.updateStatus(download.id, DownloadStatus.PAUSED)
            } catch (e: Exception) {
                downloadStorage.updateStatus(download.id, DownloadStatus.FAILED, errorMessage = e.message)
                _downloadEvents.emit(DownloadEvent.Failed(download.id, download.title, e.message ?: "Unknown error"))
            }
        }
        activeJobs[download.id] = job
    }

    fun pauseDownload(downloadId: String) {
        activeJobs[downloadId]?.cancel()
        activeJobs.remove(downloadId)
    }

    fun cancelAll() {
        activeJobs.values.forEach { it.cancel() }
        activeJobs.clear()
    }
}

sealed class DownloadEvent {
    data class Started(val downloadId: String, val title: String) : DownloadEvent()
    data class Progress(val downloadId: String, val progress: Float, val bytesDownloaded: Long, val totalBytes: Long) : DownloadEvent()
    data class Completed(val downloadId: String, val title: String) : DownloadEvent()
    data class Failed(val downloadId: String, val title: String, val error: String) : DownloadEvent()
}
```

**Step 2: Verify file compiles**

Run: `./gradlew :composeApp:compileKotlinJvm`
Expected: BUILD SUCCESSFUL

**Step 3: Commit**

```bash
git add composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/domain/download/DownloadManager.kt
git commit -m "feat(download): add download manager for queue orchestration"
```

---

## Task 10: Register Download Components in Koin

**Files:**
- Modify: `composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/di/AppModule.kt`
- Modify: `composeApp/src/androidMain/kotlin/com/lowiq/jellyfish/di/PlatformModule.kt`
- Modify: `composeApp/src/jvmMain/kotlin/com/lowiq/jellyfish/di/PlatformModule.kt`

**Step 1: Add to commonMain AppModule**

Add imports:

```kotlin
import com.lowiq.jellyfish.data.local.DownloadSettingsStorage
import com.lowiq.jellyfish.data.local.DownloadStorage
import com.lowiq.jellyfish.data.remote.DownloadClient
import com.lowiq.jellyfish.data.repository.DownloadRepositoryImpl
import com.lowiq.jellyfish.domain.download.DownloadManager
import com.lowiq.jellyfish.domain.repository.DownloadRepository
import io.ktor.client.*
```

Add to `appModule`:

```kotlin
single { DownloadStorage(get()) }
single { DownloadSettingsStorage(get()) }
```

Add to `dataModule`:

```kotlin
single { DownloadClient(get()) }
single<DownloadRepository> { DownloadRepositoryImpl(get(), get(), get(), get(), get(), get()) }
single { DownloadManager(get(), get(), get(), get(), get(), get()) }
```

**Step 2: Add HttpClient to Android platformModule**

```kotlin
single { HttpClient(OkHttp) }
```

**Step 3: Add HttpClient to JVM platformModule**

```kotlin
single { HttpClient(OkHttp) }
```

**Step 4: Verify compiles**

Run: `./gradlew :composeApp:compileKotlinJvm :composeApp:compileKotlinAndroid`
Expected: BUILD SUCCESSFUL

**Step 5: Commit**

```bash
git add composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/di/AppModule.kt
git add composeApp/src/androidMain/kotlin/com/lowiq/jellyfish/di/PlatformModule.kt
git add composeApp/src/jvmMain/kotlin/com/lowiq/jellyfish/di/PlatformModule.kt
git commit -m "feat(download): register download components in Koin DI"
```

---

## Task 11: Quality Selection Dialog

**Files:**
- Create: `composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/presentation/components/QualitySelectionDialog.kt`

**Step 1: Create quality selection dialog**

```kotlin
package com.lowiq.jellyfish.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.lowiq.jellyfish.domain.model.QualityOption
import com.lowiq.jellyfish.presentation.theme.LocalJellyFishColors

@Composable
fun QualitySelectionDialog(
    qualities: List<QualityOption>,
    recommendedQuality: String = "1080p",
    onDismiss: () -> Unit,
    onConfirm: (QualityOption, Boolean) -> Unit
) {
    val colors = LocalJellyFishColors.current
    var selectedQuality by remember { mutableStateOf(qualities.find { it.label == recommendedQuality } ?: qualities.firstOrNull()) }
    var dontAskAgain by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Choisir la qualité") },
        text = {
            Column {
                qualities.forEach { quality ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = selectedQuality == quality,
                                onClick = { selectedQuality = quality },
                                role = Role.RadioButton
                            )
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedQuality == quality,
                            onClick = null
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(quality.label)
                                if (quality.label == recommendedQuality) {
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        "(Recommandé)",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = colors.primary
                                    )
                                }
                            }
                            if (quality.estimatedSizeBytes > 0) {
                                Text(
                                    "~${formatBytes(quality.estimatedSizeBytes)}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = colors.mutedForeground
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { dontAskAgain = !dontAskAgain },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = dontAskAgain,
                        onCheckedChange = { dontAskAgain = it }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Ne plus demander (utiliser ${selectedQuality?.label ?: "cette qualité"})",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    selectedQuality?.let { onConfirm(it, dontAskAgain) }
                },
                enabled = selectedQuality != null
            ) {
                Text("Télécharger")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Annuler")
            }
        }
    )
}

private fun formatBytes(bytes: Long): String {
    return when {
        bytes >= 1_000_000_000 -> String.format("%.1f Go", bytes / 1_000_000_000.0)
        bytes >= 1_000_000 -> String.format("%.0f Mo", bytes / 1_000_000.0)
        else -> String.format("%.0f Ko", bytes / 1_000.0)
    }
}
```

**Step 2: Verify file compiles**

Run: `./gradlew :composeApp:compileKotlinJvm`
Expected: BUILD SUCCESSFUL

**Step 3: Commit**

```bash
git add composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/presentation/components/QualitySelectionDialog.kt
git commit -m "feat(download): add quality selection dialog"
```

---

## Task 12: Downloads Screen

**Files:**
- Create: `composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/presentation/screens/downloads/DownloadsScreen.kt`
- Create: `composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/presentation/screens/downloads/DownloadsScreenModel.kt`

**Step 1: Create DownloadsScreenModel**

```kotlin
package com.lowiq.jellyfish.presentation.screens.downloads

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.lowiq.jellyfish.domain.download.DownloadEvent
import com.lowiq.jellyfish.domain.download.DownloadManager
import com.lowiq.jellyfish.domain.model.Download
import com.lowiq.jellyfish.domain.model.DownloadStatus
import com.lowiq.jellyfish.domain.repository.DownloadRepository
import com.lowiq.jellyfish.domain.repository.StorageInfo
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class DownloadsState(
    val activeDownloads: List<Download> = emptyList(),
    val completedDownloads: List<Download> = emptyList(),
    val storageInfo: StorageInfo? = null,
    val isLoading: Boolean = true
)

class DownloadsScreenModel(
    private val downloadRepository: DownloadRepository,
    private val downloadManager: DownloadManager
) : ScreenModel {

    private val _state = MutableStateFlow(DownloadsState())
    val state = _state.asStateFlow()

    init {
        loadDownloads()
        observeEvents()
    }

    private fun loadDownloads() {
        screenModelScope.launch {
            combine(
                downloadRepository.getActiveDownloads(),
                downloadRepository.getCompletedDownloads()
            ) { active, completed ->
                _state.update {
                    it.copy(
                        activeDownloads = active,
                        completedDownloads = completed,
                        isLoading = false
                    )
                }
            }.collect()
        }

        screenModelScope.launch {
            val info = downloadRepository.getStorageInfo()
            _state.update { it.copy(storageInfo = info) }
        }
    }

    private fun observeEvents() {
        screenModelScope.launch {
            downloadManager.downloadEvents.collect { event ->
                when (event) {
                    is DownloadEvent.Completed -> refreshStorageInfo()
                    else -> {}
                }
            }
        }
    }

    private fun refreshStorageInfo() {
        screenModelScope.launch {
            val info = downloadRepository.getStorageInfo()
            _state.update { it.copy(storageInfo = info) }
        }
    }

    fun pauseDownload(downloadId: String) {
        screenModelScope.launch {
            downloadManager.pauseDownload(downloadId)
            downloadRepository.pauseDownload(downloadId)
        }
    }

    fun resumeDownload(downloadId: String) {
        screenModelScope.launch {
            downloadRepository.resumeDownload(downloadId)
        }
    }

    fun cancelDownload(downloadId: String) {
        screenModelScope.launch {
            downloadManager.pauseDownload(downloadId)
            downloadRepository.cancelDownload(downloadId)
        }
    }

    fun deleteDownload(downloadId: String) {
        screenModelScope.launch {
            downloadRepository.deleteDownload(downloadId)
            refreshStorageInfo()
        }
    }
}
```

**Step 2: Create DownloadsScreen**

```kotlin
package com.lowiq.jellyfish.presentation.screens.downloads

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil3.compose.AsyncImage
import com.lowiq.jellyfish.domain.model.Download
import com.lowiq.jellyfish.domain.model.DownloadStatus
import com.lowiq.jellyfish.presentation.theme.LocalJellyFishColors

class DownloadsScreen : Screen {

    @Composable
    override fun Content() {
        val colors = LocalJellyFishColors.current
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = koinScreenModel<DownloadsScreenModel>()
        val state by screenModel.state.collectAsState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colors.background)
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { navigator.pop() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                    Text(
                        "Téléchargements",
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
                state.storageInfo?.let { info ->
                    Text(
                        "Espace: ${formatBytes(info.usedBytes)}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = colors.mutedForeground
                    )
                }
            }

            if (state.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    if (state.activeDownloads.isNotEmpty()) {
                        item {
                            Text(
                                "EN COURS (${state.activeDownloads.size})",
                                style = MaterialTheme.typography.titleSmall,
                                color = colors.mutedForeground
                            )
                        }
                        items(state.activeDownloads, key = { it.id }) { download ->
                            ActiveDownloadItem(
                                download = download,
                                onPause = { screenModel.pauseDownload(download.id) },
                                onResume = { screenModel.resumeDownload(download.id) },
                                onCancel = { screenModel.cancelDownload(download.id) }
                            )
                        }
                    }

                    if (state.completedDownloads.isNotEmpty()) {
                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                "TÉLÉCHARGÉS (${state.completedDownloads.size})",
                                style = MaterialTheme.typography.titleSmall,
                                color = colors.mutedForeground
                            )
                        }
                        items(state.completedDownloads, key = { it.id }) { download ->
                            CompletedDownloadItem(
                                download = download,
                                onPlay = { /* TODO: Navigate to player with local file */ },
                                onDelete = { screenModel.deleteDownload(download.id) }
                            )
                        }
                    }

                    if (state.activeDownloads.isEmpty() && state.completedDownloads.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(32.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(
                                        Icons.Default.Download,
                                        contentDescription = null,
                                        modifier = Modifier.size(64.dp),
                                        tint = colors.mutedForeground
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Text(
                                        "Aucun téléchargement",
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = colors.mutedForeground
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ActiveDownloadItem(
    download: Download,
    onPause: () -> Unit,
    onResume: () -> Unit,
    onCancel: () -> Unit
) {
    val colors = LocalJellyFishColors.current

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = colors.card)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = download.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(60.dp, 90.dp)
                    .clip(RoundedCornerShape(4.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    download.title,
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                download.subtitle?.let {
                    Text(
                        it,
                        style = MaterialTheme.typography.bodySmall,
                        color = colors.mutedForeground
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "${download.quality} • ${formatBytes(download.totalBytes)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = colors.mutedForeground
                )
                Spacer(modifier = Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = { download.progress },
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    "${(download.progress * 100).toInt()}%",
                    style = MaterialTheme.typography.bodySmall,
                    color = colors.mutedForeground
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Column {
                IconButton(
                    onClick = if (download.status == DownloadStatus.PAUSED) onResume else onPause
                ) {
                    Icon(
                        if (download.status == DownloadStatus.PAUSED) Icons.Default.PlayArrow else Icons.Default.Pause,
                        contentDescription = null
                    )
                }
                IconButton(onClick = onCancel) {
                    Icon(Icons.Default.Close, contentDescription = "Cancel")
                }
            }
        }
    }
}

@Composable
private fun CompletedDownloadItem(
    download: Download,
    onPlay: () -> Unit,
    onDelete: () -> Unit
) {
    val colors = LocalJellyFishColors.current

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = colors.card)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = download.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(60.dp, 90.dp)
                    .clip(RoundedCornerShape(4.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    download.title,
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                download.subtitle?.let {
                    Text(
                        it,
                        style = MaterialTheme.typography.bodySmall,
                        color = colors.mutedForeground
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "${download.quality} • ${formatBytes(download.downloadedBytes)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = colors.mutedForeground
                )
            }

            IconButton(onClick = onPlay) {
                Icon(Icons.Default.PlayArrow, contentDescription = "Play")
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = colors.destructive)
            }
        }
    }
}

private fun formatBytes(bytes: Long): String {
    return when {
        bytes >= 1_000_000_000 -> String.format("%.1f Go", bytes / 1_000_000_000.0)
        bytes >= 1_000_000 -> String.format("%.0f Mo", bytes / 1_000_000.0)
        else -> String.format("%.0f Ko", bytes / 1_000.0)
    }
}
```

**Step 3: Verify files compile**

Run: `./gradlew :composeApp:compileKotlinJvm`
Expected: BUILD SUCCESSFUL

**Step 4: Commit**

```bash
git add composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/presentation/screens/downloads/
git commit -m "feat(download): add downloads screen with progress display"
```

---

## Task 13: Register DownloadsScreenModel in Koin

**Files:**
- Modify: `composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/di/AppModule.kt`

**Step 1: Add import and factory**

Add import:

```kotlin
import com.lowiq.jellyfish.presentation.screens.downloads.DownloadsScreenModel
```

Add to `presentationModule`:

```kotlin
factory { DownloadsScreenModel(get(), get()) }
```

**Step 2: Verify compiles**

Run: `./gradlew :composeApp:compileKotlinJvm`
Expected: BUILD SUCCESSFUL

**Step 3: Commit**

```bash
git add composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/di/AppModule.kt
git commit -m "feat(download): register DownloadsScreenModel in Koin"
```

---

## Task 14: Wire Download Button in MovieDetailScreenModel

**Files:**
- Modify: `composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/presentation/screens/detail/MovieDetailScreenModel.kt`
- Modify: `composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/presentation/screens/detail/MovieDetailScreen.kt`

**Step 1: Update MovieDetailScreenModel**

Add to state:

```kotlin
data class MovieDetailState(
    // ... existing fields ...
    val showQualityDialog: Boolean = false,
    val availableQualities: List<QualityOption> = emptyList(),
    val isLoadingQualities: Boolean = false,
    val posterUrl: String? = null
)
```

Add events:

```kotlin
sealed class MovieDetailEvent {
    // ... existing events ...
    object NavigateToDownloads : MovieDetailEvent()
}
```

Add repository and update constructor:

```kotlin
class MovieDetailScreenModel(
    private val itemId: String,
    private val serverRepository: ServerRepository,
    private val mediaRepository: MediaRepository,
    private val downloadRepository: DownloadRepository,
    private val downloadSettings: DownloadSettingsStorage
) : ScreenModel {
```

Implement onDownload:

```kotlin
fun onDownload() {
    screenModelScope.launch {
        val alwaysAsk = downloadSettings.alwaysAskQuality.first()

        _state.update { it.copy(isLoadingQualities = true) }

        val serverId = currentServerId ?: return@launch
        downloadRepository.getAvailableQualities(serverId, itemId)
            .onSuccess { qualities ->
                if (alwaysAsk || qualities.isEmpty()) {
                    _state.update {
                        it.copy(
                            showQualityDialog = true,
                            availableQualities = qualities,
                            isLoadingQualities = false
                        )
                    }
                } else {
                    val defaultQuality = downloadSettings.defaultQuality.first()
                    val quality = qualities.find { it.label == defaultQuality } ?: qualities.first()
                    startDownload(quality)
                }
            }
            .onFailure {
                _state.update { it.copy(isLoadingQualities = false) }
            }
    }
}

fun onQualitySelected(quality: QualityOption, dontAskAgain: Boolean) {
    screenModelScope.launch {
        if (dontAskAgain) {
            downloadSettings.setDefaultQuality(quality.label)
            downloadSettings.setAlwaysAskQuality(false)
        }
        startDownload(quality)
        _state.update { it.copy(showQualityDialog = false) }
    }
}

fun dismissQualityDialog() {
    _state.update { it.copy(showQualityDialog = false) }
}

private suspend fun startDownload(quality: QualityOption) {
    val serverId = currentServerId ?: return
    val state = _state.value

    downloadRepository.startDownload(
        serverId = serverId,
        itemId = itemId,
        title = state.title,
        subtitle = null,
        imageUrl = state.posterUrl,
        quality = quality
    )

    _events.emit(MovieDetailEvent.NavigateToDownloads)
}
```

**Step 2: Update MovieDetailScreen to show dialog**

Add to Content():

```kotlin
if (state.showQualityDialog && state.availableQualities.isNotEmpty()) {
    QualitySelectionDialog(
        qualities = state.availableQualities,
        onDismiss = { screenModel.dismissQualityDialog() },
        onConfirm = { quality, dontAsk -> screenModel.onQualitySelected(quality, dontAsk) }
    )
}
```

Handle event:

```kotlin
is MovieDetailEvent.NavigateToDownloads -> {
    navigator.push(DownloadsScreen())
}
```

**Step 3: Update Koin factory**

Update MovieDetailScreenModel factory in AppModule:

```kotlin
factory { (itemId: String) -> MovieDetailScreenModel(itemId, get(), get(), get(), get()) }
```

**Step 4: Verify compiles**

Run: `./gradlew :composeApp:compileKotlinJvm`
Expected: BUILD SUCCESSFUL

**Step 5: Commit**

```bash
git add composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/presentation/screens/detail/MovieDetailScreenModel.kt
git add composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/presentation/screens/detail/MovieDetailScreen.kt
git add composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/di/AppModule.kt
git commit -m "feat(download): wire download button to quality dialog"
```

---

## Task 15: Add Navigation to Downloads from NavigationRail

**Files:**
- Modify: `composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/presentation/screens/home/HomeScreen.kt`
- Modify: `composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/presentation/screens/home/HomeScreenModel.kt`

**Step 1: Update HomeScreenModel to emit navigation event**

Add event:

```kotlin
sealed class HomeEvent {
    // ... existing ...
    object NavigateToDownloads : HomeEvent()
}
```

Update onNavigationItemSelected:

```kotlin
fun onNavigationItemSelected(index: Int) {
    _state.update { it.copy(selectedNavIndex = index) }

    screenModelScope.launch {
        when (index) {
            3 -> _events.emit(HomeEvent.NavigateToDownloads)
            // Handle other navigation items as needed
        }
    }
}
```

**Step 2: Handle event in HomeScreen**

Add import:

```kotlin
import com.lowiq.jellyfish.presentation.screens.downloads.DownloadsScreen
```

Add event handling in LaunchedEffect:

```kotlin
is HomeEvent.NavigateToDownloads -> {
    navigator.push(DownloadsScreen())
}
```

**Step 3: Verify compiles**

Run: `./gradlew :composeApp:compileKotlinJvm`
Expected: BUILD SUCCESSFUL

**Step 4: Commit**

```bash
git add composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/presentation/screens/home/HomeScreen.kt
git add composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/presentation/screens/home/HomeScreenModel.kt
git commit -m "feat(download): add navigation to downloads from nav rail"
```

---

## Task 16: Pending Playback Sync Storage

**Files:**
- Create: `composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/data/local/PlaybackSyncStorage.kt`

**Step 1: Create pending sync storage**

```kotlin
package com.lowiq.jellyfish.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.lowiq.jellyfish.domain.model.PendingPlaybackSync
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlaybackSyncStorage(private val dataStore: DataStore<Preferences>) {

    private val pendingSyncsKey = stringPreferencesKey("pending_playback_syncs")

    fun getPendingSyncs(): Flow<List<PendingPlaybackSync>> {
        return dataStore.data.map { prefs ->
            val data = prefs[pendingSyncsKey] ?: return@map emptyList()
            parseSyncs(data)
        }
    }

    suspend fun addPendingSync(sync: PendingPlaybackSync) {
        dataStore.edit { prefs ->
            val existing = prefs[pendingSyncsKey]?.let { parseSyncs(it) } ?: emptyList()
            // Replace existing sync for same item
            val updated = existing.filter { it.itemId != sync.itemId } + sync
            prefs[pendingSyncsKey] = serializeSyncs(updated)
        }
    }

    suspend fun removePendingSync(syncId: String) {
        dataStore.edit { prefs ->
            val existing = prefs[pendingSyncsKey]?.let { parseSyncs(it) } ?: emptyList()
            val updated = existing.filter { it.id != syncId }
            prefs[pendingSyncsKey] = serializeSyncs(updated)
        }
    }

    suspend fun clearAllSyncs() {
        dataStore.edit { prefs ->
            prefs.remove(pendingSyncsKey)
        }
    }

    private fun serializeSyncs(syncs: List<PendingPlaybackSync>): String {
        return syncs.joinToString("\n") { s ->
            "${s.id}||${s.itemId}||${s.serverId}||${s.positionTicks}||${s.playedPercentage}||${s.timestamp}"
        }
    }

    private fun parseSyncs(data: String): List<PendingPlaybackSync> {
        if (data.isBlank()) return emptyList()
        return data.split("\n").mapNotNull { entry ->
            val parts = entry.split("||")
            if (parts.size >= 6) {
                PendingPlaybackSync(
                    id = parts[0],
                    itemId = parts[1],
                    serverId = parts[2],
                    positionTicks = parts[3].toLongOrNull() ?: 0,
                    playedPercentage = parts[4].toFloatOrNull() ?: 0f,
                    timestamp = parts[5].toLongOrNull() ?: 0
                )
            } else null
        }
    }
}
```

**Step 2: Register in Koin**

Add to appModule:

```kotlin
single { PlaybackSyncStorage(get()) }
```

**Step 3: Verify compiles**

Run: `./gradlew :composeApp:compileKotlinJvm`
Expected: BUILD SUCCESSFUL

**Step 4: Commit**

```bash
git add composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/data/local/PlaybackSyncStorage.kt
git add composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/di/AppModule.kt
git commit -m "feat(download): add pending playback sync storage"
```

---

## Task 17: Playback Sync Service

**Files:**
- Create: `composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/domain/sync/PlaybackSyncService.kt`

**Step 1: Create sync service**

```kotlin
package com.lowiq.jellyfish.domain.sync

import com.lowiq.jellyfish.data.local.PlaybackSyncStorage
import com.lowiq.jellyfish.data.local.ServerStorage
import com.lowiq.jellyfish.data.remote.JellyfinDataSource
import com.lowiq.jellyfish.domain.model.PendingPlaybackSync
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import java.util.UUID

class PlaybackSyncService(
    private val playbackSyncStorage: PlaybackSyncStorage,
    private val serverStorage: ServerStorage,
    private val jellyfinDataSource: JellyfinDataSource
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var syncJob: Job? = null

    fun savePlaybackProgress(
        itemId: String,
        serverId: String,
        positionTicks: Long,
        playedPercentage: Float
    ) {
        scope.launch {
            val sync = PendingPlaybackSync(
                id = UUID.randomUUID().toString(),
                itemId = itemId,
                serverId = serverId,
                positionTicks = positionTicks,
                playedPercentage = playedPercentage,
                timestamp = System.currentTimeMillis()
            )
            playbackSyncStorage.addPendingSync(sync)
        }
    }

    fun startPeriodicSync() {
        syncJob?.cancel()
        syncJob = scope.launch {
            while (isActive) {
                syncPendingProgress()
                delay(30_000) // Check every 30 seconds
            }
        }
    }

    fun stopPeriodicSync() {
        syncJob?.cancel()
        syncJob = null
    }

    suspend fun syncPendingProgress() {
        val pendingSyncs = playbackSyncStorage.getPendingSyncs().first()

        for (sync in pendingSyncs) {
            try {
                val server = serverStorage.getServer(sync.serverId) ?: continue
                val token = serverStorage.getToken(sync.serverId) ?: continue

                jellyfinDataSource.reportPlaybackProgress(
                    serverUrl = server.url,
                    token = token,
                    userId = server.userId ?: continue,
                    itemId = sync.itemId,
                    positionTicks = sync.positionTicks,
                    isPaused = true
                ).onSuccess {
                    playbackSyncStorage.removePendingSync(sync.id)
                }
            } catch (e: Exception) {
                // Will retry next time
            }
        }
    }

    fun syncNow() {
        scope.launch {
            syncPendingProgress()
        }
    }
}
```

**Step 2: Add reportPlaybackProgress to JellyfinDataSource if not exists**

Check if it exists, if not add to interface:

```kotlin
suspend fun reportPlaybackProgress(
    serverUrl: String,
    token: String,
    userId: String,
    itemId: String,
    positionTicks: Long,
    isPaused: Boolean
): Result<Unit>
```

**Step 3: Register in Koin**

Add to dataModule:

```kotlin
single { PlaybackSyncService(get(), get(), get()) }
```

**Step 4: Verify compiles**

Run: `./gradlew :composeApp:compileKotlinJvm`
Expected: BUILD SUCCESSFUL

**Step 5: Commit**

```bash
git add composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/domain/sync/PlaybackSyncService.kt
git add composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/di/AppModule.kt
git commit -m "feat(download): add playback sync service for offline progress"
```

---

## Task 18: Build and Integration Test

**Step 1: Build for JVM**

Run: `./gradlew :composeApp:run`
Expected: App launches, Downloads visible in nav rail

**Step 2: Build for Android**

Run: `./gradlew :composeApp:assembleDebug`
Expected: BUILD SUCCESSFUL

**Step 3: Manual test checklist**

- [ ] Navigate to Downloads from nav rail
- [ ] Open movie detail and click Download
- [ ] Quality dialog appears with options
- [ ] Select quality and confirm
- [ ] Navigate to Downloads screen
- [ ] Download appears in active list with progress

**Step 4: Commit any fixes**

```bash
git add -A
git commit -m "fix(download): integration fixes"
```

---

## Summary

This plan implements the download feature in 18 tasks:

1. **Tasks 1-3:** Domain models and storage
2. **Tasks 4-5:** Platform file operations and repository interface
3. **Tasks 6-9:** API integration, download client, and manager
4. **Tasks 10-13:** DI, quality dialog, and downloads screen
5. **Tasks 14-15:** Wire existing UI (download button, nav rail)
6. **Tasks 16-17:** Offline playback sync
7. **Task 18:** Build and integration test

Each task is self-contained with clear file paths, code, and verification steps.
