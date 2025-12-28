# Desktop Video Player Implementation Design

**Date:** 2025-12-28
**Status:** Approved

## Overview

Implement a fully functional video player for Desktop (JVM) with complete feature parity with the existing Android implementation. The player will use VLCJ with embedded VLC binaries for a standalone application.

## Decisions

| Decision | Choice | Rationale |
|----------|--------|-----------|
| Video library | VLCJ (uk.co.caprica:vlcj) | Same VLC engine as Android, consistent API |
| Rendering mode | Direct Rendering (CallbackVideoSurface) | Simpler Compose integration, sufficient performance |
| Feature scope | Full parity with Android | Same codebase patterns, complete UX |
| VLC distribution | Embedded binaries | No external dependencies, standalone app |

## Architecture

### Dependencies

```kotlin
// build.gradle.kts - jvmMain.dependencies
implementation("uk.co.caprica:vlcj:4.8.3")
```

### VLC Binaries Structure

```
composeApp/
├── src/jvmMain/resources/
│   └── vlc/
│       ├── macos-arm64/    # libvlc.dylib + plugins
│       ├── macos-x64/
│       ├── windows-x64/    # libvlc.dll + plugins
│       └── linux-x64/      # libvlc.so + plugins
```

Estimated size: ~80-120 MB per platform.

### Files to Modify/Create

1. **VideoPlayer.jvm.kt** - Full VLCJ implementation
2. **VideoSurface.jvm.kt** - Compose direct rendering
3. **VlcNativeLoader.kt** (new) - Extract and load embedded VLC
4. **build.gradle.kts** - Add VLCJ dependency

## Implementation Details

### VideoPlayer.jvm.kt

```kotlin
actual class VideoPlayer {
    private lateinit var mediaPlayerFactory: MediaPlayerFactory
    private lateinit var mediaPlayer: EmbeddedMediaPlayer

    private val _playbackState = MutableStateFlow<PlaybackState>(PlaybackState.Idle)
    private val _audioTracks = MutableStateFlow<List<AudioTrack>>(emptyList())
    private val _subtitleTracks = MutableStateFlow<List<SubtitleTrack>>(emptyList())

    private var positionUpdateJob: Job? = null
    private val scope = CoroutineScope(Dispatchers.Default)
    private var currentPlaybackSpeed = 1f
    private var currentScaleMode = VideoScaleMode.FIT

    // Frame callback for Compose rendering
    private var onFrameReady: ((ByteBuffer, Int, Int) -> Unit)? = null

    actual fun initialize() {
        val vlcPath = VlcNativeLoader.loadNativeLibraries()
        NativeLibrary.addSearchPath("vlc", vlcPath)

        mediaPlayerFactory = MediaPlayerFactory("--no-video-title-show", "--quiet")
        mediaPlayer = mediaPlayerFactory.mediaPlayers().newEmbeddedMediaPlayer()

        // Setup callback video surface
        mediaPlayer.videoSurface().set(CallbackVideoSurface(...))

        // Attach event listeners
        mediaPlayer.events().addMediaPlayerEventListener(object : MediaPlayerEventAdapter() {
            override fun playing(player: MediaPlayer) { updatePlaybackState() }
            override fun paused(player: MediaPlayer) { updatePlaybackState() }
            override fun stopped(player: MediaPlayer) { _playbackState.value = PlaybackState.Idle }
            override fun error(player: MediaPlayer) {
                _playbackState.value = PlaybackState.Error("Playback error", true)
            }
        })
    }
}
```

### VideoSurface.jvm.kt

```kotlin
@Composable
actual fun VideoSurface(
    videoPlayer: VideoPlayer,
    modifier: Modifier
) {
    var imageBitmap by remember { mutableStateOf<ImageBitmap?>(null) }

    DisposableEffect(videoPlayer) {
        videoPlayer.setRenderCallback { buffer, width, height ->
            imageBitmap = buffer.toComposeImageBitmap(width, height)
        }
        onDispose { videoPlayer.clearRenderCallback() }
    }

    Box(modifier = modifier.background(Color.Black)) {
        imageBitmap?.let { bitmap ->
            Image(
                bitmap = bitmap,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = videoPlayer.currentScaleMode.toContentScale()
            )
        }
    }
}
```

### VlcNativeLoader.kt

```kotlin
object VlcNativeLoader {
    fun loadNativeLibraries(): String {
        val os = detectOS()  // macos-arm64, macos-x64, windows-x64, linux-x64
        val cacheDir = File(System.getProperty("user.home"), ".jellyfish/vlc")

        if (!cacheDir.exists()) {
            extractVlcTo(cacheDir, os)
        }

        return cacheDir.absolutePath
    }

    private fun detectOS(): String {
        val osName = System.getProperty("os.name").lowercase()
        val osArch = System.getProperty("os.arch")
        return when {
            osName.contains("mac") && osArch == "aarch64" -> "macos-arm64"
            osName.contains("mac") -> "macos-x64"
            osName.contains("win") -> "windows-x64"
            else -> "linux-x64"
        }
    }
}
```

### Features Implementation

All methods mirror the Android implementation:

- **play()** - Load media with headers support
- **pause() / resume()** - Toggle playback
- **seekTo() / seekForward() / seekBackward()** - Seeking
- **setPlaybackSpeed()** - Rate control
- **selectAudioTrack()** - Audio track selection
- **addExternalSubtitle() / selectSubtitleTrack() / disableSubtitles()** - Subtitle support
- **setScaleMode()** - FIT / FILL / STRETCH modes

## Obtaining VLC Binaries

VLC binaries can be obtained from:
- **macOS:** Extract from VLC.app/Contents/MacOS/lib/
- **Windows:** VLC installation directory
- **Linux:** /usr/lib/vlc/ or build from source

Alternatively, use a Gradle task to download official VLC releases during build.

## Testing

1. Test on macOS (arm64 and x64)
2. Test on Windows
3. Test on Linux
4. Verify all playback controls work
5. Test subtitle loading (external SRT/ASS)
6. Test audio track switching
7. Test playback speed changes
8. Test scale modes
