# VLC Integration Design

## Problem

Videos play without audio when the source file contains AC3, EAC3, DTS, or TrueHD audio codecs. ExoPlayer cannot decode these formats without the FFmpeg extension, which requires manual building.

## Solution

Replace ExoPlayer with VLC (libVLC) which natively supports all audio codecs.

## Architecture

The existing `expect/actual` pattern makes this swap straightforward:

```
commonMain/
  VideoPlayer.kt (expect) ─── Interface unchanged

androidMain/
  VideoPlayer.android.kt ─── ExoPlayer → VLC
  VideoSurface.android.kt ─── PlayerView → VLCVideoLayout
```

## Dependencies

### Remove (Media3/ExoPlayer)
```toml
# libs.versions.toml - REMOVE
media3 = "1.5.1"
media3-exoplayer = { module = "androidx.media3:media3-exoplayer" }
media3-exoplayer-hls = { module = "androidx.media3:media3-exoplayer-hls" }
media3-ui = { module = "androidx.media3:media3-ui" }
media3-session = { module = "androidx.media3:media3-session" }
```

### Add (libVLC)
```toml
# libs.versions.toml - ADD
libvlc = "3.5.4"
libvlc-all = { module = "org.videolan.android:libvlc-all", version.ref = "libvlc" }
```

## Implementation

### VideoPlayer.android.kt

Key changes:
- `ExoPlayer` → `LibVLC` + `MediaPlayer`
- `Player.Listener` → `MediaPlayer.EventListener`
- `MediaSource` → `Media` object
- `trackSelectionParameters` → `audioTrack`/`spuTrack` setters

### VideoSurface.android.kt

Key changes:
- `PlayerView` → `VLCVideoLayout`
- `player = exoPlayer` → `attachViews(layout, null, false, false)`

### Track Handling

| ExoPlayer | VLC |
|-----------|-----|
| `currentTracks.groups` | `audioTracks` / `spuTracks` |
| `TrackSelectionOverride` | `audioTrack = id` |
| `C.TRACK_TYPE_AUDIO` | `TrackDescription` |

## Audio Codec Support

| Codec | ExoPlayer | VLC |
|-------|-----------|-----|
| AAC | ✓ | ✓ |
| MP3 | ✓ | ✓ |
| AC3 | Device-dependent | ✓ |
| EAC3 | Device-dependent | ✓ |
| DTS | ✗ | ✓ |
| TrueHD | ✗ | ✓ |

## Files to Modify

1. `gradle/libs.versions.toml`
2. `composeApp/build.gradle.kts`
3. `composeApp/src/androidMain/kotlin/com/lowiq/jellyfish/domain/player/VideoPlayer.android.kt`
4. `composeApp/src/androidMain/kotlin/com/lowiq/jellyfish/presentation/screens/player/VideoSurface.android.kt`

## Files Unchanged

- `VideoPlayer.kt` (expect interface)
- `VideoPlayerScreenModel.kt`
- `VideoPlayerScreen.kt`
- All UI components
