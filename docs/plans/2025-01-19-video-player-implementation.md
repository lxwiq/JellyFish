# Video Player Implementation Plan

> **For Claude:** REQUIRED SUB-SKILL: Use superpowers:executing-plans to implement this plan task-by-task.

**Goal:** Implement a full-featured video player with Direct Play support, starting with Android using ExoPlayer, with shared UI architecture.

**Architecture:** Platform-specific VideoPlayer (expect/actual) with 100% shared Compose UI. ExoPlayer on Android. Jellyfin API integration for streaming URLs and progress reporting.

**Tech Stack:** Media3 ExoPlayer, Compose Multiplatform, Koin DI, Jellyfin SDK, Voyager navigation

---

## Phase 1: Dependencies & Project Setup

### Task 1: Add Media3 ExoPlayer dependencies

**Files:**
- Modify: `gradle/libs.versions.toml`
- Modify: `composeApp/build.gradle.kts`

**Step 1: Add Media3 version and libraries to version catalog**

In `gradle/libs.versions.toml`, add after line 23 (after coil version):

```toml
media3 = "1.5.1"
```

And add these libraries after the Coil section (after line 60):

```toml
# Media3 ExoPlayer (Android)
media3-exoplayer = { module = "androidx.media3:media3-exoplayer", version.ref = "media3" }
media3-exoplayer-hls = { module = "androidx.media3:media3-exoplayer-hls", version.ref = "media3" }
media3-ui = { module = "androidx.media3:media3-ui", version.ref = "media3" }
media3-session = { module = "androidx.media3:media3-session", version.ref = "media3" }
```

**Step 2: Add dependencies to Android source set**

In `composeApp/build.gradle.kts`, add to `androidMain.dependencies` block (after line 38):

```kotlin
implementation(libs.media3.exoplayer)
implementation(libs.media3.exoplayer.hls)
implementation(libs.media3.ui)
implementation(libs.media3.session)
```

**Step 3: Sync gradle and verify**

Run: `./gradlew :composeApp:assembleDebug --dry-run`
Expected: BUILD SUCCESSFUL

**Step 4: Commit**

```bash
git add gradle/libs.versions.toml composeApp/build.gradle.kts
git commit -m "build: add Media3 ExoPlayer dependencies for Android"
```

---

## Phase 2: Domain Layer - Player Abstraction

### Task 2: Create PlaybackState sealed class

**Files:**
- Create: `composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/domain/player/PlaybackState.kt`

**Step 1: Create the PlaybackState file**

```kotlin
package com.lowiq.jellyfish.domain.player

sealed class PlaybackState {
    data object Idle : PlaybackState()
    data object Buffering : PlaybackState()
    data class Playing(
        val positionMs: Long,
        val durationMs: Long,
        val playbackSpeed: Float = 1f
    ) : PlaybackState()
    data class Paused(
        val positionMs: Long,
        val durationMs: Long
    ) : PlaybackState()
    data class Error(
        val message: String,
        val canRetry: Boolean = true
    ) : PlaybackState()
}
```

**Step 2: Commit**

```bash
git add composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/domain/player/
git commit -m "feat(player): add PlaybackState sealed class"
```

### Task 3: Create AudioTrack and SubtitleTrack models

**Files:**
- Create: `composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/domain/player/MediaTrack.kt`

**Step 1: Create the MediaTrack file**

```kotlin
package com.lowiq.jellyfish.domain.player

data class AudioTrack(
    val index: Int,
    val language: String?,
    val label: String,
    val codec: String?,
    val channels: Int?,
    val isSelected: Boolean = false
)

data class SubtitleTrack(
    val index: Int,
    val language: String?,
    val label: String,
    val isSelected: Boolean = false
)

data class QualityOption(
    val index: Int,
    val label: String, // "1080p", "720p", "Auto", "Original"
    val bitrate: Long?,
    val isSelected: Boolean = false
)
```

**Step 2: Commit**

```bash
git add composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/domain/player/MediaTrack.kt
git commit -m "feat(player): add AudioTrack, SubtitleTrack, QualityOption models"
```

### Task 4: Create VideoPlayer expect class

**Files:**
- Create: `composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/domain/player/VideoPlayer.kt`

**Step 1: Create the VideoPlayer expect class**

```kotlin
package com.lowiq.jellyfish.domain.player

import kotlinx.coroutines.flow.StateFlow

expect class VideoPlayer {
    val playbackState: StateFlow<PlaybackState>
    val audioTracks: StateFlow<List<AudioTrack>>
    val subtitleTracks: StateFlow<List<SubtitleTrack>>
    val qualityOptions: StateFlow<List<QualityOption>>

    fun initialize()
    fun release()

    fun play(url: String, headers: Map<String, String> = emptyMap(), startPositionMs: Long = 0)
    fun pause()
    fun resume()
    fun stop()

    fun seekTo(positionMs: Long)
    fun seekForward(ms: Long = 10_000)
    fun seekBackward(ms: Long = 10_000)

    fun setPlaybackSpeed(speed: Float)
    fun selectAudioTrack(index: Int)
    fun selectSubtitleTrack(index: Int)
    fun disableSubtitles()
    fun selectQuality(index: Int)
}
```

**Step 2: Commit**

```bash
git add composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/domain/player/VideoPlayer.kt
git commit -m "feat(player): add VideoPlayer expect class interface"
```

### Task 5: Create VideoPlayer actual class for Android

**Files:**
- Create: `composeApp/src/androidMain/kotlin/com/lowiq/jellyfish/domain/player/VideoPlayer.android.kt`

**Step 1: Create the Android VideoPlayer implementation**

```kotlin
package com.lowiq.jellyfish.domain.player

import android.content.Context
import androidx.annotation.OptIn
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.TrackSelectionOverride
import androidx.media3.common.Tracks
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.hls.HlsMediaSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

@OptIn(UnstableApi::class)
actual class VideoPlayer(
    private val context: Context
) {
    private var exoPlayer: ExoPlayer? = null
    private val scope = CoroutineScope(Dispatchers.Main)
    private var positionUpdateJob: Job? = null

    private val _playbackState = MutableStateFlow<PlaybackState>(PlaybackState.Idle)
    actual val playbackState: StateFlow<PlaybackState> = _playbackState.asStateFlow()

    private val _audioTracks = MutableStateFlow<List<AudioTrack>>(emptyList())
    actual val audioTracks: StateFlow<List<AudioTrack>> = _audioTracks.asStateFlow()

    private val _subtitleTracks = MutableStateFlow<List<SubtitleTrack>>(emptyList())
    actual val subtitleTracks: StateFlow<List<SubtitleTrack>> = _subtitleTracks.asStateFlow()

    private val _qualityOptions = MutableStateFlow<List<QualityOption>>(emptyList())
    actual val qualityOptions: StateFlow<List<QualityOption>> = _qualityOptions.asStateFlow()

    private var currentPlaybackSpeed = 1f

    actual fun initialize() {
        if (exoPlayer != null) return

        exoPlayer = ExoPlayer.Builder(context).build().apply {
            addListener(object : Player.Listener {
                override fun onPlaybackStateChanged(state: Int) {
                    updatePlaybackState()
                }

                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    updatePlaybackState()
                }

                override fun onPlayerError(error: PlaybackException) {
                    _playbackState.value = PlaybackState.Error(
                        message = error.message ?: "Playback error",
                        canRetry = true
                    )
                }

                override fun onTracksChanged(tracks: Tracks) {
                    updateTracks(tracks)
                }
            })
        }
    }

    actual fun release() {
        positionUpdateJob?.cancel()
        exoPlayer?.release()
        exoPlayer = null
        _playbackState.value = PlaybackState.Idle
    }

    actual fun play(url: String, headers: Map<String, String>, startPositionMs: Long) {
        val player = exoPlayer ?: return

        val dataSourceFactory = DefaultHttpDataSource.Factory()
            .setDefaultRequestProperties(headers)

        val mediaSource = if (url.contains(".m3u8")) {
            HlsMediaSource.Factory(dataSourceFactory)
                .createMediaSource(MediaItem.fromUri(url))
        } else {
            ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(MediaItem.fromUri(url))
        }

        player.setMediaSource(mediaSource)
        player.prepare()

        if (startPositionMs > 0) {
            player.seekTo(startPositionMs)
        }

        player.play()
        startPositionUpdates()
    }

    actual fun pause() {
        exoPlayer?.pause()
    }

    actual fun resume() {
        exoPlayer?.play()
    }

    actual fun stop() {
        positionUpdateJob?.cancel()
        exoPlayer?.stop()
        _playbackState.value = PlaybackState.Idle
    }

    actual fun seekTo(positionMs: Long) {
        exoPlayer?.seekTo(positionMs)
    }

    actual fun seekForward(ms: Long) {
        val player = exoPlayer ?: return
        val newPosition = (player.currentPosition + ms).coerceAtMost(player.duration)
        player.seekTo(newPosition)
    }

    actual fun seekBackward(ms: Long) {
        val player = exoPlayer ?: return
        val newPosition = (player.currentPosition - ms).coerceAtLeast(0)
        player.seekTo(newPosition)
    }

    actual fun setPlaybackSpeed(speed: Float) {
        currentPlaybackSpeed = speed
        exoPlayer?.setPlaybackSpeed(speed)
    }

    actual fun selectAudioTrack(index: Int) {
        val player = exoPlayer ?: return
        val tracks = player.currentTracks

        tracks.groups.forEachIndexed { groupIndex, group ->
            if (group.type == C.TRACK_TYPE_AUDIO) {
                for (i in 0 until group.length) {
                    if (i == index) {
                        player.trackSelectionParameters = player.trackSelectionParameters
                            .buildUpon()
                            .setOverrideForType(
                                TrackSelectionOverride(group.mediaTrackGroup, listOf(i))
                            )
                            .build()
                        updateTracks(player.currentTracks)
                        return
                    }
                }
            }
        }
    }

    actual fun selectSubtitleTrack(index: Int) {
        val player = exoPlayer ?: return
        val tracks = player.currentTracks

        tracks.groups.forEachIndexed { groupIndex, group ->
            if (group.type == C.TRACK_TYPE_TEXT) {
                for (i in 0 until group.length) {
                    if (i == index) {
                        player.trackSelectionParameters = player.trackSelectionParameters
                            .buildUpon()
                            .setOverrideForType(
                                TrackSelectionOverride(group.mediaTrackGroup, listOf(i))
                            )
                            .build()
                        updateTracks(player.currentTracks)
                        return
                    }
                }
            }
        }
    }

    actual fun disableSubtitles() {
        val player = exoPlayer ?: return
        player.trackSelectionParameters = player.trackSelectionParameters
            .buildUpon()
            .setTrackTypeDisabled(C.TRACK_TYPE_TEXT, true)
            .build()
        updateTracks(player.currentTracks)
    }

    actual fun selectQuality(index: Int) {
        // Quality selection for HLS streams
        val player = exoPlayer ?: return
        val tracks = player.currentTracks

        tracks.groups.forEachIndexed { groupIndex, group ->
            if (group.type == C.TRACK_TYPE_VIDEO) {
                for (i in 0 until group.length) {
                    if (i == index) {
                        player.trackSelectionParameters = player.trackSelectionParameters
                            .buildUpon()
                            .setOverrideForType(
                                TrackSelectionOverride(group.mediaTrackGroup, listOf(i))
                            )
                            .build()
                        return
                    }
                }
            }
        }
    }

    fun getExoPlayer(): ExoPlayer? = exoPlayer

    private fun updatePlaybackState() {
        val player = exoPlayer ?: return

        _playbackState.value = when {
            player.playbackState == Player.STATE_BUFFERING -> PlaybackState.Buffering
            player.playbackState == Player.STATE_ENDED -> PlaybackState.Paused(
                positionMs = player.duration,
                durationMs = player.duration
            )
            player.isPlaying -> PlaybackState.Playing(
                positionMs = player.currentPosition,
                durationMs = player.duration.coerceAtLeast(0),
                playbackSpeed = currentPlaybackSpeed
            )
            player.playbackState == Player.STATE_READY -> PlaybackState.Paused(
                positionMs = player.currentPosition,
                durationMs = player.duration.coerceAtLeast(0)
            )
            else -> PlaybackState.Idle
        }
    }

    private fun startPositionUpdates() {
        positionUpdateJob?.cancel()
        positionUpdateJob = scope.launch {
            while (isActive) {
                updatePlaybackState()
                delay(1000)
            }
        }
    }

    private fun updateTracks(tracks: Tracks) {
        val audioList = mutableListOf<AudioTrack>()
        val subtitleList = mutableListOf<SubtitleTrack>()
        val qualityList = mutableListOf<QualityOption>()

        var audioIndex = 0
        var subtitleIndex = 0
        var videoIndex = 0

        tracks.groups.forEach { group ->
            when (group.type) {
                C.TRACK_TYPE_AUDIO -> {
                    for (i in 0 until group.length) {
                        val format = group.getTrackFormat(i)
                        audioList.add(
                            AudioTrack(
                                index = audioIndex++,
                                language = format.language,
                                label = format.label ?: format.language ?: "Track ${audioIndex}",
                                codec = format.codecs,
                                channels = format.channelCount,
                                isSelected = group.isTrackSelected(i)
                            )
                        )
                    }
                }
                C.TRACK_TYPE_TEXT -> {
                    for (i in 0 until group.length) {
                        val format = group.getTrackFormat(i)
                        subtitleList.add(
                            SubtitleTrack(
                                index = subtitleIndex++,
                                language = format.language,
                                label = format.label ?: format.language ?: "Subtitle ${subtitleIndex}",
                                isSelected = group.isTrackSelected(i)
                            )
                        )
                    }
                }
                C.TRACK_TYPE_VIDEO -> {
                    for (i in 0 until group.length) {
                        val format = group.getTrackFormat(i)
                        val height = format.height
                        val label = when {
                            height >= 2160 -> "4K"
                            height >= 1080 -> "1080p"
                            height >= 720 -> "720p"
                            height >= 480 -> "480p"
                            else -> "${height}p"
                        }
                        qualityList.add(
                            QualityOption(
                                index = videoIndex++,
                                label = label,
                                bitrate = format.bitrate.toLong(),
                                isSelected = group.isTrackSelected(i)
                            )
                        )
                    }
                }
            }
        }

        _audioTracks.value = audioList
        _subtitleTracks.value = subtitleList
        _qualityOptions.value = qualityList
    }
}
```

**Step 2: Commit**

```bash
git add composeApp/src/androidMain/kotlin/com/lowiq/jellyfish/domain/player/
git commit -m "feat(player): implement Android VideoPlayer with ExoPlayer"
```

### Task 6: Create stub VideoPlayer for JVM/Desktop

**Files:**
- Create: `composeApp/src/jvmMain/kotlin/com/lowiq/jellyfish/domain/player/VideoPlayer.jvm.kt`

**Step 1: Create the JVM stub**

```kotlin
package com.lowiq.jellyfish.domain.player

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

actual class VideoPlayer {
    private val _playbackState = MutableStateFlow<PlaybackState>(PlaybackState.Idle)
    actual val playbackState: StateFlow<PlaybackState> = _playbackState

    private val _audioTracks = MutableStateFlow<List<AudioTrack>>(emptyList())
    actual val audioTracks: StateFlow<List<AudioTrack>> = _audioTracks

    private val _subtitleTracks = MutableStateFlow<List<SubtitleTrack>>(emptyList())
    actual val subtitleTracks: StateFlow<List<SubtitleTrack>> = _subtitleTracks

    private val _qualityOptions = MutableStateFlow<List<QualityOption>>(emptyList())
    actual val qualityOptions: StateFlow<List<QualityOption>> = _qualityOptions

    actual fun initialize() {
        // TODO: Implement with VLC or JavaFX
    }

    actual fun release() {}

    actual fun play(url: String, headers: Map<String, String>, startPositionMs: Long) {
        _playbackState.value = PlaybackState.Error("Desktop playback not yet implemented", canRetry = false)
    }

    actual fun pause() {}
    actual fun resume() {}
    actual fun stop() {}
    actual fun seekTo(positionMs: Long) {}
    actual fun seekForward(ms: Long) {}
    actual fun seekBackward(ms: Long) {}
    actual fun setPlaybackSpeed(speed: Float) {}
    actual fun selectAudioTrack(index: Int) {}
    actual fun selectSubtitleTrack(index: Int) {}
    actual fun disableSubtitles() {}
    actual fun selectQuality(index: Int) {}
}
```

**Step 2: Commit**

```bash
git add composeApp/src/jvmMain/kotlin/com/lowiq/jellyfish/domain/player/
git commit -m "feat(player): add JVM VideoPlayer stub"
```

### Task 7: Create stub VideoPlayer for iOS

**Files:**
- Create: `composeApp/src/iosMain/kotlin/com/lowiq/jellyfish/domain/player/VideoPlayer.ios.kt`

**Step 1: Create the iOS stub**

```kotlin
package com.lowiq.jellyfish.domain.player

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

actual class VideoPlayer {
    private val _playbackState = MutableStateFlow<PlaybackState>(PlaybackState.Idle)
    actual val playbackState: StateFlow<PlaybackState> = _playbackState

    private val _audioTracks = MutableStateFlow<List<AudioTrack>>(emptyList())
    actual val audioTracks: StateFlow<List<AudioTrack>> = _audioTracks

    private val _subtitleTracks = MutableStateFlow<List<SubtitleTrack>>(emptyList())
    actual val subtitleTracks: StateFlow<List<SubtitleTrack>> = _subtitleTracks

    private val _qualityOptions = MutableStateFlow<List<QualityOption>>(emptyList())
    actual val qualityOptions: StateFlow<List<QualityOption>> = _qualityOptions

    actual fun initialize() {
        // TODO: Implement with AVPlayer
    }

    actual fun release() {}

    actual fun play(url: String, headers: Map<String, String>, startPositionMs: Long) {
        _playbackState.value = PlaybackState.Error("iOS playback not yet implemented", canRetry = false)
    }

    actual fun pause() {}
    actual fun resume() {}
    actual fun stop() {}
    actual fun seekTo(positionMs: Long) {}
    actual fun seekForward(ms: Long) {}
    actual fun seekBackward(ms: Long) {}
    actual fun setPlaybackSpeed(speed: Float) {}
    actual fun selectAudioTrack(index: Int) {}
    actual fun selectSubtitleTrack(index: Int) {}
    actual fun disableSubtitles() {}
    actual fun selectQuality(index: Int) {}
}
```

**Step 2: Commit**

```bash
git add composeApp/src/iosMain/kotlin/com/lowiq/jellyfish/domain/player/
git commit -m "feat(player): add iOS VideoPlayer stub"
```

---

## Phase 3: Data Layer - Jellyfin Streaming API

### Task 8: Add streaming methods to JellyfinDataSource

**Files:**
- Modify: `composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/data/remote/JellyfinDataSource.kt`

**Step 1: Add data classes for streaming**

Add at the end of the file (before closing):

```kotlin
data class StreamInfo(
    val directPlayUrl: String,
    val transcodingUrl: String?,
    val mediaSourceId: String,
    val playSessionId: String,
    val supportsDirectPlay: Boolean
)

data class PlaybackProgressInfo(
    val itemId: String,
    val mediaSourceId: String,
    val positionTicks: Long,
    val isPaused: Boolean,
    val playSessionId: String
)
```

**Step 2: Add streaming methods to interface**

Add these methods to the `JellyfinDataSource` interface:

```kotlin
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
```

**Step 3: Commit**

```bash
git add composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/data/remote/JellyfinDataSource.kt
git commit -m "feat(player): add streaming API methods to JellyfinDataSource"
```

### Task 9: Implement streaming methods in Android JellyfinDataSourceImpl

**Files:**
- Modify: `composeApp/src/androidMain/kotlin/com/lowiq/jellyfish/data/remote/JellyfinDataSourceImpl.kt`

**Step 1: Add getStreamInfo implementation**

Add this method to the class:

```kotlin
override suspend fun getStreamInfo(
    serverUrl: String,
    token: String,
    userId: String,
    itemId: String
): Result<StreamInfo> = withContext(Dispatchers.IO) {
    runCatching {
        val api = createApi(serverUrl, token)
        val response by api.mediaInfoApi.getPostedPlaybackInfo(
            itemId = java.util.UUID.fromString(itemId),
            userId = java.util.UUID.fromString(userId),
            maxStreamingBitrate = 100_000_000,
            startTimeTicks = 0,
            autoOpenLiveStream = true
        )

        val mediaSource = response.mediaSources?.firstOrNull()
            ?: throw Exception("No media source found")

        val playSessionId = response.playSessionId ?: java.util.UUID.randomUUID().toString()
        val mediaSourceId = mediaSource.id ?: itemId

        val directPlayUrl = "$serverUrl/Videos/$itemId/stream?static=true&mediaSourceId=$mediaSourceId"
        val transcodingUrl = mediaSource.transcodingUrl?.let { "$serverUrl$it" }

        StreamInfo(
            directPlayUrl = directPlayUrl,
            transcodingUrl = transcodingUrl,
            mediaSourceId = mediaSourceId,
            playSessionId = playSessionId,
            supportsDirectPlay = mediaSource.supportsDirectPlay == true
        )
    }
}
```

**Step 2: Add playback reporting methods**

```kotlin
override suspend fun reportPlaybackStart(
    serverUrl: String,
    token: String,
    itemId: String,
    mediaSourceId: String,
    playSessionId: String
): Result<Unit> = withContext(Dispatchers.IO) {
    runCatching {
        val api = createApi(serverUrl, token)
        api.playStateApi.reportPlaybackStart(
            org.jellyfin.sdk.model.api.PlaybackStartInfo(
                itemId = java.util.UUID.fromString(itemId),
                mediaSourceId = mediaSourceId,
                playSessionId = playSessionId,
                canSeek = true
            )
        )
        Unit
    }
}

override suspend fun reportPlaybackProgress(
    serverUrl: String,
    token: String,
    progress: PlaybackProgressInfo
): Result<Unit> = withContext(Dispatchers.IO) {
    runCatching {
        val api = createApi(serverUrl, token)
        api.playStateApi.reportPlaybackProgress(
            org.jellyfin.sdk.model.api.PlaybackProgressInfo(
                itemId = java.util.UUID.fromString(progress.itemId),
                mediaSourceId = progress.mediaSourceId,
                positionTicks = progress.positionTicks,
                isPaused = progress.isPaused,
                playSessionId = progress.playSessionId,
                canSeek = true
            )
        )
        Unit
    }
}

override suspend fun reportPlaybackStopped(
    serverUrl: String,
    token: String,
    itemId: String,
    mediaSourceId: String,
    positionTicks: Long,
    playSessionId: String
): Result<Unit> = withContext(Dispatchers.IO) {
    runCatching {
        val api = createApi(serverUrl, token)
        api.playStateApi.reportPlaybackStopped(
            org.jellyfin.sdk.model.api.PlaybackStopInfo(
                itemId = java.util.UUID.fromString(itemId),
                mediaSourceId = mediaSourceId,
                positionTicks = positionTicks,
                playSessionId = playSessionId
            )
        )
        Unit
    }
}
```

**Step 3: Commit**

```bash
git add composeApp/src/androidMain/kotlin/com/lowiq/jellyfish/data/remote/JellyfinDataSourceImpl.kt
git commit -m "feat(player): implement streaming API in Android JellyfinDataSourceImpl"
```

### Task 10: Implement streaming methods in JVM JellyfinDataSourceImpl

**Files:**
- Modify: `composeApp/src/jvmMain/kotlin/com/lowiq/jellyfish/data/remote/JellyfinDataSourceImpl.kt`

**Step 1: Add the same implementations as Android**

Copy the same 4 methods from Task 9 to the JVM implementation.

**Step 2: Commit**

```bash
git add composeApp/src/jvmMain/kotlin/com/lowiq/jellyfish/data/remote/JellyfinDataSourceImpl.kt
git commit -m "feat(player): implement streaming API in JVM JellyfinDataSourceImpl"
```

### Task 11: Add streaming methods to MediaRepository

**Files:**
- Modify: `composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/domain/repository/MediaRepository.kt`
- Modify: `composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/data/repository/MediaRepositoryImpl.kt`

**Step 1: Add methods to MediaRepository interface**

```kotlin
suspend fun getStreamInfo(serverId: String, itemId: String): Result<com.lowiq.jellyfish.data.remote.StreamInfo>
suspend fun reportPlaybackStart(serverId: String, itemId: String, mediaSourceId: String, playSessionId: String): Result<Unit>
suspend fun reportPlaybackProgress(serverId: String, progress: com.lowiq.jellyfish.data.remote.PlaybackProgressInfo): Result<Unit>
suspend fun reportPlaybackStopped(serverId: String, itemId: String, mediaSourceId: String, positionTicks: Long, playSessionId: String): Result<Unit>
```

**Step 2: Implement in MediaRepositoryImpl**

```kotlin
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
```

**Step 3: Commit**

```bash
git add composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/domain/repository/MediaRepository.kt
git add composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/data/repository/MediaRepositoryImpl.kt
git commit -m "feat(player): add streaming methods to MediaRepository"
```

---

## Phase 4: Presentation Layer - Video Player Screen

### Task 12: Create VideoPlayerState

**Files:**
- Create: `composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/presentation/screens/player/VideoPlayerState.kt`

**Step 1: Create the state file**

```kotlin
package com.lowiq.jellyfish.presentation.screens.player

import com.lowiq.jellyfish.domain.player.AudioTrack
import com.lowiq.jellyfish.domain.player.PlaybackState
import com.lowiq.jellyfish.domain.player.QualityOption
import com.lowiq.jellyfish.domain.player.SubtitleTrack

data class VideoPlayerState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val title: String = "",
    val subtitle: String? = null, // e.g., "S01E03 - Episode Title"
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
    val showNextEpisodeCard: Boolean = false
)

data class NextEpisodeInfo(
    val id: String,
    val title: String,
    val seasonNumber: Int,
    val episodeNumber: Int,
    val thumbnailUrl: String?
)
```

**Step 2: Commit**

```bash
git add composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/presentation/screens/player/
git commit -m "feat(player): add VideoPlayerState"
```

### Task 13: Create VideoPlayerScreenModel

**Files:**
- Create: `composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/presentation/screens/player/VideoPlayerScreenModel.kt`

**Step 1: Create the screen model**

```kotlin
package com.lowiq.jellyfish.presentation.screens.player

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.lowiq.jellyfish.data.remote.PlaybackProgressInfo
import com.lowiq.jellyfish.domain.player.PlaybackState
import com.lowiq.jellyfish.domain.player.VideoPlayer
import com.lowiq.jellyfish.domain.repository.MediaRepository
import com.lowiq.jellyfish.domain.repository.ServerRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

sealed class VideoPlayerEvent {
    data object NavigateBack : VideoPlayerEvent()
    data class PlayNextEpisode(val episodeId: String) : VideoPlayerEvent()
}

class VideoPlayerScreenModel(
    private val itemId: String,
    private val itemTitle: String,
    private val itemSubtitle: String?,
    private val startPositionMs: Long,
    val videoPlayer: VideoPlayer,
    private val serverRepository: ServerRepository,
    private val mediaRepository: MediaRepository
) : ScreenModel {

    private val _state = MutableStateFlow(VideoPlayerState(
        title = itemTitle,
        subtitle = itemSubtitle,
        resumePositionMs = startPositionMs
    ))
    val state = _state.asStateFlow()

    private val _events = MutableSharedFlow<VideoPlayerEvent>()
    val events = _events.asSharedFlow()

    private var currentServerId: String? = null
    private var mediaSourceId: String? = null
    private var playSessionId: String? = null
    private var progressReportJob: Job? = null
    private var controlsHideJob: Job? = null

    init {
        videoPlayer.initialize()
        observePlaybackState()
        observeTracks()
        loadStreamInfo()
    }

    private fun observePlaybackState() {
        screenModelScope.launch {
            videoPlayer.playbackState.collect { playbackState ->
                _state.update { it.copy(playbackState = playbackState) }

                // Check for next episode notification (30s before end)
                if (playbackState is PlaybackState.Playing) {
                    val remainingMs = playbackState.durationMs - playbackState.positionMs
                    if (remainingMs in 1..30_000 && _state.value.nextEpisode != null) {
                        _state.update { it.copy(showNextEpisodeCard = true) }
                    }
                }
            }
        }
    }

    private fun observeTracks() {
        screenModelScope.launch {
            videoPlayer.audioTracks.collect { tracks ->
                _state.update { it.copy(audioTracks = tracks) }
            }
        }
        screenModelScope.launch {
            videoPlayer.subtitleTracks.collect { tracks ->
                _state.update { it.copy(subtitleTracks = tracks) }
            }
        }
        screenModelScope.launch {
            videoPlayer.qualityOptions.collect { options ->
                _state.update { it.copy(qualityOptions = options) }
            }
        }
    }

    private fun loadStreamInfo() {
        screenModelScope.launch {
            val server = serverRepository.getActiveServer()
                .filterNotNull()
                .first()
            currentServerId = server.id

            mediaRepository.getStreamInfo(server.id, itemId)
                .onSuccess { streamInfo ->
                    mediaSourceId = streamInfo.mediaSourceId
                    playSessionId = streamInfo.playSessionId

                    val url = if (streamInfo.supportsDirectPlay) {
                        streamInfo.directPlayUrl
                    } else {
                        streamInfo.transcodingUrl ?: streamInfo.directPlayUrl
                    }

                    val headers = mapOf(
                        "X-Emby-Token" to (getToken() ?: "")
                    )

                    _state.update { it.copy(isLoading = false) }

                    // Show resume dialog if we have a start position
                    if (startPositionMs > 0) {
                        _state.update { it.copy(showResumeDialog = true) }
                    } else {
                        startPlayback(url, headers, 0)
                    }
                }
                .onFailure { e ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = e.message ?: "Failed to load stream info"
                        )
                    }
                }
        }
    }

    fun onResumeFromPosition() {
        _state.update { it.copy(showResumeDialog = false) }
        screenModelScope.launch {
            val streamInfo = mediaRepository.getStreamInfo(currentServerId!!, itemId).getOrNull() ?: return@launch
            val url = if (streamInfo.supportsDirectPlay) streamInfo.directPlayUrl else streamInfo.transcodingUrl ?: streamInfo.directPlayUrl
            val headers = mapOf("X-Emby-Token" to (getToken() ?: ""))
            startPlayback(url, headers, startPositionMs)
        }
    }

    fun onStartFromBeginning() {
        _state.update { it.copy(showResumeDialog = false) }
        screenModelScope.launch {
            val streamInfo = mediaRepository.getStreamInfo(currentServerId!!, itemId).getOrNull() ?: return@launch
            val url = if (streamInfo.supportsDirectPlay) streamInfo.directPlayUrl else streamInfo.transcodingUrl ?: streamInfo.directPlayUrl
            val headers = mapOf("X-Emby-Token" to (getToken() ?: ""))
            startPlayback(url, headers, 0)
        }
    }

    private fun startPlayback(url: String, headers: Map<String, String>, positionMs: Long) {
        videoPlayer.play(url, headers, positionMs)
        reportPlaybackStart()
        startProgressReporting()
    }

    private suspend fun getToken(): String? {
        val serverId = currentServerId ?: return null
        // Token is retrieved through the repository internally
        return null // Headers are handled by the data source
    }

    private fun reportPlaybackStart() {
        val serverId = currentServerId ?: return
        val sourceId = mediaSourceId ?: return
        val sessionId = playSessionId ?: return

        screenModelScope.launch {
            mediaRepository.reportPlaybackStart(serverId, itemId, sourceId, sessionId)
        }
    }

    private fun startProgressReporting() {
        progressReportJob?.cancel()
        progressReportJob = screenModelScope.launch {
            while (isActive) {
                delay(10_000) // Report every 10 seconds
                reportProgress()
            }
        }
    }

    private fun reportProgress() {
        val serverId = currentServerId ?: return
        val sourceId = mediaSourceId ?: return
        val sessionId = playSessionId ?: return
        val state = _state.value.playbackState

        val (positionMs, isPaused) = when (state) {
            is PlaybackState.Playing -> state.positionMs to false
            is PlaybackState.Paused -> state.positionMs to true
            else -> return
        }

        screenModelScope.launch {
            mediaRepository.reportPlaybackProgress(
                serverId,
                PlaybackProgressInfo(
                    itemId = itemId,
                    mediaSourceId = sourceId,
                    positionTicks = positionMs * 10_000, // Convert ms to ticks
                    isPaused = isPaused,
                    playSessionId = sessionId
                )
            )
        }
    }

    fun onPlayPauseClick() {
        when (_state.value.playbackState) {
            is PlaybackState.Playing -> videoPlayer.pause()
            is PlaybackState.Paused -> videoPlayer.resume()
            else -> {}
        }
        resetControlsHideTimer()
    }

    fun onSeekTo(positionMs: Long) {
        videoPlayer.seekTo(positionMs)
        resetControlsHideTimer()
    }

    fun onSeekForward() {
        videoPlayer.seekForward()
        resetControlsHideTimer()
    }

    fun onSeekBackward() {
        videoPlayer.seekBackward()
        resetControlsHideTimer()
    }

    fun onSetPlaybackSpeed(speed: Float) {
        videoPlayer.setPlaybackSpeed(speed)
        _state.update { it.copy(playbackSpeed = speed) }
    }

    fun onSelectAudioTrack(index: Int) {
        videoPlayer.selectAudioTrack(index)
    }

    fun onSelectSubtitleTrack(index: Int) {
        videoPlayer.selectSubtitleTrack(index)
    }

    fun onDisableSubtitles() {
        videoPlayer.disableSubtitles()
    }

    fun onSelectQuality(index: Int) {
        videoPlayer.selectQuality(index)
    }

    fun onToggleControls() {
        _state.update { it.copy(controlsVisible = !it.controlsVisible) }
        if (_state.value.controlsVisible) {
            resetControlsHideTimer()
        }
    }

    fun onShowTrackSelector() {
        _state.update { it.copy(showTrackSelector = true) }
    }

    fun onHideTrackSelector() {
        _state.update { it.copy(showTrackSelector = false) }
    }

    fun onShowSettings() {
        _state.update { it.copy(showSettingsSheet = true) }
    }

    fun onHideSettings() {
        _state.update { it.copy(showSettingsSheet = false) }
    }

    fun onBackPressed() {
        stopPlayback()
        screenModelScope.launch {
            _events.emit(VideoPlayerEvent.NavigateBack)
        }
    }

    fun onPlayNextEpisode() {
        val nextEp = _state.value.nextEpisode ?: return
        stopPlayback()
        screenModelScope.launch {
            _events.emit(VideoPlayerEvent.PlayNextEpisode(nextEp.id))
        }
    }

    fun onDismissNextEpisodeCard() {
        _state.update { it.copy(showNextEpisodeCard = false) }
    }

    private fun resetControlsHideTimer() {
        controlsHideJob?.cancel()
        controlsHideJob = screenModelScope.launch {
            delay(4000)
            _state.update { it.copy(controlsVisible = false) }
        }
    }

    private fun stopPlayback() {
        progressReportJob?.cancel()

        val serverId = currentServerId ?: return
        val sourceId = mediaSourceId ?: return
        val sessionId = playSessionId ?: return
        val state = _state.value.playbackState

        val positionMs = when (state) {
            is PlaybackState.Playing -> state.positionMs
            is PlaybackState.Paused -> state.positionMs
            else -> 0
        }

        screenModelScope.launch {
            mediaRepository.reportPlaybackStopped(
                serverId,
                itemId,
                sourceId,
                positionMs * 10_000,
                sessionId
            )
        }

        videoPlayer.stop()
    }

    override fun onDispose() {
        stopPlayback()
        videoPlayer.release()
    }
}
```

**Step 2: Commit**

```bash
git add composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/presentation/screens/player/VideoPlayerScreenModel.kt
git commit -m "feat(player): add VideoPlayerScreenModel"
```

### Task 14: Create PlayerControls component

**Files:**
- Create: `composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/presentation/screens/player/components/PlayerControls.kt`

**Step 1: Create the controls component**

```kotlin
package com.lowiq.jellyfish.presentation.screens.player.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Forward10
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Replay10
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lowiq.jellyfish.domain.player.PlaybackState
import com.lowiq.jellyfish.presentation.theme.LocalJellyFishColors

@Composable
fun PlayerControls(
    title: String,
    subtitle: String?,
    playbackState: PlaybackState,
    isVisible: Boolean,
    onToggleVisibility: () -> Unit,
    onBackClick: () -> Unit,
    onPlayPauseClick: () -> Unit,
    onSeekForward: () -> Unit,
    onSeekBackward: () -> Unit,
    onSeekTo: (Long) -> Unit,
    onSettingsClick: () -> Unit,
    onAudioSubtitlesClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalJellyFishColors.current

    Box(
        modifier = modifier
            .fillMaxSize()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onToggleVisibility
            )
    ) {
        AnimatedVisibility(
            visible = isVisible,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f))
            ) {
                // Top gradient and header
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Black.copy(alpha = 0.7f), Color.Transparent)
                            )
                        )
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(onClick = onBackClick) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back",
                                    tint = Color.White
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = title,
                                    color = Color.White,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                subtitle?.let {
                                    Text(
                                        text = it,
                                        color = Color.White.copy(alpha = 0.7f),
                                        fontSize = 14.sp
                                    )
                                }
                            }
                        }

                        IconButton(onClick = onSettingsClick) {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = "Settings",
                                tint = Color.White
                            )
                        }
                    }
                }

                // Center controls
                Row(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalArrangement = Arrangement.spacedBy(32.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onSeekBackward,
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Replay10,
                            contentDescription = "Rewind 10 seconds",
                            tint = Color.White,
                            modifier = Modifier.size(36.dp)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(colors.primary)
                            .clickable(onClick = onPlayPauseClick),
                        contentAlignment = Alignment.Center
                    ) {
                        when (playbackState) {
                            is PlaybackState.Buffering -> {
                                CircularProgressIndicator(
                                    color = colors.primaryForeground,
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                            is PlaybackState.Playing -> {
                                Icon(
                                    imageVector = Icons.Default.Pause,
                                    contentDescription = "Pause",
                                    tint = colors.primaryForeground,
                                    modifier = Modifier.size(40.dp)
                                )
                            }
                            else -> {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = "Play",
                                    tint = colors.primaryForeground,
                                    modifier = Modifier.size(40.dp)
                                )
                            }
                        }
                    }

                    IconButton(
                        onClick = onSeekForward,
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Forward10,
                            contentDescription = "Forward 10 seconds",
                            tint = Color.White,
                            modifier = Modifier.size(36.dp)
                        )
                    }
                }

                // Bottom controls
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f))
                            )
                        )
                        .padding(16.dp)
                ) {
                    // Progress bar
                    val (positionMs, durationMs) = when (playbackState) {
                        is PlaybackState.Playing -> playbackState.positionMs to playbackState.durationMs
                        is PlaybackState.Paused -> playbackState.positionMs to playbackState.durationMs
                        else -> 0L to 0L
                    }

                    PlayerProgressBar(
                        positionMs = positionMs,
                        durationMs = durationMs,
                        onSeekTo = onSeekTo
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Action buttons
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = onAudioSubtitlesClick,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Audio & Subtitles", color = Color.White)
                        }
                    }
                }
            }
        }
    }
}
```

**Step 2: Commit**

```bash
git add composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/presentation/screens/player/components/
git commit -m "feat(player): add PlayerControls component"
```

### Task 15: Create PlayerProgressBar component

**Files:**
- Create: `composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/presentation/screens/player/components/PlayerProgressBar.kt`

**Step 1: Create the progress bar component**

```kotlin
package com.lowiq.jellyfish.presentation.screens.player.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lowiq.jellyfish.presentation.theme.LocalJellyFishColors

@Composable
fun PlayerProgressBar(
    positionMs: Long,
    durationMs: Long,
    onSeekTo: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalJellyFishColors.current
    val progress = if (durationMs > 0) positionMs.toFloat() / durationMs.toFloat() else 0f

    var barWidth by remember { mutableFloatStateOf(0f) }
    var isDragging by remember { mutableStateOf(false) }
    var dragProgress by remember { mutableFloatStateOf(progress) }

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = formatTime(if (isDragging) (dragProgress * durationMs).toLong() else positionMs),
            color = Color.White,
            fontSize = 12.sp
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 12.dp)
                .height(20.dp)
                .onSizeChanged { barWidth = it.width.toFloat() }
                .pointerInput(Unit) {
                    detectTapGestures { offset ->
                        val newProgress = (offset.x / barWidth).coerceIn(0f, 1f)
                        onSeekTo((newProgress * durationMs).toLong())
                    }
                }
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        onDragStart = { isDragging = true },
                        onDragEnd = {
                            onSeekTo((dragProgress * durationMs).toLong())
                            isDragging = false
                        },
                        onDragCancel = { isDragging = false },
                        onHorizontalDrag = { _, dragAmount ->
                            dragProgress = (dragProgress + dragAmount / barWidth).coerceIn(0f, 1f)
                        }
                    )
                },
            contentAlignment = Alignment.CenterStart
        ) {
            // Background track
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(colors.muted)
            )

            // Progress track
            Box(
                modifier = Modifier
                    .fillMaxWidth(if (isDragging) dragProgress else progress)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(colors.primary)
            )
        }

        Text(
            text = formatTime(durationMs),
            color = Color.White,
            fontSize = 12.sp
        )
    }
}

private fun formatTime(ms: Long): String {
    val totalSeconds = ms / 1000
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60

    return if (hours > 0) {
        String.format("%d:%02d:%02d", hours, minutes, seconds)
    } else {
        String.format("%d:%02d", minutes, seconds)
    }
}
```

**Step 2: Commit**

```bash
git add composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/presentation/screens/player/components/PlayerProgressBar.kt
git commit -m "feat(player): add PlayerProgressBar component"
```

### Task 16: Create TrackSelectorSheet component

**Files:**
- Create: `composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/presentation/screens/player/components/TrackSelectorSheet.kt`

**Step 1: Create the track selector sheet**

```kotlin
package com.lowiq.jellyfish.presentation.screens.player.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lowiq.jellyfish.domain.player.AudioTrack
import com.lowiq.jellyfish.domain.player.SubtitleTrack
import com.lowiq.jellyfish.presentation.theme.LocalJellyFishColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackSelectorSheet(
    audioTracks: List<AudioTrack>,
    subtitleTracks: List<SubtitleTrack>,
    onSelectAudioTrack: (Int) -> Unit,
    onSelectSubtitleTrack: (Int) -> Unit,
    onDisableSubtitles: () -> Unit,
    onDismiss: () -> Unit
) {
    val colors = LocalJellyFishColors.current
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = colors.card
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Audio section
            if (audioTracks.isNotEmpty()) {
                Text(
                    text = "Audio",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = colors.foreground
                )
                Spacer(modifier = Modifier.height(12.dp))

                audioTracks.forEach { track ->
                    TrackRow(
                        label = track.label,
                        subtitle = buildString {
                            track.codec?.let { append(it.uppercase()) }
                            track.channels?.let {
                                if (isNotEmpty()) append(" • ")
                                append(when(it) {
                                    1 -> "Mono"
                                    2 -> "Stereo"
                                    6 -> "5.1"
                                    8 -> "7.1"
                                    else -> "$it ch"
                                })
                            }
                        },
                        isSelected = track.isSelected,
                        onClick = { onSelectAudioTrack(track.index) }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(color = colors.border)
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Subtitles section
            Text(
                text = "Subtitles",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = colors.foreground
            )
            Spacer(modifier = Modifier.height(12.dp))

            // Off option
            val noSubtitleSelected = subtitleTracks.none { it.isSelected }
            TrackRow(
                label = "Off",
                subtitle = null,
                isSelected = noSubtitleSelected,
                onClick = onDisableSubtitles
            )

            subtitleTracks.forEach { track ->
                TrackRow(
                    label = track.label,
                    subtitle = track.language,
                    isSelected = track.isSelected,
                    onClick = { onSelectSubtitleTrack(track.index) }
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun TrackRow(
    label: String,
    subtitle: String?,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val colors = LocalJellyFishColors.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = isSelected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(
                selectedColor = colors.primary,
                unselectedColor = colors.mutedForeground
            )
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                fontSize = 16.sp,
                color = colors.foreground
            )
            subtitle?.let {
                Text(
                    text = it,
                    fontSize = 14.sp,
                    color = colors.mutedForeground
                )
            }
        }
        if (isSelected) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = colors.primary
            )
        }
    }
}
```

**Step 2: Commit**

```bash
git add composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/presentation/screens/player/components/TrackSelectorSheet.kt
git commit -m "feat(player): add TrackSelectorSheet component"
```

### Task 17: Create PlayerSettingsSheet component

**Files:**
- Create: `composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/presentation/screens/player/components/PlayerSettingsSheet.kt`

**Step 1: Create the settings sheet**

```kotlin
package com.lowiq.jellyfish.presentation.screens.player.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lowiq.jellyfish.domain.player.QualityOption
import com.lowiq.jellyfish.presentation.theme.LocalJellyFishColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerSettingsSheet(
    qualityOptions: List<QualityOption>,
    currentSpeed: Float,
    onSelectQuality: (Int) -> Unit,
    onSelectSpeed: (Float) -> Unit,
    onDismiss: () -> Unit
) {
    val colors = LocalJellyFishColors.current
    val sheetState = rememberModalBottomSheetState()

    val speedOptions = listOf(0.5f, 0.75f, 1f, 1.25f, 1.5f, 2f)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = colors.card
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Quality section
            if (qualityOptions.isNotEmpty()) {
                Text(
                    text = "Quality",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = colors.foreground
                )
                Spacer(modifier = Modifier.height(12.dp))

                qualityOptions.forEach { option ->
                    SettingsRow(
                        label = option.label,
                        subtitle = option.bitrate?.let { "${it / 1_000_000} Mbps" },
                        isSelected = option.isSelected,
                        onClick = { onSelectQuality(option.index) }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(color = colors.border)
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Playback speed section
            Text(
                text = "Playback Speed",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = colors.foreground
            )
            Spacer(modifier = Modifier.height(12.dp))

            speedOptions.forEach { speed ->
                SettingsRow(
                    label = when (speed) {
                        1f -> "Normal"
                        else -> "${speed}x"
                    },
                    subtitle = null,
                    isSelected = currentSpeed == speed,
                    onClick = { onSelectSpeed(speed) }
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun SettingsRow(
    label: String,
    subtitle: String?,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val colors = LocalJellyFishColors.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = isSelected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(
                selectedColor = colors.primary,
                unselectedColor = colors.mutedForeground
            )
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                fontSize = 16.sp,
                color = colors.foreground
            )
            subtitle?.let {
                Text(
                    text = it,
                    fontSize = 14.sp,
                    color = colors.mutedForeground
                )
            }
        }
        if (isSelected) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = colors.primary
            )
        }
    }
}
```

**Step 2: Commit**

```bash
git add composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/presentation/screens/player/components/PlayerSettingsSheet.kt
git commit -m "feat(player): add PlayerSettingsSheet component"
```

### Task 18: Create VideoPlayerScreen

**Files:**
- Create: `composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/presentation/screens/player/VideoPlayerScreen.kt`

**Step 1: Create the main screen (common code)**

```kotlin
package com.lowiq.jellyfish.presentation.screens.player

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.lowiq.jellyfish.presentation.screens.player.components.PlayerControls
import com.lowiq.jellyfish.presentation.screens.player.components.PlayerSettingsSheet
import com.lowiq.jellyfish.presentation.screens.player.components.TrackSelectorSheet
import com.lowiq.jellyfish.presentation.theme.LocalJellyFishColors
import org.koin.core.parameter.parametersOf

data class VideoPlayerScreen(
    val itemId: String,
    val title: String,
    val subtitle: String? = null,
    val startPositionMs: Long = 0
) : Screen {

    @Composable
    override fun Content() {
        val colors = LocalJellyFishColors.current
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = koinScreenModel<VideoPlayerScreenModel> {
            parametersOf(itemId, title, subtitle, startPositionMs)
        }
        val state by screenModel.state.collectAsState()

        LaunchedEffect(Unit) {
            screenModel.events.collect { event ->
                when (event) {
                    is VideoPlayerEvent.NavigateBack -> navigator.pop()
                    is VideoPlayerEvent.PlayNextEpisode -> {
                        navigator.replace(
                            VideoPlayerScreen(
                                itemId = event.episodeId,
                                title = "", // Will be loaded
                                subtitle = null
                            )
                        )
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            // Video surface (platform-specific)
            VideoSurface(
                videoPlayer = screenModel.videoPlayer,
                modifier = Modifier.fillMaxSize()
            )

            // Loading indicator
            if (state.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = colors.primary)
                }
            }

            // Error message
            state.error?.let { error ->
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = error,
                        color = colors.destructive
                    )
                }
            }

            // Controls overlay
            if (!state.isLoading && state.error == null) {
                PlayerControls(
                    title = state.title,
                    subtitle = state.subtitle,
                    playbackState = state.playbackState,
                    isVisible = state.controlsVisible,
                    onToggleVisibility = screenModel::onToggleControls,
                    onBackClick = screenModel::onBackPressed,
                    onPlayPauseClick = screenModel::onPlayPauseClick,
                    onSeekForward = screenModel::onSeekForward,
                    onSeekBackward = screenModel::onSeekBackward,
                    onSeekTo = screenModel::onSeekTo,
                    onSettingsClick = screenModel::onShowSettings,
                    onAudioSubtitlesClick = screenModel::onShowTrackSelector
                )
            }

            // Resume dialog
            if (state.showResumeDialog) {
                ResumeDialog(
                    positionMs = state.resumePositionMs,
                    onResume = screenModel::onResumeFromPosition,
                    onStartOver = screenModel::onStartFromBeginning
                )
            }

            // Track selector sheet
            if (state.showTrackSelector) {
                TrackSelectorSheet(
                    audioTracks = state.audioTracks,
                    subtitleTracks = state.subtitleTracks,
                    onSelectAudioTrack = screenModel::onSelectAudioTrack,
                    onSelectSubtitleTrack = screenModel::onSelectSubtitleTrack,
                    onDisableSubtitles = screenModel::onDisableSubtitles,
                    onDismiss = screenModel::onHideTrackSelector
                )
            }

            // Settings sheet
            if (state.showSettingsSheet) {
                PlayerSettingsSheet(
                    qualityOptions = state.qualityOptions,
                    currentSpeed = state.playbackSpeed,
                    onSelectQuality = screenModel::onSelectQuality,
                    onSelectSpeed = screenModel::onSetPlaybackSpeed,
                    onDismiss = screenModel::onHideSettings
                )
            }
        }
    }
}

@Composable
private fun ResumeDialog(
    positionMs: Long,
    onResume: () -> Unit,
    onStartOver: () -> Unit
) {
    val colors = LocalJellyFishColors.current
    val formattedTime = formatResumeTime(positionMs)

    AlertDialog(
        onDismissRequest = onStartOver,
        title = { Text("Resume Playback?") },
        text = { Text("Resume at $formattedTime?") },
        confirmButton = {
            TextButton(onClick = onResume) {
                Text("Resume", color = colors.primary)
            }
        },
        dismissButton = {
            TextButton(onClick = onStartOver) {
                Text("From Beginning")
            }
        },
        containerColor = colors.card
    )
}

private fun formatResumeTime(ms: Long): String {
    val totalSeconds = ms / 1000
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60

    return if (hours > 0) {
        String.format("%d:%02d:%02d", hours, minutes, seconds)
    } else {
        String.format("%d:%02d", minutes, seconds)
    }
}

@Composable
expect fun VideoSurface(
    videoPlayer: com.lowiq.jellyfish.domain.player.VideoPlayer,
    modifier: Modifier
)
```

**Step 2: Commit**

```bash
git add composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/presentation/screens/player/VideoPlayerScreen.kt
git commit -m "feat(player): add VideoPlayerScreen"
```

### Task 19: Create Android VideoSurface implementation

**Files:**
- Create: `composeApp/src/androidMain/kotlin/com/lowiq/jellyfish/presentation/screens/player/VideoSurface.android.kt`

**Step 1: Create the Android video surface**

```kotlin
package com.lowiq.jellyfish.presentation.screens.player

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.ui.PlayerView
import com.lowiq.jellyfish.domain.player.VideoPlayer

@Composable
actual fun VideoSurface(
    videoPlayer: VideoPlayer,
    modifier: Modifier
) {
    val exoPlayer = videoPlayer.getExoPlayer()

    DisposableEffect(Unit) {
        onDispose { }
    }

    AndroidView(
        factory = { context ->
            PlayerView(context).apply {
                player = exoPlayer
                useController = false // We use our own controls
            }
        },
        update = { playerView ->
            playerView.player = exoPlayer
        },
        modifier = modifier
    )
}
```

**Step 2: Commit**

```bash
git add composeApp/src/androidMain/kotlin/com/lowiq/jellyfish/presentation/screens/player/
git commit -m "feat(player): add Android VideoSurface implementation"
```

### Task 20: Create JVM/iOS VideoSurface stubs

**Files:**
- Create: `composeApp/src/jvmMain/kotlin/com/lowiq/jellyfish/presentation/screens/player/VideoSurface.jvm.kt`
- Create: `composeApp/src/iosMain/kotlin/com/lowiq/jellyfish/presentation/screens/player/VideoSurface.ios.kt`

**Step 1: Create JVM stub**

```kotlin
package com.lowiq.jellyfish.presentation.screens.player

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.lowiq.jellyfish.domain.player.VideoPlayer

@Composable
actual fun VideoSurface(
    videoPlayer: VideoPlayer,
    modifier: Modifier
) {
    Box(
        modifier = modifier.background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Desktop video playback not yet implemented",
            color = Color.White
        )
    }
}
```

**Step 2: Create iOS stub**

```kotlin
package com.lowiq.jellyfish.presentation.screens.player

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.lowiq.jellyfish.domain.player.VideoPlayer

@Composable
actual fun VideoSurface(
    videoPlayer: VideoPlayer,
    modifier: Modifier
) {
    Box(
        modifier = modifier.background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "iOS video playback not yet implemented",
            color = Color.White
        )
    }
}
```

**Step 3: Commit**

```bash
git add composeApp/src/jvmMain/kotlin/com/lowiq/jellyfish/presentation/screens/player/
git add composeApp/src/iosMain/kotlin/com/lowiq/jellyfish/presentation/screens/player/
git commit -m "feat(player): add JVM and iOS VideoSurface stubs"
```

---

## Phase 5: Integration

### Task 21: Register VideoPlayer and ScreenModel in Koin

**Files:**
- Modify: `composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/di/AppModule.kt`
- Modify: `composeApp/src/androidMain/kotlin/com/lowiq/jellyfish/di/PlatformModule.kt`
- Modify: `composeApp/src/jvmMain/kotlin/com/lowiq/jellyfish/di/PlatformModule.kt`
- Create: `composeApp/src/iosMain/kotlin/com/lowiq/jellyfish/di/PlatformModule.kt` (if not exists)

**Step 1: Add VideoPlayerScreenModel to presentationModule in AppModule.kt**

Add this import:
```kotlin
import com.lowiq.jellyfish.presentation.screens.player.VideoPlayerScreenModel
```

Add to `presentationModule`:
```kotlin
factory { (itemId: String, title: String, subtitle: String?, startPositionMs: Long) ->
    VideoPlayerScreenModel(itemId, title, subtitle, startPositionMs, get(), get(), get())
}
```

**Step 2: Add VideoPlayer to Android platformModule**

In `composeApp/src/androidMain/kotlin/com/lowiq/jellyfish/di/PlatformModule.kt`:

```kotlin
import com.lowiq.jellyfish.domain.player.VideoPlayer

// Add to platformModule:
factory { VideoPlayer(androidContext()) }
```

**Step 3: Add VideoPlayer to JVM platformModule**

In `composeApp/src/jvmMain/kotlin/com/lowiq/jellyfish/di/PlatformModule.kt`:

```kotlin
import com.lowiq.jellyfish.domain.player.VideoPlayer

// Add to platformModule:
factory { VideoPlayer() }
```

**Step 4: Add VideoPlayer to iOS platformModule** (create file if needed)

```kotlin
package com.lowiq.jellyfish.di

import com.lowiq.jellyfish.domain.player.VideoPlayer
import org.koin.dsl.module

actual val platformModule = module {
    factory { VideoPlayer() }
}
```

**Step 5: Commit**

```bash
git add composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/di/AppModule.kt
git add composeApp/src/androidMain/kotlin/com/lowiq/jellyfish/di/PlatformModule.kt
git add composeApp/src/jvmMain/kotlin/com/lowiq/jellyfish/di/PlatformModule.kt
git add composeApp/src/iosMain/kotlin/com/lowiq/jellyfish/di/PlatformModule.kt
git commit -m "feat(player): register VideoPlayer and ScreenModel in Koin"
```

### Task 22: Update MovieDetailScreenModel to navigate to player

**Files:**
- Modify: `composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/presentation/screens/detail/MovieDetailScreenModel.kt`

**Step 1: Add play event and update onPlay()**

Add this event class before `MovieDetailScreenModel`:
```kotlin
sealed class MovieDetailEvent {
    data class PlayVideo(
        val itemId: String,
        val title: String,
        val startPositionMs: Long
    ) : MovieDetailEvent()
}
```

Add event flow to the class:
```kotlin
private val _events = MutableSharedFlow<MovieDetailEvent>()
val events = _events.asSharedFlow()
```

Update `onPlay()`:
```kotlin
fun onPlay() {
    screenModelScope.launch {
        _events.emit(
            MovieDetailEvent.PlayVideo(
                itemId = itemId,
                title = _state.value.title,
                startPositionMs = 0 // TODO: Get from playback position
            )
        )
    }
}
```

**Step 2: Commit**

```bash
git add composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/presentation/screens/detail/MovieDetailScreenModel.kt
git commit -m "feat(player): add play event to MovieDetailScreenModel"
```

### Task 23: Update MovieDetailScreen to handle play event

**Files:**
- Modify: `composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/presentation/screens/detail/MovieDetailScreen.kt`

**Step 1: Add LaunchedEffect to handle events**

Add import:
```kotlin
import com.lowiq.jellyfish.presentation.screens.player.VideoPlayerScreen
import androidx.compose.runtime.LaunchedEffect
```

Add after `val state by screenModel.state.collectAsState()`:
```kotlin
LaunchedEffect(Unit) {
    screenModel.events.collect { event ->
        when (event) {
            is MovieDetailEvent.PlayVideo -> {
                navigator.push(
                    VideoPlayerScreen(
                        itemId = event.itemId,
                        title = event.title,
                        startPositionMs = event.startPositionMs
                    )
                )
            }
        }
    }
}
```

**Step 2: Commit**

```bash
git add composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/presentation/screens/detail/MovieDetailScreen.kt
git commit -m "feat(player): handle play event in MovieDetailScreen"
```

### Task 24: Build and test Android

**Step 1: Build Android debug APK**

Run: `./gradlew :composeApp:assembleDebug`
Expected: BUILD SUCCESSFUL

**Step 2: Test on device/emulator**

- Install APK on device
- Navigate to a movie detail screen
- Press Play button
- Verify video player opens and playback begins

**Step 3: Commit final integration**

```bash
git add -A
git commit -m "feat(player): complete video player integration"
```

---

## Summary

This implementation plan creates a full-featured video player with:

1. **Domain layer**: PlaybackState, AudioTrack, SubtitleTrack models + VideoPlayer interface
2. **Platform implementations**: ExoPlayer for Android, stubs for JVM/iOS
3. **Data layer**: Streaming API methods for Jellyfin
4. **Presentation layer**: VideoPlayerScreen with shadcn/ui styled controls
5. **Integration**: Koin DI setup and navigation from detail screens

Total tasks: 24
Estimated commits: 24+
