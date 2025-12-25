# LibraryPage - Documentation Technique

## 1. Structure de la Page (Diagramme ASCII)

```
┌─────────────────────────────────────────────────────────────────────┐
│ DetachedPage (py-16 space-y-8 min-h-screen)                        │
│                                                                      │
│  ┌────────────────────────────────────────────────────────────┐   │
│  │ Container (px-32) - Barre de navigation horizontale        │   │
│  │                                                             │   │
│  │  ┌──────────────────────────────┐  ┌──────────────────┐  │   │
│  │  │ Tabs (flex space-x-4)        │  │ Options Button   │  │   │
│  │  │  • All                       │  │ (MixerHorizontal)│  │   │
│  │  │  • Series                    │  │                  │  │   │
│  │  │  • Movies                    │  │                  │  │   │
│  │  └──────────────────────────────┘  └──────────────────┘  │   │
│  └────────────────────────────────────────────────────────────┘   │
│                                                                      │
│  ┌────────────────────────────────────────────────────────────┐   │
│  │ Container (focusedChild)                                   │   │
│  │                                                             │   │
│  │  ┌───────────────────────────────────────────────────┐    │   │
│  │  │ UPCOMING (Carousel horizontal)                    │    │   │
│  │  │  ┌────┐ ┌────┐ ┌────┐ ┌────┐                     │    │   │
│  │  │  │Card│ │Card│ │Card│ │Card│ ...                 │    │   │
│  │  │  │ lg │ │ lg │ │ lg │ │ lg │                     │    │   │
│  │  │  └────┘ └────┘ └────┘ └────┘                     │    │   │
│  │  └───────────────────────────────────────────────────┘    │   │
│  │                                                             │   │
│  │  ┌───────────────────────────────────────────────────┐    │   │
│  │  │ MY LIBRARY (CardGrid)                             │    │   │
│  │  │  ┌────┐ ┌────┐ ┌────┐ ┌────┐ ┌────┐ ┌────┐      │    │   │
│  │  │  │Card│ │Card│ │Card│ │Card│ │Card│ │Card│      │    │   │
│  │  │  └────┘ └────┘ └────┘ └────┘ └────┘ └────┘      │    │   │
│  │  │  ┌────┐ ┌────┐ ┌────┐ ┌────┐ ┌────┐ ┌────┐      │    │   │
│  │  │  │Card│ │Card│ │Card│ │Card│ │Card│ │Card│      │    │   │
│  │  │  └────┘ └────┘ └────┘ └────┘ └────┘ └────┘      │    │   │
│  │  └───────────────────────────────────────────────────┘    │   │
│  │                                                             │   │
│  │  ┌───────────────────────────────────────────────────┐    │   │
│  │  │ WATCHED (CardGrid)                                │    │   │
│  │  │  ┌────┐ ┌────┐ ┌────┐ ┌────┐ ┌────┐ ┌────┐      │    │   │
│  │  │  │Card│ │Card│ │Card│ │Card│ │Card│ │Card│      │    │   │
│  │  │  └────┘ └────┘ └────┘ └────┘ └────┘ └────┘      │    │   │
│  │  └───────────────────────────────────────────────────┘    │   │
│  └────────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────────┘
```

## 2. Composants Utilisés

### 2.1 Composants Principaux

#### **DetachedPage**
- Conteneur de page principal
- Classes: `py-16 space-y-8 min-h-screen flex flex-col`
- Gère le focus au montage (`focusOnMount`)
- Expose `let:hasFocus` aux enfants

#### **TabItem** (`TabItem.svelte`)
- Élément de navigation par onglets
- **Props**:
  - `selected: boolean` - État sélectionné
- **Events**:
  - `on:select` - Déclenché lors de la sélection
- **Style**:
  - Texte: `font-semibold text-2xl`
  - État sélectionné: `text-secondary-50`
  - État non sélectionné: `text-secondary-400`
  - État focus: `text-primary-500`
- **Fonctionnalité**: Conteneur cliquable avec gestion du focus via `focusOnClick`

#### **OptionsDialog** (`OptionsDialog.LibraryPage.svelte`)
- Dialogue modal pour les options d'affichage
- **Composants internes**:
  - 2x `SelectButtonGroup` (tri et direction)
  - 2x `Toggle` (séparation upcoming/watched)
- **Options de tri**:
  - Last Release Date
  - First Release Date
  - Date Added
  - Title
- **Directions de tri**:
  - Ascending
  - Descending
- **Toggles**:
  - Include upcoming (inverse de `separateUpcoming`)
  - Include watched (inverse de `separateWatched`)

#### **Carousel** (`Carousel.svelte`)
- Carrousel horizontal défilant
- **Props**:
  - `header: string` - Titre du carrousel
  - `scrollClass: string` - Classes CSS pour le scroll
  - `hideControls: boolean` - Masquer les contrôles
- **Fonctionnalités**:
  - Boutons de navigation gauche/droite (ChevronLeft/ChevronRight)
  - Scroll automatique par page (clientWidth - 2*128 + 32px)
  - Masque de dégradé aux extrémités
  - Gestion du focus avec retour au premier élément sur `back`
  - Espacement horizontal: `space-x-8`

#### **CardGrid** (`CardGrid.svelte`)
- Grille responsive pour les cartes
- **Props**:
  - `type: 'portrait' | 'landscape'` (défaut: 'portrait')
- **Layout**:
  - Utilise CSS Grid
  - Colonnes dynamiques basées sur la largeur de fenêtre
  - Espacement: `gap-x-8 gap-y-8`
  - Recalcul automatique au redimensionnement

#### **TmdbCard**
- Carte individuelle pour les médias
- **Props**:
  - `item: LibraryItemDto2` - Données du média
  - `size: 'lg' | 'dynamic'` - Taille de la carte
  - `progress: number` - Progression de visionnage (0-1)
  - `navigateWithType: boolean` - Navigation avec type de média
- **Events**:
  - `on:enter` - Scroll automatique vers l'élément

#### **Container**
- Système de navigation D-pad (TV/manette)
- **Props**:
  - `direction: 'horizontal' | 'vertical' | 'grid'`
  - `gridCols: number` - Nombre de colonnes pour la grille
  - `focusOnMount: boolean` - Focus automatique au montage
  - `trapFocus: boolean` - Piéger le focus dans le conteneur
  - `focusOnClick: boolean` - Focus au clic
  - `focusedChild: boolean` - Enfant avec focus
- **Events**:
  - `on:clickOrSelect`
  - `on:enter`
  - `on:mount`
  - `on:navigate`
  - `on:back`

### 2.2 Composants Utilitaires

- **Button**: Bouton avec icône (MixerHorizontal)
- **SelectButtonGroup**: Groupe de boutons radio
- **Toggle**: Interrupteur on/off
- **Dialog**: Dialogue modal de base

## 3. Layout et Dimensions

### 3.1 Espacement Global
- **Padding vertical de page**: `py-16` (4rem / 64px)
- **Espacement vertical**: `space-y-8` (2rem / 32px)
- **Padding horizontal principal**: `px-32` (8rem / 128px)

### 3.2 Sections

#### Barre de Navigation (Tabs + Options)
```
Container (px-32, horizontal)
├── Tabs Container (space-x-4)
│   ├── TabItem "All" (text-2xl)
│   ├── TabItem "Series" (text-2xl)
│   └── TabItem "Movies" (text-2xl)
└── Button "Options"
```
- **Espacement entre tabs**: 1rem (16px)
- **Taille du texte**: `text-2xl` (1.5rem / 24px)

#### Section Upcoming (Carousel)
```
mt-6 (24px)
Carousel (px-32)
  └── Cards (space-x-8, py-4)
      ├── TmdbCard size="lg"
      ├── TmdbCard size="lg"
      └── ...
```
- **Marge supérieure**: 24px
- **Espacement entre cartes**: 32px
- **Padding vertical du scroll**: 16px
- **Offset de scroll**: 128px horizontal

#### Section My Library (CardGrid)
```
my-6 (24px vertical)
px-32 (128px horizontal)
Title: h3 class (mb-6)
CardGrid
  ├── gap-x-8 (32px)
  ├── gap-y-8 (32px)
  └── Colonnes dynamiques (responsive)
```

#### Section Watched (CardGrid)
```
mt-6 (24px)
px-32 (128px)
Title: h3 class (mb-6)
CardGrid (même layout que My Library)
```

### 3.3 Dimensions Responsives (CardGrid)

Le nombre de colonnes est calculé dynamiquement par `getCardDimensions()`:
- Basé sur la largeur de la fenêtre
- Type portrait vs landscape
- Recalculé en temps réel au resize

### 3.4 Offsets de Scroll

```typescript
// Carousel
scrollIntoView({ horizontal: 128 })

// CardGrid - premier élément
scrollIntoView({ top: 128 + 64 }) // 192px

// CardGrid - autres éléments
scrollIntoView({ vertical: 128 })

// Section Upcoming
scrollIntoView({ bottom: 0 })
```

## 4. Logique de Navigation et Filtrage

### 4.1 État de la Page

```typescript
// État de la catégorie (onglet actif)
let category = writable<'all' | 'series' | 'movies'>('all');

// Paramètres d'affichage (LocalStorage)
type LibraryViewSettings = {
  sortBy: 'date-added' | 'title' | 'first-release-date' | 'last-release-date';
  sortDirection: 'asc' | 'desc';
  separateUpcoming: boolean;
  separateWatched: boolean;
};
```

### 4.2 Flux de Données (Stores Dérivés)

```
libraryItemsDataStore (source)
         ↓
    category filter
         ↓
sortedLibraryItems (sortItems)
         ↓
    categorization
         ↓
libraryItemsCategorized
  ├── upcoming[]
  ├── main[]
  └── watched[]
```

### 4.3 Fonction de Tri (`sortItems`)

**Paramètres**:
- `items: LibraryItemDto2[]` - Items à trier
- `viewSettings: LibraryViewSettings` - Paramètres de vue
- `category: 'all' | 'series' | 'movies'` - Catégorie filtrée

**Étapes**:
1. **Filtrage par catégorie**:
   ```typescript
   category === 'all' ? items.slice()
   : items.filter(i =>
       category === 'series'
         ? i.mediaType === 'Series'
         : i.mediaType === 'Movie'
     )
   ```

2. **Tri selon `sortBy`**:
   - `date-added`: Compare `createdAt`
   - `first-release-date`: Compare `first_air_date` ou `release_date`
   - `last-release-date`: Compare `last_air_date` avec fallback
   - `title`: Compare `title` ou `name`

3. **Application de la direction**:
   ```typescript
   const direction = viewSettings.sortDirection === 'asc' ? 1 : -1;
   return direction * comparison;
   ```

### 4.4 Catégorisation (`libraryItemsCategorized`)

**Logique**:
```typescript
if (!viewSettings.separateUpcoming && !viewSettings.separateWatched) {
  // Tout dans "main"
  return { main: items, upcoming: [], watched: [] };
}

for (const item of items) {
  const releaseDate = new Date(
    item.release_date ||
    (item.watched && item.next_episode_to_air?.air_date) ||
    0
  );

  const hasFutureReleases = item.watched
    ? item.seasons?.some(s => s.air_date === null)
    : item.last_air_date === null;

  if (separateUpcoming && (releaseDate > now || hasFutureReleases)) {
    categorizedItems.upcoming.push(item);
  } else if (separateWatched && item.watched) {
    categorizedItems.watched.push(item);
  } else {
    categorizedItems.main.push(item);
  }
}
```

**Tri supplémentaire pour Upcoming**:
- Par date de sortie croissante
- Fallback: +20 ans si pas de date

### 4.5 Navigation par Onglets

```typescript
// Changement d'onglet
<TabItem
  selected={$category === 'all'}
  on:select={() => category.set('all')}
>
  All
</TabItem>
```

**Effet**:
1. Met à jour le store `category`
2. Déclenche la réactivité de `sortedLibraryItems`
3. Recalcule la catégorisation
4. Re-render des sections avec nouveau `viewSettingsKey`

### 4.6 Gestion du Focus (D-pad)

**Hiérarchie de focus**:
```
DetachedPage (focusOnMount)
  └── Container (horizontal) - Barre de navigation
        ├── TabItem (focusOnClick)
        ├── TabItem (focusOnClick)
        ├── TabItem (focusOnClick)
        └── Button (Options)
  └── Container (focusedChild) - Contenu
        ├── Carousel (on:back: retour au 1er élément)
        │     └── TmdbCard (scrollIntoView)
        └── CardGrid (direction="grid")
              └── TmdbCard (scrollIntoView)
```

**Comportement**:
- Focus automatique à la page au montage
- Navigation horizontale/verticale selon le `Container.direction`
- Scroll automatique vers les éléments focalisés
- Retour au premier élément du carousel sur touche "back"

## 5. Équivalent Kotlin/Compose

```kotlin
// Types de données
data class LibraryViewSettings(
    val sortBy: SortBy = SortBy.LAST_RELEASE_DATE,
    val sortDirection: SortDirection = SortDirection.DESC,
    val separateUpcoming: Boolean = true,
    val separateWatched: Boolean = false
)

enum class SortBy {
    DATE_ADDED,
    TITLE,
    FIRST_RELEASE_DATE,
    LAST_RELEASE_DATE
}

enum class SortDirection { ASC, DESC }

enum class MediaCategory { ALL, SERIES, MOVIES }

data class CategorizedItems(
    val upcoming: List<LibraryItemDto2>,
    val main: List<LibraryItemDto2>,
    val watched: List<LibraryItemDto2>
)

// ViewModel
class LibraryViewModel : ViewModel() {
    private val _category = MutableStateFlow(MediaCategory.ALL)
    val category: StateFlow<MediaCategory> = _category.asStateFlow()

    private val _viewSettings = MutableStateFlow(LibraryViewSettings())
    val viewSettings: StateFlow<LibraryViewSettings> = _viewSettings.asStateFlow()

    private val libraryItemsRepository: LibraryItemsRepository = ...

    val sortedItems: StateFlow<List<LibraryItemDto2>> = combine(
        libraryItemsRepository.items,
        viewSettings,
        category
    ) { items, settings, cat ->
        sortItems(items, settings, cat)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val categorizedItems: StateFlow<CategorizedItems> = combine(
        sortedItems,
        viewSettings
    ) { items, settings ->
        categorizeItems(items, settings)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = CategorizedItems(emptyList(), emptyList(), emptyList())
    )

    fun selectCategory(category: MediaCategory) {
        _category.value = category
    }

    fun updateViewSettings(settings: LibraryViewSettings) {
        _viewSettings.value = settings
    }

    private fun sortItems(
        items: List<LibraryItemDto2>,
        settings: LibraryViewSettings,
        category: MediaCategory
    ): List<LibraryItemDto2> {
        val filtered = when (category) {
            MediaCategory.ALL -> items
            MediaCategory.SERIES -> items.filter { it.mediaType == "Series" }
            MediaCategory.MOVIES -> items.filter { it.mediaType == "Movie" }
        }

        val comparator = when (settings.sortBy) {
            SortBy.DATE_ADDED -> compareBy<LibraryItemDto2> { it.createdAt }
            SortBy.FIRST_RELEASE_DATE -> compareBy {
                it.firstAirDate ?: it.releaseDate ?: ""
            }
            SortBy.LAST_RELEASE_DATE -> compareBy {
                it.lastAirDate ?: it.firstAirDate ?: it.releaseDate ?: ""
            }
            SortBy.TITLE -> compareBy { it.title ?: it.name ?: "" }
        }

        return if (settings.sortDirection == SortDirection.ASC) {
            filtered.sortedWith(comparator)
        } else {
            filtered.sortedWith(comparator.reversed())
        }
    }

    private fun categorizeItems(
        items: List<LibraryItemDto2>,
        settings: LibraryViewSettings
    ): CategorizedItems {
        if (!settings.separateUpcoming && !settings.separateWatched) {
            return CategorizedItems(emptyList(), items, emptyList())
        }

        val upcoming = mutableListOf<LibraryItemDto2>()
        val main = mutableListOf<LibraryItemDto2>()
        val watched = mutableListOf<LibraryItemDto2>()

        val now = System.currentTimeMillis()

        items.forEach { item ->
            val releaseDate = item.releaseDate?.let {
                SimpleDateFormat("yyyy-MM-dd").parse(it)?.time
            } ?: (if (item.watched) {
                item.nextEpisodeToAir?.airDate?.let {
                    SimpleDateFormat("yyyy-MM-dd").parse(it)?.time
                }
            } else null) ?: 0L

            val hasFutureReleases = if (item.watched) {
                item.seasons?.any { it.airDate == null } == true
            } else {
                item.lastAirDate == null
            }

            when {
                settings.separateUpcoming &&
                (releaseDate > now || hasFutureReleases) ->
                    upcoming.add(item)

                settings.separateWatched && item.watched ->
                    watched.add(item)

                else ->
                    main.add(item)
            }
        }

        // Tri des upcoming par date de sortie
        upcoming.sortBy { item ->
            item.releaseDate?.let {
                SimpleDateFormat("yyyy-MM-dd").parse(it)?.time
            } ?: item.nextEpisodeToAir?.airDate?.let {
                SimpleDateFormat("yyyy-MM-dd").parse(it)?.time
            } ?: (now + 20L * 365 * 24 * 60 * 60 * 1000)
        }

        return CategorizedItems(upcoming, main, watched)
    }
}

// Composable principal
@Composable
fun LibraryPage(
    viewModel: LibraryViewModel = viewModel(),
    onNavigateToDetail: (String, String) -> Unit
) {
    val category by viewModel.category.collectAsState()
    val categorizedItems by viewModel.categorizedItems.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    var showOptionsDialog by remember { mutableStateOf(false) }

    Scaffold { paddingValues ->
        if (!isLoading) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(vertical = 64.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Barre de navigation
                LibraryNavigationBar(
                    selectedCategory = category,
                    onCategorySelected = { viewModel.selectCategory(it) },
                    onOptionsClick = { showOptionsDialog = true },
                    modifier = Modifier.padding(horizontal = 128.dp)
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Section Upcoming
                if (categorizedItems.upcoming.isNotEmpty()) {
                    LibraryCarouselSection(
                        title = "Upcoming",
                        items = categorizedItems.upcoming,
                        onItemClick = onNavigateToDetail,
                        modifier = Modifier.padding(top = 24.dp)
                    )
                }

                // Section My Library
                if (categorizedItems.main.isNotEmpty()) {
                    LibraryGridSection(
                        title = "My Library",
                        items = categorizedItems.main,
                        onItemClick = onNavigateToDetail,
                        modifier = Modifier
                            .padding(vertical = 24.dp)
                            .padding(horizontal = 128.dp)
                    )
                }

                // Section Watched
                if (categorizedItems.watched.isNotEmpty()) {
                    LibraryGridSection(
                        title = "Watched",
                        items = categorizedItems.watched,
                        onItemClick = onNavigateToDetail,
                        modifier = Modifier
                            .padding(top = 24.dp)
                            .padding(horizontal = 128.dp)
                    )
                }

                // État vide
                if (categorizedItems.upcoming.isEmpty() &&
                    categorizedItems.main.isEmpty() &&
                    categorizedItems.watched.isEmpty()) {
                    EmptyLibraryState(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp)
                    )
                }
            }
        }
    }

    // Dialog des options
    if (showOptionsDialog) {
        LibraryOptionsDialog(
            settings = viewModel.viewSettings.collectAsState().value,
            onSettingsChanged = { viewModel.updateViewSettings(it) },
            onDismiss = { showOptionsDialog = false }
        )
    }
}

// Barre de navigation avec onglets
@Composable
fun LibraryNavigationBar(
    selectedCategory: MediaCategory,
    onCategorySelected: (MediaCategory) -> Unit,
    onOptionsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TabItem(
                text = "All",
                selected = selectedCategory == MediaCategory.ALL,
                onClick = { onCategorySelected(MediaCategory.ALL) }
            )
            TabItem(
                text = "Series",
                selected = selectedCategory == MediaCategory.SERIES,
                onClick = { onCategorySelected(MediaCategory.SERIES) }
            )
            TabItem(
                text = "Movies",
                selected = selectedCategory == MediaCategory.MOVIES,
                onClick = { onCategorySelected(MediaCategory.MOVIES) }
            )
        }

        Button(
            onClick = onOptionsClick,
            colors = ButtonDefaults.buttonColors()
        ) {
            Icon(Icons.Default.Settings, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Options")
        }
    }
}

// Élément d'onglet
@Composable
fun TabItem(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val focusRequester = remember { FocusRequester() }
    var isFocused by remember { mutableStateOf(false) }

    Text(
        text = text,
        style = MaterialTheme.typography.headlineSmall,
        fontWeight = FontWeight.SemiBold,
        color = when {
            isFocused -> MaterialTheme.colorScheme.primary
            selected -> MaterialTheme.colorScheme.onSurface
            else -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        },
        modifier = modifier
            .clickable(onClick = onClick)
            .focusRequester(focusRequester)
            .onFocusChanged { isFocused = it.isFocused }
            .focusable()
    )
}

// Section carrousel (Upcoming)
@Composable
fun LibraryCarouselSection(
    title: String,
    items: List<LibraryItemDto2>,
    onItemClick: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 128.dp)
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall
            )
        }

        LazyRow(
            contentPadding = PaddingValues(horizontal = 128.dp),
            horizontalArrangement = Arrangement.spacedBy(32.dp),
            modifier = Modifier.padding(vertical = 16.dp)
        ) {
            items(
                items = items,
                key = { it.tmdbId }
            ) { item ->
                TmdbCard(
                    item = item,
                    size = CardSize.LARGE,
                    onClick = { onItemClick(item.tmdbId, item.mediaType) }
                )
            }
        }
    }
}

// Section grille (My Library / Watched)
@Composable
fun LibraryGridSection(
    title: String,
    items: List<LibraryItemDto2>,
    onItemClick: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        val configuration = LocalConfiguration.current
        val screenWidth = configuration.screenWidthDp.dp
        val columns = calculateGridColumns(screenWidth)

        LazyVerticalGrid(
            columns = GridCells.Fixed(columns),
            horizontalArrangement = Arrangement.spacedBy(32.dp),
            verticalArrangement = Arrangement.spacedBy(32.dp),
            modifier = Modifier.heightIn(max = 10000.dp)
        ) {
            items(
                items = items,
                key = { it.tmdbId }
            ) { item ->
                TmdbCard(
                    item = item,
                    size = CardSize.DYNAMIC,
                    progress = item.playStates?.firstOrNull()?.progress ?: 0f,
                    onClick = { onItemClick(item.tmdbId, item.mediaType) }
                )
            }
        }
    }
}

// Calcul du nombre de colonnes
fun calculateGridColumns(screenWidth: Dp): Int {
    return when {
        screenWidth >= 1536.dp -> 6
        screenWidth >= 1280.dp -> 5
        screenWidth >= 768.dp -> 4
        else -> 3
    }
}

// État vide
@Composable
fun EmptyLibraryState(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "You haven't added anything to your library",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
    }
}

// Dialog des options
@Composable
fun LibraryOptionsDialog(
    settings: LibraryViewSettings,
    onSettingsChanged: (LibraryViewSettings) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = MaterialTheme.shapes.large,
            tonalElevation = 6.dp
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = "View Options",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Sort by
                SelectButtonGroup(
                    title = "Sort by",
                    options = listOf(
                        "Last Release Date" to SortBy.LAST_RELEASE_DATE,
                        "First Release Date" to SortBy.FIRST_RELEASE_DATE,
                        "Date Added" to SortBy.DATE_ADDED,
                        "Title" to SortBy.TITLE
                    ),
                    selected = settings.sortBy,
                    onSelected = {
                        onSettingsChanged(settings.copy(sortBy = it))
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Direction
                SelectButtonGroup(
                    title = "Direction",
                    options = listOf(
                        "Ascending" to SortDirection.ASC,
                        "Descending" to SortDirection.DESC
                    ),
                    selected = settings.sortDirection,
                    onSelected = {
                        onSettingsChanged(settings.copy(sortDirection = it))
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Toggles
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    ToggleRow(
                        label = "Include upcoming",
                        checked = !settings.separateUpcoming,
                        onCheckedChange = {
                            onSettingsChanged(
                                settings.copy(separateUpcoming = !it)
                            )
                        }
                    )

                    ToggleRow(
                        label = "Include watched",
                        checked = !settings.separateWatched,
                        onCheckedChange = {
                            onSettingsChanged(
                                settings.copy(separateWatched = !it)
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun <T> SelectButtonGroup(
    title: String,
    options: List<Pair<String, T>>,
    selected: T,
    onSelected: (T) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            options.forEach { (label, value) ->
                FilterChip(
                    selected = selected == value,
                    onClick = { onSelected(value) },
                    label = { Text(label) }
                )
            }
        }
    }
}

@Composable
fun ToggleRow(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium
        )

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}
```

## 6. Points Clés d'Implémentation

### 6.1 Gestion de l'État
- **Svelte**: Stores réactifs (`writable`, `derived`) avec auto-subscription
- **Compose**: StateFlow avec `combine` pour les dérivations

### 6.2 Performance
- **Svelte**: `{#key viewSettingsKey}` pour forcer le re-render
- **Compose**: `key = { it.tmdbId }` dans les listes

### 6.3 Navigation TV
- **Svelte**: Système `Selectable` custom avec D-pad
- **Compose**: `FocusRequester` + `Modifier.focusable()` + gestion des touches

### 6.4 Responsive
- **Svelte**: `svelte:window` avec listener sur resize
- **Compose**: `LocalConfiguration.current` pour obtenir les dimensions

### 6.5 Scroll Automatique
- **Svelte**: Fonction `scrollIntoView` custom avec offsets
- **Compose**: `LazyListState.animateScrollToItem()` avec padding

### 6.6 Persistence
- **Svelte**: LocalStorage via store custom
- **Compose**: DataStore Preferences ou SharedPreferences

---

**Fichiers sources analysés**:
- `/Users/lolo/Dev/reiverr/src/lib/pages/LibraryPage/LibraryPage.svelte`
- `/Users/lolo/Dev/reiverr/src/lib/pages/LibraryPage/TabItem.svelte`
- `/Users/lolo/Dev/reiverr/src/lib/pages/LibraryPage/OptionsDialog.LibraryPage.svelte`
- `/Users/lolo/Dev/reiverr/src/lib/components/CardGrid.svelte`
- `/Users/lolo/Dev/reiverr/src/lib/components/Carousel/Carousel.svelte`
- `/Users/lolo/Dev/reiverr/src/lib/components/Container.svelte`
- `/Users/lolo/Dev/reiverr/src/lib/stores/localstorage.store.ts`
