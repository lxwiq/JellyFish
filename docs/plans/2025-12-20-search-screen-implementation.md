# SearchScreen Implementation Plan

> **For Claude:** REQUIRED SUB-SKILL: Use superpowers:executing-plans to implement this plan task-by-task.

**Goal:** Implement minimalist search screen with debounce, history, and grid results.

**Architecture:** Add search API to data layer, create SearchScreen with ScreenModel, integrate into navigation.

**Tech Stack:** Kotlin, Compose, Voyager, Koin, DataStore, Jellyfin SDK

---

## Task 1: Add searchItems to JellyfinDataSource interface

**Files:**
- Modify: `composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/data/remote/JellyfinDataSource.kt`

**What to do:**
Add method after `getNextUpEpisodes`:

```kotlin
suspend fun searchItems(
    serverUrl: String,
    token: String,
    userId: String,
    query: String,
    limit: Int = 30
): Result<List<MediaItem>>
```

**Commit:** `feat(search): add searchItems to JellyfinDataSource interface`

---

## Task 2: Implement searchItems in JellyfinDataSourceImpl (both platforms)

**Files:**
- Modify: `composeApp/src/jvmMain/kotlin/com/lowiq/jellyfish/data/remote/JellyfinDataSourceImpl.kt`
- Modify: `composeApp/src/androidMain/kotlin/com/lowiq/jellyfish/data/remote/JellyfinDataSourceImpl.kt`

**What to do (same for both files):**
Add after `getNextUpEpisodes` implementation:

```kotlin
override suspend fun searchItems(
    serverUrl: String,
    token: String,
    userId: String,
    query: String,
    limit: Int
): Result<List<MediaItem>> = withContext(Dispatchers.IO) {
    runCatching {
        val api = createApi(serverUrl, token)
        val response by api.itemsApi.getItems(
            userId = java.util.UUID.fromString(userId),
            searchTerm = query,
            limit = limit,
            recursive = true,
            enableImages = true,
            includeItemTypes = listOf(
                BaseItemKind.MOVIE,
                BaseItemKind.SERIES,
                BaseItemKind.EPISODE,
                BaseItemKind.MUSIC_ALBUM,
                BaseItemKind.AUDIO
            )
        )
        response.items.orEmpty().map { it.toMediaItem(serverUrl) }
    }
}
```

**Commit:** `feat(search): implement searchItems in JellyfinDataSourceImpl`

---

## Task 3: Add search to MediaRepository

**Files:**
- Modify: `composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/domain/repository/MediaRepository.kt`
- Modify: `composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/data/repository/MediaRepositoryImpl.kt`

**What to do:**

In `MediaRepository.kt` interface, add:
```kotlin
suspend fun search(serverId: String, query: String): Result<List<MediaItem>>
```

In `MediaRepositoryImpl.kt`, add implementation:
```kotlin
override suspend fun search(serverId: String, query: String): Result<List<MediaItem>> {
    val server = serverStorage.getServers().first().find { it.id == serverId }
        ?: return Result.failure(Exception("Server not found"))
    val token = secureStorage.getToken(serverId)
        ?: return Result.failure(Exception("Not authenticated"))
    val userId = server.userId ?: return Result.failure(Exception("No user ID"))

    return jellyfinDataSource.searchItems(server.url, token, userId, query)
}
```

**Commit:** `feat(search): add search method to MediaRepository`

---

## Task 4: Create SearchHistoryStorage

**Files:**
- Create: `composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/data/local/SearchHistoryStorage.kt`

**What to do:**

```kotlin
package com.lowiq.jellyfish.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SearchHistoryStorage(private val dataStore: DataStore<Preferences>) {

    private val historyKey = stringPreferencesKey("search_history")
    private val maxHistorySize = 10

    fun getHistory(): Flow<List<String>> {
        return dataStore.data.map { prefs ->
            prefs[historyKey]?.split("||")?.filter { it.isNotBlank() } ?: emptyList()
        }
    }

    suspend fun addToHistory(query: String) {
        if (query.isBlank()) return
        dataStore.edit { prefs ->
            val existing = prefs[historyKey]?.split("||")?.filter { it.isNotBlank() } ?: emptyList()
            val updated = listOf(query) + existing.filter { it != query }
            prefs[historyKey] = updated.take(maxHistorySize).joinToString("||")
        }
    }

    suspend fun removeFromHistory(query: String) {
        dataStore.edit { prefs ->
            val existing = prefs[historyKey]?.split("||")?.filter { it.isNotBlank() } ?: emptyList()
            prefs[historyKey] = existing.filter { it != query }.joinToString("||")
        }
    }

    suspend fun clearHistory() {
        dataStore.edit { prefs ->
            prefs.remove(historyKey)
        }
    }
}
```

**Commit:** `feat(search): create SearchHistoryStorage`

---

## Task 5: Create SearchState

**Files:**
- Create: `composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/presentation/screens/search/SearchState.kt`

**What to do:**

```kotlin
package com.lowiq.jellyfish.presentation.screens.search

import com.lowiq.jellyfish.domain.model.MediaItem

data class SearchState(
    val query: String = "",
    val results: List<MediaItem> = emptyList(),
    val history: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val hasSearched: Boolean = false,
    val error: String? = null
)
```

**Commit:** `feat(search): create SearchState`

---

## Task 6: Create SearchScreenModel

**Files:**
- Create: `composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/presentation/screens/search/SearchScreenModel.kt`

**What to do:**

```kotlin
package com.lowiq.jellyfish.presentation.screens.search

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.lowiq.jellyfish.data.local.SearchHistoryStorage
import com.lowiq.jellyfish.domain.repository.MediaRepository
import com.lowiq.jellyfish.domain.repository.ServerRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchScreenModel(
    private val serverRepository: ServerRepository,
    private val mediaRepository: MediaRepository,
    private val searchHistoryStorage: SearchHistoryStorage
) : ScreenModel {

    private val _state = MutableStateFlow(SearchState())
    val state = _state.asStateFlow()

    private var searchJob: Job? = null
    private var currentServerId: String? = null

    init {
        loadHistory()
        loadServerId()
    }

    private fun loadServerId() {
        screenModelScope.launch {
            serverRepository.getActiveServer()
                .filterNotNull()
                .first()
                .let { server ->
                    currentServerId = server.id
                }
        }
    }

    private fun loadHistory() {
        screenModelScope.launch {
            searchHistoryStorage.getHistory().collect { history ->
                _state.update { it.copy(history = history) }
            }
        }
    }

    fun onQueryChange(query: String) {
        _state.update { it.copy(query = query, error = null) }

        searchJob?.cancel()

        if (query.isBlank()) {
            _state.update { it.copy(results = emptyList(), hasSearched = false, isLoading = false) }
            return
        }

        searchJob = screenModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            delay(300) // Debounce
            performSearch(query)
        }
    }

    fun onHistoryItemClick(query: String) {
        _state.update { it.copy(query = query) }
        searchJob?.cancel()
        searchJob = screenModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            performSearch(query)
        }
    }

    fun onRemoveHistoryItem(query: String) {
        screenModelScope.launch {
            searchHistoryStorage.removeFromHistory(query)
        }
    }

    fun onClearHistory() {
        screenModelScope.launch {
            searchHistoryStorage.clearHistory()
        }
    }

    private suspend fun performSearch(query: String) {
        val serverId = currentServerId ?: return

        mediaRepository.search(serverId, query)
            .onSuccess { results ->
                _state.update {
                    it.copy(
                        results = results,
                        isLoading = false,
                        hasSearched = true,
                        error = null
                    )
                }
                searchHistoryStorage.addToHistory(query)
            }
            .onFailure { e ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        hasSearched = true,
                        error = e.message ?: "Search failed"
                    )
                }
            }
    }

    fun clearQuery() {
        _state.update { it.copy(query = "", results = emptyList(), hasSearched = false) }
        searchJob?.cancel()
    }
}
```

**Commit:** `feat(search): create SearchScreenModel`

---

## Task 7: Create SearchScreen UI

**Files:**
- Create: `composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/presentation/screens/search/SearchScreen.kt`

**What to do:**

```kotlin
package com.lowiq.jellyfish.presentation.screens.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil3.compose.AsyncImage
import com.lowiq.jellyfish.domain.model.MediaItem
import com.lowiq.jellyfish.presentation.navigation.navigateToDetail
import com.lowiq.jellyfish.presentation.theme.LocalJellyFishColors

class SearchScreen : Screen {

    @Composable
    override fun Content() {
        val colors = LocalJellyFishColors.current
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = koinScreenModel<SearchScreenModel>()
        val state by screenModel.state.collectAsState()
        val focusRequester = remember { FocusRequester() }

        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colors.background)
        ) {
            // Search bar
            SearchBar(
                query = state.query,
                onQueryChange = screenModel::onQueryChange,
                onClear = screenModel::clearQuery,
                isLoading = state.isLoading,
                focusRequester = focusRequester
            )

            when {
                state.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = colors.primary)
                    }
                }

                state.error != null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = state.error ?: "Error",
                            color = colors.destructive
                        )
                    }
                }

                state.query.isBlank() && state.history.isNotEmpty() -> {
                    SearchHistory(
                        history = state.history,
                        onItemClick = screenModel::onHistoryItemClick,
                        onRemoveItem = screenModel::onRemoveHistoryItem,
                        onClearAll = screenModel::onClearHistory
                    )
                }

                state.hasSearched && state.results.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Aucun résultat",
                            color = colors.mutedForeground
                        )
                    }
                }

                state.results.isNotEmpty() -> {
                    SearchResults(
                        results = state.results,
                        onItemClick = { id, type -> navigateToDetail(navigator, id, type) }
                    )
                }

                else -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                Icons.Default.Search,
                                contentDescription = null,
                                modifier = Modifier.size(64.dp),
                                tint = colors.mutedForeground
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Rechercher des films, séries...",
                                color = colors.mutedForeground
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onClear: () -> Unit,
    isLoading: Boolean,
    focusRequester: FocusRequester
) {
    val colors = LocalJellyFishColors.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(colors.card)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            Icons.Default.Search,
            contentDescription = null,
            tint = colors.mutedForeground
        )

        Spacer(modifier = Modifier.width(12.dp))

        BasicTextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier
                .weight(1f)
                .focusRequester(focusRequester),
            textStyle = MaterialTheme.typography.bodyLarge.copy(color = colors.foreground),
            cursorBrush = SolidColor(colors.primary),
            singleLine = true,
            decorationBox = { innerTextField ->
                Box {
                    if (query.isEmpty()) {
                        Text(
                            "Rechercher...",
                            color = colors.mutedForeground
                        )
                    }
                    innerTextField()
                }
            }
        )

        if (query.isNotEmpty()) {
            IconButton(onClick = onClear) {
                Icon(
                    Icons.Default.Clear,
                    contentDescription = "Clear",
                    tint = colors.mutedForeground
                )
            }
        }
    }
}

@Composable
private fun SearchHistory(
    history: List<String>,
    onItemClick: (String) -> Unit,
    onRemoveItem: (String) -> Unit,
    onClearAll: () -> Unit
) {
    val colors = LocalJellyFishColors.current

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Recherches récentes",
                style = MaterialTheme.typography.titleSmall,
                color = colors.mutedForeground
            )
            TextButton(onClick = onClearAll) {
                Text("Effacer", color = colors.primary)
            }
        }

        LazyColumn {
            items(history) { query ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onItemClick(query) }
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.History,
                        contentDescription = null,
                        tint = colors.mutedForeground
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        query,
                        modifier = Modifier.weight(1f),
                        color = colors.foreground
                    )
                    IconButton(onClick = { onRemoveItem(query) }) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "Remove",
                            tint = colors.mutedForeground
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SearchResults(
    results: List<MediaItem>,
    onItemClick: (String, String) -> Unit
) {
    val colors = LocalJellyFishColors.current

    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 120.dp),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(results, key = { it.id }) { item ->
            SearchResultItem(
                item = item,
                onClick = { onItemClick(item.id, item.type) }
            )
        }
    }
}

@Composable
private fun SearchResultItem(
    item: MediaItem,
    onClick: () -> Unit
) {
    val colors = LocalJellyFishColors.current

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
    ) {
        AsyncImage(
            model = item.imageUrl,
            contentDescription = item.title,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(2f / 3f)
                .clip(RoundedCornerShape(8.dp))
                .background(colors.card),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = item.title,
            style = MaterialTheme.typography.bodySmall,
            color = colors.foreground,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )

        item.subtitle?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.labelSmall,
                color = colors.mutedForeground,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
```

**Commit:** `feat(search): create SearchScreen UI`

---

## Task 8: Register in Koin and add navigation

**Files:**
- Modify: `composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/di/AppModule.kt`
- Modify: `composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/presentation/screens/home/HomeScreenModel.kt`
- Modify: `composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/presentation/screens/home/HomeScreen.kt`

**What to do:**

**AppModule.kt** - In appModule, add:
```kotlin
single { SearchHistoryStorage(get()) }
```

In presentationModule, add:
```kotlin
factory { SearchScreenModel(get(), get(), get()) }
```

**HomeScreenModel.kt** - Add to HomeEvent:
```kotlin
object NavigateToSearch : HomeEvent()
```

Update onNavigationItemSelected:
```kotlin
fun onNavigationItemSelected(index: Int) {
    _state.update { it.copy(selectedNavIndex = index) }

    screenModelScope.launch {
        when (index) {
            1 -> _events.emit(HomeEvent.NavigateToSearch)
            3 -> _events.emit(HomeEvent.NavigateToDownloads)
        }
    }
}
```

**HomeScreen.kt** - Add import:
```kotlin
import com.lowiq.jellyfish.presentation.screens.search.SearchScreen
```

Update event collection:
```kotlin
is HomeEvent.NavigateToSearch -> navigator.push(SearchScreen())
```

**Commit:** `feat(search): register SearchScreen in Koin and add navigation`

---

## Task 9: Build and verify

**What to do:**
1. Run `./gradlew :composeApp:assembleDebug`
2. Verify build succeeds

**Commit:** None needed if no fixes required
