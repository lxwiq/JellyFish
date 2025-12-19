# Offline Playback Implementation Plan

> **For Claude:** REQUIRED SUB-SKILL: Use superpowers:executing-plans to implement this plan task-by-task.

**Goal:** Enable playback of downloaded media files with progress sync to Jellyfin server.

**Architecture:** Modify existing `VideoPlayerScreen` to support both streaming and local file playback. Use existing `PlaybackSyncService` for progress synchronization.

**Tech Stack:** Kotlin, Compose Multiplatform, ExoPlayer (Android), Koin DI, DataStore

---

## Task 1: Add playback tracking fields to Download model

**Files:**
- Modify: `composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/domain/model/Download.kt`

**Step 1: Add new fields to Download data class**

```kotlin
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
    val errorMessage: String? = null,
    // NEW: offline playback tracking
    val lastPlayedPositionMs: Long = 0,
    val lastPlayedAt: Long? = null
)
```

**Step 2: Verify build compiles**

Run: `./gradlew :composeApp:compileKotlinJvm`
Expected: BUILD SUCCESSFUL (will fail later when DownloadStorage serialization is updated)

**Step 3: Commit**

```bash
git add composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/domain/model/Download.kt
git commit -m "feat(download): add playback tracking fields to Download model"
```

---

## Task 2: Update DownloadStorage serialization

**Files:**
- Modify: `composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/data/local/DownloadStorage.kt`

**Step 1: Update serializeDownloads to include new fields**

Replace the `serializeDownloads` function:

```kotlin
private fun serializeDownloads(downloads: List<Download>): String {
    return downloads.joinToString("\n\n") { d ->
        listOf(
            d.id, d.itemId, d.serverId, d.title, d.subtitle.orEmpty(),
            d.imageUrl.orEmpty(), d.quality, d.bitrate.toString(), d.status.name,
            d.progress.toString(), d.totalBytes.toString(), d.downloadedBytes.toString(),
            d.filePath.orEmpty(), d.createdAt.toString(), d.completedAt?.toString().orEmpty(),
            d.errorMessage.orEmpty(),
            d.lastPlayedPositionMs.toString(), d.lastPlayedAt?.toString().orEmpty()
        ).joinToString("||")
    }
}
```

**Step 2: Update parseDownloads to read new fields**

Replace the `parseDownloads` function:

```kotlin
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
                errorMessage = parts.getOrNull(15)?.takeIf { it.isNotEmpty() },
                lastPlayedPositionMs = parts.getOrNull(16)?.toLongOrNull() ?: 0,
                lastPlayedAt = parts.getOrNull(17)?.toLongOrNull()
            )
        } else null
    }
}
```

**Step 3: Add updatePlaybackPosition method**

Add after the `updateStatus` function:

```kotlin
suspend fun updatePlaybackPosition(downloadId: String, positionMs: Long) {
    dataStore.edit { prefs ->
        val existing = prefs[downloadsKey]?.let { parseDownloads(it) } ?: emptyList()
        val now = currentTimeMillis()
        val updated = existing.map {
            if (it.id == downloadId) it.copy(
                lastPlayedPositionMs = positionMs,
                lastPlayedAt = now
            )
            else it
        }
        prefs[downloadsKey] = serializeDownloads(updated)
    }
}
```

**Step 4: Verify build compiles**

Run: `./gradlew :composeApp:compileKotlinJvm`
Expected: BUILD SUCCESSFUL

**Step 5: Commit**

```bash
git add composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/data/local/DownloadStorage.kt
git commit -m "feat(download): update serialization for playback tracking fields"
```

---

## Task 3: Add updatePlaybackPosition to DownloadRepository

**Files:**
- Modify: `composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/domain/repository/DownloadRepository.kt`
- Modify: `composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/data/repository/DownloadRepositoryImpl.kt`

**Step 1: Add method to interface**

Add to `DownloadRepository.kt` before `getStorageInfo()`:

```kotlin
suspend fun updatePlaybackPosition(downloadId: String, positionMs: Long)
```

**Step 2: Implement in DownloadRepositoryImpl**

Add to `DownloadRepositoryImpl.kt`:

```kotlin
override suspend fun updatePlaybackPosition(downloadId: String, positionMs: Long) {
    downloadStorage.updatePlaybackPosition(downloadId, positionMs)
}
```

**Step 3: Verify build compiles**

Run: `./gradlew :composeApp:compileKotlinJvm`
Expected: BUILD SUCCESSFUL

**Step 4: Commit**

```bash
git add composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/domain/repository/DownloadRepository.kt
git add composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/data/repository/DownloadRepositoryImpl.kt
git commit -m "feat(download): add updatePlaybackPosition to repository"
```

---

## Task 4: Add isOfflineMode to VideoPlayerState

**Files:**
- Modify: `composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/presentation/screens/player/VideoPlayerState.kt`

**Step 1: Add isOfflineMode field**

Add after `showNextEpisodeCard`:

```kotlin
data class VideoPlayerState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val title: String = "",
    val subtitle: String? = null,
    val playbackState: PlaybackState = PlaybackState.Idle,
    val audioTracks: List<AudioTrack> = emptyList(),
    val subtitleTracks: List<SubtitleTrack> = emptyList(),
    val qualityOptions: List<QualityOption> = emptyList(),
    val playbackSpeed: Float = 1f,
    val controlsVisible: Boolean = true,
    val showTrackSelector: Boolean = false,
    val showSettingsSheet: Boolean = false,
    val showResumeDialog: Boolean = false,
    val resumePositionMs: Long = 0,
    val nextEpisode: NextEpisodeInfo? = null,
    val showNextEpisodeCard: Boolean = false,
    val isOfflineMode: Boolean = false  // NEW
)
```

**Step 2: Verify build compiles**

Run: `./gradlew :composeApp:compileKotlinJvm`
Expected: BUILD SUCCESSFUL

**Step 3: Commit**

```bash
git add composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/presentation/screens/player/VideoPlayerState.kt
git commit -m "feat(player): add isOfflineMode to VideoPlayerState"
```

---

## Task 5: Add offline parameters to VideoPlayerScreen

**Files:**
- Modify: `composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/presentation/screens/player/VideoPlayerScreen.kt`

**Step 1: Add offline parameters to VideoPlayerScreen data class**

Update the data class:

```kotlin
data class VideoPlayerScreen(
    val itemId: String,
    val title: String,
    val subtitle: String? = null,
    val startPositionMs: Long = 0,
    // Offline playback support
    val offlineFilePath: String? = null,
    val downloadId: String? = null
) : Screen {
```

**Step 2: Update parametersOf call**

Update the koinScreenModel call:

```kotlin
val screenModel = koinScreenModel<VideoPlayerScreenModel> {
    parametersOf(itemId, title, subtitle, startPositionMs, offlineFilePath, downloadId)
}
```

**Step 3: Verify build compiles (will fail - ScreenModel not updated yet)**

Run: `./gradlew :composeApp:compileKotlinJvm`
Expected: FAIL (expected, ScreenModel signature mismatch)

**Step 4: Commit**

```bash
git add composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/presentation/screens/player/VideoPlayerScreen.kt
git commit -m "feat(player): add offline parameters to VideoPlayerScreen"
```

---

## Task 6: Update VideoPlayerScreenModel for offline playback

**Files:**
- Modify: `composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/presentation/screens/player/VideoPlayerScreenModel.kt`

**Step 1: Add new constructor parameters and dependencies**

Update the class signature and add imports:

```kotlin
import com.lowiq.jellyfish.domain.repository.DownloadRepository
import com.lowiq.jellyfish.domain.sync.PlaybackSyncService
import com.lowiq.jellyfish.util.currentTimeMillis

class VideoPlayerScreenModel(
    private val itemId: String,
    private val itemTitle: String,
    private val itemSubtitle: String?,
    private val startPositionMs: Long,
    private val offlineFilePath: String?,
    private val downloadId: String?,
    val videoPlayer: VideoPlayer,
    private val serverRepository: ServerRepository,
    private val mediaRepository: MediaRepository,
    private val downloadRepository: DownloadRepository,
    private val playbackSyncService: PlaybackSyncService
) : ScreenModel {
```

**Step 2: Add offline progress tracking job**

Add after `private var streamHeaders`:

```kotlin
private var offlineProgressJob: Job? = null
```

**Step 3: Update init block for offline mode**

Replace the init block:

```kotlin
init {
    videoPlayer.initialize()
    observePlaybackState()
    observeTracks()

    if (offlineFilePath != null) {
        setupOfflinePlayback()
    } else {
        loadStreamInfo()
    }
}
```

**Step 4: Add setupOfflinePlayback method**

Add after the `observeTracks` function:

```kotlin
private fun setupOfflinePlayback() {
    _state.update { it.copy(isLoading = false, isOfflineMode = true) }

    // Check if we should show resume dialog
    if (startPositionMs > 0) {
        _state.update { it.copy(showResumeDialog = true) }
    } else {
        startOfflinePlayback(0)
    }
}

private fun startOfflinePlayback(positionMs: Long) {
    val fileUrl = "file://$offlineFilePath"
    videoPlayer.play(fileUrl, emptyMap(), positionMs)
    startOfflineProgressTracking()
}

private fun startOfflineProgressTracking() {
    offlineProgressJob?.cancel()
    offlineProgressJob = screenModelScope.launch {
        while (isActive) {
            delay(10_000) // Save every 10 seconds
            saveOfflineProgress()
        }
    }
}

private fun saveOfflineProgress() {
    val state = _state.value.playbackState
    val positionMs = when (state) {
        is PlaybackState.Playing -> state.positionMs
        is PlaybackState.Paused -> state.positionMs
        else -> return
    }

    val durationMs = when (state) {
        is PlaybackState.Playing -> state.durationMs
        is PlaybackState.Paused -> state.durationMs
        else -> return
    }

    downloadId?.let { id ->
        screenModelScope.launch {
            downloadRepository.updatePlaybackPosition(id, positionMs)
        }
    }
}
```

**Step 5: Update onResumeFromPosition for offline mode**

Update the function:

```kotlin
fun onResumeFromPosition() {
    _state.update { it.copy(showResumeDialog = false) }
    if (_state.value.isOfflineMode) {
        startOfflinePlayback(startPositionMs)
    } else {
        streamUrl?.let { url ->
            startPlayback(url, streamHeaders, startPositionMs)
        }
    }
}
```

**Step 6: Update onStartFromBeginning for offline mode**

Update the function:

```kotlin
fun onStartFromBeginning() {
    _state.update { it.copy(showResumeDialog = false) }
    if (_state.value.isOfflineMode) {
        startOfflinePlayback(0)
    } else {
        streamUrl?.let { url ->
            startPlayback(url, streamHeaders, 0)
        }
    }
}
```

**Step 7: Update stopPlayback for offline mode**

Update the function to add pending sync:

```kotlin
private fun stopPlayback() {
    progressReportJob?.cancel()
    offlineProgressJob?.cancel()

    val state = _state.value.playbackState
    val positionMs = when (state) {
        is PlaybackState.Playing -> state.positionMs
        is PlaybackState.Paused -> state.positionMs
        else -> 0
    }

    val durationMs = when (state) {
        is PlaybackState.Playing -> state.durationMs
        is PlaybackState.Paused -> state.durationMs
        else -> 0
    }

    if (_state.value.isOfflineMode) {
        // Save final position for offline playback
        downloadId?.let { id ->
            screenModelScope.launch {
                downloadRepository.updatePlaybackPosition(id, positionMs)
            }
        }

        // Queue sync for when online
        if (durationMs > 0) {
            val playedPercentage = (positionMs.toFloat() / durationMs) * 100
            screenModelScope.launch {
                val servers = serverRepository.getActiveServer().filterNotNull().first()
                playbackSyncService.savePlaybackProgress(
                    itemId = itemId,
                    serverId = servers.id,
                    positionTicks = positionMs * 10_000,
                    playedPercentage = playedPercentage
                )
            }
        }
    } else {
        val serverId = currentServerId ?: return
        val sourceId = mediaSourceId ?: return
        val sessionId = playSessionId ?: return

        screenModelScope.launch {
            mediaRepository.reportPlaybackStopped(
                serverId,
                itemId,
                sourceId,
                positionMs * 10_000,
                sessionId
            )
        }
    }

    videoPlayer.stop()
}
```

**Step 8: Verify build compiles (will fail - Koin not updated)**

Run: `./gradlew :composeApp:compileKotlinJvm`
Expected: FAIL (expected, Koin signature mismatch)

**Step 9: Commit**

```bash
git add composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/presentation/screens/player/VideoPlayerScreenModel.kt
git commit -m "feat(player): implement offline playback support in VideoPlayerScreenModel"
```

---

## Task 7: Update Koin module for new dependencies

**Files:**
- Modify: `composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/di/AppModule.kt`

**Step 1: Update VideoPlayerScreenModel factory**

Replace the VideoPlayerScreenModel factory in `presentationModule`:

```kotlin
factory { (itemId: String, title: String, subtitle: String?, startPositionMs: Long, offlineFilePath: String?, downloadId: String?) ->
    VideoPlayerScreenModel(
        itemId, title, subtitle, startPositionMs, offlineFilePath, downloadId,
        get(), get(), get(), get(), get()
    )
}
```

**Step 2: Verify build compiles**

Run: `./gradlew :composeApp:compileKotlinJvm`
Expected: BUILD SUCCESSFUL

**Step 3: Commit**

```bash
git add composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/di/AppModule.kt
git commit -m "feat(di): update Koin for VideoPlayerScreenModel offline parameters"
```

---

## Task 8: Wire up DownloadsScreen play button

**Files:**
- Modify: `composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/presentation/screens/downloads/DownloadsScreen.kt`

**Step 1: Add import for VideoPlayerScreen**

Add import at the top:

```kotlin
import com.lowiq.jellyfish.presentation.screens.player.VideoPlayerScreen
```

**Step 2: Update CompletedDownloadItem onPlay callback**

Update the items block (around line 104-110):

```kotlin
items(state.completedDownloads, key = { it.id }) { download ->
    CompletedDownloadItem(
        download = download,
        onPlay = {
            download.filePath?.let { path ->
                navigator.push(
                    VideoPlayerScreen(
                        itemId = download.itemId,
                        title = download.title,
                        subtitle = download.subtitle,
                        startPositionMs = download.lastPlayedPositionMs,
                        offlineFilePath = path,
                        downloadId = download.id
                    )
                )
            }
        },
        onDelete = { screenModel.deleteDownload(download.id) }
    )
}
```

**Step 3: Verify build compiles**

Run: `./gradlew :composeApp:compileKotlinJvm`
Expected: BUILD SUCCESSFUL

**Step 4: Commit**

```bash
git add composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/presentation/screens/downloads/DownloadsScreen.kt
git commit -m "feat(downloads): wire up play button to VideoPlayerScreen with offline support"
```

---

## Task 9: Build and test on Android

**Step 1: Build Android debug APK**

Run: `./gradlew :composeApp:assembleDebug`
Expected: BUILD SUCCESSFUL

**Step 2: Manual testing checklist**

Test on device or emulator:
- [ ] Download a video completes successfully
- [ ] Tap Play on completed download
- [ ] Video plays from local file
- [ ] Seek/pause/resume work correctly
- [ ] Exit player, re-enter - resume dialog appears
- [ ] Progress syncs when back online (check Jellyfin "Continue Watching")

**Step 3: Final commit**

```bash
git add -A
git commit -m "feat(offline): complete offline playback implementation"
```

---

## Summary of Changes

| File | Change |
|------|--------|
| `domain/model/Download.kt` | Add `lastPlayedPositionMs`, `lastPlayedAt` |
| `data/local/DownloadStorage.kt` | Update serialization, add `updatePlaybackPosition` |
| `domain/repository/DownloadRepository.kt` | Add `updatePlaybackPosition` method |
| `data/repository/DownloadRepositoryImpl.kt` | Implement `updatePlaybackPosition` |
| `presentation/screens/player/VideoPlayerState.kt` | Add `isOfflineMode` |
| `presentation/screens/player/VideoPlayerScreen.kt` | Add `offlineFilePath`, `downloadId` params |
| `presentation/screens/player/VideoPlayerScreenModel.kt` | Implement offline playback logic |
| `di/AppModule.kt` | Update Koin factory |
| `presentation/screens/downloads/DownloadsScreen.kt` | Wire play button |

## Existing Infrastructure Used

- `PlaybackSyncStorage` - stores pending syncs
- `PlaybackSyncService` - handles sync to server
- `PendingPlaybackSync` - model for pending syncs
