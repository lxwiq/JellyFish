# Offline Playback Design

## Overview

Enable playback of downloaded media files with progress synchronization to the Jellyfin server when back online.

## Scope

- **Platform:** Android only
- **Sync:** Progress saved locally, synced to server when online

## Architecture

### Approach

Modify existing `VideoPlayerScreen` to support both streaming and offline playback, rather than creating a separate screen. This minimizes code duplication since ExoPlayer natively supports local file URLs.

### Data Flow

```
DownloadsScreen
    |
    | (click Play on completed download)
    v
VideoPlayerScreen(offlineFilePath = download.filePath)
    |
    | (playback with local file)
    v
VideoPlayerScreenModel
    |
    | (save progress every 10s)
    v
DownloadStorage (local progress)
    |
    | (on playback end)
    v
PendingPlaybackSync queue
    |
    | (when online)
    v
PlaybackSyncManager -> Jellyfin Server
```

## Models

### Modified: Download

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
    // NEW fields for offline playback
    val lastPlayedPositionMs: Long = 0,
    val lastPlayedAt: Long? = null
)
```

### New: PendingPlaybackSync

```kotlin
data class PendingPlaybackSync(
    val id: String,
    val itemId: String,
    val serverId: String,
    val positionMs: Long,
    val positionTicks: Long,  // Jellyfin uses ticks (1 tick = 100ns)
    val playedPercentage: Float,
    val isCompleted: Boolean,  // true if watched > 90%
    val timestamp: Long
)
```

## Screen Changes

### VideoPlayerScreen

```kotlin
data class VideoPlayerScreen(
    val itemId: String,
    val title: String,
    val subtitle: String? = null,
    val startPositionMs: Long = 0,
    // NEW: for offline playback
    val offlineFilePath: String? = null,
    val downloadId: String? = null
) : Screen
```

### VideoPlayerState

```kotlin
data class VideoPlayerState(
    // ... existing fields ...
    val isOfflineMode: Boolean = false  // NEW
)
```

### VideoPlayerScreenModel Changes

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

private fun setupOfflinePlayback() {
    _state.update { it.copy(isLoading = false, isOfflineMode = true) }

    // Load last played position from download
    screenModelScope.launch {
        downloadRepository.getDownloadByItemId(itemId)?.let { download ->
            if (download.lastPlayedPositionMs > 0) {
                _state.update {
                    it.copy(
                        showResumeDialog = true,
                        resumePositionMs = download.lastPlayedPositionMs
                    )
                }
            } else {
                startOfflinePlayback(0)
            }
        }
    }
}

private fun startOfflinePlayback(positionMs: Long) {
    val fileUrl = "file://$offlineFilePath"
    videoPlayer.play(fileUrl, emptyMap(), positionMs)
    startOfflineProgressTracking()
}

private fun startOfflineProgressTracking() {
    // Save progress every 10 seconds
    screenModelScope.launch {
        while (isActive) {
            delay(10_000)
            saveOfflineProgress()
        }
    }
}

private suspend fun saveOfflineProgress() {
    val position = (state.value.playbackState as? PlaybackState.Playing)?.positionMs ?: return
    downloadId?.let { id ->
        downloadRepository.updatePlaybackPosition(id, position)
    }
}
```

## New Components

### ConnectivityObserver (androidMain)

```kotlin
class ConnectivityObserver(context: Context) {
    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    val isConnected: StateFlow<Boolean> = callbackFlow {
        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) { trySend(true) }
            override fun onLost(network: Network) { trySend(false) }
        }
        connectivityManager.registerDefaultNetworkCallback(callback)
        trySend(connectivityManager.activeNetwork != null)
        awaitClose { connectivityManager.unregisterNetworkCallback(callback) }
    }.stateIn(scope, SharingStarted.Eagerly, false)
}
```

### PlaybackSyncManager

```kotlin
class PlaybackSyncManager(
    private val downloadStorage: DownloadStorage,
    private val mediaRepository: MediaRepository,
    private val connectivityObserver: ConnectivityObserver
) {
    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    fun startObserving() {
        scope.launch {
            connectivityObserver.isConnected
                .filter { it }
                .collect { syncPendingProgress() }
        }
    }

    private suspend fun syncPendingProgress() {
        val pendingSyncs = downloadStorage.getPendingSyncs()
        pendingSyncs.forEach { sync ->
            mediaRepository.reportPlaybackProgress(
                serverId = sync.serverId,
                itemId = sync.itemId,
                positionTicks = sync.positionTicks,
                isPaused = true
            ).onSuccess {
                downloadStorage.removePendingSync(sync.id)
            }
        }
    }
}
```

## DownloadStorage Extensions

```kotlin
// New methods in DownloadStorage
suspend fun updatePlaybackPosition(downloadId: String, positionMs: Long)
suspend fun savePendingSync(sync: PendingPlaybackSync)
fun getPendingSyncs(): Flow<List<PendingPlaybackSync>>
suspend fun removePendingSync(syncId: String)
```

## DownloadsScreen Wiring

```kotlin
CompletedDownloadItem(
    download = download,
    onPlay = {
        navigator.push(VideoPlayerScreen(
            itemId = download.itemId,
            title = download.title,
            subtitle = download.subtitle,
            startPositionMs = download.lastPlayedPositionMs,
            offlineFilePath = download.filePath,
            downloadId = download.id
        ))
    },
    onDelete = { screenModel.deleteDownload(download.id) }
)
```

## Koin Module Changes

```kotlin
// In platformModule (androidMain)
single { ConnectivityObserver(androidContext()) }

// In appModule
single { PlaybackSyncManager(get(), get(), get()) }

// Initialize in Application.onCreate() or via Koin callback
single {
    PlaybackSyncManager(get(), get(), get()).also {
        it.startObserving()
    }
}
```

## Files to Create/Modify

| Action | File |
|--------|------|
| Modify | `domain/model/Download.kt` |
| Modify | `data/local/DownloadStorage.kt` |
| Modify | `presentation/screens/player/VideoPlayerScreen.kt` |
| Modify | `presentation/screens/player/VideoPlayerScreenModel.kt` |
| Modify | `presentation/screens/player/VideoPlayerState.kt` |
| Modify | `presentation/screens/downloads/DownloadsScreen.kt` |
| Modify | `domain/repository/DownloadRepository.kt` |
| Modify | `data/repository/DownloadRepositoryImpl.kt` |
| Modify | `di/AppModule.kt` |
| Create | `domain/model/PendingPlaybackSync.kt` |
| Create | `data/local/ConnectivityObserver.kt` (androidMain) |
| Create | `domain/sync/PlaybackSyncManager.kt` |

## Testing Checklist

- [ ] Play downloaded file from DownloadsScreen
- [ ] Resume dialog appears if previously watched
- [ ] Progress saves every 10 seconds
- [ ] Progress syncs to server when online
- [ ] Offline indicator shown during playback
- [ ] All player controls work (seek, pause, speed)
