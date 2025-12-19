# Detail Pages Design

## Overview

Three detail screens for media items: Movies, Series, and Episodes. All screens follow the shadcn/ui design system with the app's dark theme.

## Design Decisions

- **Style**: shadcn/ui dark theme (slate/blue-gray palette)
- **Navigation**: Voyager push/pop with SlideTransition
- **Actions**: Play, Favorite, Watched, Download, Share on all pages
- **Menu**: Kebab menu (⋮) for secondary actions (report issue, edit metadata)

---

## MovieDetailScreen

### Layout

```
┌─────────────────────────────────┐
│ ←                          ⋮   │  Header (transparent overlay)
├─────────────────────────────────┤
│                                 │
│        BACKDROP IMAGE           │  ~250dp height
│        (gradient bottom)        │
│                                 │
├─────────────────────────────────┤
│ Title                           │  24sp, semi-bold, foreground
│ 2024 • 2h 15min • ⭐ 8.2        │  mutedForeground
│ [Genre] [Genre] [Genre]         │  Badges (secondary bg)
│ Studio Name                     │  mutedForeground
│                                 │
│ [▶ Play] [♡] [✓] [↓] [↗]       │  Primary + outline buttons
│                                 │
│ Description text that can be    │  foreground, expandable
│ expanded if too long...         │
│                                 │
├─────────────────────────────────┤
│ Cast                            │  Section title
│ ○ ○ ○ ○ ○ →                     │  Horizontal carousel (circular avatars + name)
│                                 │
├─────────────────────────────────┤
│ Similar Movies                  │  Section title
│ [poster] [poster] [poster] →    │  Reuse MediaCarousel component
│                                 │
├─────────────────────────────────┤
│ Trailer                         │  If available
│ [thumbnail with play overlay]   │  Opens YouTube/external link
└─────────────────────────────────┘
```

### Data Required

- `id`, `title`, `overview` (description)
- `backdropUrl`, `posterUrl`
- `year`, `runtime` (formatted as "Xh Xmin")
- `rating` (community rating)
- `genres[]` (list of genre names)
- `studio` (production studio name)
- `cast[]` (actors with name + photo)
- `similarItems[]` (MediaItem list)
- `trailerUrl` (optional, YouTube link)
- User state: `isFavorite`, `isWatched`

---

## SeriesDetailScreen

### Layout

```
┌─────────────────────────────────┐
│ ←                          ⋮   │  Header (transparent overlay)
├─────────────────────────────────┤
│                                 │
│        BACKDROP IMAGE           │  ~250dp height
│        (gradient bottom)        │
│                                 │
├─────────────────────────────────┤
│ Title                           │  24sp, semi-bold
│ 2019 • 4 Seasons • ⭐ 9.3       │  mutedForeground
│ [Genre] [Genre]                 │  Badges
│ Network Name                    │
│                                 │
│ [▶ Play] [♡] [✓] [↓] [↗]       │  Play = resume or S1E1
│                                 │
│ Description...                  │
│                                 │
├─────────────────────────────────┤
│ Seasons                         │
│ ┌─────────────────────────┐     │
│ │ Season 1              ▼ │     │  Select dropdown (shadcn style)
│ └─────────────────────────┘     │
│                                 │
│ ┌─────────────────────────────┐ │
│ │[thumb] 1. Pilot            │ │  Episode card
│ │        55min               │ │  - Thumbnail (16:9)
│ │        Episode description │ │  - Number + Title
│ └─────────────────────────────┘ │  - Duration
│ ┌─────────────────────────────┐ │  - Description (1-2 lines)
│ │[thumb] 2. The Kingsroad    │ │
│ │        56min               │ │  Progress bar if partially watched
│ │        Episode description │ │
│ └─────────────────────────────┘ │
│ ...                             │
│                                 │
├─────────────────────────────────┤
│ Cast                            │
│ ○ ○ ○ ○ ○ →                     │  Horizontal carousel
│                                 │
├─────────────────────────────────┤
│ Similar Series                  │
│ [poster] [poster] [poster] →    │  MediaCarousel
└─────────────────────────────────┘
```

### Season Dropdown Behavior

- Default: First unwatched season, or Season 1
- On selection: Load episodes for selected season
- Loading state while fetching episodes

### Episode Card

- Click → Navigate to EpisodeDetailScreen
- Shows progress bar if partially watched
- "NEW" badge if unwatched and recently added

### Data Required

- Same as Movie plus:
- `seasons[]` (id, name, number, episodeCount)
- `episodes[]` (for selected season)
- `currentSeason` (selected season number)

---

## EpisodeDetailScreen

### Layout

```
┌─────────────────────────────────┐
│ ←  Series Name             ⋮   │  Header with series link
├─────────────────────────────────┤
│ ┌─────────────────────────────┐ │
│ │                             │ │
│ │     THUMBNAIL (16:9)        │ │  Large thumbnail
│ │          ▶                  │ │  Play button overlay
│ │                             │ │
│ └─────────────────────────────┘ │
│                                 │
│ S1 E5 • The Wolf and the Lion  │  Episode identifier + title
│ 55min • ⭐ 9.1                  │  Duration + rating
│                                 │
│ [▶ Play] [♡] [✓] [↓] [↗]       │  Actions
│                                 │
│ Episode description text...     │
│                                 │
├─────────────────────────────────┤
│ ◀ Previous    5 / 10   Next ▶  │  Episode navigation
├─────────────────────────────────┤
│ Guest Stars                     │  If available
│ ○ ○ ○ →                         │  Horizontal carousel
│                                 │
├─────────────────────────────────┤
│ More from Season 1              │
│ [ep] [ep] [ep] [ep] →           │  Horizontal episode carousel
└─────────────────────────────────┘
```

### Navigation Behavior

- Header "Series Name" → pop to SeriesDetailScreen
- Back button (←) → pop to previous screen
- Previous/Next → replace current with adjacent episode
- Episode carousel click → replace current episode

### Data Required

- `id`, `title`, `overview`
- `thumbnailUrl` (backdrop/screenshot)
- `seasonNumber`, `episodeNumber`
- `runtime`, `rating`
- `seriesId`, `seriesName` (for navigation)
- `guestStars[]` (actors)
- `seasonEpisodes[]` (other episodes in season)
- `previousEpisodeId`, `nextEpisodeId` (nullable)
- User state: `isFavorite`, `isWatched`, `progress`

---

## Shared Components

### ActionButtonsRow

```kotlin
@Composable
fun ActionButtonsRow(
    onPlay: () -> Unit,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    isWatched: Boolean,
    onToggleWatched: () -> Unit,
    onDownload: () -> Unit,
    onShare: () -> Unit
)
```

- Play button: Primary style (light bg, dark text)
- Other buttons: Outline style (border, icon only)
- Icons: Play, Heart, Check, Download, Share

### CastCarousel

```kotlin
@Composable
fun CastCarousel(
    title: String,
    cast: List<CastMember>,
    onPersonClick: (String) -> Unit
)
```

- Circular avatar (56dp)
- Name below (12sp, mutedForeground)
- Horizontal scroll

### GenreBadges

```kotlin
@Composable
fun GenreBadges(genres: List<String>)
```

- Row of badges
- Background: secondary
- Text: secondaryForeground
- Rounded corners, padding

### EpisodeCard

```kotlin
@Composable
fun EpisodeCard(
    episode: Episode,
    onClick: () -> Unit
)
```

- Horizontal layout
- Thumbnail (100dp width, 16:9)
- Title, duration, description
- Optional progress bar

### SeasonDropdown

```kotlin
@Composable
fun SeasonDropdown(
    seasons: List<Season>,
    selectedSeason: Int,
    onSeasonSelected: (Int) -> Unit
)
```

- shadcn/ui Select style
- Card background, border
- Dropdown with season list

---

## File Structure

```
presentation/
├── screens/
│   └── detail/
│       ├── MovieDetailScreen.kt
│       ├── MovieDetailScreenModel.kt
│       ├── SeriesDetailScreen.kt
│       ├── SeriesDetailScreenModel.kt
│       ├── EpisodeDetailScreen.kt
│       └── EpisodeDetailScreenModel.kt
├── components/
│   ├── ActionButtonsRow.kt
│   ├── CastCarousel.kt
│   ├── GenreBadges.kt
│   ├── EpisodeCard.kt
│   └── SeasonDropdown.kt
```

---

## API Requirements

New repository methods needed:

```kotlin
interface MediaRepository {
    // Existing methods...

    // New methods for detail pages
    suspend fun getMovieDetails(id: String): MovieDetails
    suspend fun getSeriesDetails(id: String): SeriesDetails
    suspend fun getSeasonEpisodes(seriesId: String, seasonNumber: Int): List<Episode>
    suspend fun getEpisodeDetails(id: String): EpisodeDetails
    suspend fun getSimilarItems(id: String): List<MediaItem>

    // User actions
    suspend fun toggleFavorite(id: String): Boolean
    suspend fun toggleWatched(id: String): Boolean
}
```

---

## Navigation Integration

Update `onItemClick` handlers in:
- `HomeScreen.kt` - all MediaCarousel items
- `LibraryScreen.kt` - MediaGrid items

Navigation logic:
```kotlin
fun navigateToDetail(navigator: Navigator, item: MediaItem) {
    when (item.type) {
        MediaType.MOVIE -> navigator.push(MovieDetailScreen(item.id))
        MediaType.SERIES -> navigator.push(SeriesDetailScreen(item.id))
        MediaType.EPISODE -> navigator.push(EpisodeDetailScreen(item.id))
    }
}
```

Requires adding `type: MediaType` to `MediaItem` model.
