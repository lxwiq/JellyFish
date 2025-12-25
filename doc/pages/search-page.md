# Page de recherche - Reiverr

**Fichier**: `/src/lib/pages/SearchPage.svelte`

## Description
Page dédiée à la recherche de titres (films, séries, personnes) via l'API TMDB. Implémente un champ de recherche avec debouncing et affiche les résultats dans une grille responsive.

---

## Structure de la page (ASCII)

```
┌──────────────────────────────────────────────────────────────────┐
│ DetachedPage (px-32 py-16 h-screen)                             │
│                                                                  │
│  ┌────────────────────────────────────────────────────────────┐ │
│  │ Container (horizontal) - Barre de recherche               │ │
│  │ ┌──────────────────────────────────────────────────────┐   │ │
│  │ │ ○ [Icône loupe] "Search titles..."                   │   │ │
│  │ └──────────────────────────────────────────────────────┘   │ │
│  │ ═══════════════════════════════════════════════════════════ │ │
│  └────────────────────────────────────────────────────────────┘ │
│                                                                  │
│  ┌────────────────────────────────────────────────────────────┐ │
│  │ Conteneur de résultats (overflow-y-auto)                  │ │
│  │                                                            │ │
│  │  ┌──────────────────────────────────────────────────┐     │ │
│  │  │ CardGrid (responsive)                            │     │ │
│  │  │                                                  │     │ │
│  │  │  ┌─────┐  ┌─────┐  ┌─────┐  ┌─────┐  ┌─────┐   │     │ │
│  │  │  │Card │  │Card │  │Card │  │Card │  │Card │   │     │ │
│  │  │  │ 1   │  │ 2   │  │ 3   │  │ 4   │  │ 5   │   │     │ │
│  │  │  └─────┘  └─────┘  └─────┘  └─────┘  └─────┘   │     │ │
│  │  │                                                  │     │ │
│  │  │  ┌─────┐  ┌─────┐  ┌─────┐  ┌─────┐  ┌─────┐   │     │ │
│  │  │  │Card │  │Card │  │Card │  │Card │  │Card │   │     │ │
│  │  │  │ 6   │  │ 7   │  │ 8   │  │ 9   │  │ 10  │   │     │ │
│  │  │  └─────┘  └─────┘  └─────┘  └─────┘  └─────┘   │     │ │
│  │  │                                                  │     │ │
│  │  └──────────────────────────────────────────────────┘     │ │
│  │                                                            │ │
│  └────────────────────────────────────────────────────────────┘ │
│                                                                  │
└──────────────────────────────────────────────────────────────────┘
```

---

## Composants utilisés

### 1. DetachedPage
**Fichier**: `/src/lib/components/DetachedPage/DetachedPage.svelte`

Conteneur principal de la page avec classes:
```css
px-32    /* Padding horizontal: 128px */
py-16    /* Padding vertical: 64px */
h-screen /* Hauteur plein écran */
flex
flex-col /* Direction verticale */
```

### 2. Container (horizontal)
**Fichier**: `/src/lib/components/Container.svelte`

Conteneur pour la barre de recherche avec:
- **Direction**: `horizontal`
- **Classes**:
  - `h3` - Taille de texte (heading 3)
  - `pb-3` - Padding bottom: 12px
  - `border-b-2` - Bordure inférieure 2px
  - `border-secondary-700` - Couleur bordure
  - `w-full` - Largeur 100%
  - `mb-4` - Margin bottom: 16px

**Props utilisées**:
- `bind:hasFocus={inputFocused}` - Binding du focus
- `on:clickOrSelect` - Événement de clic/sélection

### 3. AnimateScale
**Fichier**: `/src/lib/components/AnimateScale.svelte`

Anime l'icône de loupe au focus:
```svelte
<AnimateScale hasFocus={$inputFocused}>
  <MagnifyingGlass size={32} />
</AnimateScale>
```

### 4. Input (champ de recherche)
Input natif HTML avec:
```css
bg-transparent          /* Fond transparent */
outline-none           /* Pas d'outline au focus */
placeholder:text-secondary-400 /* Couleur placeholder */
```

**Bindings**:
- `bind:value={searchQuery}` - Valeur bidirectionnelle
- `on:input={() => handleInput(searchQuery)}` - Déclencheur de recherche
- `bind:this={searchInput}` - Référence DOM

### 5. CardGrid
**Fichier**: `/src/lib/components/CardGrid.svelte`

Grille responsive pour les résultats:
- **Type**: `portrait` (par défaut)
- **Gap**: `gap-x-8 gap-y-8` (32px horizontal et vertical)
- **Classes**: `px-4` (padding 16px)
- **Colonnes**: Calculées dynamiquement selon la largeur de fenêtre

### 6. TmdbCard
**Fichier**: `/src/lib/components/Card/TmdbCard.svelte`

Carte pour afficher un résultat de recherche (film/série/personne):
```svelte
<TmdbCard {...result} on:enter={scrollIntoView({ vertical: 128 })} />
```

**Props**:
- `item` - Objet résultat TMDB

**Événements**:
- `on:enter` - Scroll automatique à la sélection (128px offset vertical)

---

## Layout et dimensions

### Espacements principaux

| Élément | Propriété | Valeur |
|---------|-----------|--------|
| Page | Padding horizontal | 128px (`px-32`) |
| Page | Padding vertical | 64px (`py-16`) |
| Barre de recherche | Margin bottom | 16px (`mb-4`) |
| Barre de recherche | Padding bottom | 12px (`pb-3`) |
| Icône/Input | Spacing | 16px (`space-x-4`) |
| CardGrid | Padding horizontal | 16px (`px-4`) |
| CardGrid | Gap horizontal | 32px (`gap-x-8`) |
| CardGrid | Gap vertical | 32px (`gap-y-8`) |
| Zone de résultats | Padding top | 16px (`pt-4`) |

### Colonnes de la grille (responsive)

Basé sur `getCardDimensions()` dans `/src/lib/utils`:

| Largeur fenêtre | Colonnes |
|-----------------|----------|
| < 768px | 3 |
| 768px - 1279px | 4 |
| 1280px - 1535px | 5 |
| ≥ 1536px | 6 |

### Zone de résultats

```css
min-h-0           /* Hauteur min 0 pour overflow */
overflow-y-auto   /* Scroll vertical */
scrollbar-hide    /* Masque la scrollbar */
pt-4              /* Padding top: 16px */
```

---

## Logique de recherche

### Variables d'état

```typescript
let searchQuery = '';  // Requête de recherche
let typingTimeout: ReturnType<typeof setTimeout> | undefined = undefined;
let loadingTimeout: ReturnType<typeof setTimeout> | undefined = undefined;
let results: Promise<ComponentProps<TmdbCard>[]> = Promise.resolve([]);
let searchInput: HTMLInputElement | undefined = undefined;
let inputFocused: Readable<boolean>;  // Store du focus
```

### Fonction handleInput avec debouncing

```typescript
function handleInput(query: string) {
    clearTimeout(typingTimeout);

    typingTimeout = setTimeout(async () => {
        if (!query) return;
        results = tmdbApi.searchTitles(query).then((results) =>
            results.map((result) => ({
                item: result
            }))
        );
        if (query !== searchQuery) return;
    }, 500);
}
```

**Mécanisme de debouncing**:
1. À chaque saisie, le timeout précédent est annulé
2. Un nouveau timeout de **500ms** est créé
3. Si l'utilisateur tape à nouveau avant 500ms, le cycle recommence
4. Après 500ms d'inactivité, la recherche est lancée

**Protection contre les conditions de course**:
```typescript
if (query !== searchQuery) return;
```
Vérifie que la requête est toujours d'actualité avant d'utiliser les résultats.

### API TMDB

**Endpoint**: `/3/search/multi`

```typescript
searchTitles = (query: string) =>
    this.getClient()
        ?.GET('/3/search/multi', {
            params: {
                query: {
                    query
                }
            }
        })
        .then((res) => res.data?.results || []) || Promise.resolve([]);
```

**Retour**: Array de résultats mixtes (films, séries, personnes)

### Rendu conditionnel

```svelte
{#if !!searchQuery}
    {#await results then results}
        <CardGrid class="px-4">
            {#each results as result}
                <TmdbCard {...result} on:enter={scrollIntoView({ vertical: 128 })} />
            {/each}
        </CardGrid>
    {/await}
{/if}
```

**Logique**:
1. Affiche uniquement si `searchQuery` n'est pas vide
2. Attend la résolution de la Promise `results`
3. Itère sur les résultats pour créer les cartes

### Nettoyage

```typescript
onDestroy(() => {
    clearTimeout(typingTimeout);
});
```

Nettoie le timeout lors de la destruction du composant pour éviter les fuites mémoire.

---

## États visuels

### Barre de recherche - Normal
```css
border-secondary-700  /* Bordure grise */
text-secondary-300    /* Texte gris clair */
```

### Barre de recherche - Focus
```css
border-secondary-400  /* Bordure plus claire (commenté dans le code) */
```

### Icône de loupe
- **Taille**: 32px
- **Animation**: Scale au focus via `AnimateScale`
- **Couleur normale**: `text-secondary-200`

### Placeholder
```css
placeholder:text-secondary-400  /* Gris moyen */
```

---

## Équivalent Kotlin/Compose

```kotlin
@Composable
fun SearchPage(
    viewModel: SearchViewModel = viewModel()
) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val searchResults by viewModel.searchResults.collectAsState()
    val inputFocused = remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 128.dp, vertical = 64.dp)
    ) {
        // Barre de recherche
        SearchBar(
            query = searchQuery,
            onQueryChange = { viewModel.onSearchQueryChanged(it) },
            focused = inputFocused.value,
            onFocusChange = { inputFocused.value = it },
            focusRequester = focusRequester,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Résultats
        if (searchQuery.isNotEmpty()) {
            SearchResults(
                results = searchResults,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            )
        }
    }
}

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    focused: Boolean,
    onFocusChange: (Boolean) -> Unit,
    focusRequester: FocusRequester,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Icône avec animation
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                modifier = Modifier
                    .size(32.dp)
                    .scale(if (focused) 1.1f else 1.0f)
                    .animateContentSize(),
                tint = if (focused) Color.White else Secondary200
            )

            // Champ de saisie
            BasicTextField(
                value = query,
                onValueChange = onQueryChange,
                modifier = Modifier
                    .weight(1f)
                    .focusRequester(focusRequester)
                    .onFocusChanged { onFocusChange(it.isFocused) },
                textStyle = TextStyle(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                ),
                decorationBox = { innerTextField ->
                    Box {
                        if (query.isEmpty()) {
                            Text(
                                "Search titles...",
                                color = Secondary400,
                                fontSize = 24.sp
                            )
                        }
                        innerTextField()
                    }
                },
                cursorBrush = SolidColor(Color.White),
                singleLine = true
            )
        }

        // Bordure inférieure
        Divider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 2.dp,
            color = if (focused) Secondary400 else Secondary700
        )
    }
}

@Composable
fun SearchResults(
    results: List<TmdbSearchResult>,
    modifier: Modifier = Modifier
) {
    val configuration = LocalConfiguration.current
    val columns = when {
        configuration.screenWidthDp >= 1536 -> 6
        configuration.screenWidthDp >= 1280 -> 5
        configuration.screenWidthDp >= 768 -> 4
        else -> 3
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(columns),
        modifier = modifier,
        contentPadding = PaddingValues(top = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(32.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        items(results) { result ->
            TmdbCard(
                item = result,
                onClick = { /* Navigation */ }
            )
        }
    }
}

// ViewModel avec debouncing
class SearchViewModel : ViewModel() {
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _searchResults = MutableStateFlow<List<TmdbSearchResult>>(emptyList())
    val searchResults: StateFlow<List<TmdbSearchResult>> = _searchResults.asStateFlow()

    private val tmdbApi: TmdbApi = TmdbApi()

    init {
        // Debouncing avec Flow
        viewModelScope.launch {
            searchQuery
                .debounce(500) // 500ms de debounce
                .filter { it.isNotEmpty() }
                .distinctUntilChanged()
                .collectLatest { query ->
                    performSearch(query)
                }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    private suspend fun performSearch(query: String) {
        try {
            val results = tmdbApi.searchTitles(query)
            if (_searchQuery.value == query) { // Protection contre race conditions
                _searchResults.value = results
            }
        } catch (e: Exception) {
            // Gestion d'erreur
        }
    }
}

// Couleurs
object Secondary {
    val Secondary200 = Color(0xFFE5E7EB)
    val Secondary400 = Color(0xFF9CA3AF)
    val Secondary700 = Color(0xFF374151)
}
```

### Points clés de l'implémentation Kotlin

1. **Debouncing**: Utilise `Flow.debounce(500)` au lieu de `setTimeout`
2. **État réactif**: `StateFlow` pour `searchQuery` et `searchResults`
3. **Grille responsive**: `LazyVerticalGrid` avec calcul dynamique des colonnes
4. **Focus management**: `FocusRequester` et `onFocusChanged`
5. **Animation**: `animateContentSize()` pour l'icône
6. **Protection race conditions**: Vérifie que la query n'a pas changé avant de mettre à jour les résultats

---

## Navigation D-pad

La page utilise le système `Selectable` pour la navigation au clavier/télécommande:

```typescript
import { scrollIntoView } from '../selectable';

// Sur chaque carte
<TmdbCard on:enter={scrollIntoView({ vertical: 128 })} />
```

**Comportement**:
- Navigation dans la grille avec les touches directionnelles
- Scroll automatique avec offset de 128px lors de la sélection
- Focus management via les `Container` composants

---

## Bonnes pratiques

1. **Debouncing obligatoire**: Toujours debouncer les recherches API (500ms recommandé)
2. **Protection race conditions**: Vérifier que la query est toujours d'actualité
3. **Nettoyage**: Toujours `clearTimeout()` dans `onDestroy()`
4. **Affichage conditionnel**: Ne pas afficher de résultats si query vide
5. **Promise handling**: Utiliser `{#await}` pour gérer les états de chargement
6. **Scroll automatique**: Implémenter `scrollIntoView` pour meilleure UX navigation
7. **Focus visuel**: Indiquer clairement l'état de focus (bordures, couleurs)
8. **Grille responsive**: Adapter le nombre de colonnes à la largeur d'écran

---

## Améliorations possibles

1. **Loading state**: Afficher un spinner pendant la recherche
2. **Empty state**: Message si aucun résultat trouvé
3. **Error handling**: Gérer les erreurs API
4. **Historique**: Sauvegarder les recherches récentes
5. **Suggestions**: Auto-complétion basée sur l'historique
6. **Filtres**: Par type (films, séries, personnes)
7. **Infinite scroll**: Pagination pour plus de résultats
8. **Keyboard shortcuts**: Raccourcis pour focus rapide (Ctrl+K)
