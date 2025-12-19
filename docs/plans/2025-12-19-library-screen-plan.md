# Implementation Plan: LibraryScreen

## Task 1: Extend Data Layer for Filtered Queries

### 1.1 Update JellyfinDataSource interface
**File:** `composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/data/remote/JellyfinDataSource.kt`

Add new method:
```kotlin
suspend fun getLibraryItemsFiltered(
    serverUrl: String,
    token: String,
    userId: String,
    libraryId: String,
    limit: Int = 20,
    startIndex: Int = 0,
    sortBy: String = "DateCreated",
    sortOrder: String = "Descending",
    genres: List<String>? = null,
    years: List<Int>? = null,
    isPlayed: Boolean? = null,
    isFavorite: Boolean? = null
): Result<LibraryItemsResponse>

data class LibraryItemsResponse(
    val items: List<MediaItem>,
    val totalCount: Int
)
```

### 1.2 Implement in JellyfinDataSourceImpl (jvmMain + androidMain)
**Files:**
- `composeApp/src/jvmMain/kotlin/com/lowiq/jellyfish/data/remote/JellyfinDataSourceImpl.kt`
- `composeApp/src/androidMain/kotlin/com/lowiq/jellyfish/data/remote/JellyfinDataSourceImpl.kt`

Use itemsApi.getItems() with:
- `startIndex` for pagination
- `sortBy` / `sortOrder`
- `genres` filter
- `years` filter
- `isPlayed` filter
- `isFavorite` filter

### 1.3 Add method to get available filters (genres, years)
Add to JellyfinDataSource:
```kotlin
suspend fun getLibraryFilters(
    serverUrl: String,
    token: String,
    userId: String,
    libraryId: String
): Result<LibraryFilters>

data class LibraryFilters(
    val genres: List<String>,
    val years: List<Int>
)
```

Use itemsApi or filtersApi to get available genres/years for the library.

---

## Task 2: Update Domain Layer

### 2.1 Add domain models for filters
**File:** `composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/domain/model/LibraryFilter.kt` (new)

```kotlin
data class LibraryFilters(
    val genres: List<String>,
    val years: List<Int>
)

enum class SortOption(val apiValue: String) {
    NAME("SortName"),
    DATE_ADDED("DateCreated"),
    YEAR("ProductionYear"),
    RATING("CommunityRating")
}
```

### 2.2 Add repository methods
**File:** `composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/domain/repository/MediaRepository.kt`

```kotlin
suspend fun getLibraryItemsPaginated(
    serverId: String,
    libraryId: String,
    limit: Int = 20,
    offset: Int = 0,
    sortBy: SortOption = SortOption.DATE_ADDED,
    genres: List<String>? = null,
    years: List<Int>? = null,
    isWatched: Boolean? = null,
    isFavorite: Boolean? = null
): Result<PaginatedResult<MediaItem>>

suspend fun getLibraryFilters(serverId: String, libraryId: String): Result<LibraryFilters>

data class PaginatedResult<T>(
    val items: List<T>,
    val totalCount: Int,
    val hasMore: Boolean
)
```

### 2.3 Implement in MediaRepositoryImpl
**File:** `composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/data/repository/MediaRepositoryImpl.kt`

---

## Task 3: Create Presentation Layer

### 3.1 Create LibraryScreenModel
**File:** `composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/presentation/screens/library/LibraryScreenModel.kt` (new)

State, events, and methods for:
- Loading initial items + available filters
- Pagination (loadMore)
- Filter changes (resets pagination)
- Sort changes

### 3.2 Create LibraryScreen
**File:** `composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/presentation/screens/library/LibraryScreen.kt` (new)

Compose UI with:
- TopAppBar with back navigation
- FilterBar component
- LazyVerticalGrid with infinite scroll

### 3.3 Create FilterBar component
**File:** `composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/presentation/components/FilterBar.kt` (new)

Horizontal row with:
- Sort dropdown
- Genre dropdown
- Year dropdown
- Watched toggle chip
- Favorites toggle chip

### 3.4 Create MediaGrid component
**File:** `composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/presentation/components/MediaGrid.kt` (new)

LazyVerticalGrid with:
- Responsive column count
- Infinite scroll detection
- Loading indicator at bottom

---

## Task 4: Integration

### 4.1 Register LibraryScreenModel in DI
**File:** `composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/di/AppModule.kt`

```kotlin
factory { (library: Library) -> LibraryScreenModel(library, get(), get()) }
```

### 4.2 Add navigation from HomeScreen
**File:** `composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/presentation/screens/home/HomeScreen.kt`

Update LibraryChips onLibraryClick to navigate:
```kotlin
onLibraryClick = { library ->
    library?.let { navigator.push(LibraryScreen(it)) }
}
```

---

## Execution Order

1. **Task 1** (Data Layer) - Must complete first
2. **Task 2** (Domain Layer) - Depends on Task 1
3. **Task 3** (Presentation Layer) - Can partially parallel with Task 2
4. **Task 4** (Integration) - Final step

## Parallelization Strategy

- **Agent 1**: Task 1 (Data layer - both JellyfinDataSource interface + implementations)
- **Agent 2**: Task 3.3 + 3.4 (UI Components - FilterBar + MediaGrid) - no dependencies
- **Agent 3**: After Agent 1 completes → Task 2 + Task 3.1 + 3.2 + Task 4
