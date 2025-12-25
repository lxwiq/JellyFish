# PersonPage - Page de Profil d'une Personne

## Vue d'ensemble

La page `PersonPage.svelte` affiche le profil détaillé d'une personne (acteur, réalisateur, etc.) avec sa photo, sa biographie et sa filmographie complète.

**Fichier**: `/Users/lolo/Dev/reiverr/src/lib/pages/PersonPage.svelte`

## Structure de la Page

```
┌─────────────────────────────────────────────────────────────────────┐
│ DetachedPage (fullscreen overlay avec sidebar)                     │
│ ┌─────────────────────────────────────────────────────────────────┐ │
│ │ Container (padding: 32px horizontal, 16px vertical)             │ │
│ │                                                                   │ │
│ │ ┌───────────────────────────────────────────────────────────┐   │ │
│ │ │ Section En-tête (flex horizontal, gap: 8px)              │   │ │
│ │ │ ┌──────────────┐  ┌───────────────────────────────────┐  │   │ │
│ │ │ │   Photo      │  │  HeroTitleInfo                    │  │   │ │
│ │ │ │   Profil     │  │  ┌─────────────────────────────┐  │  │   │ │
│ │ │ │              │  │  │ Nom (titre 4xl-6xl)         │  │  │   │ │
│ │ │ │  176x256px   │  │  └─────────────────────────────┘  │  │   │ │
│ │ │ │  (w-44 h-64) │  │  ┌─────────────────────────────┐  │  │   │ │
│ │ │ │  rounded-xl  │  │  │ Date naissance • Crédits    │  │  │   │ │
│ │ │ │              │  │  └─────────────────────────────┘  │  │   │ │
│ │ │ │              │  │  ┌─────────────────────────────┐  │  │   │ │
│ │ │ │              │  │  │ Biographie (3 lignes max)   │  │  │   │ │
│ │ │ └──────────────┘  │  └─────────────────────────────┘  │  │   │ │
│ │ │                   └───────────────────────────────────┘  │   │ │
│ │ └───────────────────────────────────────────────────────────┘   │ │
│ │                                                                   │ │
│ │ ┌───────────────────────────────────────────────────────────┐   │ │
│ │ │ CardGrid - Filmographie (grille responsive)              │   │ │
│ │ │ ┌────────┐ ┌────────┐ ┌────────┐ ┌────────┐ ┌────────┐  │   │ │
│ │ │ │ Titre  │ │ Titre  │ │ Titre  │ │ Titre  │ │ Titre  │  │   │ │
│ │ │ │  Card  │ │  Card  │ │  Card  │ │  Card  │ │  Card  │  │   │ │
│ │ │ └────────┘ └────────┘ └────────┘ └────────┘ └────────┘  │   │ │
│ │ │ ┌────────┐ ┌────────┐ ┌────────┐ ┌────────┐ ┌────────┐  │   │ │
│ │ │ │ Titre  │ │ Titre  │ │ Titre  │ │ Titre  │ │ Titre  │  │   │ │
│ │ │ └────────┘ └────────┘ └────────┘ └────────┘ └────────┘  │   │ │
│ │ └───────────────────────────────────────────────────────────┘   │ │
│ └─────────────────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────────────┘
```

## Composants Utilisés

### 1. **DetachedPage**
- **Fichier**: `/Users/lolo/Dev/reiverr/src/lib/components/DetachedPage/DetachedPage.svelte`
- **Rôle**: Page overlay plein écran avec navigation au clavier
- **Caractéristiques**:
  - Position fixée en plein écran (`fixed inset-0`)
  - Z-index élevé (`z-20`)
  - Fond sombre (`bg-secondary-900`)
  - Gestion du focus et navigation au clavier
  - Possibilité d'afficher une sidebar
  - Fonction `handleGoBack()` pour retourner en arrière

### 2. **Container**
- **Fichier**: `/Users/lolo/Dev/reiverr/src/lib/components/Container.svelte`
- **Usage**: Conteneur principal avec espacement
- **Classes CSS**: `px-32 py-16 space-y-16`
- **Padding**:
  - Horizontal: 128px (8rem)
  - Vertical: 64px (4rem)
- **Espacement vertical entre enfants**: 64px (4rem)

### 3. **Photo de Profil** (Container avec background-image)
- **Type**: Container stylisé comme image de fond
- **Dimensions**:
  - Largeur: 176px (`w-44`)
  - Hauteur: 256px (`h-64`)
- **Style**:
  - Coins arrondis: `rounded-xl` (0.75rem)
  - Background: `bg-center bg-cover`
  - Curseur: `cursor-pointer`
- **Source image**: `TMDB_POSTER_SMALL + person.profile_path`
- **Interaction**: Scroll au clic avec offset vertical de 128px

### 4. **HeroTitleInfo**
- **Fichier**: `/Users/lolo/Dev/reiverr/src/lib/pages/TitlePages/HeroTitleInfo.svelte`
- **Props**:
  - `title`: Nom de la personne
  - `overview`: Biographie
  - `properties`: Tableau de propriétés (date de naissance, nombre de crédits)
- **Affichage**:
  - **Titre**: Taille responsive (4xl à 6xl selon la longueur)
  - **Propriétés**: En ligne avec séparateurs (DotFilled icon)
  - **Biographie**: Limitée à 3 lignes (`line-clamp-3`), max-width 4xl (896px), opacité 75%

### 5. **CardGrid**
- **Fichier**: `/Users/lolo/Dev/reiverr/src/lib/components/CardGrid.svelte`
- **Type**: Grille responsive CSS Grid
- **Props**: `type="portrait"` (par défaut)
- **Configuration**:
  - Espacement: `gap-x-8 gap-y-8` (32px)
  - Colonnes adaptatives selon la largeur d'écran
  - Calcul dynamique via `getCardDimensions()`

### 6. **TmdbCard**
- **Fichier**: `/Users/lolo/Dev/reiverr/src/lib/components/Card/TmdbCard.svelte`
- **Usage**: Carte pour chaque titre de la filmographie
- **Props**: `item` (objet titre avec poster, titre, date)
- **Interaction**: Scroll au focus avec offset vertical de 128px

## Layout et Dimensions

### Hiérarchie des espacements
```
DetachedPage (full screen)
└─ Container principale
   ├─ Padding: px-32 py-16 (128px H, 64px V)
   ├─ Espacement vertical: space-y-16 (64px)
   │
   ├─ Section En-tête (flex space-x-8)
   │  ├─ Gap horizontal: 32px
   │  ├─ Photo: 176x256px
   │  └─ Info: flexible width
   │
   └─ CardGrid
      └─ Gap: 32px (horizontal et vertical)
```

### Dimensions clés
- **Photo de profil**: 176px × 256px (ratio ~2:3)
- **Biographie max-width**: 896px (4xl)
- **Padding conteneur principal**: 128px (horizontal), 64px (vertical)
- **Espacement entre sections**: 64px
- **Gap grille filmographie**: 32px

### Points de rupture responsive (CardGrid)
Le nombre de colonnes s'adapte automatiquement à la largeur de l'écran via la fonction `getCardDimensions()`.

## Données Affichées

### 1. **Informations de la Personne** (via API TMDB)

```typescript
// Récupération des données
person = tmdbApi.getPerson(Number(id))
```

**Champs affichés**:
- `name`: Nom complet
- `profile_path`: Photo de profil
- `biography`: Biographie complète
- `birthday`: Date de naissance
- `known_for_department`: Département connu (Acting, Directing, etc.)

### 2. **Propriétés Calculées**

```typescript
infoProperties: { href?: string; label: string }[]
```

#### Date de naissance et âge
```typescript
label: `Born ${date} (${age} years old)`
```
- Format: "Born January 1, 1980 (45 years old)"
- Calcul automatique de l'âge en années

#### Nombre de crédits
```typescript
label: `${totalCredits} Credits`
href: `https://www.themoviedb.org/person/${id}`
```
- Somme des rôles cast dans movies et TV
- Lien vers la page TMDB

### 3. **Filmographie**

#### Logique de tri et filtrage

**Pour les acteurs** (`known_for_department === 'Acting'`):
```typescript
[...movie_credits.cast, ...tv_credits.cast]
  .sort((a, b) => date_desc)
```

**Pour les autres rôles** (réalisateur, producteur, etc.):
```typescript
[...movie_credits.crew, ...tv_credits.crew]
  .sort((a, b) => date_desc)
```

#### Tri par date
Les titres sont triés par date de sortie décroissante:
- Films: `release_date`
- Séries TV: `first_air_date`
- Les plus récents en premier

### 4. **Structure des données**

```typescript
Person {
  id: number
  name: string
  profile_path: string
  biography: string
  birthday: string
  known_for_department: string
  movie_credits: {
    cast: Array<MovieCast>
    crew: Array<MovieCrew>
  }
  tv_credits: {
    cast: Array<TvCast>
    crew: Array<TvCrew>
  }
}
```

## Interactions et Navigation

### Navigation au Clavier
1. **Focus initial**: Sur le conteneur principal (`focusOnMount`)
2. **Retour arrière**: Gestion via `handleGoBack()`
3. **Navigation dans la grille**: Focus automatique sur les cartes
4. **Scroll automatique**: Offset vertical de 128px au focus

### Actions
- **Clic sur photo**: Scroll vers la photo
- **Clic sur nombre de crédits**: Ouvre la page TMDB dans un nouvel onglet
- **Clic sur carte de titre**: Navigation vers la page du titre

## Équivalent Kotlin/Compose

Voici comment cette page pourrait être implémentée en Jetpack Compose:

```kotlin
@Composable
fun PersonPage(
    personId: Int,
    onNavigateBack: () -> Unit,
    viewModel: PersonViewModel = viewModel()
) {
    val person by viewModel.getPerson(personId).collectAsState(initial = null)
    val titles by viewModel.getTitles(personId).collectAsState(initial = emptyList())

    // Page détachée plein écran
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        person?.let { personData ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 128.dp, vertical = 64.dp)
            ) {
                // Section En-tête
                PersonHeader(
                    person = personData,
                    modifier = Modifier.padding(bottom = 64.dp)
                )

                // Grille de filmographie
                PersonFilmography(
                    titles = titles,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun PersonHeader(
    person: Person,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        // Photo de profil
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data("${TMDB_POSTER_SMALL}${person.profilePath}")
                .crossfade(true)
                .build(),
            contentDescription = person.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .width(176.dp)
                .height(256.dp)
                .clip(RoundedCornerShape(12.dp))
                .clickable { /* Scroll vers la photo */ }
        )

        // Informations
        Column(
            modifier = Modifier
                .weight(1f)
                .align(Alignment.Bottom)
        ) {
            PersonInfo(person = person)
        }
    }
}

@Composable
private fun PersonInfo(person: Person) {
    // Nom
    Text(
        text = person.name,
        style = MaterialTheme.typography.displayLarge.copy(
            fontSize = if (person.name.length < 15) 60.sp else 48.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = 0.05.em
        ),
        color = Color(0xFFE7E5E4), // stone-200
        modifier = Modifier.padding(bottom = 8.dp)
    )

    // Propriétés (date naissance, crédits)
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(bottom = 16.dp)
    ) {
        person.birthday?.let { birthday ->
            val age = calculateAge(birthday)
            Text(
                text = "Born ${formatDate(birthday)} ($age years old)",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 0.05.em
                ),
                color = Color(0xFFD4D4D8) // zinc-300
            )
        }

        if (person.totalCredits > 0) {
            Icon(
                imageVector = Icons.Filled.Circle,
                contentDescription = null,
                modifier = Modifier.size(6.dp),
                tint = Color(0xFFD4D4D8)
            )

            ClickableText(
                text = AnnotatedString("${person.totalCredits} Credits"),
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 0.05.em,
                    color = Color(0xFFD4D4D8)
                ),
                onClick = {
                    // Ouvrir lien TMDB
                    openUrl("https://www.themoviedb.org/person/${person.id}")
                }
            )
        }
    }

    // Biographie
    Text(
        text = person.biography,
        style = MaterialTheme.typography.bodyLarge,
        color = Color(0xFFE7E5E4).copy(alpha = 0.75f),
        maxLines = 3,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier.widthIn(max = 896.dp)
    )
}

@Composable
private fun PersonFilmography(
    titles: List<Title>,
    modifier: Modifier = Modifier
) {
    val configuration = LocalConfiguration.current
    val columns = calculateColumns(configuration.screenWidthDp)

    LazyVerticalGrid(
        columns = GridCells.Fixed(columns),
        horizontalArrangement = Arrangement.spacedBy(32.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp),
        modifier = modifier
    ) {
        items(titles) { title ->
            TmdbCard(
                item = title,
                onClick = { /* Navigation vers page titre */ }
            )
        }
    }
}

// Fonctions utilitaires
private fun calculateColumns(screenWidthDp: Int): Int {
    return when {
        screenWidthDp >= 1536 -> 6
        screenWidthDp >= 1280 -> 5
        screenWidthDp >= 768 -> 4
        else -> 3
    }
}

private fun calculateAge(birthday: String): Int {
    val birthDate = LocalDate.parse(birthday)
    val today = LocalDate.now()
    return Period.between(birthDate, today).years
}

private fun formatDate(dateString: String): String {
    val date = LocalDate.parse(dateString)
    return date.format(DateTimeFormatter.ofPattern("MMMM d, yyyy"))
}

// ViewModel
class PersonViewModel : ViewModel() {
    private val tmdbApi = TmdbApi()

    fun getPerson(id: Int): Flow<Person> = flow {
        emit(tmdbApi.getPerson(id))
    }

    fun getTitles(personId: Int): Flow<List<Title>> = flow {
        val person = tmdbApi.getPerson(personId)
        val titles = if (person.knownForDepartment == "Acting") {
            (person.movieCredits.cast + person.tvCredits.cast)
        } else {
            (person.movieCredits.crew + person.tvCredits.crew)
        }
        emit(titles.sortedByDescending { it.releaseDate ?: it.firstAirDate })
    }
}

// Modèles de données
data class Person(
    val id: Int,
    val name: String,
    val profilePath: String?,
    val biography: String,
    val birthday: String?,
    val knownForDepartment: String,
    val movieCredits: Credits,
    val tvCredits: Credits
) {
    val totalCredits: Int
        get() = (movieCredits.cast.size + tvCredits.cast.size)
}

data class Credits(
    val cast: List<CastMember>,
    val crew: List<CrewMember>
)

data class Title(
    val id: Int,
    val title: String?,
    val name: String?,
    val posterPath: String?,
    val releaseDate: String?,
    val firstAirDate: String?
)
```

### Dépendances Compose requises

```kotlin
// build.gradle.kts
dependencies {
    // Jetpack Compose
    implementation("androidx.compose.ui:ui:1.5.4")
    implementation("androidx.compose.material3:material3:1.1.2")
    implementation("androidx.compose.ui:ui-tooling-preview:1.5.4")

    // Images asynchrones
    implementation("io.coil-kt:coil-compose:2.5.0")

    // Navigation
    implementation("androidx.navigation:navigation-compose:2.7.5")

    // ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")

    // Grilles
    implementation("androidx.compose.foundation:foundation:1.5.4")
}
```

## Notes Techniques

### Chargement Asynchrone
- Utilisation de promesses (`{#await person}`)
- Double await pour les données imbriquées (person puis titles)
- Pas de gestion d'erreur visible dans le template

### Performance
- Tri des titres fait côté client
- Calcul de l'âge fait côté client
- Images chargées en lazy (via background-image)

### Accessibilité
- Navigation au clavier complète
- Focus management via `scrollIntoView`
- Container focusable pour la photo

### Constantes
```typescript
TMDB_POSTER_SMALL = 'https://image.tmdb.org/t/p/w342'
```

## Améliorations Potentielles

1. **Gestion d'erreur**: Ajouter un état d'erreur si l'API échoue
2. **Loading state**: Afficher un skeleton pendant le chargement
3. **Photos manquantes**: Placeholder si `profile_path` est null
4. **Pagination**: Limiter le nombre initial de cartes pour de grandes filmographies
5. **Filtres**: Permettre de filtrer par type (films/TV) ou rôle
6. **Recherche**: Barre de recherche dans la filmographie
7. **Tri personnalisé**: Options de tri (date, popularité, note)
