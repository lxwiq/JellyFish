# SearchScreen Design

## Overview

Implement a minimalist search screen for JellyFish following the existing shadcn theme.

## Architecture

### Files to create
- `presentation/screens/search/SearchScreen.kt`
- `presentation/screens/search/SearchScreenModel.kt`
- `presentation/screens/search/SearchState.kt`
- `data/local/SearchHistoryStorage.kt`

### Files to modify
- `data/remote/JellyfinDataSource.kt` - add searchItems()
- `jvmMain/.../JellyfinDataSourceImpl.kt` - implement searchItems()
- `androidMain/.../JellyfinDataSourceImpl.kt` - implement searchItems()
- `domain/repository/MediaRepository.kt` - add search()
- `data/repository/MediaRepositoryImpl.kt` - implement search()
- `presentation/screens/home/HomeScreenModel.kt` - add NavigateToSearch event
- `presentation/screens/home/HomeScreen.kt` - handle search navigation
- `di/AppModule.kt` - register SearchScreenModel

## Data Flow

1. User types in search field → 300ms debounce
2. SearchScreenModel calls MediaRepository.search()
3. Results displayed in LazyVerticalGrid
4. History stored in DataStore (max 10 entries)

## UI States

- **Empty field**: Show search history (if any)
- **Typing**: Show loading indicator
- **Results**: Show grid of MediaItems
- **No results**: Show empty state message
- **Error**: Show error with retry

## API

```kotlin
// JellyfinDataSource
suspend fun searchItems(
    serverUrl: String,
    token: String,
    userId: String,
    query: String,
    limit: Int = 30
): Result<List<MediaItem>>
```

Uses `itemsApi.getItems()` with `searchTerm` parameter.

## Navigation

- Index 1 in NavigationRail
- HomeEvent.NavigateToSearch added
- HomeScreen pushes SearchScreen on event
