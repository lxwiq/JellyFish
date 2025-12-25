# Documentation de la Page Utilisateurs (UsersPage)

## Vue d'ensemble

La page `UsersPage.svelte` est une page de sélection d'utilisateurs inspirée du style Netflix "Who is watching?". Elle permet de gérer plusieurs sessions utilisateur, de basculer entre les comptes et d'ajouter de nouveaux utilisateurs.

**Fichier source:** `/Users/lolo/Dev/reiverr/src/lib/pages/UsersPage.svelte`

---

## 1. Structure de la page (Diagramme ASCII)

```
┌──────────────────────────────────────────────────────────────────────┐
│                         DetachedPage                                  │
│                    (plein écran, centrée)                            │
│  ┌────────────────────────────────────────────────────────────────┐  │
│  │                                                                 │  │
│  │                  "Who is watching?"                             │  │
│  │                      (Titre H1)                                 │  │
│  │                                                                 │  │
│  │  ┌────────────────────────────────────────────────────────┐   │  │
│  │  │          Container (Grid, 4 colonnes)                   │   │  │
│  │  │                                                          │   │  │
│  │  │  ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌────────┐ │   │  │
│  │  │  │ Profile  │  │ Profile  │  │ Profile  │  │  Plus  │ │   │  │
│  │  │  │  Icon 1  │  │  Icon 2  │  │  Icon 3  │  │  Icon  │ │   │  │
│  │  │  │  (User)  │  │  (User)  │  │  (User)  │  │  (+)   │ │   │  │
│  │  │  ├──────────┤  ├──────────┤  ├──────────┤  └────────┘ │   │  │
│  │  │  │Username 1│  │Username 2│  │Username 3│             │   │  │
│  │  │  └──────────┘  └──────────┘  └──────────┘             │   │  │
│  │  │                                                          │   │  │
│  │  └────────────────────────────────────────────────────────┘   │  │
│  │                                                                 │  │
│  │  ┌────────────────────────────────────────────────────────┐   │  │
│  │  │         Container (Horizontal)                          │   │  │
│  │  │  ┌──────────────────────────────────────────────────┐  │   │  │
│  │  │  │  Button: "Remove all Accounts" [Trash Icon]     │  │   │  │
│  │  │  └──────────────────────────────────────────────────┘  │   │  │
│  │  └────────────────────────────────────────────────────────┘   │  │
│  │                                                                 │  │
│  └────────────────────────────────────────────────────────────────┘  │
└──────────────────────────────────────────────────────────────────────┘

État alternatif (aucun utilisateur):
┌──────────────────────────────────────────────────────────────────────┐
│                         DetachedPage                                  │
│  ┌────────────────────────────────────────────────────────────────┐  │
│  │                                                                 │  │
│  │            ┌─────────────────────────────────┐                 │  │
│  │            │  Conteneur de connexion         │                 │  │
│  │            │  (LoginForm)                    │                 │  │
│  │            │  bg-primary-800                 │                 │  │
│  │            │  Arrondi, ombré                 │                 │  │
│  │            └─────────────────────────────────┘                 │  │
│  │                                                                 │  │
│  └────────────────────────────────────────────────────────────────┘  │
└──────────────────────────────────────────────────────────────────────┘
```

---

## 2. Composants utilisés

### Composants importés

| Composant | Source | Rôle |
|-----------|--------|------|
| `DetachedPage` | `../components/DetachedPage/DetachedPage.svelte` | Conteneur de page plein écran avec gestion du focus et navigation |
| `Container` | `$components/Container.svelte` | Composant de mise en page avec support pour la navigation au clavier (d-pad) |
| `Button` | `../components/Button.svelte` | Bouton interactif avec états de focus et animations |
| `ProfileIcon` | `../components/ProfileIcon.svelte` | Icône de profil circulaire avec image et overlay optionnel |
| `Login` | `../components/LoginForm.svelte` | Formulaire de connexion affiché quand aucun utilisateur n'existe |
| `AddUserDialog` | `../components/Dialog/AddUserDialog.svelte` | Dialogue modal pour ajouter un nouvel utilisateur |

### Icônes (radix-icons-svelte)

- **Plus** : Icône "+" pour ajouter un utilisateur
- **Trash** : Icône corbeille pour supprimer tous les comptes

### Stores

- **sessions** : Store gérant les sessions utilisateur actives
- **modal.store** : Store pour la gestion des dialogues modaux

---

## 3. Layout et mise en page

### Structure DetachedPage

```css
Classes: px-32 py-16 h-full flex flex-col items-center justify-center
```

- **Padding horizontal** : `px-32` (8rem = 128px de chaque côté)
- **Padding vertical** : `py-16` (4rem = 64px en haut et en bas)
- **Hauteur** : `h-full` (100% de la hauteur disponible)
- **Display** : `flex flex-col` (colonne flexbox)
- **Alignement** : `items-center justify-center` (centrage vertical et horizontal)
- **Sidebar** : Désactivée (`sidebar={false}`)

### Titre principal

```css
Classes: h1 mb-16
```

- **Style** : Style de titre H1 (défini dans le thème)
- **Marge inférieure** : `mb-16` (4rem = 64px)
- **Texte** : "Who is watching?"

### Grille de profils utilisateurs

```svelte
Container direction="grid" gridCols={4}
Classes: flex space-x-8 mb-16
```

- **Type de layout** : Grille
- **Colonnes** : 4 colonnes
- **Espacement horizontal** : `space-x-8` (2rem = 32px entre les éléments)
- **Marge inférieure** : `mb-16` (4rem = 64px)

### ProfileIcon

```css
Dimensions: w-40 h-40 (10rem x 10rem = 160px x 160px)
Classes: mb-4 rounded-xl overflow-hidden cursor-pointer
```

- **Taille** : 160x160 pixels
- **Marge inférieure** : `mb-4` (1rem = 16px)
- **Bordure arrondie** : `rounded-xl` (0.75rem = 12px)
- **Débordement** : Caché (`overflow-hidden`)
- **Curseur** : Pointeur

### Nom d'utilisateur

```css
Classes: text-center h4
État focus: !text-secondary-100
```

- **Alignement** : Centré
- **Style** : H4 (titre de niveau 4)
- **Couleur au focus** : `text-secondary-100` (couleur claire)

### Conteneur de boutons

```svelte
Container direction="horizontal"
Classes: flex space-x-4
```

- **Direction** : Horizontale
- **Espacement** : `space-x-4` (1rem = 16px entre les boutons)

### État de connexion (aucun utilisateur)

```css
Classes: bg-primary-800 rounded-2xl p-10 shadow-xl max-w-lg
```

- **Fond** : `bg-primary-800` (couleur primaire foncée)
- **Bordure arrondie** : `rounded-2xl` (1rem = 16px)
- **Padding** : `p-10` (2.5rem = 40px de tous les côtés)
- **Ombre** : `shadow-xl` (ombre portée extra-large)
- **Largeur maximale** : `max-w-lg` (32rem = 512px)

---

## 4. Interactions utilisateur

### 4.1 Chargement des utilisateurs

```typescript
$: users = getUsers($sessions.sessions);
```

- **Récupération réactive** : Les utilisateurs sont récupérés automatiquement quand les sessions changent
- **Logique** :
  - Pour chaque session, un appel API est effectué vers `/users/{id}`
  - Les erreurs sont gérées gracieusement
  - Seuls les utilisateurs valides sont filtrés et retournés

### 4.2 Sélection d'utilisateur

**Action** : Clic ou sélection (clavier/manette) sur un ProfileIcon d'utilisateur

**Événement** : `on:clickOrSelect={() => user && handleSwitchUser(item)}`

**Comportement** :
1. Définit la session active : `sessions.setActiveSession(session)`
2. Navigue vers la page d'accueil : `navigate('/')`

### 4.3 Ajout d'utilisateur

**Action** : Clic ou sélection sur l'icône "+"

**Événement** : `on:clickOrSelect={() => createModal(AddUserDialog, {})}`

**Comportement** :
- Ouvre un dialogue modal `AddUserDialog` pour ajouter un nouvel utilisateur

### 4.4 Suppression de tous les comptes

**Action** : Clic ou sélection sur le bouton "Remove all Accounts"

**Événement** :
```typescript
on:clickOrSelect={() => {
    sessions.removeSessions();
    navigate('/');
}}
```

**Comportement** :
1. Supprime toutes les sessions utilisateur du store
2. Redirige vers la page d'accueil
3. Affichera le formulaire de connexion au prochain chargement

### 4.5 État de focus

- **ProfileIcon** : Animation de mise à l'échelle au survol/focus
- **Nom d'utilisateur** : Change de couleur quand l'élément parent a le focus (`hasFocusWithin`)
- **Button** : Styles de sélection visuels (définis dans le composant Button)

### 4.6 Navigation au clavier/manette

Le composant utilise le système de navigation `Selectable` qui permet :
- Navigation avec les touches directionnelles (flèches, d-pad)
- Navigation en grille (4 colonnes)
- Sélection avec Enter/Espace/Bouton A
- Retour avec Échap/Bouton B

---

## 5. Équivalent Kotlin/Jetpack Compose

Voici une implémentation équivalente en Kotlin avec Jetpack Compose :

```kotlin
@Composable
fun UsersPage(
    viewModel: UsersViewModel = hiltViewModel(),
    onNavigateHome: () -> Unit
) {
    val sessions by viewModel.sessions.collectAsState()
    val users by viewModel.users.collectAsState()

    DetachedPage(
        showSidebar = false,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 128.dp, vertical = 64.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            when {
                users.isEmpty() -> LoginSection()
                else -> UsersSelectionSection(
                    users = users,
                    onUserSelect = { session ->
                        viewModel.setActiveSession(session)
                        onNavigateHome()
                    },
                    onAddUser = { viewModel.showAddUserDialog() },
                    onRemoveAllAccounts = {
                        viewModel.removeSessions()
                        onNavigateHome()
                    }
                )
            }
        }
    }
}

@Composable
private fun UsersSelectionSection(
    users: List<UserWithSession>,
    onUserSelect: (Session) -> Unit,
    onAddUser: () -> Unit,
    onRemoveAllAccounts: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        // Titre
        Text(
            text = "Who is watching?",
            style = MaterialTheme.typography.h1,
            modifier = Modifier.padding(bottom = 64.dp)
        )

        // Grille de profils utilisateurs
        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            horizontalArrangement = Arrangement.spacedBy(32.dp),
            verticalArrangement = Arrangement.spacedBy(32.dp),
            modifier = Modifier.padding(bottom = 64.dp)
        ) {
            // Utilisateurs existants
            items(users) { item ->
                UserProfileItem(
                    user = item.user,
                    onClick = { onUserSelect(item.session) }
                )
            }

            // Bouton d'ajout d'utilisateur
            item {
                AddUserButton(onClick = onAddUser)
            }
        }

        // Bouton de suppression de tous les comptes
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(top = 32.dp)
        ) {
            Button(
                onClick = onRemoveAllAccounts,
                modifier = Modifier.height(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text("Remove all Accounts")
            }
        }
    }
}

@Composable
private fun UserProfileItem(
    user: User,
    onClick: () -> Unit
) {
    var isFocused by remember { mutableStateOf(false) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable(onClick = onClick)
            .focusable()
            .onFocusChanged { isFocused = it.isFocused }
    ) {
        // Icône de profil avec animation
        AnimatedProfileIcon(
            url = user.profilePicture ?: ProfilePictures.keanu,
            isFocused = isFocused,
            modifier = Modifier
                .size(160.dp)
                .padding(bottom = 16.dp)
                .clip(RoundedCornerShape(12.dp))
        )

        // Nom d'utilisateur
        Text(
            text = user.name,
            style = MaterialTheme.typography.h4,
            textAlign = TextAlign.Center,
            color = if (isFocused)
                MaterialTheme.colors.secondary
            else
                MaterialTheme.colors.onSurface
        )
    }
}

@Composable
private fun AnimatedProfileIcon(
    url: String,
    isFocused: Boolean,
    modifier: Modifier = Modifier
) {
    val scale by animateFloatAsState(
        targetValue = if (isFocused) 1.1f else 1.0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    )

    Box(
        modifier = modifier
            .scale(scale)
            .background(
                color = MaterialTheme.colors.surface,
                shape = RoundedCornerShape(12.dp)
            )
    ) {
        AsyncImage(
            model = url,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
private fun AddUserButton(onClick: () -> Unit) {
    var isFocused by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (isFocused) 1.1f else 1.0f
    )

    Box(
        modifier = Modifier
            .size(160.dp)
            .scale(scale)
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .focusable()
            .onFocusChanged { isFocused = it.isFocused }
    ) {
        AsyncImage(
            model = "profile-pictures/leo.webp",
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Overlay avec icône Plus
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.4f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add User",
                tint = Color.White,
                modifier = Modifier.size(48.dp)
            )
        }
    }
}

@Composable
private fun LoginSection() {
    Card(
        modifier = Modifier
            .widthIn(max = 512.dp)
            .padding(40.dp),
        backgroundColor = MaterialTheme.colors.primary,
        shape = RoundedCornerShape(16.dp),
        elevation = 24.dp
    ) {
        LoginForm()
    }
}

// ViewModel
@HiltViewModel
class UsersViewModel @Inject constructor(
    private val sessionRepository: SessionRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    val sessions = sessionRepository.getSessions()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val users = sessions.flatMapLatest { sessions ->
        flow {
            val usersList = sessions.mapNotNull { session ->
                try {
                    val user = userRepository.getUser(session.baseUrl, session.token, session.id)
                    UserWithSession(session, user)
                } catch (e: Exception) {
                    null
                }
            }
            emit(usersList)
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun setActiveSession(session: Session) {
        viewModelScope.launch {
            sessionRepository.setActiveSession(session)
        }
    }

    fun removeSessions() {
        viewModelScope.launch {
            sessionRepository.removeAllSessions()
        }
    }

    fun showAddUserDialog() {
        // Implémentation du dialogue
    }
}

data class UserWithSession(
    val session: Session,
    val user: User
)
```

### Dépendances Gradle nécessaires

```gradle
dependencies {
    // Compose
    implementation "androidx.compose.ui:ui:1.5.4"
    implementation "androidx.compose.material:material:1.5.4"
    implementation "androidx.compose.foundation:foundation:1.5.4"

    // Navigation
    implementation "androidx.navigation:navigation-compose:2.7.5"

    // ViewModel
    implementation "androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2"

    // Images asynchrones
    implementation "io.coil-kt:coil-compose:2.5.0"

    // Hilt (Injection de dépendances)
    implementation "com.google.dagger:hilt-android:2.48"
    kapt "com.google.dagger:hilt-compiler:2.48"
    implementation "androidx.hilt:hilt-navigation-compose:1.1.0"

    // Coroutines
    implementation "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3"
}
```

---

## Différences clés Svelte vs Compose

| Aspect | Svelte | Jetpack Compose |
|--------|--------|-----------------|
| **Réactivité** | Compilateur réactif automatique (`$:`) | Flow/StateFlow avec `collectAsState()` |
| **Gestion d'état** | Stores Svelte | ViewModel + Flow |
| **Slots** | `<slot>` natif | `@Composable` lambda parameters |
| **Navigation** | Fonction `navigate()` custom | NavController |
| **Await en template** | `{#await}` natif | State hoisting + when expression |
| **Événements** | `on:event` directives | Lambda callbacks |
| **Classes CSS** | Tailwind inline | Modifier chains |
| **Grille** | CSS Grid + Container custom | LazyVerticalGrid |
| **Animations** | CSS + composants custom | `animateFloatAsState()` + spring() |

---

## Notes techniques

### Gestion des sessions
- Les sessions sont stockées dans un store Svelte persistant
- Chaque session contient : `baseUrl`, `token`, `id`
- La récupération des utilisateurs se fait de manière asynchrone et résiliente aux erreurs

### Navigation focus-based
- Le système utilise un mécanisme de focus personnalisé pour la navigation au clavier/manette
- Les `Container` gèrent la logique de navigation directionnelle
- Support natif pour TV et gaming controllers

### Performances
- Les profils utilisateurs sont chargés en parallèle avec `Promise.all()`
- Les erreurs API n'empêchent pas le chargement des autres profils
- Filtrage des résultats invalides avant l'affichage

### Accessibilité
- Navigation au clavier complète
- États de focus visuels clairs
- Support des lecteurs d'écran via les composants sémantiques
