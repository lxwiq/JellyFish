# Video Player Design

## Overview

Implementation of a video player for JellyFish with Direct Play support, targeting Android first with a shared architecture for future iOS/Desktop support.

## Requirements

- **Platform**: Android first, architecture shared via expect/actual
- **Playback**: Direct Play priority, transcoding fallback
- **Style**: shadcn/ui design language (consistent with existing app theme)
- **Features**: Complete feature set (see below)

## Architecture

### Code Structure

```
commonMain/
  └── presentation/screens/player/
      ├── VideoPlayerScreen.kt      # Shared Compose screen
      ├── VideoPlayerScreenModel.kt # Business logic
      ├── VideoPlayerState.kt       # Player state
      └── components/
          ├── PlayerControls.kt     # Controls overlay
          ├── ProgressBar.kt        # shadcn-style progress bar
          ├── TrackSelector.kt      # Audio/subtitle sheet
          └── PlayerSettingsSheet.kt

commonMain/
  └── domain/player/
      ├── VideoPlayer.kt            # expect class - common interface
      └── PlaybackState.kt          # States: Idle, Buffering, Playing, Paused, Error

androidMain/
  └── domain/player/
      └── VideoPlayer.android.kt    # actual class - ExoPlayer implementation
```

### Key Principle

UI (Compose) is 100% shared. Only `VideoPlayer` has platform-specific implementations.

## UI Design (shadcn/ui Style)

### Visual Philosophy

- **Background**: Semi-transparent overlay with slight blur (`background.copy(alpha = 0.8f)`)
- **Controls**: Ghost and outline button styles from existing theme
- **Progress bar**: Thin (4dp), rounded corners, `primary` color on `muted` background
- **Panels**: Bottom sheets with Card style (card background, subtle border)
- **Animations**: Smooth fade in/out (300ms), no abrupt transitions

### Controls Layout

```
┌─────────────────────────────────────────────────────┐
│ ← Back                Title - S01E03          ⚙️ ••• │  ← Header (ghost buttons)
│                                                     │
│                                                     │
│                                                     │
│                    [ -10s ]  [ +10s ]               │
│                       [  ▶  ]                       │  ← Center (large buttons)
│                                                     │
│                                                     │
│ advancement advancement advancement advancement advancement advancement advancement advancement advancement advancement advancement advancement advancement advancement advancement advancement advancement advancement advancement  │  ← Progress bar
│ 23:45                                      1:32:00 │
│                                                     │
│  [Audio]  [Subtitles]  [Quality]  [Speed]          │  ← Footer (outline pills)
└─────────────────────────────────────────────────────┘
```

### Behavior

- Tap on video → toggle controls (fade animation)
- Controls hide after 4s of inactivity
- Progress bar always visible in "discreet" mode (thin line at bottom)

## Features

### Playback Controls

- Play/Pause: Center button (`primary` style, round, 32dp icon)
- Skip ±10s: Ghost buttons on each side
- Double-tap zones: Left/right for ±10s (ripple feedback)
- Seekable progress bar with thumbnail preview on drag (if available)

### Track Selector (Bottom Sheet)

```
┌─ Audio ──────────────────────────────┐
│ ○ English (AAC 5.1)                  │
│ ● Français (AAC Stereo)    ✓         │
│ ○ Japanese (AAC Stereo)              │
├─ Subtitles ──────────────────────────┤
│ ○ Disabled                           │
│ ● Français                 ✓         │
│ ○ English (SDH)                      │
└──────────────────────────────────────┘
```

### Settings (Bottom Sheet)

- Quality: Auto / Original (Direct Play) / 1080p / 720p / 480p
- Speed: 0.5x / 0.75x / 1x / 1.25x / 1.5x / 2x
- Each option uses RadioButton shadcn style

### Next Episode

- Notification 30s before end: Card in bottom-right "Next episode in 30s"
- Buttons: "Play now" / "Cancel"
- Auto-play if no action taken

### Picture-in-Picture

- Button in header (PiP icon)
- Also triggers on back navigation (optional)

## Jellyfin Integration

### Video Stream URLs

```kotlin
// Direct Play URL
GET /Videos/{itemId}/stream?static=true&mediaSourceId={id}
Headers: X-Emby-Token: {token}

// Transcoding URL (if needed)
GET /Videos/{itemId}/master.m3u8?mediaSourceId={id}&audioCodec=aac&videoCodec=h264
```

### Progress Reporting

For "Continue Watching" feature across all Jellyfin clients:

- `POST /Sessions/Playing` → on playback start
- `POST /Sessions/Playing/Progress` → every 10s during playback
- `POST /Sessions/Playing/Stopped` → on stop/close

### Resume Playback

On launch, if `playbackPositionTicks > 0`, show dialog:
> "Resume at 45:23?" → [Resume] [From beginning]

### Error Handling

- Unsupported codec → offer transcoding option
- Network error → automatic retry with backoff, then user message
- Timeout → display state with "Retry" button

## Technical Notes

### ExoPlayer (Android)

- Use Media3 ExoPlayer for modern API
- Configure for Jellyfin authentication headers
- Support HLS for transcoded streams
- Support direct file streams for Direct Play

### State Management

```kotlin
sealed class PlaybackState {
    object Idle : PlaybackState()
    object Buffering : PlaybackState()
    data class Playing(val position: Long, val duration: Long) : PlaybackState()
    data class Paused(val position: Long, val duration: Long) : PlaybackState()
    data class Error(val message: String, val canRetry: Boolean) : PlaybackState()
}
```

### Navigation

- New screen: `VideoPlayerScreen(itemId: String, startPosition: Long = 0)`
- Full-screen immersive mode
- System UI hidden during playback
