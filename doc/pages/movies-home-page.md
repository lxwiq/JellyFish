# MoviesHomePage - Documentation

## Vue d'ensemble

La page `MoviesHomePage.svelte` est la page d'accueil principale pour la section Films de l'application. Elle présente une collection organisée de films avec un hero showcase en haut et plusieurs carrousels de contenu défilant verticalement.

**Fichier source**: `/Users/lolo/Dev/reiverr/src/lib/pages/MoviesHomePage.svelte`

---

## 1. Structure de la page (Diagramme ASCII)

```
┌─────────────────────────────────────────────────────────────────┐
│                        DetachedPage                             │
│  ┌───────────────────────────────────────────────────────────┐  │
│  │  Container (h: calc(100vh-12rem), px: 32)                 │  │
│  │  ┌─────────────────────────────────────────────────────┐  │  │
│  │  │                                                       │  │  │
│  │  │         TmdbMoviesHeroShowcase                        │  │  │
│  │  │         (Top 10 films recommandés)                    │  │  │
│  │  │                                                       │  │  │
│  │  └─────────────────────────────────────────────────────┘  │  │
│  └───────────────────────────────────────────────────────────┘  │
│                                                                 │
│  ┌───────────────────────────────────────────────────────────┐  │
│  │  Section Carrousels (my-16, space-y-8, z-10)             │  │
│  │                                                           │  │
│  │  [Si items non visionnés existent]                       │  │
│  │  ┌─────────────────────────────────────────────────────┐ │  │
│  │  │ Carousel: "Continue Watching"                       │ │  │
│  │  │ ┌────┐ ┌────┐ ┌────┐ ┌────┐                        │ │  │
│  │  │ │Card│ │Card│ │Card│ │Card│ ... (lg)               │ │  │
│  │  │ └────┘ └────┘ └────┘ └────┘                        │ │  │
│  │  └─────────────────────────────────────────────────────┘ │  │
│  │                                                           │  │
│  │  ┌─────────────────────────────────────────────────────┐ │  │
│  │  │ Carousel: "Popular"                                 │ │  │
│  │  │ ┌────┐ ┌────┐ ┌────┐ ┌────┐                        │ │  │
│  │  │ │Card│ │Card│ │Card│ │Card│ ... (lg)               │ │  │
│  │  │ └────┘ └────┘ └────┘ └────┘                        │ │  │
│  │  └─────────────────────────────────────────────────────┘ │  │
│  │                                                           │  │
│  │  ┌─────────────────────────────────────────────────────┐ │  │
│  │  │ Carousel: [Genre 1 recommandé]                      │ │  │
│  │  │ ┌────┐ ┌────┐ ┌────┐ ┌────┐                        │ │  │
│  │  │ │Card│ │Card│ │Card│ │Card│ ... (lg)               │ │  │
│  │  │ └────┘ └────┘ └────┘ └────┘                        │ │  │
│  │  └─────────────────────────────────────────────────────┘ │  │
│  │                                                           │  │
│  │  ┌─────────────────────────────────────────────────────┐ │  │
│  │  │ Carousel: "New Digital Releases"                    │ │  │
│  │  │ ┌────┐ ┌────┐ ┌────┐ ┌────┐                        │ │  │
│  │  │ │Card│ │Card│ │Card│ │Card│ ... (lg)               │ │  │
│  │  │ └────┘ └────┘ └────┘ └────┘                        │ │  │
│  │  └─────────────────────────────────────────────────────┘ │  │
│  │                                                           │  │
│  │  ┌─────────────────────────────────────────────────────┐ │  │
│  │  │ Carousel: [Genre 2 recommandé]                      │ │  │
│  │  └─────────────────────────────────────────────────────┘ │  │
│  │                                                           │  │
│  │  ┌─────────────────────────────────────────────────────┐ │  │
│  │  │ Carousel: "Upcoming Movies"                         │ │  │
│  │  └─────────────────────────────────────────────────────┘ │  │
│  │                                                           │  │
│  │  ┌─────────────────────────────────────────────────────┐ │  │
│  │  │ Carousel: [Genres 3, 4, 5, 6 recommandés]           │ │  │
│  │  └─────────────────────────────────────────────────────┘ │  │
│  │                                                           │  │
│  └───────────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────────┘
```

---

## 2. Composants utilisés

### Composants principaux

| Composant | Chemin | Utilisation |
|-----------|--------|-------------|
| `DetachedPage` | `$lib/components/DetachedPage/DetachedPage.svelte` | Conteneur principal de la page avec positionnement détaché |
| `Container` | `$lib/components/Container.svelte` | Conteneur pour le hero showcase avec dimensions fixes |
| `TmdbMoviesHeroShowcase` | `$lib/components/HeroShowcase/TmdbMoviesHeroShowcase.svelte` | Showcase hero affichant les films en rotation |
| `Carousel` | `$lib/components/Carousel/Carousel.svelte` | Carrousel horizontal scrollable pour afficher des collections de films |
| `TmdbCard` | `$lib/components/Card/TmdbCard.svelte` | Carte individuelle pour afficher un film |

### Imports de stores

- `libraryItemsDataStore` - Store contenant les éléments de la bibliothèque locale
- `setScrollContext` - Contexte pour gérer le défilement
- `setUiVisibilityContext` - Contexte pour gérer la visibilité de l'UI

### Imports d'API

- `TmdbApi`, `tmdbApi` - API pour récupérer les données depuis The Movie Database
- `TMDB_MOVIE_GENRES` - Constante contenant les genres de films

---

## 3. Layout (Dimensions, Espacement, Comportement de défilement)

### Dimensions

#### Hero Showcase Container
- **Hauteur**: `calc(100vh - 12rem)` (viewport moins 12rem)
- **Padding horizontal**: `px-32` (8rem de chaque côté)
- **Display**: `flex`

#### Section Carrousels
- **Margin vertical**: `my-16` (4rem en haut et en bas)
- **Espacement vertical**: `space-y-8` (2rem entre chaque carrousel)
- **Z-index**: `z-10` (positionnement au-dessus du hero)

### Espacement

#### Carrousels
- **Padding horizontal**: `px-32` (8rem sur les côtés, appliqué via `scrollClass`)
- Chaque carrousel a un espacement vertical de `2rem` entre eux

#### Cartes
- **Taille**: `lg` (grande taille)
- **Défilement horizontal**: décalage de `128px` pour le scrollIntoView

### Comportement de défilement

#### Enregistrement du scroll
```svelte
<div use:registerScroll />
```
Enregistre le contexte de défilement pour la page.

#### ScrollIntoView - Vertical
```svelte
on:enter={scrollIntoView({ vertical: 128 })}
```
Déclenché sur les carrousels - défile verticalement avec un offset de 128px.

#### ScrollIntoView - Horizontal
```svelte
on:enter={scrollIntoView({ horizontal: 128 })}
```
Déclenché sur les cartes - défile horizontalement avec un offset de 128px.

#### ScrollIntoView - Hero
```svelte
on:enter={scrollIntoView({ top: 0 })}
```
Déclenché sur le container du hero - force le défilement en haut de page.

---

## 4. Récupération de données et état

### Sources de données

#### 1. Bibliothèque locale - Continue Watching

```typescript
const { ...libraryData } = libraryItemsDataStore.subscribe();
const libraryContinueWatching = derived(libraryData, (libraryData) => {
    if (!libraryData) return [];

    const movies = libraryData.filter(
        (i) => i.mediaType === 'Movie' && i.playStates?.length && !i.watched
    );

    movies.sort((a, b) => {
        const aMax = Math.max(...(a.playStates?.map((p) => new Date(p.lastPlayedAt).getTime()) || [0]));
        const bMax = Math.max(...(b.playStates?.map((p) => new Date(p.lastPlayedAt).getTime()) || [0]));
        return bMax - aMax;
    });

    return movies;
});
```

**Logique**:
- Filtre les films non terminés avec un état de lecture
- Trie par date de dernière lecture (du plus récent au plus ancien)
- Utilise un store dérivé pour réactivité

#### 2. Films populaires

```typescript
const popularMovies = tmdbApi.getPopularMovies();
```

Récupère les films populaires via l'API TMDB.

#### 3. Nouvelles sorties digitales

```typescript
function getDigitalReleases() {
    return TmdbApi.getClient()
        .GET('/3/discover/movie', {
            params: {
                query: {
                    with_release_type: 4,
                    sort_by: 'popularity.desc',
                    'release_date.lte': formatDateToYearMonthDay(new Date())
                }
            }
        })
        .then((res) => res.data?.results || []);
}
```

**Paramètres**:
- `with_release_type: 4` - Type de sortie digitale
- `sort_by: popularity.desc` - Tri par popularité décroissante
- `release_date.lte: [aujourd'hui]` - Date de sortie antérieure ou égale à aujourd'hui

#### 4. Films à venir

```typescript
function getUpcomingMovies() {
    return TmdbApi.getClient()
        .GET('/3/discover/movie', {
            params: {
                query: {
                    'primary_release_date.gte': formatDateToYearMonthDay(new Date()),
                    sort_by: 'popularity.desc'
                }
            }
        })
        .then((res) => res.data?.results || []);
}
```

**Paramètres**:
- `primary_release_date.gte: [aujourd'hui]` - Date de sortie postérieure ou égale à aujourd'hui
- `sort_by: popularity.desc` - Tri par popularité décroissante

#### 5. Films recommandés par genre

```typescript
const recommendedMovies = tmdbApi.getRecommendedMovies();
const mostRecommendedGenres: Promise<number[]> = recommendedMovies.then(({ genreIdToMovie }) => {
    const allGenres = Object.keys(genreIdToMovie).map((k) => Number(k));
    allGenres.sort((a, b) => (genreIdToMovie[b]?.length || 0) - (genreIdToMovie[a]?.length || 0));
    return allGenres;
});
```

**Logique**:
- Récupère les films recommandés groupés par genre
- Trie les genres par nombre de films (du plus au moins de contenu)
- Utilisé pour afficher 6 carrousels de genres (indices 0-5)

### Gestion d'état réactif

#### Hero Showcase
```svelte
<TmdbMoviesHeroShowcase
    movies={recommendedMovies.then(({ top10 }) =>
        top10.length ? top10 : upcomingMovies.then((m) => m.slice(0, 10))
    )}
/>
```

Affiche le top 10 des recommandations, ou les 10 premiers films à venir si aucune recommandation.

#### Carrousels conditionnels
```svelte
{#if $libraryContinueWatching.length}
    <Carousel>...</Carousel>
{/if}
```

Les carrousels s'affichent uniquement si des données existent.

#### Await blocks
```svelte
{#await popularMovies then popularMovies}
    <Carousel>...</Carousel>
{/await}
```

Gestion asynchrone - le carrousel s'affiche une fois les données chargées.

### Lifecycle

```typescript
onDestroy(() => {
    libraryData.unsubscribe();
});
```

Nettoyage lors de la destruction du composant pour éviter les fuites mémoire.

---

## 5. Code équivalent Kotlin/Compose

```kotlin
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun MoviesHomePage(
    viewModel: MoviesHomeViewModel = viewModel()
) {
    // États collectés depuis le ViewModel
    val libraryData by viewModel.libraryData.collectAsStateWithLifecycle()
    val continueWatching by viewModel.continueWatching.collectAsStateWithLifecycle()
    val popularMovies by viewModel.popularMovies.collectAsStateWithLifecycle()
    val newDigitalReleases by viewModel.newDigitalReleases.collectAsStateWithLifecycle()
    val upcomingMovies by viewModel.upcomingMovies.collectAsStateWithLifecycle()
    val recommendedMovies by viewModel.recommendedMovies.collectAsStateWithLifecycle()
    val genreCarousels by viewModel.genreCarousels.collectAsStateWithLifecycle()

    // Contexte de scroll
    val scrollState = rememberScrollState()

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            // Hero Showcase
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(height = with(LocalDensity.current) {
                            // calc(100vh - 12rem) équivalent
                            LocalConfiguration.current.screenHeightDp.dp - 192.dp
                        })
                        .padding(horizontal = 128.dp)
                ) {
                    TmdbMoviesHeroShowcase(
                        movies = recommendedMovies?.top10?.takeIf { it.isNotEmpty() }
                            ?: upcomingMovies?.take(10) ?: emptyList()
                    )
                }
            }

            // Section des carrousels
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 64.dp),
                    verticalArrangement = Arrangement.spacedBy(32.dp)
                ) {
                    // Continue Watching
                    if (continueWatching.isNotEmpty()) {
                        MovieCarousel(
                            title = "Continue Watching",
                            items = continueWatching,
                            cardSize = CardSize.Large
                        )
                    }

                    // Popular
                    popularMovies?.let { movies ->
                        MovieCarousel(
                            title = "Popular",
                            items = movies,
                            cardSize = CardSize.Large
                        )
                    }

                    // Genre recommandé 1
                    genreCarousels.getOrNull(0)?.let { (genre, movies) ->
                        if (movies.isNotEmpty()) {
                            MovieCarousel(
                                title = genre.name,
                                items = movies,
                                cardSize = CardSize.Large
                            )
                        }
                    }

                    // New Digital Releases
                    newDigitalReleases?.let { movies ->
                        MovieCarousel(
                            title = "New Digital Releases",
                            items = movies,
                            cardSize = CardSize.Large
                        )
                    }

                    // Genres recommandés 2-6
                    (1..5).forEach { index ->
                        genreCarousels.getOrNull(index)?.let { (genre, movies) ->
                            if (movies.isNotEmpty()) {
                                MovieCarousel(
                                    title = genre.name,
                                    items = movies,
                                    cardSize = CardSize.Large
                                )
                            }
                        }
                    }

                    // Upcoming Movies
                    upcomingMovies?.let { movies ->
                        MovieCarousel(
                            title = "Upcoming Movies",
                            items = movies,
                            cardSize = CardSize.Large
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MovieCarousel(
    title: String,
    items: List<Movie>,
    cardSize: CardSize,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        // En-tête
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(horizontal = 128.dp, vertical = 8.dp)
        )

        // Liste horizontale
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(horizontal = 128.dp)
        ) {
            items(items) { movie ->
                TmdbCard(
                    item = movie,
                    size = cardSize
                )
            }
        }
    }
}

// ViewModel
class MoviesHomeViewModel : ViewModel() {
    private val tmdbApi = TmdbApi()
    private val libraryItemsRepository = LibraryItemsRepository()

    val libraryData = libraryItemsRepository.getLibraryItems()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val continueWatching = libraryData.map { items ->
        items.filter {
            it.mediaType == MediaType.Movie &&
            it.playStates.isNotEmpty() &&
            !it.watched
        }
        .sortedByDescending { item ->
            item.playStates.maxOfOrNull { it.lastPlayedAt } ?: 0L
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val popularMovies = flow {
        emit(tmdbApi.getPopularMovies())
    }.stateIn(viewModelScope, SharingStarted.Lazily, null)

    val newDigitalReleases = flow {
        emit(tmdbApi.getDigitalReleases())
    }.stateIn(viewModelScope, SharingStarted.Lazily, null)

    val upcomingMovies = flow {
        emit(tmdbApi.getUpcomingMovies())
    }.stateIn(viewModelScope, SharingStarted.Lazily, null)

    val recommendedMovies = flow {
        emit(tmdbApi.getRecommendedMovies())
    }.stateIn(viewModelScope, SharingStarted.Lazily, null)

    val genreCarousels = recommendedMovies.map { recommended ->
        recommended?.let {
            val sortedGenres = it.genreIdToMovie.entries
                .sortedByDescending { entry -> entry.value.size }
                .map { entry ->
                    val genre = TMDB_MOVIE_GENRES.find { g -> g.id == entry.key }
                    genre to entry.value
                }
                .filterNotNull()
            sortedGenres
        } ?: emptyList()
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
}

// Extension pour TmdbApi
suspend fun TmdbApi.getDigitalReleases(): List<Movie> {
    return client.get("/3/discover/movie") {
        parameter("with_release_type", 4)
        parameter("sort_by", "popularity.desc")
        parameter("release_date.lte", formatDateToYearMonthDay(Date()))
    }.body<DiscoverResponse>().results
}

suspend fun TmdbApi.getUpcomingMovies(): List<Movie> {
    return client.get("/3/discover/movie") {
        parameter("primary_release_date.gte", formatDateToYearMonthDay(Date()))
        parameter("sort_by", "popularity.desc")
    }.body<DiscoverResponse>().results
}

enum class CardSize {
    Small, Medium, Large
}
```

### Différences clés Svelte vs Compose

| Aspect | Svelte | Kotlin Compose |
|--------|--------|----------------|
| **Réactivité** | Stores avec `$` prefix | StateFlow/collectAsStateWithLifecycle |
| **Async** | `{#await}` blocks | Flow avec `.stateIn()` |
| **Boucles** | `{#each}` | `items()` dans LazyRow/LazyColumn |
| **Conditions** | `{#if}` | `if` classique ou `?.let {}` |
| **Lifecycle** | `onDestroy()` | `DisposableEffect` ou ViewModel cleanup |
| **Context** | Svelte context API | CompositionLocal |
| **Scroll** | Custom directives `use:` | rememberScrollState() |
| **Layout** | Tailwind classes | Modifier avec dp |

### Points d'attention pour la conversion

1. **Gestion d'état**: Svelte utilise des stores réactifs, Compose utilise StateFlow/State
2. **Asynchrone**: Les promises Svelte deviennent des Flow en Kotlin
3. **Padding**: `px-32` (Tailwind) = 128.dp en Compose (32 × 4)
4. **Hauteur viewport**: `calc(100vh - 12rem)` nécessite accès à LocalConfiguration en Compose
5. **Scroll behavior**: Les directives custom Svelte doivent être réimplémentées avec Modifier
6. **Keys**: `{#key}` en Svelte vs `key()` dans Compose pour forcer recomposition

---

## Conclusion

La page `MoviesHomePage` suit une structure claire et modulaire:
- Un hero showcase imposant en haut
- Une collection verticale de carrousels horizontaux
- Récupération asynchrone de données depuis TMDB et la bibliothèque locale
- Gestion intelligente du scroll et de la visibilité
- Réactivité complète grâce aux stores Svelte

Cette architecture est facilement transposable en Kotlin/Compose avec l'utilisation de StateFlow, LazyColumn/LazyRow, et un ViewModel pour la gestion d'état.
