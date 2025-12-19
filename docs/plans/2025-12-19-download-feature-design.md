# Download Feature Design

## Overview

Implement offline download functionality for JellyFish with server-side transcoding quality selection, progress tracking, and Jellyfin playback synchronization.

## Requirements

### Functional Requirements

1. **Download all video content** - Movies and series episodes
2. **Quality selection** - User chooses transcoding quality (Original, 1080p, 720p, 480p) via Jellyfin server-side transcoding
3. **Storage** - Internal app storage (no permissions required)
4. **Parallel downloads** - Configurable number of simultaneous downloads
5. **Downloads page** - Dedicated screen showing progress and completed downloads
6. **Notifications** - System notifications (background) + in-app notifications (foreground)
7. **Offline playback** - Access downloaded content without internet
8. **Jellyfin sync** - Playback progress syncs to Jellyfin when back online
9. **Storage management** - Manual deletion + optional auto-cleanup with configurable limit

### Non-Functional Requirements

- Works on Android, iOS, and Desktop (KMP)
- Minimal battery impact during downloads
- Resilient to app restarts (resume downloads)

## Architecture

### Component Overview

```
┌─────────────────────────────────────────────────────────────┐
│                      Presentation Layer                      │
├──────────────────┬──────────────────┬───────────────────────┤
│  DownloadsScreen │  QualityDialog   │  DownloadNotification │
│  (list + progress)│  (quality picker)│  (in-app banner)      │
└────────┬─────────┴────────┬─────────┴───────────┬───────────┘
         │                  │                     │
┌────────▼──────────────────▼─────────────────────▼───────────┐
│                       Domain Layer                           │
├─────────────────┬───────────────────┬───────────────────────┤
│ DownloadManager │ PlaybackTracker   │ StorageManager        │
│ (queue, state)  │ (progress sync)   │ (space, cleanup)      │
└────────┬────────┴─────────┬─────────┴───────────┬───────────┘
         │                  │                     │
┌────────▼──────────────────▼─────────────────────▼───────────┐
│                        Data Layer                            │
├─────────────────┬───────────────────┬───────────────────────┤
│ DownloadRepo    │ SyncQueueRepo     │ DownloadDatabase      │
│ (file I/O)      │ (pending syncs)   │ (SQLDelight)          │
└─────────────────┴───────────────────┴───────────────────────┘
```

### Data Models

```kotlin
// Download state
enum class DownloadStatus {
    QUEUED,      // Waiting in queue
    DOWNLOADING, // Currently downloading
    PAUSED,      // Paused by user
    COMPLETED,   // Successfully downloaded
    FAILED       // Download failed
}

// Download entity
data class Download(
    val id: String,              // UUID
    val itemId: String,          // Jellyfin item ID
    val serverId: String,        // Server ID
    val title: String,           // Display title
    val subtitle: String?,       // Episode info if applicable
    val imageUrl: String?,       // Thumbnail
    val quality: String,         // "Original", "1080p", etc.
    val status: DownloadStatus,
    val progress: Float,         // 0.0 to 1.0
    val totalBytes: Long,
    val downloadedBytes: Long,
    val filePath: String?,       // Local file path when completed
    val createdAt: Long,         // Timestamp
    val completedAt: Long?       // Completion timestamp
)

// Quality option from Jellyfin
data class QualityOption(
    val id: String,
    val label: String,           // "1080p", "720p", etc.
    val bitrate: Int,            // Bitrate in kbps
    val estimatedSize: Long      // Estimated file size
)

// Pending sync for offline playback
data class PendingPlaybackSync(
    val id: String,
    val itemId: String,
    val serverId: String,
    val positionTicks: Long,
    val playedPercentage: Float,
    val timestamp: Long
)
```

### Database Schema (SQLDelight)

```sql
-- downloads.sq
CREATE TABLE downloads (
    id TEXT PRIMARY KEY,
    item_id TEXT NOT NULL,
    server_id TEXT NOT NULL,
    title TEXT NOT NULL,
    subtitle TEXT,
    image_url TEXT,
    quality TEXT NOT NULL,
    status TEXT NOT NULL,
    progress REAL NOT NULL DEFAULT 0,
    total_bytes INTEGER NOT NULL DEFAULT 0,
    downloaded_bytes INTEGER NOT NULL DEFAULT 0,
    file_path TEXT,
    created_at INTEGER NOT NULL,
    completed_at INTEGER
);

-- pending_syncs.sq
CREATE TABLE pending_syncs (
    id TEXT PRIMARY KEY,
    item_id TEXT NOT NULL,
    server_id TEXT NOT NULL,
    position_ticks INTEGER NOT NULL,
    played_percentage REAL NOT NULL,
    timestamp INTEGER NOT NULL
);

-- download_settings.sq
CREATE TABLE download_settings (
    key TEXT PRIMARY KEY,
    value TEXT NOT NULL
);
```

## User Interface

### Downloads Screen

```
┌─────────────────────────────────────────────────────────────┐
│  ← Téléchargements                        Espace: 2.3 Go    │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  EN COURS (2)                                    [⏸ Tout]  │
│  ┌─────────────────────────────────────────────────────┐   │
│  │ [Poster] Movie Title                                │   │
│  │          720p • 1.2 Go                              │   │
│  │          ▓▓▓▓▓▓▓░░░░░░░░░░░ 45%           [⏸] [✕]  │   │
│  ├─────────────────────────────────────────────────────┤   │
│  │ [Poster] Series - S01E03                            │   │
│  │          1080p • 2.1 Go                             │   │
│  │          ▓▓░░░░░░░░░░░░░░░░ 12%           [⏸] [✕]  │   │
│  └─────────────────────────────────────────────────────┘   │
│                                                             │
│  TÉLÉCHARGÉS (3)                                           │
│  ┌─────────────────────────────────────────────────────┐   │
│  │ [Poster] Film A                                     │   │
│  │          1080p • 1.2 Go • Téléchargé hier    [▶][🗑]│   │
│  ├─────────────────────────────────────────────────────┤   │
│  │ [Poster] Film B                                     │   │
│  │          720p • 800 Mo • Téléchargé il y a 3j[▶][🗑]│   │
│  └─────────────────────────────────────────────────────┘   │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

### Quality Selection Dialog

```
┌─────────────────────────────────────────┐
│  Choisir la qualité                     │
├─────────────────────────────────────────┤
│  ○ Original     ~4.2 Go                 │
│  ● 1080p        ~2.1 Go    (Recommandé) │
│  ○ 720p         ~1.2 Go                 │
│  ○ 480p         ~600 Mo                 │
├─────────────────────────────────────────┤
│  □ Ne plus demander (utiliser 1080p)    │
├─────────────────────────────────────────┤
│        [Annuler]    [Télécharger]       │
└─────────────────────────────────────────┘
```

### Settings Section

```
┌─────────────────────────────────────────────────────────────┐
│  Téléchargements                                            │
├─────────────────────────────────────────────────────────────┤
│  Qualité par défaut                              [1080p ▼]  │
│  Toujours demander la qualité                        [OFF]  │
│  Téléchargements simultanés                           [2 ▼] │
├─────────────────────────────────────────────────────────────┤
│  Gestion du stockage                                        │
│  Espace utilisé                                    2.3 Go   │
│  Limite automatique                                  [OFF]  │
│  Limite de stockage                               [10 Go ▼] │
│  Supprimer tous les téléchargements                    [→]  │
└─────────────────────────────────────────────────────────────┘
```

## Download Flow

### 1. Initiate Download

```
User clicks Download button
        │
        ▼
┌───────────────────┐
│ Check "always ask"│
│ setting           │
└────────┬──────────┘
         │
    ┌────┴────┐
    │         │
  Yes         No
    │         │
    ▼         ▼
┌────────┐  ┌─────────────┐
│ Show   │  │ Use default │
│ dialog │  │ quality     │
└───┬────┘  └──────┬──────┘
    │              │
    └──────┬───────┘
           ▼
┌─────────────────────┐
│ Fetch quality       │
│ options from server │
└──────────┬──────────┘
           ▼
┌─────────────────────┐
│ Create Download     │
│ entry (QUEUED)      │
└──────────┬──────────┘
           ▼
┌─────────────────────┐
│ DownloadManager     │
│ picks up from queue │
└─────────────────────┘
```

### 2. Download Execution

```kotlin
// Jellyfin transcoding URL pattern
fun getDownloadUrl(
    serverUrl: String,
    itemId: String,
    token: String,
    bitrate: Int
): String {
    return "$serverUrl/Videos/$itemId/stream" +
        "?static=false" +
        "&mediaSourceId=$itemId" +
        "&videoBitRate=$bitrate" +
        "&audioBitRate=192000" +
        "&api_key=$token"
}
```

### 3. Offline Playback & Sync

```
User plays downloaded content
        │
        ▼
┌───────────────────────┐
│ VideoPlayer reads     │
│ local file            │
└──────────┬────────────┘
           │
           ▼
┌───────────────────────┐
│ PlaybackTracker       │
│ saves position locally│
│ every 10 seconds      │
└──────────┬────────────┘
           │
           ▼
┌───────────────────────┐
│ Add to PendingSync    │
│ queue (SQLite)        │
└──────────┬────────────┘
           │
     ┌─────┴─────┐
     │           │
  Online      Offline
     │           │
     ▼           ▼
┌─────────┐  ┌──────────┐
│ Sync    │  │ Keep in  │
│ to API  │  │ queue    │
└─────────┘  └──────────┘
```

## Platform-Specific Implementation

### Android
- Use `WorkManager` for background downloads (survives app kill)
- `NotificationCompat` for system notifications
- `Context.filesDir` for internal storage

### iOS
- Use `URLSession` background download tasks
- `UNUserNotificationCenter` for notifications
- `FileManager.default.urls(for: .documentDirectory)` for storage

### Desktop (JVM)
- Use Kotlin Coroutines with `Dispatchers.IO`
- No system notifications (in-app only)
- `System.getProperty("user.home")/.jellyfish/downloads` for storage

## Settings Keys

| Key | Type | Default | Description |
|-----|------|---------|-------------|
| `download_quality_default` | String | "1080p" | Default quality |
| `download_always_ask` | Boolean | true | Show quality dialog |
| `download_parallel_count` | Int | 2 | Max simultaneous downloads |
| `storage_auto_cleanup` | Boolean | false | Enable auto cleanup |
| `storage_limit_gb` | Int | 10 | Storage limit in GB |

## Error Handling

| Error | Handling |
|-------|----------|
| Network lost during download | Pause download, resume when back online |
| Storage full | Notify user, pause download queue |
| File corrupted | Delete and offer retry |
| Server unreachable | Retry with exponential backoff (max 3 attempts) |
| Transcoding failed | Fallback to next lower quality or original |

## Testing Strategy

1. **Unit tests** - DownloadManager queue logic, StorageManager calculations
2. **Integration tests** - Database operations, file I/O
3. **UI tests** - Downloads screen, quality dialog
4. **Manual tests** - Offline playback, background downloads, sync after reconnection
