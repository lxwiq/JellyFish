# Page de Détail d'une Série

## Vue d'ensemble

La page de détail d'une série (`SeriesPage.svelte`) affiche les informations complètes d'une série télévisée, incluant un carrousel héro avec bandes-annonces, la liste des saisons et épisodes, le casting et les recommandations.

**Fichier principal** : `/Users/lolo/Dev/reiverr/src/lib/pages/TitlePages/SeriesPage/SeriesPage.svelte`

---

## 1. Structure de la Page (Diagramme ASCII)

```
┌─────────────────────────────────────────────────────────────────┐
│ DetachedPage (Full screen overlay avec sidebar)                │
│ ┌─────────────────────────────────────────────────────────────┐ │
│ │ Container Hero (h-[calc(100vh-4rem)] py-16 px-32)           │ │
│ │ ┌─────────────────────────────────────────────────────────┐ │ │
│ │ │ HeroCarousel (Images/Vidéos en fond)                    │ │ │
│ │ │ ┌─────────────────────────────────────────────────────┐ │ │ │
│ │ │ │ Backdrop Images (5 max, avec transition fade)      │ │ │ │
│ │ │ │ + YouTube Video (trailer si disponible)            │ │ │ │
│ │ │ │                                                     │ │ │ │
│ │ │ │ Gradient Overlay (from-transparent to-secondary-900)│ │ │ │
│ │ │ └─────────────────────────────────────────────────────┘ │ │ │
│ │ │                                                           │ │ │
│ │ │ [Contenu superposé sur le carrousel]                     │ │ │
│ │ │ ┌─────────────────────────────────────────────────────┐ │ │ │
│ │ │ │ TitleProperties (HeroTitleInfo)                     │ │ │ │
│ │ │ │ • Titre (text-4xl/5xl/6xl selon longueur)           │ │ │ │
│ │ │ │ • Propriétés : Since/Ended • Note TMDB • Genres    │ │ │ │
│ │ │ │ • Overview (line-clamp-3, max-w-4xl)               │ │ │ │
│ │ │ └─────────────────────────────────────────────────────┘ │ │ │
│ │ │                                                           │ │ │
│ │ │ ┌─────────────────────────────────────────────────────┐ │ │ │
│ │ │ │ Boutons d'Action (Container horizontal, mt-8)       │ │ │ │
│ │ │ │ [Play S2E3] [Add/Remove Library] [Mark Watched]    │ │ │ │
│ │ │ │ [Open TMDB] [Open Jellyfin]                        │ │ │ │
│ │ │ └─────────────────────────────────────────────────────┘ │ │ │
│ │ └───────────────────────────────────────────────────────────┘ │ │
│ └─────────────────────────────────────────────────────────────┘ │
│                                                                   │
│ [Zone défilante avec contenu des saisons/épisodes]               │
│ ┌─────────────────────────────────────────────────────────────┐ │
│ │ EpisodeGrid (mx-32, -translate-y-16 quand scroll en haut)   │ │
│ │ ┌─────────────────────────────────────────────────────────┐ │ │
│ │ │ UICarousel - Sélecteur de Saisons (mb-8)               │ │ │
│ │ │ [Season 1] [Season 2] [Season 3] [Season 4]            │ │ │
│ │ │ (text-2xl, fond primary-500 si sélectionné)            │ │ │
│ │ └─────────────────────────────────────────────────────────┘ │ │
│ │                                                               │ │
│ │ ┌─────────────────────────────────────────────────────────┐ │ │
│ │ │ CardGrid (type: landscape, grille responsive)           │ │ │
│ │ │ ┌────────┐ ┌────────┐ ┌────────┐                       │ │ │
│ │ │ │Episode │ │Episode │ │Episode │                       │ │ │
│ │ │ │  1     │ │  2     │ │  3     │  (gap-x-8 gap-y-8)    │ │ │
│ │ │ └────────┘ └────────┘ └────────┘                       │ │ │
│ │ │ ┌────────┐ ┌────────┐ ┌────────┐                       │ │ │
│ │ │ │Episode │ │Episode │ │Episode │                       │ │ │
│ │ │ │  4     │ │  5     │ │  6     │                       │ │ │
│ │ │ └────────┘ └────────┘ └────────┘                       │ │ │
│ │ └─────────────────────────────────────────────────────────┘ │ │
│ └─────────────────────────────────────────────────────────────┘ │
│                                                                   │
│ ┌─────────────────────────────────────────────────────────────┐ │
│ │ Carousel - Show Cast (px-32, mb-8)                          │ │
│ │ [PersonCard] [PersonCard] [PersonCard] ... (15 max)         │ │
│ └─────────────────────────────────────────────────────────────┘ │
│                                                                   │
│ ┌─────────────────────────────────────────────────────────────┐ │
│ │ Carousel - Recommendations (px-32, mb-8)                    │ │
│ │ [TmdbCard] [TmdbCard] [TmdbCard] ...                        │ │
│ └─────────────────────────────────────────────────────────────┘ │
│                                                                   │
│ ┌─────────────────────────────────────────────────────────────┐ │
│ │ Container - More Information (bg-secondary-950 pt-8 px-32)  │ │
│ │ ┌──────────────────────┐  ┌──────────────────────┐         │ │
│ │ │ Created By          │  │ Language            │         │ │
│ │ │ Network             │  │ Last Air Date       │         │ │
│ │ └──────────────────────┘  └──────────────────────┘         │ │
│ └─────────────────────────────────────────────────────────────┘ │
└───────────────────────────────────────────────────────────────────┘
```

---

## 2. Composants Utilisés

### 2.1 Composants Principaux

| Composant | Fichier | Rôle |
|-----------|---------|------|
| `DetachedPage` | `/src/lib/components/DetachedPage/DetachedPage.svelte` | Page en plein écran avec sidebar, gestion de navigation et retour |
| `HeroCarousel` | `/src/lib/components/HeroCarousel/HeroCarousel.svelte` | Carrousel d'images/vidéos en fond avec navigation |
| `TitleProperties` (HeroTitleInfo) | `/src/lib/pages/TitlePages/HeroTitleInfo.svelte` | Affichage du titre, propriétés et synopsis |
| `EpisodeGrid` | `/src/lib/pages/TitlePages/SeriesPage/EpisodeGrid.svelte` | Grille des épisodes avec sélecteur de saisons |
| `Container` | `/src/lib/components/Container.svelte` | Conteneur avec gestion de navigation et focus |

### 2.2 Composants Secondaires

| Composant | Fichier | Rôle |
|-----------|---------|------|
| `Button` | `/src/lib/components/Button.svelte` | Boutons d'action (Play, Library, etc.) |
| `UICarousel` | `/src/lib/components/Carousel/UICarousel.svelte` | Carrousel horizontal pour sélection saisons |
| `Carousel` | `/src/lib/components/Carousel/Carousel.svelte` | Carrousel standard pour cast et recommandations |
| `CardGrid` | `/src/lib/components/CardGrid.svelte` | Grille responsive pour les épisodes |
| `TmdbEpisodeCard` | `/src/lib/components/EpisodeCard/TmdbEpisodeCard.svelte` | Carte d'un épisode avec backdrop, progression |
| `TmdbPersonCard` | `/src/lib/components/PersonCard/TmdbPersonCard.svelte` | Carte d'un membre du casting |
| `TmdbCard` | `/src/lib/components/Card/TmdbCard.svelte` | Carte générique TMDB (recommandations) |
| `YoutubeVideo` | `/src/lib/components/YoutubeVideo.svelte` | Lecteur vidéo YouTube intégré |

---

## 3. Layout : Dimensions, Espacement et Scroll

### 3.1 Dimensions de la Page

```typescript
// Page principale
DetachedPage: {
  position: "fixed",
  inset: 0,
  zIndex: 20,
  overflow: "auto", // scrollbar-hide
  background: "secondary-900"
}

// Container Hero
HeroContainer: {
  height: "calc(100vh - 4rem)", // Hauteur viewport - 4rem
  padding: "4rem 8rem", // py-16 px-32
  display: "flex",
  flexDirection: "column"
}
```

### 3.2 Espacement et Marges

```css
/* Section Hero */
.title-properties {
  margin-top: 0.25rem; /* mt-1 */
}

.properties-list {
  margin-top: 0.5rem; /* mt-2 */
  gap: 0.25rem; /* gap-1 */
}

.overview {
  margin-top: 1rem; /* mt-4 */
  max-width: 56rem; /* max-w-4xl */
}

.action-buttons {
  margin-top: 2rem; /* mt-8 */
}

.button {
  margin-right: 1rem; /* mr-4 */
}

/* EpisodeGrid */
.episode-grid-container {
  margin-left: 8rem; /* mx-32 */
  margin-right: 8rem;
  transform: translateY(-4rem); /* -translate-y-16 quand topVisible */
}

.season-carousel {
  margin-bottom: 2rem; /* mb-8 */
}

.card-grid {
  gap: 2rem; /* gap-x-8 gap-y-8 */
}

/* Carousels */
.carousel {
  padding-left: 8rem; /* px-32 */
  padding-right: 8rem;
  margin-bottom: 2rem; /* mb-8 */
}

.carousel-items {
  gap: 2rem; /* space-x-8 */
  padding: 1rem 0; /* py-4 */
}

/* More Information */
.more-info {
  padding-top: 2rem; /* pt-8 */
  padding-left: 8rem; /* px-32 */
  padding-right: 8rem;
}

.info-section {
  margin-bottom: 2rem; /* mb-8 */
}
```

### 3.3 Grille Responsive (CardGrid)

La grille d'épisodes s'adapte automatiquement selon la largeur de l'écran :

```typescript
// Calcul dynamique des colonnes basé sur la largeur
function getCardDimensions(width: number, type: 'landscape' | 'portrait') {
  // Pour type 'landscape' (épisodes)
  if (width >= 1920) return { columns: 4 }; // Ultra-wide
  if (width >= 1536) return { columns: 3 }; // 2XL
  if (width >= 1280) return { columns: 2 }; // XL
  if (width >= 768)  return { columns: 1 }; // MD
  return { columns: 1 }; // Mobile
}

// Appliqué comme style inline
style="grid-template-columns: repeat(${cols}, minmax(0, 1fr));"
```

### 3.4 Système de Scroll

La page utilise plusieurs mécanismes de scroll :

```typescript
// 1. Scroll principal (DetachedPage)
overflow-y-auto scrollbar-hide

// 2. Context de scroll (scroll.store)
const { registerScroll } = setScrollContext();
const { topVisible } = getScrollContext();

// Comportements :
// - topVisible = true : on est en haut (hero visible)
// - topVisible = false : on a scrollé vers le bas

// 3. Animations liées au scroll
EpisodeGrid: {
  // Se déplace vers le haut quand on scroll
  class: classNames('transition-transform', {
    '-translate-y-16': topVisible // -4rem vers le haut
  })
}

SeasonCarousel: {
  // Devient invisible quand on scroll
  class: classNames('transition-opacity', {
    'opacity-0': topVisible
  })
}

// 4. Auto-scroll sur focus
scrollIntoView({
  top: 0,      // Offset haut
  bottom: 128, // Offset bas
  horizontal: 128 // Offset horizontal pour carousels
})
```

### 3.5 Masques de Fade (Carousels)

```css
/* Carousel horizontal standard */
-webkit-mask-image: linear-gradient(
  to right,
  transparent,           /* Fade gauche */
  black 6rem,            /* Opaque après 6rem */
  black calc(100% - 6rem), /* Opaque jusqu'à 6rem de la fin */
  transparent            /* Fade droite */
);

/* UICarousel dynamique */
mask-image: linear-gradient(
  to right,
  transparent 0%,
  ${fadeLeft ? '' : 'black 0%, '} /* Pas de fade si scrollX = 0 */
  black 5%,
  black 95%,
  ${fadeRight ? '' : 'black 100%, '} /* Pas de fade si fin atteinte */
  transparent 100%
);
```

---

## 4. Navigation Saisons/Épisodes

### 4.1 Architecture de Navigation

```
SeriesPage (ID série)
    ↓
    └─ useSeriesUserData(id)
        ├─ tmdbSeries (données complètes série)
        ├─ nextEpisode (prochain épisode à regarder)
        └─ episodesUserData (progression de tous les épisodes)

    └─ EpisodeGrid
        ├─ tmdbSeasons (toutes les saisons chargées)
        ├─ seasonIndex (état local, index de la saison affichée)
        │   • Initialisé à 0
        │   • Change quand on clique sur un bouton de saison
        │   • Auto-focus sur la saison du nextEpisode
        │
        ├─ UICarousel (Sélection de saison)
        │   └─ Pour chaque saison :
        │       • Container avec focusOnClick
        │       • handleMountSeasonButton : auto-select si nextEpisode
        │       • onEnter : change seasonIndex
        │
        └─ CardGrid (Grille d'épisodes)
            └─ tmdbSeasons[seasonIndex].episodes
                └─ Pour chaque épisode :
                    • TmdbEpisodeCard
                    • Cliquable → navigate vers page détail épisode
                    • Route : /series/{id}/season/{season}/episode/{episode}
```

### 4.2 Flux de Données

```typescript
// 1. Chargement initial
const tmdbSeries = tmdbApi.getSeriesFull(tmdbId);

// 2. Chargement des saisons
const tmdbSeasons = tmdbSeries.then(series =>
  tmdbApi.getTmdbSeriesSeasons(
    tmdbId,
    series?.seasons?.length ?? 1
  )
);
// Retourne : Promise<TmdbSeason[]>
// Chaque TmdbSeason contient : { season_number, episodes: TmdbEpisode[] }

// 3. État de la saison affichée
let seasonIndex = 0; // 0-based (Season 1 = index 0)

// 4. Auto-sélection de la saison active
function handleMountSeasonButton(s: Selectable, seasonNumber: number) {
  nextEpisode.subscribe(episode => {
    if (episode?.season === seasonNumber) {
      seasonIndex = seasonNumber - 1; // Conversion 1-based → 0-based
      s.focus({ setFocusedElement: false, propagate: false });
    }
  });
}

// 5. Affichage des épisodes
{#each tmdbSeasons?.[seasonIndex]?.episodes || [] as episode}
  {@const userData = episodesUserData.find(
    e => e.season === episode.season_number &&
         e.episode === episode.episode_number
  )}
  <TmdbEpisodeCard
    {episode}
    isWatched={userData?.watched || false}
    progress={userData?.progress}
    on:clickOrSelect={() => handleOpenEpisodePage(episode)}
  />
{/each}
```

### 4.3 Gestion de Focus et Navigation

```typescript
// Navigation clavier dans EpisodeGrid
Container.on:navigate = ({ detail }) => {
  if (detail.direction === 'down' && detail.willLeaveContainer) {
    $episodeCards?.focus(); // Focus sur la grille d'épisodes
    detail.preventNavigation();
  }
}

// Scroll automatique sur focus
on:enter = scrollIntoView({
  top: 92,    // Espace pour le header de saison
  bottom: 128 // Espace pour voir l'épisode suivant
})

// Focus automatique sur l'épisode actuel (commenté dans le code actuel)
function handleMountCard(s: Selectable, episode: TmdbEpisode) {
  // Potentiellement utilisé pour auto-focus sur currentEpisode
}
```

### 4.4 États des Épisodes

```typescript
interface EpisodeData {
  season: number;
  episode: number;
  watched: boolean;    // Épisode marqué comme vu
  progress: number;    // Progression en % (0-100)
}

// Récupéré via useSeriesUserData
const {
  nextEpisode,        // Readable<EpisodeData>
  episodesUserData,   // EpisodeData[]
  isWatched,          // Readable<boolean> (série complète)
  toggleIsWatched     // Fonction pour marquer série vue/non-vue
} = useSeriesUserData(id);
```

---

## 5. Équivalent Kotlin/Jetpack Compose

Voici une implémentation conceptuelle de la page en Kotlin/Compose :

```kotlin
// SeriesDetailScreen.kt
@Composable
fun SeriesDetailScreen(
    seriesId: String,
    viewModel: SeriesDetailViewModel = viewModel(),
    onNavigateBack: () -> Unit
) {
    val seriesState by viewModel.seriesState.collectAsState()
    val seasonsState by viewModel.seasonsState.collectAsState()
    val nextEpisode by viewModel.nextEpisode.collectAsState()
    val episodesUserData by viewModel.episodesUserData.collectAsState()

    val scrollState = rememberScrollState()
    val isScrolled = scrollState.value > 100

    LaunchedEffect(seriesId) {
        viewModel.loadSeries(seriesId)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            // 1. Hero Background avec Carousel
            HeroCarouselBackground(
                images = seriesState.data?.images?.backdrops ?: emptyList(),
                trailerKey = seriesState.data?.videos?.results
                    ?.find { it.type == "Trailer" && it.site == "YouTube" }
                    ?.key,
                isScrolled = isScrolled
            )

            // 2. Contenu scrollable
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
            ) {
                // Hero Section (100vh - 4rem)
                HeroSection(
                    series = seriesState.data,
                    nextEpisode = nextEpisode,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(screenHeight - 64.dp)
                        .padding(vertical = 64.dp, horizontal = 128.dp)
                )

                // Grille d'épisodes
                EpisodeGridSection(
                    seasons = seasonsState.data ?: emptyList(),
                    episodesUserData = episodesUserData,
                    nextEpisode = nextEpisode,
                    isScrolled = isScrolled,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 128.dp)
                        .offset(y = if (isScrolled) (-64).dp else 0.dp)
                )

                // Cast
                CastCarousel(
                    cast = seriesState.data?.aggregateCredits?.cast?.take(15) ?: emptyList(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 128.dp, vertical = 32.dp)
                )

                // Recommendations
                RecommendationsCarousel(
                    recommendations = viewModel.recommendations.collectAsState().value,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 128.dp, vertical = 32.dp)
                )

                // More Information
                MoreInformationSection(
                    series = seriesState.data,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(horizontal = 128.dp, vertical = 32.dp)
                )
            }
        }
    }
}

// HeroSection.kt
@Composable
fun HeroSection(
    series: TmdbSeriesFull?,
    nextEpisode: EpisodeData?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Bottom
    ) {
        // Titre
        Text(
            text = series?.name ?: "",
            style = MaterialTheme.typography.displayLarge.copy(
                fontSize = when {
                    (series?.name?.length ?: 0) < 15 -> 60.sp
                    else -> 48.sp
                }
            ),
            color = Color(0xFFE7E5E4) // text-stone-200
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Propriétés (Status, Rating, Genres)
        TitleProperties(
            properties = buildTitleProperties(series),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Synopsis
        Text(
            text = series?.overview ?: "",
            style = MaterialTheme.typography.bodyLarge,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .alpha(0.75f)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Boutons d'action
        ActionButtons(
            nextEpisode = nextEpisode,
            inLibrary = series?.inLibrary ?: false,
            isWatched = series?.isWatched ?: false,
            onPlay = { /* ... */ },
            onAddToLibrary = { /* ... */ },
            onToggleWatched = { /* ... */ }
        )
    }
}

// EpisodeGridSection.kt
@Composable
fun EpisodeGridSection(
    seasons: List<TmdbSeason>,
    episodesUserData: List<EpisodeData>,
    nextEpisode: EpisodeData?,
    isScrolled: Boolean,
    modifier: Modifier = Modifier
) {
    var selectedSeasonIndex by remember {
        mutableStateOf(
            nextEpisode?.let { it.season - 1 } ?: 0
        )
    }

    Column(modifier = modifier) {
        // Sélecteur de saisons
        AnimatedVisibility(
            visible = !isScrolled,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            SeasonSelector(
                seasons = seasons,
                selectedIndex = selectedSeasonIndex,
                onSeasonSelected = { index -> selectedSeasonIndex = index },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp)
            )
        }

        // Grille d'épisodes
        val currentSeason = seasons.getOrNull(selectedSeasonIndex)
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 300.dp),
            horizontalArrangement = Arrangement.spacedBy(32.dp),
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            items(currentSeason?.episodes ?: emptyList()) { episode ->
                val userData = episodesUserData.find {
                    it.season == episode.seasonNumber &&
                    it.episode == episode.episodeNumber
                }

                EpisodeCard(
                    episode = episode,
                    isWatched = userData?.watched ?: false,
                    progress = userData?.progress ?: 0f,
                    onClick = {
                        // Navigate to episode detail
                    }
                )
            }
        }
    }
}

// SeasonSelector.kt
@Composable
fun SeasonSelector(
    seasons: List<TmdbSeason>,
    selectedIndex: Int,
    onSeasonSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        itemsIndexed(seasons) { index, season ->
            Surface(
                onClick = { onSeasonSelected(index) },
                shape = RoundedCornerShape(8.dp),
                color = if (index == selectedIndex) {
                    MaterialTheme.colorScheme.primary
                } else {
                    Color.Transparent
                },
                modifier = Modifier.padding(8.dp)
            ) {
                Text(
                    text = "Season ${season.seasonNumber}",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = when {
                        index == selectedIndex -> Color.Black
                        else -> Color(0xFF9CA3AF) // text-zinc-400
                    },
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                )
            }
        }
    }
}

// EpisodeCard.kt
@Composable
fun EpisodeCard(
    episode: TmdbSeasonEpisode,
    isWatched: Boolean,
    progress: Float,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(16f / 9f)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Backdrop Image
            AsyncImage(
                model = "${TmdbImageConfig.BACKDROP_SMALL}${episode.stillPath}",
                contentDescription = episode.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Gradient Overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.7f)
                            )
                        )
                    )
            )

            // Progress Bar
            if (progress > 0f) {
                LinearProgressIndicator(
                    progress = progress / 100f,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .align(Alignment.BottomCenter)
                )
            }

            // Watched Indicator
            if (isWatched) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Watched",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .size(24.dp)
                )
            }

            // Episode Info
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            ) {
                Text(
                    text = "Episode ${episode.episodeNumber}",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White
                )
                Text(
                    text = episode.name ?: "",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

// ActionButtons.kt
@Composable
fun ActionButtons(
    nextEpisode: EpisodeData?,
    inLibrary: Boolean,
    isWatched: Boolean,
    onPlay: () -> Unit,
    onAddToLibrary: () -> Unit,
    onToggleWatched: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Play Button
        Button(
            onClick = onPlay,
            modifier = Modifier.height(48.dp)
        ) {
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = "Play"
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = nextEpisode?.let {
                    "Play S${it.season}E${it.episode}"
                } ?: "Play"
            )
        }

        // Library Button
        OutlinedButton(
            onClick = onAddToLibrary,
            modifier = Modifier.height(48.dp)
        ) {
            Icon(
                imageVector = if (inLibrary) {
                    Icons.Default.Remove
                } else {
                    Icons.Default.Bookmark
                },
                contentDescription = if (inLibrary) "Remove" else "Add"
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (inLibrary) "Remove from Library" else "Add to Library"
            )
        }

        // Watched Button
        OutlinedButton(
            onClick = onToggleWatched,
            modifier = Modifier.height(48.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Watched"
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (isWatched) "Mark as Unwatched" else "Mark as Watched"
            )
        }
    }
}

// ViewModel
class SeriesDetailViewModel : ViewModel() {
    private val _seriesState = MutableStateFlow<UiState<TmdbSeriesFull>>(UiState.Loading)
    val seriesState: StateFlow<UiState<TmdbSeriesFull>> = _seriesState.asStateFlow()

    private val _seasonsState = MutableStateFlow<UiState<List<TmdbSeason>>>(UiState.Loading)
    val seasonsState: StateFlow<UiState<List<TmdbSeason>>> = _seasonsState.asStateFlow()

    private val _nextEpisode = MutableStateFlow<EpisodeData?>(null)
    val nextEpisode: StateFlow<EpisodeData?> = _nextEpisode.asStateFlow()

    private val _episodesUserData = MutableStateFlow<List<EpisodeData>>(emptyList())
    val episodesUserData: StateFlow<List<EpisodeData>> = _episodesUserData.asStateFlow()

    private val _recommendations = MutableStateFlow<List<TmdbSeries>>(emptyList())
    val recommendations: StateFlow<List<TmdbSeries>> = _recommendations.asStateFlow()

    fun loadSeries(seriesId: String) {
        viewModelScope.launch {
            try {
                // Charger les données de la série
                val series = tmdbRepository.getSeriesFull(seriesId.toInt())
                _seriesState.value = UiState.Success(series)

                // Charger les saisons
                val seasons = tmdbRepository.getSeriesSeasons(
                    seriesId.toInt(),
                    series.seasons?.size ?: 1
                )
                _seasonsState.value = UiState.Success(seasons)

                // Charger les données utilisateur
                loadUserData(seriesId)

                // Charger les recommandations
                val recs = tmdbRepository.getSeriesRecommendations(seriesId.toInt())
                _recommendations.value = recs
            } catch (e: Exception) {
                _seriesState.value = UiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    private suspend fun loadUserData(seriesId: String) {
        // Charger depuis la base de données locale
        val userData = userDataRepository.getSeriesUserData(seriesId)
        _episodesUserData.value = userData.episodes
        _nextEpisode.value = userData.nextEpisode
    }
}

sealed class UiState<out T> {
    object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
}
```

---

## 6. Points Clés d'Implémentation

### 6.1 Gestion des États

```typescript
// États réactifs Svelte
$: recommendations = tmdbApi.getSeriesRecommendations(tmdbId);
$: images = $tmdbSeries.then(series => { /* ... */ });

// Équivalent Compose
val recommendations by viewModel.recommendations.collectAsState()
val images by viewModel.images.collectAsState()
```

### 6.2 Navigation et Focus

**Svelte** : Système de `Selectable` custom avec `Container` et événements
**Compose** : Navigation native avec `NavController` et focus `Modifier.focusable()`

### 6.3 Performance

- **Images** : Lazy loading avec placeholders
- **Épisodes** : Seule la saison sélectionnée est affichée
- **Scroll** : Virtual scrolling pour les carousels (LazyRow en Compose)
- **Vidéo** : Chargement conditionnel du YouTube player

### 6.4 Animations

```css
/* Svelte/TailwindCSS */
transition-transform duration-500
transition-opacity duration-500

/* Compose */
AnimatedVisibility(
    enter = fadeIn() + slideInVertically(),
    exit = fadeOut() + slideOutVertically()
)

animateContentSize()
```

---

## 7. Arbre de Dépendances

```
SeriesPage.svelte
├── DetachedPage
│   └── Sidebar
├── Container (hero)
│   └── HeroCarousel
│       ├── YoutubeVideo
│       └── PageDots
├── TitleProperties (HeroTitleInfo)
├── Button (x5+)
│   └── Icon (radix-icons-svelte)
├── EpisodeGrid
│   ├── UICarousel (seasons)
│   └── CardGrid
│       └── TmdbEpisodeCard
│           └── EpisodeCard
├── Carousel (cast)
│   └── TmdbPersonCard
├── Carousel (recommendations)
│   └── TmdbCard
└── Container (more info)
```

---

## 8. Stores et État Global

```typescript
// Stores utilisés dans SeriesPage
import { localSettings } from '$lib/stores/localstorage.store';
// → { enableTrailers, autoplayTrailers }

import { setScrollContext, getScrollContext } from '$lib/stores/scroll.store';
// → { topVisible, registerScroll }

import { setUiVisibilityContext } from '$lib/stores/ui-visibility.store';
// → { visible, visibleStyle }

import { useSeriesUserData } from '$lib/stores/media-user-data.store';
// → { tmdbSeries, inLibrary, nextEpisode, episodesUserData,
//     handleAddToLibrary, handleAutoplay, isWatched, ... }
```

---

## Résumé

La page de détail d'une série est une page complexe avec :

- **Hero plein écran** (100vh - 4rem) avec carrousel d'images et vidéo trailer
- **Grille d'épisodes responsive** avec sélecteur de saisons horizontal
- **Carousels horizontaux** pour casting et recommandations
- **Système de scroll sophistiqué** avec animations (translate-y, opacity)
- **Navigation clavier/télécommande** via le système Selectable
- **États utilisateur** (progression, épisodes vus, bibliothèque)

L'équivalent Compose utiliserait :
- `LazyColumn` pour le scroll principal
- `LazyRow` pour les carousels horizontaux
- `LazyVerticalGrid` pour la grille d'épisodes
- `AnimatedVisibility` pour les transitions
- `ViewModel` + `StateFlow` pour la gestion d'état
- `Coil` ou `Glide` pour le chargement d'images
