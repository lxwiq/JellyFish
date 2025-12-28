# Android Fullscreen & Video Scaling

## Overview

Add fullscreen immersive mode during video playback and video scaling options (Fit, Fill, Stretch) with a dedicated toggle button in player controls.

## Requirements

1. **Fullscreen Mode**: Hide system bars (status bar + navigation bar) only during video playback
2. **Video Scaling**: Three modes - Fit, Fill, Stretch
3. **UI Control**: Dedicated button in player controls to cycle through scaling modes

## Implementation

### Part 1: Immersive Mode

**File:** `composeApp/src/androidMain/kotlin/com/lowiq/jellyfish/presentation/screens/player/VideoSurface.android.kt`

Add a `DisposableEffect` that enables sticky immersive mode via `WindowInsetsController`:

```kotlin
DisposableEffect(Unit) {
    val activity = context as? Activity
    val window = activity?.window
    val controller = window?.insetsController

    controller?.hide(WindowInsets.Type.systemBars())
    controller?.systemBarsBehavior =
        WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

    onDispose {
        controller?.show(WindowInsets.Type.systemBars())
    }
}
```

**Behavior:**
- System bars hide when entering player
- Swipe from edge shows bars temporarily
- Bars restore when leaving player

### Part 2: Video Scale Modes

**New enum in `commonMain`:**

```kotlin
enum class VideoScaleMode {
    FIT,    // Full video visible, letterboxed if needed
    FILL,   // Fill screen, may crop edges
    STRETCH // Stretch to fill (may distort)
}
```

**VLC Implementation (Android):**

| Mode | VLC ScaleType |
|------|---------------|
| FIT | `SURFACE_BEST_FIT` |
| FILL | `SURFACE_FILL` |
| STRETCH | `SURFACE_FIT_SCREEN` |

Add to `VideoPlayer.android.kt`:

```kotlin
actual fun setScaleMode(mode: VideoScaleMode) {
    val vlcScale = when (mode) {
        VideoScaleMode.FIT -> MediaPlayer.ScaleType.SURFACE_BEST_FIT
        VideoScaleMode.FILL -> MediaPlayer.ScaleType.SURFACE_FILL
        VideoScaleMode.STRETCH -> MediaPlayer.ScaleType.SURFACE_FIT_SCREEN
    }
    mediaPlayer?.videoScale = vlcScale
}
```

### Part 3: UI Button

**Location:** Bottom row of `PlayerControls.kt`, next to Settings and Audio/Subtitles buttons.

**Icons per mode:**

| Mode | Icon | Label |
|------|------|-------|
| FIT | `FitScreen` | "Fit" |
| FILL | `Crop` | "Fill" |
| STRETCH | `AspectRatio` | "Stretch" |

**Behavior:**
- Single tap cycles to next mode
- Icon updates immediately
- Optional: brief label/toast shows new mode name

## Files to Modify

1. `VideoSurface.android.kt` - Add immersive mode
2. `VideoPlayer.kt` (commonMain) - Add `expect fun setScaleMode()`
3. `VideoPlayer.android.kt` - Add `actual fun setScaleMode()`
4. `VideoPlayer.jvm.kt` - Add `actual fun setScaleMode()` (stub or VLC implementation)
5. `VideoPlayer.ios.kt` - Add `actual fun setScaleMode()` (stub)
6. `VideoPlayerState.kt` - Add `scaleMode: VideoScaleMode`
7. `VideoPlayerScreenModel.kt` - Add state management and `onCycleScaleMode()`
8. `PlayerControls.kt` - Add scale mode button

## New Files

1. `VideoScaleMode.kt` (commonMain) - Enum definition
