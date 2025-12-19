# Library Screen Design

## Overview

When users click on a library chip in HomeScreen, the app navigates to `LibraryScreen` displaying all content from that library in a filterable grid.

## UI Structure

```
┌─────────────────────────────────────┐
│ ← [Library Name]                    │  ← TopAppBar with back button
├─────────────────────────────────────┤
│ [Sort ▼] [Genre ▼] [Year ▼] [👁] [♥]│  ← Horizontal filter bar
├─────────────────────────────────────┤
│ ┌───┐ ┌───┐ ┌───┐ ┌───┐ ┌───┐      │
│ │   │ │   │ │   │ │   │ │   │      │
│ │ 🎬│ │ 🎬│ │ 🎬│ │ 🎬│ │ 🎬│      │  ← Responsive grid
│ └───┘ └───┘ └───┘ └───┘ └───┘      │
│ ┌───┐ ┌───┐ ┌───┐ ┌───┐ ┌───┐      │
│ │   │ │   │ │   │ │   │ │   │      │
│ ...                                 │  ← Infinite scroll
└─────────────────────────────────────┘
```

## Components

### LibraryScreen.kt
Voyager Screen receiving a `Library` object as parameter.

### LibraryScreenModel
State management with:
- `LibraryState`: items, loading states, filters, available filter options
- `SortOption` enum: NAME, DATE_ADDED, YEAR, RATING
- Pagination: loads ~20 items at a time, `loadMore()` triggered near bottom

### State Structure
```kotlin
data class LibraryState(
    val library: Library,
    val items: List<MediaItem> = emptyList(),
    val isLoading: Boolean = true,
    val isLoadingMore: Boolean = false,
    val hasMoreItems: Boolean = true,
    val error: String? = null,

    // Filters
    val sortBy: SortOption = SortOption.DATE_ADDED,
    val selectedGenre: String? = null,
    val selectedYear: Int? = null,
    val showWatchedOnly: Boolean? = null,
    val showFavoritesOnly: Boolean = false,

    // Dropdown data
    val availableGenres: List<String> = emptyList(),
    val availableYears: List<Int> = emptyList()
)
```

## Filter Bar Components
- Sort dropdown: Name, Date Added, Year, Rating
- Genre dropdown: Dynamic from library content
- Year dropdown: Dynamic from library content
- Watched toggle: All / Watched / Unwatched
- Favorites toggle: Show favorites only

## Behavior
- Click on library chip in HomeScreen → Navigate to LibraryScreen(library)
- Infinite scroll loads more items automatically
- Filters apply immediately and reset pagination
- Click on grid item → No action (future: MediaDetailScreen)

## Files to Create
- `presentation/screens/library/LibraryScreen.kt`
- `presentation/screens/library/LibraryScreenModel.kt`
- `presentation/components/FilterBar.kt`
- `presentation/components/MediaGrid.kt`

## Files to Modify
- `HomeScreen.kt`: Add navigation on library chip click
- `MediaRepository.kt`: Add filtered/sorted query method
- `MediaRepositoryImpl.kt`: Implement filtered queries
- `di/PresentationModule.kt`: Register LibraryScreenModel
