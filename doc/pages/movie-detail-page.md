# Page de Détail de Film (MoviePage)

## Vue d'ensemble

La page de détail de film (MoviePage) est une page immersive qui affiche les informations détaillées d'un film, permettant la lecture, la gestion de la bibliothèque et l'affichage de contenu connexe.

**Fichier principal:** `/Users/lolo/Dev/reiverr/src/lib/pages/TitlePages/MoviePage/MoviePage.svelte`

## Structure de la Page (Diagramme ASCII)

```
┌─────────────────────────────────────────────────────────────────────┐
│                         DetachedPage                                 │
│  ┌───────────────────────────────────────────────────────────────┐  │
│  │                    HERO SECTION                                │  │
│  │  Hauteur: calc(100vh - 4rem)                                   │  │
│  │  Padding: py-16 px-32 (4rem vertical, 8rem horizontal)         │  │
│  │  ┌─────────────────────────────────────────────────────────┐  │  │
│  │  │              HeroCarousel (Images/Vidéo)                │  │  │
│  │  │  - Fond: Images backdrop TMDB (jusqu'à 5)              │  │  │
│  │  │  - Vidéo: Bande-annonce YouTube (optionnelle)          │  │  │
│  │  │  - Dégradé: transparent → secondary-900                │  │  │
│  │  │  ┌──────────────────────────────────────────────────┐  │  │  │
│  │  │  │         HeroTitleInfo                            │  │  │  │
│  │  │  │  • Titre + Année                                 │  │  │  │
│  │  │  │  • Durée • Note TMDB • Genres • Lien Trailer    │  │  │  │
│  │  │  │  • Synopsis (3 lignes max)                       │  │  │  │
│  │  │  └──────────────────────────────────────────────────┘  │  │  │
│  │  │  ┌──────────────────────────────────────────────────┐  │  │  │
│  │  │  │      Boutons d'Action (Container horizontal)    │  │  │  │
│  │  │  │  [Play] [Add/Remove Library] [Watched]          │  │  │  │
│  │  │  │  [Open TMDB] [Open Jellyfin] (Web uniquement)   │  │  │  │
│  │  │  └──────────────────────────────────────────────────┘  │  │  │
│  │  └─────────────────────────────────────────────────────────┘  │  │
│  └───────────────────────────────────────────────────────────────┘  │
│  ┌───────────────────────────────────────────────────────────────┐  │
│  │                 SECTION CAST (Carrousel)                       │  │
│  │  Padding: px-32 (8rem), mb-8                                   │  │
│  │  "Show Cast" - 15 acteurs maximum                              │  │
│  │  ┌────┐ ┌────┐ ┌────┐ ┌────┐ ┌────┐                          │  │
│  │  │Card│ │Card│ │Card│ │Card│ │Card│ ...                       │  │
│  │  └────┘ └────┘ └────┘ └────┘ └────┘                          │  │
│  └───────────────────────────────────────────────────────────────┘  │
│  ┌───────────────────────────────────────────────────────────────┐  │
│  │            SECTION RECOMMENDATIONS (Carrousel)                 │  │
│  │  Padding: px-32 (8rem), mb-8                                   │  │
│  │  "Recommendations"                                             │  │
│  │  ┌────┐ ┌────┐ ┌────┐ ┌────┐                                 │  │
│  │  │Card│ │Card│ │Card│ │Card│ ...                              │  │
│  │  └────┘ └────┘ └────┘ └────┘                                 │  │
│  └───────────────────────────────────────────────────────────────┘  │
│  ┌───────────────────────────────────────────────────────────────┐  │
│  │              SECTION MORE INFORMATION                          │  │
│  │  Background: bg-secondary-950, Padding: pt-8 px-32            │  │
│  │  ┌─────────────────────┬──────────────────────┐              │  │
│  │  │   Directed By       │    Languages         │              │  │
│  │  │   Written By        │    Release Date      │              │  │
│  │  └─────────────────────┴──────────────────────┘              │  │
│  └───────────────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────────────┘
```

## Composants Utilisés

### 1. Composants Principaux

#### DetachedPage
- **Rôle:** Conteneur de page avec navigation arrière
- **Props exportées:** `handleGoBack`, `registrar`
- **Fichier:** `/src/lib/components/DetachedPage/DetachedPage.svelte`

#### HeroCarousel
- **Rôle:** Affichage du carrousel d'images/vidéo en arrière-plan
- **Props:**
  - `itemsP`: Promise<{backdropUrl: string, videoUrl?: string}[]>
  - `autoFocusVideo`: boolean (true pour les films)
- **Fichier:** `/src/lib/components/HeroCarousel/HeroCarousel.svelte`
- **Fonctionnalités:**
  - Navigation gauche/droite entre les images
  - Lecture automatique de la bande-annonce YouTube
  - Effet de zoom parallaxe
  - Gestion du focus vidéo

#### HeroTitleInfo
- **Rôle:** Affichage du titre, métadonnées et synopsis
- **Props:**
  - `title`: string - Titre avec année (ex: "Inception (2010)")
  - `properties`: TitleInfoProperty[] - Durée, note, genres, trailer
  - `overview`: string - Synopsis du film
- **Fichier:** `/src/lib/pages/TitlePages/HeroTitleInfo.svelte`
- **Type TitleInfoProperty:**
  ```typescript
  {
    href?: string;      // Lien externe (TMDB, YouTube)
    label?: string;     // Texte à afficher
    icon?: ComponentType; // Icône Svelte
  }
  ```

### 2. Composants de Navigation

#### Carousel
- **Rôle:** Carrousel horizontal avec navigation
- **Props:**
  - `scrollClass`: "px-32" - Classes CSS pour le padding
  - `class`: "mb-8" - Marge inférieure
- **Slots:**
  - `header`: Titre du carrousel
  - Default: Contenu (cartes)
- **Fichier:** `/src/lib/components/Carousel/Carousel.svelte`

#### TmdbCard
- **Rôle:** Carte de film/série pour les recommandations
- **Props:** `item` - Données TMDB
- **Fichier:** `/src/lib/components/Card/TmdbCard.svelte`

#### TmdbPersonCard
- **Rôle:** Carte d'acteur/membre du casting
- **Props:** `tmdbCredit` - Informations de crédit TMDB
- **Fichier:** `/src/lib/components/PersonCard/TmdbPersonCard.svelte`

### 3. Composants d'Action

#### Button
- **Rôle:** Bouton d'action avec support multi-action
- **Props clés:**
  - `action`: Function - Action principale (clic/sélection)
  - `secondaryAction`: Function - Action secondaire
  - `disabled`: boolean - État désactivé
  - `icon`: ComponentType - Icône à afficher
- **Slots:**
  - `icon`: Icône avant le texte
  - `icon-after`: Icône après le texte
- **Fichier:** `/src/lib/components/Button.svelte`

#### Container
- **Rôle:** Conteneur navigable avec gestion du focus
- **Props:**
  - `direction`: "horizontal" | "vertical" | "grid"
  - `class`: Classes CSS personnalisées
  - `focusOnMount`: boolean
- **Fichier:** `/src/components/Container.svelte`

## Layout et Dimensions

### Espacements Principaux

| Zone | Padding Horizontal | Padding Vertical | Hauteur |
|------|-------------------|------------------|---------|
| Hero Section | `px-32` (8rem = 128px) | `py-16` (4rem = 64px) | `calc(100vh - 4rem)` |
| Cast Carousel | `px-32` (8rem) | - | Auto |
| Recommendations | `px-32` (8rem) | - | Auto |
| More Information | `px-32` (8rem) | `pt-8` (2rem) | Auto |

### Espacements entre Boutons

- Marge droite: `mr-4` (1rem = 16px)
- Marge supérieure du container: `mt-8` (2rem = 32px)

### Carrousels

- Espacement entre items: `space-x-8` (2rem = 32px)
- Padding vertical des items: `py-4` (1rem)
- Marge inférieure: `mb-8` (2rem)
- Masque de dégradé: 6rem transparent de chaque côté

### Texte

- **Titre principal:**
  - < 15 caractères: `text-4xl sm:text-5xl 2xl:text-6xl`
  - ≥ 15 caractères: `text-3xl sm:text-4xl 2xl:text-5xl`
- **Propriétés:** `text-lg` (18px)
- **Synopsis:** `max-w-4xl`, `line-clamp-3` (3 lignes max)
- **Section headings:** `text-2xl`

## Logique de Sélection de Stream

### Flux de Lecture Automatique (handleAutoplay)

```
1. Récupération des streams disponibles
   ↓
2. getAllStreams(tmdbId) → Promise<{source, streams}[]>
   ↓
3. Pour chaque source configurée:
   - getMovieStreams(source.id, tmdbId)
   - Retourne: VideoStreamCandidateDto[]
   ↓
4. Sélection du premier stream disponible
   - Première source avec streams.length > 0
   - Premier stream de cette source (index 0)
   ↓
5. Lancement de la lecture
   streamTmdbItem({tmdbId, progress, source, key})
   ↓
6. Si erreur: Notification "No source found" ou "No key found"
```

### Flux de Sélection Manuelle (handleOpenStreamSelector)

```
1. Ouverture du modal StreamSelectorModal
   ↓
2. Affichage des sources disponibles
   - Jellyfin, Jackett, etc.
   ↓
3. Utilisateur sélectionne une source
   ↓
4. Chargement des streams pour cette source
   getStreams(source, tmdbId)
   ↓
5. Affichage des streams avec propriétés
   - Titre
   - Résolution, codec, taille, etc.
   ↓
6. Utilisateur sélectionne un stream
   ↓
7. Lancement de la lecture
   streamTmdbItem({tmdbId, progress, source, key})
```

### Structure VideoStreamCandidateDto

```typescript
{
  key: string;              // Identifiant unique du stream
  title: string;            // Nom du fichier/stream
  properties: [             // Propriétés affichées
    {
      label: string;        // Ex: "Resolution", "Codec"
      value: any;           // Valeur brute
      formatted?: string;   // Valeur formatée (affichée)
    }
  ]
}
```

### Composants de Stream

#### MovieStreams.MoviePage.svelte
- **Non utilisé** dans MoviePage.svelte actuel
- Afficherait les streams groupés par source
- Grid responsive: 1 colonne si < 2 streams, 2 colonnes sinon
- Fichier: `/src/lib/pages/TitlePages/MoviePage/MovieStreams.MoviePage.svelte`

#### StreamDetailsDialog.MoviePage.svelte
- **Non utilisé** dans MoviePage.svelte actuel
- Dialogue modal avec détails du stream
- Affiche toutes les propriétés en grille 2 colonnes
- Actions: Play, Delete (désactivé)
- Fichier: `/src/lib/pages/TitlePages/MoviePage/StreamDetailsDialog.MoviePage.svelte`

## Stores et Gestion d'État

### tmdbMovieDataStore
- **Rôle:** Cache des données film TMDB
- **Retourne:** Promise<TmdbMovieDetails>
- **Contient:** title, overview, genres, runtime, vote_average, credits, images, videos

### useMovieUserData(id)
- **Rôle:** Hook pour les données utilisateur du film
- **Retourne:**
  ```typescript
  {
    inLibrary: Writable<boolean>,
    progress: Readable<number>,
    canStream: Writable<boolean>,
    isWatched: Writable<boolean>,
    handleAddToLibrary: () => Promise<void>,
    handleRemoveFromLibrary: () => Promise<void>,
    handleAutoplay: () => Promise<void>,
    handleOpenStreamSelector: () => Promise<void>,
    toggleIsWatched: () => Promise<void>,
    unsubscribe: () => void
  }
  ```

### Contexts
- **UiVisibilityContext:** `visibleStyle` pour masquer les éléments lors de la lecture vidéo
- **ScrollContext:** `registerScroll` pour enregistrer le scroll de la page

## Code Équivalent Kotlin/Compose

```kotlin
// MovieDetailScreen.kt
@Composable
fun MovieDetailScreen(
    movieId: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel: MovieDetailViewModel = viewModel()
    val movieState by viewModel.getMovie(movieId).collectAsState()
    val userDataState by viewModel.getUserData(movieId).collectAsState()

    val scrollState = rememberScrollState()
    val lazyListState = rememberLazyListState()

    Box(modifier = modifier.fillMaxSize()) {
        // Section Hero avec parallaxe
        HeroSection(
            movie = movieState,
            scrollOffset = scrollState.value,
            modifier = Modifier
                .fillMaxWidth()
                .height((LocalConfiguration.current.screenHeightDp - 64).dp)
                .padding(vertical = 64.dp, horizontal = 128.dp)
        ) {
            // Informations du titre
            HeroTitleInfo(
                title = "${movieState.title} (${movieState.releaseYear})",
                properties = buildTitleProperties(movieState),
                overview = movieState.overview,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // Boutons d'action
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(top = 32.dp)
            ) {
                // Bouton Play (action principale)
                PrimaryButton(
                    onClick = { viewModel.handleAutoplay(movieId, userDataState.progress) },
                    onLongClick = { viewModel.handleOpenStreamSelector(movieId) },
                    enabled = userDataState.canStream
                ) {
                    Icon(Icons.Default.PlayArrow, contentDescription = null)
                    Text("Play")
                }

                // Bouton Bibliothèque
                if (!userDataState.inLibrary) {
                    SecondaryButton(
                        onClick = { viewModel.handleAddToLibrary(movieId) }
                    ) {
                        Icon(Icons.Default.Bookmark, contentDescription = null)
                        Text("Add to Library")
                    }
                } else {
                    SecondaryButton(
                        onClick = { viewModel.handleRemoveFromLibrary(movieId) }
                    ) {
                        Icon(Icons.Default.Remove, contentDescription = null)
                        Text("Remove from Library")
                    }
                }

                // Bouton Watched
                SecondaryButton(
                    onClick = { viewModel.toggleIsWatched(movieId) }
                ) {
                    Icon(Icons.Default.Check, contentDescription = null)
                    Text(if (userDataState.isWatched) "Mark as Unwatched" else "Mark as Watched")
                }

                // Boutons externes (Web uniquement)
                if (BuildConfig.PLATFORM_WEB) {
                    SecondaryButton(
                        onClick = { openUrl("https://www.themoviedb.org/movie/${movieState.tmdbId}") }
                    ) {
                        Text("Open In TMDB")
                        Icon(Icons.Default.ExternalLink, contentDescription = null)
                    }

                    SecondaryButton(
                        onClick = { /* TODO: Jellyfin link */ }
                    ) {
                        Text("Open In Jellyfin")
                        Icon(Icons.Default.ExternalLink, contentDescription = null)
                    }
                }
            }
        }

        // Section scrollable avec casting et recommandations
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(top = (LocalConfiguration.current.screenHeightDp - 64).dp)
        ) {
            // Carrousel Cast
            CarouselSection(
                title = "Show Cast",
                modifier = Modifier.padding(horizontal = 128.dp, bottom = 32.dp)
            ) {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(32.dp),
                    contentPadding = PaddingValues(horizontal = 128.dp)
                ) {
                    items(movieState.credits.cast.take(15)) { credit ->
                        TmdbPersonCard(credit = credit)
                    }
                }
            }

            // Carrousel Recommandations
            CarouselSection(
                title = "Recommendations",
                modifier = Modifier.padding(horizontal = 128.dp, bottom = 32.dp)
            ) {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(32.dp),
                    contentPadding = PaddingValues(horizontal = 128.dp)
                ) {
                    items(movieState.recommendations) { recommendation ->
                        TmdbCard(item = recommendation)
                    }
                }
            }

            // Section More Information
            MoreInformationSection(
                movie = movieState,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF0A0A0F)) // secondary-950
                    .padding(horizontal = 128.dp, vertical = 32.dp)
            )
        }
    }
}

// HeroSection.kt
@Composable
fun HeroSection(
    movie: Movie?,
    scrollOffset: Int,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Box(modifier = modifier) {
        // Images de fond avec parallaxe
        movie?.images?.backdrops
            ?.sortedByDescending { it.voteCount }
            ?.take(5)
            ?.forEachIndexed { index, backdrop ->
                AsyncImage(
                    model = "https://image.tmdb.org/t/p/original${backdrop.filePath}",
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer {
                            alpha = if (index == 0) 1f else 0f
                            scaleX = 1f + (scrollOffset / 5000f)
                            scaleY = 1f + (scrollOffset / 5000f)
                        },
                    contentScale = ContentScale.Crop
                )
            }

        // Dégradé
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color(0xFF1C1C24)) // secondary-900
                    )
                )
        )

        // Contenu (titre et boutons)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 64.dp),
            verticalArrangement = Arrangement.Bottom
        ) {
            content()
        }
    }
}

// HeroTitleInfo.kt
@Composable
fun HeroTitleInfo(
    title: String,
    properties: List<TitleProperty>,
    overview: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        // Titre
        Text(
            text = title,
            fontSize = if (title.length < 15) 60.sp else 48.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = 0.5.sp,
            color = Color(0xFFF5F5F4), // stone-200
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Propriétés (durée, note, genres)
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            properties.forEachIndexed { index, property ->
                if (index > 0) {
                    Icon(
                        Icons.Default.Circle,
                        contentDescription = null,
                        modifier = Modifier.size(8.dp),
                        tint = Color(0xFFD4D4D8) // zinc-300
                    )
                }

                if (property.href != null) {
                    Text(
                        text = property.label ?: "",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFFD4D4D8),
                        modifier = Modifier.clickable { openUrl(property.href) }
                    )
                } else if (property.label != null) {
                    Text(
                        text = property.label,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFFD4D4D8)
                    )
                } else if (property.icon != null) {
                    Icon(
                        imageVector = property.icon,
                        contentDescription = null,
                        modifier = Modifier.size(22.dp),
                        tint = Color(0xFFD4D4D8)
                    )
                }
            }
        }

        // Synopsis
        Text(
            text = overview,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            color = Color(0xFFD4D4D8).copy(alpha = 0.75f),
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.widthIn(max = 896.dp) // max-w-4xl
        )
    }
}

// StreamSelectionViewModel.kt
class MovieDetailViewModel : ViewModel() {

    suspend fun handleAutoplay(tmdbId: String, progress: Int) {
        val sources = getAllStreams(tmdbId)

        // Sélection automatique du premier stream disponible
        val firstSource = sources.firstOrNull { it.streams.isNotEmpty() }
        val source = firstSource?.source
        val key = firstSource?.streams?.firstOrNull()?.key

        if (source == null) {
            showNotification("No source found")
            return
        }
        if (key == null) {
            showNotification("No key found")
            return
        }

        // Lancement de la lecture
        streamTmdbItem(
            tmdbId = tmdbId,
            progress = progress,
            source = source,
            key = key
        )
    }

    suspend fun handleOpenStreamSelector(tmdbId: String) {
        // Ouvrir le dialogue de sélection de stream
        val selectedStream = showStreamSelectorDialog(
            getStreams = { source -> getMovieStreams(source, tmdbId) },
            selectStream = { source, stream ->
                streamTmdbItem(
                    tmdbId = tmdbId,
                    progress = progress.value,
                    source = source,
                    key = stream.key
                )
            }
        )
    }

    private suspend fun getAllStreams(
        tmdbId: String
    ): List<SourceWithStreams> {
        return sources.map { source ->
            SourceWithStreams(
                source = source,
                streams = getMovieStreams(source, tmdbId)
            )
        }
    }

    private suspend fun getMovieStreams(
        source: MediaSource,
        tmdbId: String
    ): List<VideoStreamCandidate> {
        return try {
            api.sources.getMovieStreams(source.id, tmdbId).candidates
        } catch (e: Exception) {
            emptyList()
        }
    }
}

// Data classes
data class TitleProperty(
    val href: String? = null,
    val label: String? = null,
    val icon: ImageVector? = null
)

data class VideoStreamCandidate(
    val key: String,
    val title: String,
    val properties: List<StreamProperty>
)

data class StreamProperty(
    val label: String,
    val value: Any,
    val formatted: String?
)

data class SourceWithStreams(
    val source: MediaSource,
    val streams: List<VideoStreamCandidate>
)
```

## Notes d'Implémentation

### Gestion des Images
- Les backdrops TMDB sont triés par `vote_count` (popularité)
- Maximum 5 images dans le carrousel
- URL des images: `https://image.tmdb.org/t/p/original/{file_path}`
- La première image peut afficher la bande-annonce YouTube

### Bande-annonce Vidéo
- Recherche du premier trailer YouTube dans `videos.results`
- Lecture automatique si `enableTrailers` et `autoplayTrailers` activés
- Composant `YoutubeVideo` gère l'embed YouTube
- Le son est muet sauf si la vidéo a le focus

### Responsive
- Les carrousels utilisent un masque de dégradé sur les bords
- Navigation par chevrons gauche/droite (masqués sur TV)
- Scroll horizontal avec `scrollbar-hide`
- Adaptation du titre selon sa longueur

### Accessibilité
- Support navigation clavier/télécommande via `Container`
- `scrollIntoView` pour auto-scroll lors de la navigation
- Focus management avec `focusOnMount`
- Actions primaire et secondaire sur le bouton Play

### Performance
- Chargement lazy des recommandations
- Promise-based data fetching avec Svelte stores
- Unsubscribe automatique dans `onDestroy`
- Transition CSS optimisée avec `translate3d` pour GPU

## API et Types

### TMDB API
```typescript
tmdbApi.getMovieRecommendations(tmdbId: number): Promise<TmdbMovie[]>
```

### Reiverr API
```typescript
reiverrApi.sources.getMovieStreams(
  sourceId: string,
  tmdbId: string
): Promise<{ candidates: VideoStreamCandidateDto[] }>

reiverrApi.users.updateMoviePlayStateByTmdbId(
  userId: string,
  tmdbId: string,
  state: { watched: boolean }
): Promise<void>
```

### Utilitaires
```typescript
formatMinutesToTime(minutes: number): string    // Ex: "2h 28m"
formatThousands(num: number): string            // Ex: "1,234"
capitalize(str: string): string                 // Ex: "jellyfin" → "Jellyfin"
```

## Constantes

```typescript
PLATFORM_WEB: boolean               // true si plateforme web
TMDB_IMAGES_ORIGINAL: string        // "https://image.tmdb.org/t/p/original"
```
