# SeriesHomePage - Documentation Technique

## Vue d'ensemble

`SeriesHomePage.svelte` est la page d'accueil principale pour la navigation des séries télévisées dans l'application Reiverr. Elle affiche une vitrine hero avec des recommandations et plusieurs carrousels de contenu organisés par catégories.

**Fichier source:** `/Users/lolo/Dev/reiverr/src/lib/pages/SeriesHomePage.svelte`

---

## 1. Structure de la page (Diagramme ASCII)

```
┌─────────────────────────────────────────────────────────────────┐
│ DetachedPage (conteneur principal)                              │
│                                                                  │
│  ┌────────────────────────────────────────────────────────────┐ │
│  │ Container (Hero Section)                                    │ │
│  │ Hauteur: calc(100vh - 12rem)                                │ │
│  │ Padding: px-32 (8rem horizontal)                            │ │
│  │                                                              │ │
│  │  ┌──────────────────────────────────────────────────────┐  │ │
│  │  │ TmdbSeriesHeroShowcase                                │  │ │
│  │  │ (Vitrine animée avec les séries recommandées)         │  │ │
│  │  └──────────────────────────────────────────────────────┘  │ │
│  └────────────────────────────────────────────────────────────┘ │
│                                                                  │
│  ┌────────────────────────────────────────────────────────────┐ │
│  │ Section Carrousels (my-16 space-y-8)                        │ │
│  │ Margin vertical: 4rem, Espacement: 2rem                     │ │
│  │                                                              │ │
│  │  ┌────────────────────────────────────────────────────────┐ │ │
│  │  │ Carousel: "Continue Watching" (conditionnel)            │ │ │
│  │  │ - TmdbCard (size="lg") x N séries                       │ │ │
│  │  └────────────────────────────────────────────────────────┘ │ │
│  │                                                              │ │
│  │  ┌────────────────────────────────────────────────────────┐ │ │
│  │  │ Carousel: "Popular"                                     │ │ │
│  │  │ - TmdbCard (size="lg") x N séries                       │ │ │
│  │  └────────────────────────────────────────────────────────┘ │ │
│  │                                                              │ │
│  │  ┌────────────────────────────────────────────────────────┐ │ │
│  │  │ Carousel: Genre recommandé #1 (conditionnel)            │ │ │
│  │  │ - TmdbCard (size="lg") x N séries                       │ │ │
│  │  └────────────────────────────────────────────────────────┘ │ │
│  │                                                              │ │
│  │  ┌────────────────────────────────────────────────────────┐ │ │
│  │  │ Carousel: "Now Streaming"                               │ │ │
│  │  │ - TmdbCard (size="lg") x N séries                       │ │ │
│  │  └────────────────────────────────────────────────────────┘ │ │
│  │                                                              │ │
│  │  ┌────────────────────────────────────────────────────────┐ │ │
│  │  │ Carousel: Genre recommandé #2 (conditionnel)            │ │ │
│  │  │ - TmdbCard (size="lg") x N séries                       │ │ │
│  │  └────────────────────────────────────────────────────────┘ │ │
│  │                                                              │ │
│  │  ┌────────────────────────────────────────────────────────┐ │ │
│  │  │ Carousel: "Upcoming Series"                             │ │ │
│  │  │ - TmdbCard (size="lg") x N séries                       │ │ │
│  │  └────────────────────────────────────────────────────────┘ │ │
│  │                                                              │ │
│  │  ┌────────────────────────────────────────────────────────┐ │ │
│  │  │ Carousel: Genre recommandé #3 (conditionnel)            │ │ │
│  │  │ - TmdbCard (size="lg") x N séries                       │ │ │
│  │  └────────────────────────────────────────────────────────┘ │ │
│  │                                                              │ │
│  │  ┌────────────────────────────────────────────────────────┐ │ │
│  │  │ Carousel: Genre recommandé #4 (conditionnel)            │ │ │
│  │  │ - TmdbCard (size="lg") x N séries                       │ │ │
│  │  └────────────────────────────────────────────────────────┘ │ │
│  │                                                              │ │
│  │  ┌────────────────────────────────────────────────────────┐ │ │
│  │  │ Carousel: Genre recommandé #5 (conditionnel)            │ │ │
│  │  │ - TmdbCard (size="lg") x N séries                       │ │ │
│  │  └────────────────────────────────────────────────────────┘ │ │
│  │                                                              │ │
│  │  ┌────────────────────────────────────────────────────────┐ │ │
│  │  │ Carousel: Genre recommandé #6 (conditionnel)            │ │ │
│  │  │ - TmdbCard (size="lg") x N séries                       │ │ │
│  │  └────────────────────────────────────────────────────────┘ │ │
│  │                                                              │ │
│  └────────────────────────────────────────────────────────────┘ │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

---

## 2. Composants utilisés

### 2.1 Composants importés

| Composant | Source | Rôle |
|-----------|--------|------|
| `DetachedPage` | `$lib/components/DetachedPage/DetachedPage.svelte` | Conteneur de page principal avec gestion du layout |
| `Container` | `$lib/components/Container.svelte` | Conteneur pour la section hero |
| `TmdbSeriesHeroShowcase` | `$lib/components/HeroShowcase/TmdbSeriesHeroShowcase.svelte` | Vitrine animée plein écran pour les séries |
| `Carousel` | `$lib/components/Carousel/Carousel.svelte` | Carrousel horizontal scrollable |
| `TmdbCard` | `$lib/components/Card/TmdbCard.svelte` | Carte individuelle pour chaque série |

### 2.2 Hiérarchie des composants

```
DetachedPage
├── div (scroll registration)
├── Container (Hero)
│   └── TmdbSeriesHeroShowcase
└── div (Carousels container)
    ├── Carousel (Continue Watching)
    │   └── TmdbCard[] (size="lg")
    ├── Carousel (Popular)
    │   └── TmdbCard[] (size="lg")
    ├── Carousel (Genre #1)
    │   └── TmdbCard[] (size="lg")
    ├── Carousel (Now Streaming)
    │   └── TmdbCard[] (size="lg")
    ├── Carousel (Genre #2)
    │   └── TmdbCard[] (size="lg")
    ├── Carousel (Upcoming Series)
    │   └── TmdbCard[] (size="lg")
    ├── Carousel (Genre #3)
    │   └── TmdbCard[] (size="lg")
    ├── Carousel (Genre #4)
    │   └── TmdbCard[] (size="lg")
    ├── Carousel (Genre #5)
    │   └── TmdbCard[] (size="lg")
    └── Carousel (Genre #6)
        └── TmdbCard[] (size="lg")
```

---

## 3. Layout (Dimensions, Espacement, Comportement de Scroll)

### 3.1 Dimensions

| Élément | Classe CSS | Valeur calculée |
|---------|-----------|-----------------|
| Hero Container | `h-[calc(100vh-12rem)]` | Hauteur viewport - 12rem |
| Hero Container | `px-32` | Padding horizontal: 8rem (128px) |
| Carousels Container | `my-16` | Margin vertical: 4rem (64px) |
| Carousels Container | `space-y-8` | Espacement vertical entre carrousels: 2rem (32px) |
| Carousels | `scrollClass="px-32"` | Padding horizontal du scroll: 8rem (128px) |
| TmdbCard | `size="lg"` | Taille large (définie dans le composant) |

### 3.2 Espacements

- **Hero Section**: Occupe environ 75% de la hauteur de viewport
- **Marge entre Hero et Carrousels**: 4rem (64px)
- **Espacement entre carrousels**: 2rem (32px)
- **Padding horizontal global**: 8rem (128px) de chaque côté

### 3.3 Comportement de scroll

#### Enregistrement du scroll
```javascript
const { registerScroll } = setScrollContext();
```
- Un `div` avec `use:registerScroll` enregistre le contexte de scroll de la page

#### Scroll automatique vers les éléments
- **Hero Container**: `on:enter={scrollIntoView({ top: 0 })}`
  - Scroll en haut (position 0) lors de l'entrée

- **Carousels**: `on:enter={scrollIntoView({ vertical: 128 })}`
  - Offset vertical de 128px lors de l'entrée sur un carrousel

- **TmdbCards**: `on:enter={scrollIntoView({ horizontal: 128 })}`
  - Offset horizontal de 128px lors de la sélection d'une carte

#### Visibilité de l'interface
```javascript
const { visibleStyle } = setUiVisibilityContext();
```
- La section carrousels utilise `style={$visibleStyle}` pour gérer l'affichage/masquage de l'UI

---

## 4. Récupération des données et état

### 4.1 Sources de données

#### 4.1.1 API TMDB

| Variable | Fonction API | Description |
|----------|-------------|-------------|
| `popular` | `tmdbApi.getPopularSeries()` | Séries populaires du moment |
| `recommendations` | `tmdbApi.getRecommendedSeries()` | Recommandations personnalisées avec genres |
| `nowStreaming` | `getNowStreaming()` | Séries actuellement diffusées |
| `upcomingSeries` | `fetchUpcomingSeries()` | Séries à venir |

#### 4.1.2 Bibliothèque locale

```javascript
const { ...libraryData } = libraryItemsDataStore.subscribe();
const libraryContinueWatching = derived(libraryData, (libraryData) => {
    // Filtrage et tri des séries en cours de visionnage
});
```

**Logique de filtrage "Continue Watching":**
1. Filtre les séries avec `mediaType === 'Series'`
2. Doit avoir des `playStates` (historique de lecture)
3. Ne doit pas être marquée comme `watched` (terminée)
4. Tri par date de dernière lecture (plus récent en premier)

### 4.2 Fonctions de récupération personnalisées

#### `getNowStreaming()`
```javascript
GET '/3/discover/tv'
Paramètres:
  - air_date.gte: aujourd'hui (séries avec épisodes diffusés aujourd'hui ou après)
  - first_air_date.lte: aujourd'hui (séries commencées avant ou aujourd'hui)
  - sort_by: popularity.desc
```

#### `fetchUpcomingSeries()`
```javascript
GET '/3/discover/tv'
Paramètres:
  - first_air_date.gte: aujourd'hui (séries qui commencent dans le futur)
  - sort_by: popularity.desc
```

### 4.3 Gestion d'état

#### Stores Svelte
- `libraryItemsDataStore`: Store global pour les éléments de bibliothèque
- `libraryContinueWatching`: Store dérivé pour le filtrage Continue Watching
- `visibleStyle`: Store de contexte pour la visibilité UI
- `registerScroll`: Store de contexte pour le scroll

#### Clés réactives
```javascript
$: libraryContinueWatchingKey = $libraryContinueWatching && Symbol();
```
- Crée une nouvelle clé (Symbol) quand `libraryContinueWatching` change
- Force le re-render du bloc `{#key}` pour mettre à jour le carrousel

#### Promesses asynchrones
Les données TMDB sont gérées via des blocs `{#await}`:
```svelte
{#await popular then popular}
  <!-- Rendu une fois les données chargées -->
{/await}
```

#### Lifecycle
```javascript
onDestroy(() => {
    libraryData.unsubscribe();
});
```
- Nettoyage de l'abonnement au store lors de la destruction du composant

### 4.4 Structure des recommandations

Le store `recommendations` retourne:
```javascript
{
  top10: Series[],          // Top 10 séries pour le hero
  genreIdToMovie: Map,      // Map genre ID -> séries
  topGenres: string[]       // IDs des genres les plus pertinents (triés)
}
```

Les carrousels de genres utilisent les index `topGenres[0]` à `topGenres[5]` pour afficher jusqu'à 6 genres recommandés.

---

## 5. Équivalent Kotlin/Compose

Voici une implémentation équivalente en Jetpack Compose pour Android:

```kotlin
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

// ViewModel pour la gestion d'état
class SeriesHomeViewModel(
    private val tmdbApi: TmdbApi,
    private val libraryRepository: LibraryRepository
) : ViewModel() {

    // État pour les différentes sources de données
    private val _popular = MutableStateFlow<List<TmdbSeries>>(emptyList())
    val popular: StateFlow<List<TmdbSeries>> = _popular.asStateFlow()

    private val _recommendations = MutableStateFlow<SeriesRecommendations?>(null)
    val recommendations: StateFlow<SeriesRecommendations?> = _recommendations.asStateFlow()

    private val _nowStreaming = MutableStateFlow<List<TmdbSeries>>(emptyList())
    val nowStreaming: StateFlow<List<TmdbSeries>> = _nowStreaming.asStateFlow()

    private val _upcomingSeries = MutableStateFlow<List<TmdbSeries>>(emptyList())
    val upcomingSeries: StateFlow<List<TmdbSeries>> = _upcomingSeries.asStateFlow()

    // Store dérivé pour "Continue Watching"
    val libraryContinueWatching: StateFlow<List<LibraryItem>> =
        libraryRepository.libraryItemsFlow
            .map { items ->
                items.filter { item ->
                    item.mediaType == MediaType.SERIES &&
                    item.playStates.isNotEmpty() &&
                    !item.watched
                }.sortedByDescending { item ->
                    item.playStates.maxOfOrNull { it.lastPlayedAt } ?: 0L
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            // Lancement parallèle de toutes les requêtes
            launch { _popular.value = tmdbApi.getPopularSeries() }
            launch { _recommendations.value = tmdbApi.getRecommendedSeries() }
            launch { _nowStreaming.value = getNowStreaming() }
            launch { _upcomingSeries.value = fetchUpcomingSeries() }
        }
    }

    private suspend fun getNowStreaming(): List<TmdbSeries> {
        val today = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
        return tmdbApi.discoverSeries(
            airDateGte = today,
            firstAirDateLte = today,
            sortBy = "popularity.desc"
        )
    }

    private suspend fun fetchUpcomingSeries(): List<TmdbSeries> {
        val today = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
        return tmdbApi.discoverSeries(
            firstAirDateGte = today,
            sortBy = "popularity.desc"
        )
    }
}

// Data classes
data class SeriesRecommendations(
    val top10: List<TmdbSeries>,
    val genreIdToMovie: Map<String, List<TmdbSeries>>,
    val topGenres: List<String>
)

// Composable principal
@Composable
fun SeriesHomePage(
    viewModel: SeriesHomeViewModel = viewModel()
) {
    val scrollState = rememberScrollState()
    val popular by viewModel.popular.collectAsState()
    val recommendations by viewModel.recommendations.collectAsState()
    val nowStreaming by viewModel.nowStreaming.collectAsState()
    val upcomingSeries by viewModel.upcomingSeries.collectAsState()
    val continueWatching by viewModel.libraryContinueWatching.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        // Hero Section
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(
                    // calc(100vh - 12rem) approximativement
                    with(LocalDensity.current) {
                        (LocalConfiguration.current.screenHeightDp.dp - 192.dp)
                    }
                )
                .padding(horizontal = 128.dp)
        ) {
            TmdbSeriesHeroShowcase(
                series = recommendations?.top10?.takeIf { it.isNotEmpty() }
                    ?: upcomingSeries.take(10)
            )
        }

        // Section Carrousels
        Column(
            modifier = Modifier
                .padding(vertical = 64.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            // Continue Watching (conditionnel)
            if (continueWatching.isNotEmpty()) {
                SeriesCarousel(
                    title = "Continue Watching",
                    items = continueWatching
                )
            }

            // Popular
            SeriesCarousel(
                title = "Popular",
                items = popular
            )

            // Genre recommandé #1
            recommendations?.let { recs ->
                if (recs.topGenres.isNotEmpty()) {
                    val genreId = recs.topGenres[0]
                    recs.genreIdToMovie[genreId]?.let { items ->
                        SeriesCarousel(
                            title = TmdbSeriesGenres.findById(genreId)?.name ?: "",
                            items = items
                        )
                    }
                }
            }

            // Now Streaming
            SeriesCarousel(
                title = "Now Streaming",
                items = nowStreaming
            )

            // Genres recommandés #2 à #6
            recommendations?.let { recs ->
                for (index in 1..5) {
                    if (index < recs.topGenres.size) {
                        val genreId = recs.topGenres[index]
                        recs.genreIdToMovie[genreId]?.let { items ->
                            SeriesCarousel(
                                title = TmdbSeriesGenres.findById(genreId)?.name ?: "",
                                items = items
                            )
                        }
                    }
                }
            }

            // Upcoming Series
            SeriesCarousel(
                title = "Upcoming Series",
                items = upcomingSeries
            )
        }
    }
}

// Composable de carrousel
@Composable
fun <T> SeriesCarousel(
    title: String,
    items: List<T>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        // Header
        Text(
            text = title,
            modifier = Modifier.padding(horizontal = 128.dp, vertical = 8.dp),
            style = MaterialTheme.typography.headlineSmall
        )

        // Carrousel horizontal
        LazyRow(
            contentPadding = PaddingValues(horizontal = 128.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(items) { item ->
                TmdbCard(
                    item = item,
                    size = CardSize.Large
                )
            }
        }
    }
}

// Composable HeroShowcase
@Composable
fun TmdbSeriesHeroShowcase(
    series: List<TmdbSeries>,
    modifier: Modifier = Modifier
) {
    var currentIndex by remember { mutableStateOf(0) }

    // Auto-rotation toutes les 5 secondes
    LaunchedEffect(series) {
        if (series.isNotEmpty()) {
            while (true) {
                kotlinx.coroutines.delay(5000)
                currentIndex = (currentIndex + 1) % series.size
            }
        }
    }

    if (series.isNotEmpty()) {
        Box(modifier = modifier.fillMaxSize()) {
            // Affichage de la série actuelle avec image de fond, titre, etc.
            SeriesHeroCard(series[currentIndex])

            // Indicateurs de pagination
            HeroPaginationIndicators(
                count = series.size,
                currentIndex = currentIndex,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}

// Composable de carte TMDB
@Composable
fun TmdbCard(
    item: Any,
    size: CardSize,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .width(
                when (size) {
                    CardSize.Large -> 300.dp
                    CardSize.Medium -> 200.dp
                    CardSize.Small -> 150.dp
                }
            )
            .aspectRatio(2f / 3f) // Ratio poster typique
            .clickable { /* Navigation vers les détails */ }
    ) {
        // Contenu de la carte: image, titre, etc.
        when (item) {
            is TmdbSeries -> SeriesCardContent(item)
            is LibraryItem -> LibraryItemCardContent(item)
        }
    }
}

enum class CardSize {
    Small, Medium, Large
}
```

### Points clés de l'équivalent Compose:

1. **Architecture MVVM**: Le `ViewModel` gère l'état et les appels API
2. **StateFlow**: Équivalent des stores Svelte pour la réactivité
3. **Coroutines**: Gestion asynchrone similaire aux Promises JavaScript
4. **LazyRow**: Équivalent du carrousel horizontal avec scroll
5. **Modifiers**: Système de styling similaire aux classes Tailwind
6. **collectAsState()**: Observe les StateFlow et recompose automatiquement
7. **Spacing**: Utilisation de `Arrangement.spacedBy()` et `padding()`
8. **Conditional rendering**: `if` statements pour affichage conditionnel

### Différences notables:

- **Scroll**: Compose utilise `verticalScroll()` au lieu de scroll natif HTML
- **Layout**: `Column` et `LazyRow` remplacent les `div` et carrousels CSS
- **État dérivé**: `map()` sur Flow au lieu de `derived()` de Svelte
- **Lifecycle**: `viewModelScope` gère automatiquement le nettoyage

---

## Notes supplémentaires

### Fonctionnalités commentées
Le code source contient des commentaires indiquant des fonctionnalités futures:
- `<!-- NETWORKS -->`: Section réseaux TV à implémenter
- `<!-- GENRES -->`: Section genres supplémentaires
- `<!-- TOP RATED -->`: Séries les mieux notées
- `<!-- TRENDING PEOPLE -->`: Personnes tendance
- `<!-- Watchlist -->`: Liste de suivi utilisateur

### Paramètres commentés
Plusieurs paramètres API sont commentés, probablement pour une future internationalisation:
```javascript
// language: $settings.language,
// with_original_language: parseIncludedLanguages($settings.discover.includedLanguages)
```

### Optimisations possibles
1. **Lazy loading**: Les carrousels pourraient charger les données à la demande
2. **Pagination**: Actuellement, toutes les données sont chargées d'un coup
3. **Cache**: Pas de mise en cache explicite des résultats API
4. **Virtualisation**: Les carrousels pourraient bénéficier de virtualisation pour de grandes listes
