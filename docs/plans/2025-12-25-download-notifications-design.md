# Download Progress Indicator & Notifications Design

## Overview

Add real-time download progress visibility in the sidebar and Android notifications for background downloads.

## Requirements

### Sidebar Indicator
- Circular progress arc around the Downloads icon showing average progress of all active downloads
- Badge showing count of active downloads (1, 2, 3...)
- Progress = average of all individual download progressions

### Android Notifications
- Only shown when app is in background
- Grouped notification style (Gmail-like):
  - Summary notification: "X téléchargements - Y%"
  - Individual notifications per download with progress bar
- On completion: notification shows "✓ Terminé" for 3 seconds then disappears
- Display clean titles (movie/series/episode names, not filenames)

## Architecture

### New Components

#### DownloadStateHolder (commonMain)
```kotlin
class DownloadStateHolder(downloadManager: DownloadManager) {
    val activeDownloads: StateFlow<List<ActiveDownload>>
    val activeCount: StateFlow<Int>
    val averageProgress: StateFlow<Float>  // 0.0 to 1.0
}

data class ActiveDownload(
    val id: String,
    val title: String,
    val progress: Float
)
```

#### DownloadNotifier (expect/actual)
- `expect class DownloadNotifier` in commonMain
- `actual class DownloadNotifier` in androidMain (full implementation)
- No-op implementations for jvmMain and iosMain

#### DownloadIndicator (Composable)
- Draws circular progress arc around icon
- Draws badge with count
- Uses theme colors

### Data Flow

```
DownloadManager.downloadEvents
    → DownloadStateHolder (collects and aggregates)
    → StateFlow<activeCount, averageProgress>
    → NavigationRail/DownloadIndicator (UI)
    → DownloadNotifier (Android notifications)
```

### Notification Lifecycle

1. App in foreground → No notifications, sidebar badge active
2. App goes to background → DownloadNotifier.start()
3. Download progresses → Update notification
4. Download completes → Show "✓ Terminé" 3sec → Dismiss
5. App returns to foreground → DownloadNotifier.stop()

## Files to Create

- `commonMain/.../domain/download/DownloadStateHolder.kt`
- `commonMain/.../domain/download/DownloadNotifier.kt` (expect)
- `androidMain/.../domain/download/DownloadNotifier.android.kt`
- `jvmMain/.../domain/download/DownloadNotifier.jvm.kt`
- `iosMain/.../domain/download/DownloadNotifier.ios.kt`
- `commonMain/.../presentation/components/DownloadIndicator.kt`

## Files to Modify

- `NavigationRail.kt` - add indicator parameters
- `AppScaffold.kt` - pass download state
- `HomeScreen.kt` - inject DownloadStateHolder
- `AppModule.kt` - register new dependencies
- `AndroidManifest.xml` - notification permissions + foreground service
