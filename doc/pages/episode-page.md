# Page Épisode (EpisodePage.svelte)

## Vue d'ensemble

La page Episode est une page détachée en plein écran qui affiche les informations d'un épisode spécifique d'une série télévisée. Elle permet la lecture, le marquage comme vu/non vu, et l'accès aux liens externes.

**Fichier**: `/Users/lolo/Dev/reiverr/src/lib/pages/TitlePages/EpisodePage.svelte`

## 1. Structure de la page (Diagramme ASCII)

```
┌──────────────────────────────────────────────────────────────────────────┐
│ DetachedPage (full-screen overlay, z-index: 20)                         │
│  ┌────────────────────────────────────────────────────────────────────┐  │
│  │ Sidebar (optionnel)                                                │  │
│  └────────────────────────────────────────────────────────────────────┘  │
│                                                                            │
│  ┌────────────────────────────────────────────────────────────────────┐  │
│  │ Arrière-plan (-z-10)                                               │  │
│  │ ┌──────────────────────────────────────────────────────────────┐   │  │
│  │ │ Image de fond: still_path de l'épisode                        │   │  │
│  │ │ (position: absolute, bg-cover, bg-center)                     │   │  │
│  │ └──────────────────────────────────────────────────────────────┘   │  │
│  │ ┌──────────────────────────────────────────────────────────────┐   │  │
│  │ │ Gradient overlay (transparent → secondary-900)                │   │  │
│  │ │ Height: 100vh                                                 │   │  │
│  │ └──────────────────────────────────────────────────────────────┘   │  │
│  │ ┌──────────────────────────────────────────────────────────────┐   │  │
│  │ │ Fond coloré: bg-secondary-500                                 │   │  │
│  │ │ Flex-1 (remplissage restant)                                  │   │  │
│  │ └──────────────────────────────────────────────────────────────┘   │  │
│  └────────────────────────────────────────────────────────────────────┘  │
│                                                                            │
│  ┌────────────────────────────────────────────────────────────────────┐  │
│  │ Container principal (mx-32, py-16, h-screen)                      │  │
│  │                                                                     │  │
│  │   [Disposition: flex flex-col justify-end]                         │  │
│  │                                                                     │  │
│  │   ┌─────────────────────────────────────────────────────────────┐  │  │
│  │   │ Numéro Saison/Épisode                                       │  │  │
│  │   │ (text-zinc-200, text-lg, font-medium)                       │  │  │
│  │   │ "Season X Episode Y"                                        │  │  │
│  │   └─────────────────────────────────────────────────────────────┘  │  │
│  │                                                                     │  │
│  │   ┌─────────────────────────────────────────────────────────────┐  │  │
│  │   │ TitleProperties                                             │  │  │
│  │   │  - Titre de l'épisode (responsive: 4xl-6xl)                 │  │  │
│  │   │  - Propriétés (note TMDB • durée • date diffusion)          │  │  │
│  │   │  - Synopsis (line-clamp-3, max-w-4xl)                       │  │  │
│  │   └─────────────────────────────────────────────────────────────┘  │  │
│  │                                                                     │  │
│  │   ┌─────────────────────────────────────────────────────────────┐  │  │
│  │   │ Conteneur Boutons (horizontal, mt-8, space-x-4)             │  │  │
│  │   │                                                              │  │  │
│  │   │  ┌──────────┐ ┌──────────┐ ┌───────────┐ ┌──────────────┐  │  │  │
│  │   │  │  Play    │ │Mark as   │ │Open In    │ │Open In       │  │  │  │
│  │   │  │  [▶]     │ │Watched   │ │TMDB       │ │Jellyfin      │  │  │  │
│  │   │  │          │ │[✓]       │ │[↗]        │ │[↗]           │  │  │  │
│  │   │  └──────────┘ └──────────┘ └───────────┘ └──────────────┘  │  │  │
│  │   │   (primary)    (primary)    (web only)     (web only)      │  │  │
│  │   └─────────────────────────────────────────────────────────────┘  │  │
│  └────────────────────────────────────────────────────────────────────┘  │
└──────────────────────────────────────────────────────────────────────────┘
```

## 2. Composants utilisés

### Composants principaux

1. **DetachedPage** (`src/lib/components/DetachedPage/DetachedPage.svelte`)
   - Wrapper de page modale en plein écran
   - Gère la navigation (retour, focus trap)
   - Fournit: `handleGoBack`, `registrar`, `hasFocus`
   - Z-index: 20, position fixée, fond secondary-900

2. **Container** (`src/lib/components/Container.svelte`)
   - Système de navigation au clavier (d-pad)
   - Gère le focus et la sélection
   - Direction: vertical ou horizontal
   - Props: `focusOnMount`, `direction`, `class`

3. **TitleProperties** (`src/lib/pages/TitlePages/HeroTitleInfo.svelte`)
   - Affiche le titre, propriétés et synopsis
   - Props:
     - `title`: Nom de l'épisode
     - `properties`: Tableau d'objets `{label, href?, icon?}`
     - `overview`: Description de l'épisode
   - Styles responsifs (4xl-6xl selon longueur)

4. **Button** (`src/lib/components/Button.svelte`)
   - Boutons interactifs avec icônes
   - Types: `primary`, `secondary`, `primary-dark`
   - Props spéciales:
     - `action`: Fonction principale
     - `secondaryAction`: Action secondaire (menu avec `DotsVertical`)
     - `disabled`: État désactivé
     - `confirmDanger`: Confirmation requise
   - Slots: `icon`, `icon-after`, `icon-absolute`

### Icônes (radix-icons-svelte)

- **Play**: Bouton de lecture
- **Check**: Marquage vu/non vu
- **ExternalLink**: Liens externes
- **DotFilled**: Séparateurs dans TitleProperties

## 3. Layout et dimensions

### Structure globale

```css
/* DetachedPage */
position: fixed
inset: 0 (top, right, bottom, left: 0)
z-index: 20
overflow-y: auto

/* Container principal */
height: 100vh (h-screen)
margin-x: 8rem (mx-32 = 128px)
padding-y: 4rem (py-16 = 64px)
display: flex
flex-direction: column
justify-content: flex-end
```

### Arrière-plan

```css
/* Image de fond */
position: absolute
inset-x: 0
height: 100vh
z-index: -10
background-size: cover
background-position: center

/* Gradient overlay */
height: 100vh
background: linear-gradient(to bottom, transparent, secondary-900)

/* Fond de remplissage */
flex: 1
background-color: secondary-500
```

### Zones de contenu

#### Numéro Saison/Épisode
```css
margin-top: 0.5rem (mt-2)
color: zinc-200
font-size: 1.125rem (text-lg)
font-weight: medium
letter-spacing: wider
```

#### TitleProperties

**Titre**:
```css
/* Titre court (< 15 caractères) */
font-size: 2.25rem - 3.75rem (text-4xl to text-6xl) /* responsive */

/* Titre long (>= 15 caractères) */
font-size: 1.875rem - 3rem (text-3xl to text-5xl) /* responsive */

color: stone-200
font-weight: medium
letter-spacing: wider
margin-top: 0.25rem
```

**Propriétés**:
```css
display: flex
align-items: center
gap: 0.25rem
text-transform: uppercase
color: zinc-300
font-size: 1.125rem (text-lg)
font-weight: semibold
letter-spacing: wider
margin-top: 0.5rem
```

**Synopsis**:
```css
max-width: 56rem (max-w-4xl = 896px)
margin-top: 1rem
opacity: 0.75
line-clamp: 3 (limité à 3 lignes)
```

#### Conteneur de boutons
```css
display: flex
flex-direction: row (horizontal)
margin-top: 2rem (mt-8)
gap: 1rem (space-x-4)
```

#### Boutons individuels
```css
height: 3rem (h-12 = 48px)
padding-x: 1.5rem (px-6 = 24px)
border-radius: 0.75rem (rounded-xl)
background: secondary-800 (type primary)
font-weight: medium
letter-spacing: wide
```

### Espacement et marges

```
Écran complet
├── Marges horizontales: 128px (mx-32)
├── Padding vertical: 64px (py-16)
├── Contenu aligné en bas (justify-end)
└── Espacements internes:
    ├── Titre saison/épisode → TitleProperties: 8px (mt-2)
    ├── Titre → Propriétés: 8px (mt-2)
    ├── Propriétés → Synopsis: 16px (mt-4)
    └── Synopsis → Boutons: 32px (mt-8)
```

## 4. Navigation des épisodes (Précédent/Suivant)

### Système actuel

La page EpisodePage **ne possède pas** de navigation intégrée précédent/suivant. La navigation est gérée par:

1. **Navigation externe**: Via le routeur et les paramètres d'URL
   ```typescript
   export let id: string;      // TMDB ID de la série
   export let season: string;  // Numéro de saison
   export let episode: string; // Numéro d'épisode
   ```

2. **Retour arrière uniquement**:
   - Bouton "retour" fourni par `DetachedPage`
   - Handler: `handleGoBack()` → `history.back()`
   - Navigation clavier: focus sur l'élément supérieur + retour arrière

### Implémentation suggérée pour navigation Prev/Next

Pour ajouter une navigation d'épisode, il faudrait:

```typescript
// Stores nécessaires
const { promise: seriesData } = tmdbSeriesDataStore.subscribe(Number(id));

// Calculer épisode précédent/suivant
let prevEpisode: { season: number; episode: number } | null = null;
let nextEpisode: { season: number; episode: number } | null = null;

$seriesData.then((series) => {
  const currentSeason = series.seasons.find(s => s.season_number === Number(season));
  const currentEpisodeNum = Number(episode);

  // Logique pour calculer prev/next
  if (currentEpisodeNum > 1) {
    prevEpisode = { season: Number(season), episode: currentEpisodeNum - 1 };
  } else {
    // Vérifier saison précédente
  }

  if (currentEpisodeNum < currentSeason.episode_count) {
    nextEpisode = { season: Number(season), episode: currentEpisodeNum + 1 };
  } else {
    // Vérifier saison suivante
  }
});
```

**Ajout UI**:
```svelte
<!-- Boutons de navigation en bas du container -->
<Container direction="horizontal" class="flex mt-4 space-x-4">
  {#if prevEpisode}
    <Button action={() => navigate(`/series/${id}/season/${prevEpisode.season}/episode/${prevEpisode.episode}`)}>
      ← Épisode précédent
    </Button>
  {/if}

  {#if nextEpisode}
    <Button action={() => navigate(`/series/${id}/season/${nextEpisode.season}/episode/${nextEpisode.episode}`)}>
      Épisode suivant →
    </Button>
  {/if}
</Container>
```

## 5. Stores et gestion de données

### Stores utilisés

1. **tmdbEpisodeDataStore** (`$lib/stores/data.store`)
   ```typescript
   const { promise: tmdbEpisode, unsubscribe: unsubscribeTmdbEpisode } =
     tmdbEpisodeDataStore.subscribe(Number(id), Number(season), Number(episode));
   ```
   - Récupère les données TMDB de l'épisode
   - Retourne: `name`, `overview`, `still_path`, `vote_average`, `vote_count`, `runtime`, `air_date`, `season_number`, `episode_number`

2. **useEpisodeUserData** (`$lib/stores/media-user-data.store`)
   ```typescript
   const {
     progress,              // Progression de visionnage
     handleAutoplay,        // Lecture automatique
     handleOpenStreamSelector, // Sélecteur de flux
     canStream,             // Capacité de lecture
     isWatched,             // État vu/non vu
     toggleIsWatched,       // Basculer état vu
     unsubscribe
   } = useEpisodeUserData(id, Number(season), Number(episode));
   ```

### Cycle de vie

```typescript
// Montage: Souscriptions automatiques aux stores
onDestroy(() => {
  unsubscribe();                // Nettoyage user data
  unsubscribeTmdbEpisode();     // Nettoyage TMDB data
});
```

### Propriétés calculées

```typescript
let titleProperties: { href?: string; label: string }[] = [];

$tmdbEpisode.then((episode) => {
  // Note TMDB avec lien externe
  if (episode?.vote_average) {
    titleProperties.push({
      label: `${episode.vote_average.toFixed(1)} TMDB (${formatThousands(episode.vote_count)})`,
      href: `https://www.themoviedb.org/tv/${id}/season/${season}/episode/${episode}`
    });
  }

  // Durée
  if (episode?.runtime) {
    titleProperties.push({ label: `${episode.runtime} Minutes` });
  }

  // Date de diffusion
  if (episode?.air_date) {
    titleProperties.push({
      label: `Aired on ${new Date(episode.air_date).toLocaleDateString('en-US', {...})}`
    });
  }
});
```

## 6. Actions utilisateur

### Bouton Play
- **Action primaire**: `handleAutoplay()` - Lecture automatique avec flux par défaut
- **Action secondaire**: `handleOpenStreamSelector()` - Ouvre le sélecteur de flux (icône `DotsVertical`)
- **État**: Désactivé si `!$canStream`
- **Icône**: Play (▶)

### Bouton Mark as Watched/Unwatched
- **Action**: `toggleIsWatched()` - Bascule l'état vu/non vu
- **Label dynamique**: Selon `$isWatched`
- **Icône**: Check (✓)

### Boutons externes (PLATFORM_WEB uniquement)
1. **Open In TMDB**: Lien vers la page TMDB
2. **Open In Jellyfin**: Lien vers Jellyfin

## 7. Équivalent Kotlin/Compose

```kotlin
// File: EpisodePage.kt
package com.reiverr.ui.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.reiverr.api.tmdb.TmdbApi
import com.reiverr.data.Episode
import com.reiverr.data.UserMediaData
import com.reiverr.ui.components.DetachedPage
import com.reiverr.ui.components.TitleProperties
import com.reiverr.ui.components.AppButton
import com.reiverr.viewmodels.EpisodeViewModel
import kotlinx.coroutines.launch

/**
 * Page détaillée d'un épisode de série TV
 *
 * @param seriesId ID TMDB de la série
 * @param seasonNumber Numéro de la saison
 * @param episodeNumber Numéro de l'épisode
 * @param onNavigateBack Callback de retour arrière
 * @param viewModel ViewModel pour la gestion des données et actions
 */
@Composable
fun EpisodePage(
    seriesId: String,
    seasonNumber: Int,
    episodeNumber: Int,
    onNavigateBack: () -> Unit,
    viewModel: EpisodeViewModel = viewModel()
) {
    // États et données
    val episode by viewModel.episodeData.collectAsState()
    val userData by viewModel.userDataState.collectAsState()
    val canStream by viewModel.canStream.collectAsState()
    val isWatched by viewModel.isWatched.collectAsState()

    val coroutineScope = rememberCoroutineScope()

    // Chargement des données au montage
    LaunchedEffect(seriesId, seasonNumber, episodeNumber) {
        viewModel.loadEpisode(seriesId, seasonNumber, episodeNumber)
    }

    // Calcul des propriétés
    val titleProperties = remember(episode) {
        buildList {
            episode?.let { ep ->
                // Note TMDB
                if (ep.voteAverage > 0) {
                    add(
                        TitleProperty(
                            label = "${ep.voteAverage} TMDB (${ep.voteCount.formatThousands()})",
                            href = "https://www.themoviedb.org/tv/$seriesId/season/$seasonNumber/episode/$episodeNumber"
                        )
                    )
                }

                // Durée
                if (ep.runtime > 0) {
                    add(TitleProperty(label = "${ep.runtime} Minutes"))
                }

                // Date de diffusion
                ep.airDate?.let { date ->
                    add(
                        TitleProperty(
                            label = "Aired on ${date.formatLocalizedDate()}"
                        )
                    )
                }
            }
        }
    }

    DetachedPage(
        onNavigateBack = onNavigateBack,
        topmost = true,
        showSidebar = true
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Arrière-plan avec image et gradient
            EpisodeBackground(
                stillPath = episode?.stillPath,
                modifier = Modifier.fillMaxSize()
            )

            // Contenu principal
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 128.dp, vertical = 64.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Bottom
            ) {
                // Numéro saison/épisode
                Text(
                    text = "Season ${episode?.seasonNumber} Episode ${episode?.episodeNumber}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFFE4E4E7), // zinc-200
                    letterSpacing = 0.1.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // Titre, propriétés et synopsis
                TitleProperties(
                    title = episode?.name ?: "",
                    properties = titleProperties,
                    overview = episode?.overview ?: "",
                    modifier = Modifier.padding(bottom = 32.dp)
                )

                // Boutons d'action
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Bouton Play
                    AppButton(
                        text = "Play",
                        icon = Icons.Default.PlayArrow,
                        onClick = {
                            coroutineScope.launch {
                                viewModel.handleAutoplay()
                            }
                        },
                        onSecondaryClick = {
                            viewModel.handleOpenStreamSelector()
                        },
                        enabled = canStream,
                        modifier = Modifier.height(48.dp)
                    )

                    // Bouton Mark as Watched/Unwatched
                    AppButton(
                        text = if (isWatched) "Mark as Unwatched" else "Mark as Watched",
                        icon = Icons.Default.Check,
                        onClick = {
                            coroutineScope.launch {
                                viewModel.toggleIsWatched()
                            }
                        },
                        modifier = Modifier.height(48.dp)
                    )

                    // Boutons web uniquement
                    if (BuildConfig.PLATFORM_WEB) {
                        AppButton(
                            text = "Open In TMDB",
                            iconAfter = Icons.Default.OpenInNew,
                            onClick = {
                                viewModel.openInTmdb(seriesId, seasonNumber, episodeNumber)
                            },
                            modifier = Modifier.height(48.dp)
                        )

                        AppButton(
                            text = "Open In Jellyfin",
                            iconAfter = Icons.Default.OpenInNew,
                            onClick = {
                                viewModel.openInJellyfin(seriesId, seasonNumber, episodeNumber)
                            },
                            modifier = Modifier.height(48.dp)
                        )
                    }
                }
            }
        }
    }
}

/**
 * Composable pour l'arrière-plan de la page épisode
 * Affiche l'image still de l'épisode avec un gradient overlay
 */
@Composable
private fun EpisodeBackground(
    stillPath: String?,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        // Image de fond
        AsyncImage(
            model = "${TmdbApi.TMDB_IMAGES_ORIGINAL}$stillPath",
            contentDescription = "Episode background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Gradient overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color(0xFF1E293B) // secondary-900
                        ),
                        startY = 0f,
                        endY = 1000f
                    )
                )
        )

        // Fond de remplissage en bas
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .align(Alignment.BottomCenter)
                .background(Color(0xFF64748B)) // secondary-500
        )
    }
}

/**
 * Data class pour les propriétés du titre
 */
data class TitleProperty(
    val label: String,
    val href: String? = null,
    val icon: Any? = null
)

/**
 * Extension pour formater les milliers
 */
fun Int.formatThousands(): String {
    return when {
        this >= 1_000_000 -> "${this / 1_000_000}M"
        this >= 1_000 -> "${this / 1_000}K"
        else -> this.toString()
    }
}

/**
 * Extension pour formater les dates localisées
 */
fun String.formatLocalizedDate(): String {
    // Implémentation de formatage de date
    return this // Placeholder
}
```

### ViewModel associé

```kotlin
// File: EpisodeViewModel.kt
package com.reiverr.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reiverr.api.tmdb.TmdbApi
import com.reiverr.data.Episode
import com.reiverr.data.UserMediaData
import com.reiverr.repositories.EpisodeRepository
import com.reiverr.repositories.UserMediaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EpisodeViewModel @Inject constructor(
    private val episodeRepository: EpisodeRepository,
    private val userMediaRepository: UserMediaRepository,
    private val tmdbApi: TmdbApi
) : ViewModel() {

    // États observables
    private val _episodeData = MutableStateFlow<Episode?>(null)
    val episodeData: StateFlow<Episode?> = _episodeData.asStateFlow()

    private val _userDataState = MutableStateFlow<UserMediaData?>(null)
    val userDataState: StateFlow<UserMediaData?> = _userDataState.asStateFlow()

    private val _canStream = MutableStateFlow(false)
    val canStream: StateFlow<Boolean> = _canStream.asStateFlow()

    private val _isWatched = MutableStateFlow(false)
    val isWatched: StateFlow<Boolean> = _isWatched.asStateFlow()

    private val _progress = MutableStateFlow(0f)
    val progress: StateFlow<Float> = _progress.asStateFlow()

    /**
     * Charge les données de l'épisode depuis TMDB et les données utilisateur
     */
    fun loadEpisode(seriesId: String, seasonNumber: Int, episodeNumber: Int) {
        viewModelScope.launch {
            // Charger données TMDB
            try {
                val episode = tmdbApi.getEpisode(seriesId.toInt(), seasonNumber, episodeNumber)
                _episodeData.value = episode
            } catch (e: Exception) {
                // Gestion d'erreur
            }

            // Charger données utilisateur
            userMediaRepository.getEpisodeUserData(seriesId, seasonNumber, episodeNumber)
                .collect { userData ->
                    _userDataState.value = userData
                    _isWatched.value = userData?.isWatched ?: false
                    _progress.value = userData?.progress ?: 0f
                    _canStream.value = userData?.canStream ?: false
                }
        }
    }

    /**
     * Lance la lecture automatique avec le flux par défaut
     */
    suspend fun handleAutoplay() {
        userMediaRepository.autoplayEpisode(
            seriesId = _episodeData.value?.seriesId ?: return,
            seasonNumber = _episodeData.value?.seasonNumber ?: return,
            episodeNumber = _episodeData.value?.episodeNumber ?: return
        )
    }

    /**
     * Ouvre le sélecteur de flux
     */
    fun handleOpenStreamSelector() {
        // Navigation vers StreamSelector
    }

    /**
     * Bascule l'état vu/non vu
     */
    suspend fun toggleIsWatched() {
        val episode = _episodeData.value ?: return
        val currentState = _isWatched.value

        userMediaRepository.setEpisodeWatched(
            seriesId = episode.seriesId,
            seasonNumber = episode.seasonNumber,
            episodeNumber = episode.episodeNumber,
            watched = !currentState
        )

        _isWatched.value = !currentState
    }

    /**
     * Ouvre la page TMDB dans le navigateur
     */
    fun openInTmdb(seriesId: String, seasonNumber: Int, episodeNumber: Int) {
        // Ouvrir URL externe
    }

    /**
     * Ouvre la page Jellyfin dans le navigateur
     */
    fun openInJellyfin(seriesId: String, seasonNumber: Int, episodeNumber: Int) {
        // Ouvrir URL externe
    }
}
```

### Composants UI réutilisables

```kotlin
// File: TitleProperties.kt
package com.reiverr.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FiberManualRecord
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TitleProperties(
    title: String,
    properties: List<TitleProperty>,
    overview: String,
    modifier: Modifier = Modifier,
    onClickTitle: (() -> Unit)? = null
) {
    Column(modifier = modifier) {
        // Titre
        Text(
            text = title,
            fontSize = if (title.length < 15) 48.sp else 36.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFFE7E5E4), // stone-200
            letterSpacing = 0.1.sp,
            modifier = Modifier
                .padding(bottom = 8.dp)
                .then(
                    if (onClickTitle != null) {
                        Modifier.clickable { onClickTitle() }
                    } else Modifier
                )
        )

        // Propriétés (séparées par des points)
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            properties.forEachIndexed { index, property ->
                if (index > 0) {
                    Icon(
                        imageVector = Icons.Default.FiberManualRecord,
                        contentDescription = null,
                        tint = Color(0xFFD4D4D8), // zinc-300
                        modifier = Modifier.size(8.dp)
                    )
                }

                if (property.href != null) {
                    Text(
                        text = property.label.uppercase(),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFFD4D4D8), // zinc-300
                        letterSpacing = 0.1.sp,
                        modifier = Modifier.clickable {
                            // Ouvrir lien externe
                        }
                    )
                } else {
                    Text(
                        text = property.label.uppercase(),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFFD4D4D8), // zinc-300
                        letterSpacing = 0.1.sp
                    )
                }
            }
        }

        // Synopsis (limité à 3 lignes)
        Text(
            text = overview,
            fontSize = 16.sp,
            color = Color.White.copy(alpha = 0.75f),
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.widthIn(max = 896.dp)
        )
    }
}
```

```kotlin
// File: AppButton.kt
package com.reiverr.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AppButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    iconAfter: ImageVector? = null,
    onSecondaryClick: (() -> Unit)? = null,
    enabled: Boolean = true,
    type: ButtonType = ButtonType.Primary
) {
    Row(
        modifier = modifier
            .height(48.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(
                if (enabled) {
                    when (type) {
                        ButtonType.Primary -> Color(0xFF1E293B) // secondary-800
                        ButtonType.PrimaryDark -> Color(0xFF1E293B) // primary-900
                        ButtonType.Secondary -> Color.Transparent
                    }
                } else {
                    Color(0xFF1E293B).copy(alpha = 0.4f)
                }
            )
            .then(
                if (enabled) {
                    Modifier.clickable { onClick() }
                } else {
                    Modifier
                }
            )
    ) {
        // Contenu principal du bouton
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .weight(1f)
        ) {
            // Icône avant
            icon?.let {
                Icon(
                    imageVector = it,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier
                        .size(19.dp)
                        .padding(end = 8.dp)
                )
            }

            // Texte
            Text(
                text = text,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White,
                letterSpacing = 0.05.sp
            )

            // Icône après
            iconAfter?.let {
                Icon(
                    imageVector = it,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier
                        .size(19.dp)
                        .padding(start = 8.dp)
                )
            }
        }

        // Action secondaire (menu)
        if (onSecondaryClick != null) {
            Box(
                modifier = Modifier
                    .width(1.dp)
                    .fillMaxHeight()
                    .background(Color(0xFF334155)) // secondary-700
            )

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .width(48.dp)
                    .fillMaxHeight()
                    .clickable(enabled = enabled) { onSecondaryClick() }
            ) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "More options",
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

enum class ButtonType {
    Primary,
    PrimaryDark,
    Secondary
}
```

## 8. Palette de couleurs

```kotlin
object EpisodePageColors {
    val Background = Color(0xFF1E293B)      // secondary-900
    val BackgroundLight = Color(0xFF64748B) // secondary-500
    val BackgroundDark = Color(0xFF0F172A)  // secondary-950

    val TextPrimary = Color(0xFFE7E5E4)     // stone-200
    val TextSecondary = Color(0xFFE4E4E7)   // zinc-200
    val TextTertiary = Color(0xFFD4D4D8)    // zinc-300

    val ButtonPrimary = Color(0xFF1E293B)   // secondary-800
    val ButtonDivider = Color(0xFF334155)   // secondary-700
    val ButtonHover = Color(0xFFF59E0B)     // amber-500

    val Overlay = Color.Black.copy(alpha = 0.6f)
}
```

## Notes techniques

### Gestion du focus et navigation
- Le système utilise un **Container** Svelte personnalisé pour la navigation au clavier (d-pad)
- Équivalent Compose: Utiliser `Modifier.focusable()` et `FocusRequester`
- La navigation retour est gérée via `history.back()` (web) ou `NavController.popBackStack()` (Android)

### Chargement asynchrone
- Svelte: Utilisation de `{#await promise}`
- Compose: Utilisation de `LaunchedEffect` et `StateFlow`

### Responsive design
- Svelte: Classes Tailwind responsive (`text-4xl sm:text-5xl 2xl:text-6xl`)
- Compose: Utiliser `LocalConfiguration` et conditions sur `screenWidthDp`

### Images
- Svelte: Balise `<div>` avec `background-image` CSS
- Compose: `AsyncImage` de Coil avec `ContentScale.Crop`
