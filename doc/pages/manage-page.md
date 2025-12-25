# ManagePage - Documentation Technique

## Vue d'ensemble

La page `ManagePage` est la page principale de paramètres de l'application Reiverr. Elle permet aux utilisateurs de gérer leur compte, leurs sources médias, leurs intégrations tierces et les options d'interface.

**Chemin du fichier**: `/Users/lolo/Dev/reiverr/src/lib/pages/ManagePage/ManagePage.svelte`

---

## 1. Structure de la page (Diagramme ASCII)

```
┌─────────────────────────────────────────────────────────────────────────────┐
│  DetachedPage (écran plein, pt-16, px-32)                                   │
│  ┌───────────────────────────────────────────────────────────────────────┐  │
│  │  En-tête (border-b-2)                                                  │  │
│  │  ┌─────────────────────────────────────────────────────────────────┐  │  │
│  │  │  <h1> Settings </h1>                                             │  │  │
│  │  │  <p> Manage your settings and integrations </p>                  │  │  │
│  │  └─────────────────────────────────────────────────────────────────┘  │  │
│  └───────────────────────────────────────────────────────────────────────┘  │
│                                                                              │
│  ┌──────────────────────────────────────────────────────────────────────┐  │
│  │  Contenu principal (flex, max-w-7xl, space-x-16)                     │  │
│  │  ┌────────────────┐  ┌──────────────────────────────────────────┐   │  │
│  │  │  Menu latéral  │  │  Zone de contenu des onglets             │   │  │
│  │  │  (w-52/w-72)   │  │  (flex-1, overflow-y-auto)               │   │  │
│  │  │                │  │                                           │   │  │
│  │  │  ┌──────────┐  │  │  ┌────────────────────────────────────┐  │   │  │
│  │  │  │ Account  │  │  │  │  Tab: Account                       │  │   │  │
│  │  │  └──────────┘  │  │  │  ┌──────────────────────────────┐   │  │   │  │
│  │  │  ┌──────────┐  │  │  │  │  Section: My Profile         │   │  │   │  │
│  │  │  │ Options  │  │  │  │  │  - SelectField (utilisateur) │   │  │   │  │
│  │  │  └──────────┘  │  │  │  │  - Bouton Log Out           │   │  │   │  │
│  │  │  ┌──────────┐  │  │  │  │  - Gestion utilisateurs      │   │  │   │  │
│  │  │  │  About   │  │  │  │  └──────────────────────────────┘   │  │   │  │
│  │  │  └──────────┘  │  │  │                                       │  │   │  │
│  │  │                │  │  │  ┌──────────────────────────────┐   │  │   │  │
│  │  │                │  │  │  │  Section: Media Sources       │   │  │   │  │
│  │  │                │  │  │  │  (MediaSources component)     │   │  │   │  │
│  │  │                │  │  │  └──────────────────────────────┘   │  │   │  │
│  │  │                │  │  │                                       │  │   │  │
│  │  │                │  │  │  ┌──────────────────────────────┐   │  │   │  │
│  │  │                │  │  │  │  Section: Integrations        │   │  │   │  │
│  │  │                │  │  │  │  - TMDB Account              │   │  │   │  │
│  │  │                │  │  │  └──────────────────────────────┘   │  │   │  │
│  │  │                │  │  └────────────────────────────────────┘  │   │  │
│  │  │                │  │                                           │   │  │
│  │  │                │  │  ┌────────────────────────────────────┐  │   │  │
│  │  │                │  │  │  Tab: Interface                    │  │   │  │
│  │  │                │  │  │  - Toggle: Animate scrolling       │  │   │  │
│  │  │                │  │  │  - Toggle: Use CSS Transitions     │  │   │  │
│  │  │                │  │  │  - Toggle: Check for Updates       │  │   │  │
│  │  │                │  │  │  - Toggle: Enable Trailers         │  │   │  │
│  │  │                │  │  │  - Toggle: Autoplay Trailers       │  │   │  │
│  │  │                │  │  └────────────────────────────────────┘  │   │  │
│  │  │                │  │                                           │   │  │
│  │  │                │  │  ┌────────────────────────────────────┐  │   │  │
│  │  │                │  │  │  Tab: About                        │  │   │  │
│  │  │                │  │  │  - Informations version            │  │   │  │
│  │  │                │  │  │  - Informations debug              │  │   │  │
│  │  │                │  │  │  - Actions: Clear Cache, Log Out   │  │   │  │
│  │  │                │  │  └────────────────────────────────────┘  │   │  │
│  │  └────────────────┘  └──────────────────────────────────────────┘   │  │
│  └──────────────────────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## 2. Sections principales

### 2.1 Onglet "Account" (Compte)

#### Section "My Profile"
- **Objectif**: Gérer le profil de l'utilisateur connecté
- **Composants**:
  - `SelectField`: Affiche le nom de l'utilisateur actuel avec icône crayon (Pencil2)
  - `Button` (Log Out): Bouton de déconnexion avec icône Exit

#### Section "Server Accounts" (réservée aux administrateurs)
- **Objectif**: Gérer tous les comptes utilisateurs du serveur
- **Condition d'affichage**: Visible uniquement si `$user?.isAdmin === true`
- **Composants**:
  - Grille 2 colonnes de `SelectField` pour chaque utilisateur
  - `SelectField` "New Account" pour créer un nouvel utilisateur (icône Plus)
- **Fonctionnalités**:
  - Modification des utilisateurs existants
  - Création de nouveaux comptes
  - Indicateur "Admin" ou "User" pour chaque compte

#### Section "Media Sources"
- **Composant**: `MediaSources.ManagePage.svelte`
- **Objectif**: Gérer les sources de médias externes (plugins)
- **Sous-composants**:
  - `MediaSourceButton`: Bouton pour chaque source configurée
  - `Button` "Add Source": Ajouter une nouvelle source média

#### Section "Integrations"
- **Objectif**: Connecter des services tiers
- **Intégrations disponibles**:
  - **TMDB Account**: Intégration avec The Movie Database
    - Composant: `TmdbIntegration`
    - Dialog: `TmdbIntegrationConnectDialog`
    - Bouton "Connect" si non connecté (icône ArrowRight)

### 2.2 Onglet "Options" (Interface)

Liste de toggles pour contrôler le comportement de l'interface:

| Option | Variable | Description |
|--------|----------|-------------|
| Animate scrolling | `localSettings.animateScrolling` | Active/désactive l'animation du défilement |
| Use CSS Transitions | `localSettings.useCssTransitions` | Active/désactive les transitions CSS |
| Check for Updates | `localSettings.checkForUpdates` | Vérification automatique des mises à jour |
| Enable Trailers | `localSettings.enableTrailers` | Active/désactive les bandes-annonces |
| Autoplay Trailers | `localSettings.autoplayTrailers` | Lecture automatique des bandes-annonces |

### 2.3 Onglet "About" (À propos)

Informations de débogage et système:
- Version de Reiverr (`REIVERR_VERSION`)
- Mode d'environnement (`import.meta.env.MODE`)
- Variables d'environnement complètes
- User agent du navigateur
- Informations clavier (dernière touche pressée)
- Support Tizen (pour TV Samsung)
- Actions:
  - `Button`: Clear TMDB Cache
  - `Button`: Log Out (avec style rouge au hover)

---

## 3. Composants utilisés

### 3.1 Composants de base

#### Container
- **Fichier**: `src/lib/components/Container.svelte`
- **Props principales**:
  - `direction`: 'vertical' | 'horizontal' | 'grid'
  - `gridCols`: nombre (pour mode grille)
  - `focusOnMount`: boolean
  - `trapFocus`: boolean
  - `focusOnClick`: boolean
- **Fonctionnalités**: Gestion de la navigation au clavier (d-pad), focus, et événements

#### DetachedPage
- **Fichier**: `src/lib/components/DetachedPage/DetachedPage.svelte`
- **Utilisation**: Conteneur de page plein écran avec sidebar optionnelle
- **Props**:
  - `topmost`: boolean (affichage au premier plan)
  - `sidebar`: boolean (affichage de la sidebar)
- **Classes**: `fixed inset-0 z-20 bg-secondary-900`

#### Tab
- **Fichier**: `src/lib/components/Tab/Tab.svelte`
- **Hook**: `useTabs(defaultTab, options)`
- **Utilisation**: Système d'onglets avec navigation

### 3.2 Composants de formulaire

#### TextField
- **Fichier**: `src/lib/components/TextField.svelte`
- **Props**:
  - `value`: string
  - `placeholder`: string
  - `type`: 'text' | 'password' | 'number'
- **Événements**: `on:change`

#### SelectField
- **Fichier**: `src/lib/components/SelectField.svelte`
- **Props**:
  - `value`: string (texte affiché)
  - `color`: 'primary' | ...
- **Slots**:
  - `default`: Contenu principal
  - `icon`: Icône à droite
  - `content`: Contenu personnalisé
- **Événements**: `on:clickOrSelect`

#### Toggle
- **Fichier**: `src/lib/components/Toggle.svelte`
- **Props**:
  - `checked`: boolean
- **Événements**: `on:change` (retourne `{ detail: boolean }`)

#### Button
- **Fichier**: `src/lib/components/Button.svelte`
- **Props**:
  - `type`: 'primary-dark' | ...
  - `icon`: Composant d'icône (début)
  - `iconAfter`: Composant d'icône (fin)
  - `disabled`: boolean
  - `confirmDanger`: boolean
  - `focusedChild`: boolean
- **Événements**: `on:clickOrSelect`, `action`

### 3.3 Composants de dialogue

#### Dialog
- **Fichier**: `src/lib/components/Dialog/Dialog.svelte`
- **Props**:
  - `size`: 'sm' | 'lg' | 'full' | 'dynamic'
- **Utilisation**: Conteneur modal avec fond semi-transparent
- **Structure**:
  - Fond: `bg-primary-900/75`
  - Padding: `py-20 px-32`
  - Transition: fade (100ms)

#### EditProfileModal (CreateOrEditProfileModal)
- **Fichier**: `src/lib/components/Dialog/CreateOrEditProfileModal.svelte`
- **Props**:
  - `user`: ReiverrUser | undefined
  - `createNew`: boolean
  - `admin`: boolean
  - `onComplete`: () => void
- **Fonctionnalités**:
  - Modification de profil utilisateur
  - Création de nouveau compte
  - Changement de mot de passe
  - Changement de photo de profil
  - Suppression de compte

#### SelectDialog
- **Fichier**: `src/lib/components/Dialog/SelectDialog.svelte`
- **Props**:
  - `title`: string
  - `subtitle`: string
  - `options`: string[]
  - `selectedOption`: string | undefined
  - `handleSelectOption`: (option: string) => void
- **Utilisation**: Sélection d'une option dans une liste

#### TmdbIntegrationConnectDialog
- **Fichier**: `src/lib/components/Integrations/TmdbIntegrationConnectDialog.svelte`
- **Fonctionnalités**:
  - Affiche un QR code pour la connexion TMDB
  - Lien vers la page d'authentification TMDB
  - Bouton "Complete Connection" pour finaliser

### 3.4 Sous-composants spécifiques

#### MediaSources.ManagePage
- **Fichier**: `src/lib/pages/ManagePage/MediaSources.ManagePage.svelte`
- **Fonctionnalités**:
  - Liste toutes les sources médias configurées
  - Bouton pour ajouter de nouvelles sources
  - Gestion du chargement asynchrone
- **API utilisée**: `reiverrApiNew.providers.getSourceProviders()`

#### MediaSourceButton.ManagePage
- **Fichier**: `src/lib/pages/ManagePage/MediaSourceButton.ManagePage.svelte`
- **Props**: `mediaSource`: MediaSourceDto
- **Affichage**:
  - Indicateur de statut (vert si activé, primaire si désactivé)
  - Nom de la source (capitalisé)
- **Action**: Ouvre `EditMediaSourceDialog`

#### EditMediaSourceDialog.ManagePage
- **Fichier**: `src/lib/pages/ManagePage/EditMediaSourceDialog.ManagePage.svelte`
- **Props**: `sourceId`: string
- **Fonctionnalités**:
  - Édition du nom de la source
  - Configuration des paramètres du plugin (dynamique)
  - Validation des paramètres
  - Sauvegarde avec notification
  - Suppression de la source
- **Types de champs supportés**: string, number, password, boolean, link

#### TmdbIntegration
- **Fichier**: `src/lib/components/Integrations/TmdbIntegration.svelte`
- **Fonctionnalités**:
  - Affiche le compte TMDB connecté
  - Bouton de déconnexion (icône Trash)
  - Slot avec prop `connected`: boolean

---

## 4. Layout et dimensions

### 4.1 Structure globale

```
DetachedPage
├─ Padding vertical: pt-16 (4rem / 64px en haut)
├─ Padding horizontal: px-32 (8rem / 128px)
├─ Hauteur: h-screen (100vh)
├─ Espacement vertical: space-y-16 (4rem / 64px)
```

### 4.2 En-tête

```
border-b-2 border-secondary-700
pb-4 (1rem / 16px)
space-y-2 (0.5rem / 8px)
```

### 4.3 Zone de contenu

```
Container principal
├─ flex-1 flex mx-auto
├─ max-w-7xl (80rem / 1280px)
├─ h-full overflow-y-hidden
├─ space-x-16 (4rem / 64px entre menu et contenu)
```

#### Menu latéral (sidebar)

```
Container direction="vertical"
├─ Largeur: w-52 (13rem / 208px) ou lg:w-72 (18rem / 288px)
├─ Espacement vertical: space-y-4 (1rem / 16px)
├─ Flex: flex-col *:flex-1

Chaque élément de menu:
├─ Padding: py-3 px-6 (12px vertical, 24px horizontal)
├─ Border-radius: rounded-xl
├─ Background: bg-secondary-800 (si actif)
├─ Couleurs texte:
    ├─ Non actif: text-secondary-400
    ├─ Actif sans focus: text-secondary-200
    └─ Focus: text-primary-500
```

#### Zone de contenu des onglets

```
Container
├─ flex-1 grid w-full h-full
├─ overflow-y-hidden relative
├─ Padding bottom: *:pb-16 (4rem / 64px)
├─ Scroll: *:overflow-y-auto *:scrollbar-hide
├─ Margin compensation: -mx-4 px-4 *:-mx-4 *:px-4

Chaque Tab:
├─ direction="vertical"
├─ Espacement: space-y-16 (4rem / 64px entre sections)
```

### 4.4 Sections de contenu

#### Containers de section (Account)

```
Container bg-primary-800
├─ Border-radius: rounded-xl
├─ Padding: p-8 (2rem / 32px)
├─ Max-width (pour Integrations): max-w-sm (24rem / 384px)
```

#### Grille utilisateurs (Server Accounts)

```
Container direction="grid" gridCols={2}
├─ grid grid-cols-2
├─ gap-4 (1rem / 16px)
```

#### Media Sources

```
Container flex flex-col
├─ gap-4 (1rem / 16px)
├─ max-w-sm (24rem / 384px)
```

### 4.5 Typographie

```
Titres de page:
├─ h1: class="h1"
├─ Description: class="body"

Titres de section:
├─ h1: class="h3" ou "h4"
├─ font-semibold text-2xl text-secondary-100

En-têtes de sections:
├─ Marge bottom: mb-4, mb-8
```

### 4.6 Espacement (système Tailwind)

| Class | Valeur | Pixels |
|-------|--------|--------|
| space-y-2 | 0.5rem | 8px |
| space-y-4 | 1rem | 16px |
| space-x-4 | 1rem | 16px |
| space-y-8 | 2rem | 32px |
| mb-8 | 2rem | 32px |
| space-y-16 | 4rem | 64px |
| px-32 | 8rem | 128px |
| pt-16 | 4rem | 64px |

---

## 5. Équivalent Kotlin/Compose

Voici comment implémenter l'équivalent de cette page en Jetpack Compose:

### 5.1 Structure principale

```kotlin
@Composable
fun ManagePage(
    viewModel: ManagePageViewModel = viewModel(),
    onNavigateBack: () -> Unit
) {
    var selectedTab by remember { mutableStateOf(ManageTab.ACCOUNT) }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 128.dp)
                .padding(top = 64.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // En-tête
            ManagePageHeader()

            Spacer(modifier = Modifier.height(64.dp))

            // Contenu principal
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .widthIn(max = 1280.dp),
                horizontalArrangement = Arrangement.spacedBy(64.dp)
            ) {
                // Menu latéral
                SidebarMenu(
                    selectedTab = selectedTab,
                    onTabSelected = { selectedTab = it },
                    modifier = Modifier.width(208.dp)
                )

                // Zone de contenu
                TabContent(
                    selectedTab = selectedTab,
                    viewModel = viewModel,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

enum class ManageTab {
    ACCOUNT,
    INTERFACE,
    ABOUT
}
```

### 5.2 En-tête

```kotlin
@Composable
fun ManagePageHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f),
                shape = RectangleShape
            )
            .padding(bottom = 16.dp)
    ) {
        Text(
            text = "Settings",
            style = MaterialTheme.typography.displayLarge,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Manage your settings and integrations.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
        )
    }
}
```

### 5.3 Menu latéral

```kotlin
@Composable
fun SidebarMenu(
    selectedTab: ManageTab,
    onTabSelected: (ManageTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        MenuTabItem(
            title = "Account",
            isSelected = selectedTab == ManageTab.ACCOUNT,
            onClick = { onTabSelected(ManageTab.ACCOUNT) }
        )

        MenuTabItem(
            title = "Options",
            isSelected = selectedTab == ManageTab.INTERFACE,
            onClick = { onTabSelected(ManageTab.INTERFACE) }
        )

        MenuTabItem(
            title = "About",
            isSelected = selectedTab == ManageTab.ABOUT,
            onClick = { onTabSelected(ManageTab.ABOUT) }
        )
    }
}

@Composable
fun MenuTabItem(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick),
        color = if (isSelected) {
            MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f)
        } else {
            Color.Transparent
        }
    ) {
        Text(
            text = title,
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 24.dp),
            style = MaterialTheme.typography.headlineSmall,
            color = when {
                isSelected -> MaterialTheme.colorScheme.primary
                else -> MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
            }
        )
    }
}
```

### 5.4 Contenu des onglets

```kotlin
@Composable
fun TabContent(
    selectedTab: ManageTab,
    viewModel: ManagePageViewModel,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(64.dp)
    ) {
        when (selectedTab) {
            ManageTab.ACCOUNT -> AccountTab(viewModel)
            ManageTab.INTERFACE -> InterfaceTab(viewModel)
            ManageTab.ABOUT -> AboutTab(viewModel)
        }
    }
}
```

### 5.5 Onglet Account

```kotlin
@Composable
fun AccountTab(viewModel: ManagePageViewModel) {
    val user by viewModel.currentUser.collectAsState()
    val users by viewModel.allUsers.collectAsState()

    Column(verticalArrangement = Arrangement.spacedBy(64.dp)) {
        // Section: My Profile
        SectionContainer {
            Text(
                text = "My Profile",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            SelectField(
                label = "Logged in as",
                value = user?.name ?: "",
                onClick = { /* Ouvrir EditProfileDialog */ },
                trailingIcon = Icons.Outlined.Edit
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { viewModel.logout() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Icon(Icons.Outlined.ExitToApp, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Log Out")
            }

            // Section: Server Accounts (si admin)
            if (user?.isAdmin == true && users.isNotEmpty()) {
                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "Server Accounts",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.height(/* calculer hauteur */)
                ) {
                    items(users.filter { it.id != user?.id }) { account ->
                        SelectField(
                            label = if (account.isAdmin) "Admin" else "User",
                            value = account.name,
                            onClick = { /* Ouvrir EditProfileDialog */ },
                            trailingIcon = Icons.Outlined.Edit
                        )
                    }

                    item {
                        SelectField(
                            label = "Create",
                            value = "New Account",
                            onClick = { /* Ouvrir CreateProfileDialog */ },
                            trailingIcon = Icons.Outlined.Add
                        )
                    }
                }
            }
        }

        // Section: Media Sources
        MediaSourcesSection(viewModel)

        // Section: Integrations
        IntegrationsSection(viewModel)
    }
}
```

### 5.6 Section Media Sources

```kotlin
@Composable
fun MediaSourcesSection(viewModel: ManagePageViewModel) {
    val mediaSources by viewModel.mediaSources.collectAsState()
    val isLoading by viewModel.isLoadingMediaSources.collectAsState()

    Column {
        Text(
            text = "Media Sources",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        Text(
            text = "External media sources allow reiverr to play content from different sources. " +
                   "Additional media sources can be added via external plugins.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
            modifier = Modifier.padding(bottom = 32.dp)
        )

        Column(
            modifier = Modifier.widthIn(max = 384.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator()
            } else {
                mediaSources.forEach { source ->
                    MediaSourceItem(
                        mediaSource = source,
                        onClick = { /* Ouvrir EditMediaSourceDialog */ }
                    )
                }
            }

            Button(
                onClick = { /* Ouvrir SelectDialog pour ajouter source */ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Outlined.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Add Source")
            }
        }
    }
}

@Composable
fun MediaSourceItem(
    mediaSource: MediaSource,
    onClick: () -> Unit
) {
    SelectField(
        value = mediaSource.name.capitalize(),
        onClick = onClick,
        leadingIcon = {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(
                        if (mediaSource.enabled) Color.Green
                        else MaterialTheme.colorScheme.primary
                    )
            )
        }
    )
}
```

### 5.7 Onglet Interface

```kotlin
@Composable
fun InterfaceTab(viewModel: ManagePageViewModel) {
    val localSettings by viewModel.localSettings.collectAsState()

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        ToggleSettingRow(
            label = "Animate scrolling",
            checked = localSettings.animateScrolling,
            onCheckedChange = { viewModel.updateAnimateScrolling(it) }
        )

        ToggleSettingRow(
            label = "Use CSS Transitions",
            checked = localSettings.useCssTransitions,
            onCheckedChange = { viewModel.updateUseCssTransitions(it) }
        )

        ToggleSettingRow(
            label = "Check for Updates",
            checked = localSettings.checkForUpdates,
            onCheckedChange = { viewModel.updateCheckForUpdates(it) }
        )

        ToggleSettingRow(
            label = "Enable Trailers",
            checked = localSettings.enableTrailers,
            onCheckedChange = { viewModel.updateEnableTrailers(it) }
        )

        ToggleSettingRow(
            label = "Autoplay Trailers",
            checked = localSettings.autoplayTrailers,
            onCheckedChange = { viewModel.updateAutoplayTrailers(it) }
        )
    }
}

@Composable
fun ToggleSettingRow(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
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

### 5.8 Onglet About

```kotlin
@Composable
fun AboutTab(viewModel: ManagePageViewModel) {
    val appVersion = BuildConfig.VERSION_NAME
    val buildMode = if (BuildConfig.DEBUG) "Debug" else "Release"

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Version: $appVersion")
        Text("Mode: $buildMode")

        Spacer(modifier = Modifier.height(16.dp))

        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Button(
                onClick = { viewModel.clearTmdbCache() },
                modifier = Modifier.wrapContentWidth()
            ) {
                Text("Clear TMDB Cache")
            }

            Button(
                onClick = { viewModel.logout() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Red
                ),
                modifier = Modifier.wrapContentWidth()
            ) {
                Text("Log Out")
            }
        }
    }
}
```

### 5.9 Composants réutilisables

```kotlin
@Composable
fun SectionContainer(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .widthIn(max = 384.dp),
        color = MaterialTheme.colorScheme.primaryContainer,
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(32.dp),
            content = content
        )
    }
}

@Composable
fun SelectField(
    label: String? = null,
    value: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: ImageVector? = null
) {
    Surface(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                leadingIcon?.invoke()

                Column {
                    if (label != null) {
                        Text(
                            text = label,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                    Text(
                        text = value,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            if (trailingIcon != null) {
                Icon(
                    imageVector = trailingIcon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }
    }
}
```

### 5.10 ViewModel

```kotlin
class ManagePageViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val mediaSourceRepository: MediaSourceRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    val currentUser = userRepository.getCurrentUser()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val allUsers = userRepository.getAllUsers()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val mediaSources = mediaSourceRepository.getMediaSources()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val isLoadingMediaSources = mediaSourceRepository.isLoading
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val localSettings = settingsRepository.getLocalSettings()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), LocalSettings())

    fun logout() {
        viewModelScope.launch {
            userRepository.logout()
        }
    }

    fun updateAnimateScrolling(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.updateAnimateScrolling(enabled)
        }
    }

    fun updateUseCssTransitions(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.updateUseCssTransitions(enabled)
        }
    }

    fun updateCheckForUpdates(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.updateCheckForUpdates(enabled)
        }
    }

    fun updateEnableTrailers(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.updateEnableTrailers(enabled)
        }
    }

    fun updateAutoplayTrailers(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.updateAutoplayTrailers(enabled)
        }
    }

    fun clearTmdbCache() {
        viewModelScope.launch {
            mediaSourceRepository.clearTmdbCache()
        }
    }
}
```

### 5.11 Modèles de données

```kotlin
data class LocalSettings(
    val animateScrolling: Boolean = true,
    val useCssTransitions: Boolean = true,
    val checkForUpdates: Boolean = true,
    val enableTrailers: Boolean = true,
    val autoplayTrailers: Boolean = false
)

data class MediaSource(
    val id: String,
    val name: String,
    val pluginId: String,
    val enabled: Boolean,
    val pluginSettings: Map<String, Any>?,
    val priority: Int?
)

data class User(
    val id: String,
    val name: String,
    val isAdmin: Boolean,
    val profilePicture: String?
)
```

---

## 6. Gestion des états et interactions

### 6.1 États réactifs (Svelte stores)

```typescript
// Stores utilisés
import { user } from '../../stores/user.store';
import { sessions } from '../../stores/session.store';
import { localSettings } from '../../stores/localstorage.store';
import { sources } from '$lib/stores/sources.store';

// Déclaration d'états
const tab = useTabs(Tabs.Interface, { size: 'stretch' });
let users = getUsers(); // Promise<ReiverrUser[]>
$: tmdbAccount = $user?.settings.tmdb.userId ? tmdbApi.getAccountDetails() : undefined;
```

### 6.2 Gestion des modaux

```typescript
import { createModal, modalStack } from '../../components/Modal/modal.store';

// Ouverture d'un modal
createModal(EditProfileModal, {
    user: u,
    onComplete: () => (users = getUsers())
});

// Ouverture du dialog de sélection
createModal(SelectDialog, {
    options: availablePlugins,
    handleSelectOption: (id) => addSource(id),
    title: 'Add Media Source',
    subtitle: 'Select a source provider'
});

// Fermeture
modalStack.closeTopmost();
modalStack.close(modalId);
```

### 6.3 Navigation et focus

```typescript
import { scrollIntoView } from '../../selectable';

// Scroll automatique lors du focus
<Container on:enter={scrollIntoView({ vertical: 64 })}>
<Container on:enter={scrollIntoView({ top: 9999 })}>
```

### 6.4 Événements clavier

```svelte
<svelte:window
    on:keydown={(e) => {
        lastKeyCode = e.keyCode;
        lastKey = e.key;
    }}
/>
```

---

## 7. API et appels réseau

### 7.1 Endpoints utilisés

```typescript
// Gestion utilisateurs
reiverrApi.getUsers()
reiverrApi.updateUser(id, { name, password, profilePicture, isAdmin })
reiverrApi.createUser({ name, password, isAdmin, profilePicture })

// Gestion utilisateur courant
user.updateUser((prev) => ({ ...prev, /* modifications */ }))

// Sources médias
reiverrApiNew.providers.getSourceProviders()
reiverrApiNew.users.findUserById(userId)
reiverrApiNew.providers.getSourceSettingsTemplate(pluginId)
reiverrApiNew.providers.validateSourceSettings(pluginId, { settings })
sources.updateSource(mediaSource)
sources.deleteSource(sourceId)
sources.addSource(pluginId)

// TMDB
tmdbApi.getAccountDetails()
tmdbApi.getConnectAccountLink()

// Cache
reiverrApiNew.metadata.clearCache()
```

### 7.2 Gestion des promesses

```svelte
{#await users then usersR}
    {#if usersR?.length}
        <!-- Contenu -->
    {/if}
{/await}

{#await tmdbAccount then tmdbAccount}
    {#if tmdbAccount}
        <!-- Connecté -->
    {:else}
        <!-- Non connecté -->
    {/if}
{/await}

{#await allProviders then availablePlugins}
    <Button disabled={!availablePlugins.length}>
        Add Source
    </Button>
{/await}
```

---

## 8. Styles et thème

### 8.1 Palette de couleurs

```
Backgrounds:
- bg-secondary-900: Fond principal de la page
- bg-secondary-800: Fond des éléments de menu actifs
- bg-primary-800: Fond des containers de section

Bordures:
- border-secondary-700: Bordures et séparateurs

Textes:
- text-secondary-100: Titres principaux
- text-secondary-200: Texte actif sans focus
- text-secondary-300: Texte secondaire
- text-secondary-400: Texte inactif
- text-primary-500: Texte avec focus

Accents:
- bg-green-500: Indicateur actif (enabled)
- bg-primary-500: Indicateur inactif
- hover:bg-red-500: Bouton de déconnexion (danger)
```

### 8.2 Classes typographiques personnalisées

```
.h1 - Titre de page principal
.h3 - Titre de section
.h4 - Sous-titre de section
.body - Texte de description
.error - Message d'erreur
```

### 8.3 Transitions

```typescript
// Dialog fade
transition:fade={{ duration: 100 }}

// Contrôlé par settings
$localSettings.useCssTransitions
$localSettings.animateScrolling
```

---

## 9. Accessibilité et navigation

### 9.1 Navigation au clavier (D-Pad)

Le système `Container` avec `Selectable` permet:
- Navigation directionnelle (haut, bas, gauche, droite)
- Gestion du focus
- Événements `enter`, `back`, `playPause`
- Support TV (Tizen)

### 9.2 Props de focus

```typescript
focusOnMount: boolean    // Focus automatique au montage
trapFocus: boolean       // Piège le focus dans le container
focusOnClick: boolean    // Focus au clic
focusedChild: boolean    // Enfant par défaut à focus
```

### 9.3 Support TV

```typescript
// Détection des touches média sur Tizen (TV Samsung)
// Code commenté dans le composant (lignes 45-58)
tizen?.tvinputdevice?.registerKey?.('MediaPlayPause')
tizen?.mediakey?.setMediaKeyEventListener?.(listener)
```

---

## 10. Gestion d'erreurs et notifications

### 10.1 Validation

```typescript
// Validation des sources médias
const validationResponse = await reiverrApiNew.providers
    .validateSourceSettings(pluginId, { settings })
    .then((r) => r.data);

if (validationResponse?.errors?.[key]) {
    // Afficher l'erreur
}
```

### 10.2 Notifications

```typescript
import {
    createErrorNotification,
    createInfoNotification
} from '$lib/components/Notifications/notification.store';

// Succès
createInfoNotification(
    'Media source updated',
    `${mediaSource.name} has been enabled`
);

// Erreur
createErrorNotification(
    'Incomplete configuration',
    `${mediaSource.name} has been disabled`
);
```

---

## 11. Points d'amélioration identifiés

### 11.1 Fautes de frappe

- Ligne 20: "Media Soruces" → "Media Sources" (2 occurrences)

### 11.2 Code commenté

- Intégrations Sonarr et Radarr commentées (lignes 216-236)
- Code Jellyfin commenté (lignes 287-304)
- Gestion TMDB alternative commentée (lignes 258-284)
- Support Tizen commenté (lignes 45-58)

### 11.3 Optimisations possibles

- Lazy loading des onglets non actifs
- Virtualisation de la liste utilisateurs si > 20
- Debounce sur les inputs de formulaire
- Cache des templates de settings plugins

---

## Résumé

La `ManagePage` est une interface de paramètres complète et bien structurée qui:
- Utilise un système d'onglets avec menu latéral
- Gère les comptes utilisateurs (profil, création, édition)
- Configure les sources médias via plugins
- Intègre des services tiers (TMDB)
- Offre des options d'interface personnalisables
- Fournit des informations de débogage
- Supporte la navigation au clavier et TV
- Utilise un système de composants réutilisables et modulaires

L'architecture Compose équivalente conserve la même structure mais s'appuie sur les patterns Android modernes (ViewModel, StateFlow, Compose UI).
