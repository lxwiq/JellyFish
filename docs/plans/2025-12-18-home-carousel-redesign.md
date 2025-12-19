# Home Page Carousel Redesign

## Overview

Replace the current vertical list activity feed with horizontal carousels organized by sections, similar to Netflix/Plex style.

## Sections

Six sections displayed in this order:
1. Continue Watching - Items with playback progress
2. Latest Movies - Recently added movies
3. Latest Series - Recently added series
4. Latest Music - Recently added music
5. Favorites - User's favorite items
6. Next Up - Next episode to watch in series

Empty sections are hidden.

## Components

### MediaCarousel

Reusable section component containing:
- Section title (headline style)
- Horizontal LazyRow with MediaCard items
- Free horizontal scroll (no navigation arrows)
- 12dp spacing between cards

### MediaCard

Individual card component:
- Ratio: 16:9 (160dp x 90dp)
- Rounded corners: 8dp
- Content:
  - Backdrop image filling the card
  - Placeholder (#27272A) with Play icon if no image
  - Title below image (1-2 lines, ellipsis)
  - Optional subtitle for episodes (series name, S1 E3)
- Progress bar (3dp) overlaid at bottom for Continue Watching / Next Up
- States:
  - Hover: scale 1.05 + elevation (desktop)
  - Pressed: scale 0.98

## File Structure

```
presentation/components/
  MediaCarousel.kt
  MediaCard.kt
presentation/screens/home/
  HomeScreen.kt (modified)
  HomeScreenModel.kt (modified)
domain/repository/
  MediaRepository.kt (modified)
data/repository/
  MediaRepositoryImpl.kt (modified)
```

## Data Layer

### HomeState

```kotlin
data class HomeState(
    val username: String = "",
    val serverName: String = "",
    val continueWatching: List<MediaItem> = emptyList(),
    val latestMovies: List<MediaItem> = emptyList(),
    val latestSeries: List<MediaItem> = emptyList(),
    val latestMusic: List<MediaItem> = emptyList(),
    val favorites: List<MediaItem> = emptyList(),
    val nextUp: List<MediaItem> = emptyList(),
    val selectedNavIndex: Int = 0,
    val isLoading: Boolean = true,
    val error: String? = null
)
```

### MediaRepository Methods

Six separate methods replacing `getActivityFeed()`:
- `getContinueWatching(serverId: String): Result<List<MediaItem>>`
- `getLatestMovies(serverId: String): Result<List<MediaItem>>`
- `getLatestSeries(serverId: String): Result<List<MediaItem>>`
- `getLatestMusic(serverId: String): Result<List<MediaItem>>`
- `getFavorites(serverId: String): Result<List<MediaItem>>`
- `getNextUp(serverId: String): Result<List<MediaItem>>`

All calls executed in parallel on screen load.

## UI Layout

```
+--+----------------------------------------+
|  |  Continue Watching                     |
|N |  [card] [card] [card] [card] ...  -->  |
|A |                                        |
|V |  Latest Movies                         |
|  |  [card] [card] [card] [card] ...  -->  |
|R |                                        |
|A |  Latest Series                         |
|I |  [card] [card] [card] [card] ...  -->  |
|L |                                        |
|  |  ...                                   |
+--+----------------------------------------+
```
