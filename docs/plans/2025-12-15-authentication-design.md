# Design: Authentification Multi-Serveurs Jellyfin

**Date**: 2025-12-15
**Statut**: Validé
**Plateformes cibles**: Android, Desktop (JVM)

## Objectif

Implémenter l'authentification multi-serveurs Jellyfin avec support login classique et Quick Connect, suivi d'un écran Hello World post-connexion.

## Stack Technique

| Composant | Choix |
|-----------|-------|
| Navigation | Voyager |
| Architecture | Clean Architecture (presentation/domain/data) |
| DI | Koin |
| UI | Compose Multiplatform + Material3 Window Size Classes |
| Stockage sécurisé | Android Keystore / Desktop credentials store |
| Stockage serveurs | DataStore Preferences |

## Flux Utilisateur

```
Lancement → CheckSession
         → Si token valide → Home
         → Sinon → ServerList (vide au départ)
                 → Ajouter serveur → Saisir URL
                 → Choix : Login classique OU Quick Connect
                 → Authentification réussie → Home
                 → Switch serveur possible depuis la home
```

## Écrans

1. **ServerListScreen** - Liste des serveurs configurés + bouton ajouter
2. **AddServerScreen** - Saisie URL du serveur Jellyfin
3. **LoginScreen** - Login/Password + option Quick Connect
4. **QuickConnectScreen** - Affiche le code, attend validation
5. **HomeScreen** - Simple "Hello {username}" pour l'instant

## Architecture & Structure des Packages

```
com.lowiq.jellyfish/
├── di/                          # Modules Koin
│   ├── AppModule.kt
│   ├── DataModule.kt
│   └── DomainModule.kt
│
├── data/
│   ├── repository/              # Implémentations Repository
│   │   ├── AuthRepositoryImpl.kt
│   │   └── ServerRepositoryImpl.kt
│   ├── local/                   # Stockage local
│   │   ├── SecureStorage.kt     # expect/actual pour tokens
│   │   └── ServerStorage.kt     # Liste des serveurs (DataStore)
│   └── remote/                  # Sources API
│       └── JellyfinDataSource.kt
│
├── domain/
│   ├── model/                   # Entités métier
│   │   ├── Server.kt
│   │   └── User.kt
│   ├── repository/              # Interfaces
│   │   ├── AuthRepository.kt
│   │   └── ServerRepository.kt
│   └── usecase/
│       ├── LoginUseCase.kt
│       ├── QuickConnectUseCase.kt
│       ├── GetServersUseCase.kt
│       └── CheckSessionUseCase.kt
│
└── presentation/
    ├── navigation/              # Config Voyager
    │   └── AppNavigator.kt
    ├── theme/                   # Material3 theme
    └── screens/
        ├── serverlist/
        │   ├── ServerListScreen.kt
        │   ├── ServerListViewModel.kt
        │   └── ServerListState.kt
        ├── addserver/
        ├── login/
        ├── quickconnect/
        └── home/
```

## Modèles

### Entités Domain

```kotlin
// domain/model/Server.kt
data class Server(
    val id: String,           // UUID généré localement
    val name: String,         // Nom du serveur Jellyfin
    val url: String,          // URL de base
    val userId: String?,      // null si pas encore connecté
    val username: String?     // Pour affichage
)

// domain/model/User.kt
data class User(
    val id: String,
    val name: String,
    val serverId: String
)
```

### UI States

```kotlin
// Login
sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    data class Success(val user: User) : LoginState()
    data class Error(val message: String) : LoginState()
}

// Quick Connect
sealed class QuickConnectState {
    object Idle : QuickConnectState()
    data class WaitingForAuth(val code: String) : QuickConnectState()
    object Polling : QuickConnectState()
    data class Success(val user: User) : QuickConnectState()
    data class Error(val message: String) : QuickConnectState()
}
```

## Interfaces Repository

```kotlin
// domain/repository/AuthRepository.kt
interface AuthRepository {
    suspend fun login(serverUrl: String, username: String, password: String): Result<User>
    suspend fun initiateQuickConnect(serverUrl: String): Result<String>
    suspend fun pollQuickConnect(serverUrl: String, secret: String): Result<User?>
    suspend fun logout(serverId: String)
    suspend fun getCurrentUser(serverId: String): User?
}

// domain/repository/ServerRepository.kt
interface ServerRepository {
    fun getServers(): Flow<List<Server>>
    suspend fun addServer(url: String): Result<Server>
    suspend fun removeServer(serverId: String)
    suspend fun setActiveServer(serverId: String)
    fun getActiveServer(): Flow<Server?>
}
```

## Stockage Sécurisé (expect/actual)

```kotlin
// commonMain
expect class SecureStorage {
    suspend fun saveToken(serverId: String, token: String)
    suspend fun getToken(serverId: String): String?
    suspend fun deleteToken(serverId: String)
    suspend fun isBiometricAvailable(): Boolean
    suspend fun authenticateWithBiometric(): Boolean
}
```

## Navigation

```
┌─────────────────────────────────────────────────────────┐
│  App Launch                                             │
│      │                                                  │
│      ▼                                                  │
│  CheckSession ──── token valide ────► Home              │
│      │                                                  │
│      │ pas de token/session                             │
│      ▼                                                  │
│  ServerList ◄─────────────────────────────────┐         │
│      │                                        │         │
│      │ "+" ajouter                            │         │
│      ▼                                        │         │
│  AddServer ── URL valide ──► Login            │         │
│                                │              │         │
│                 ┌──────────────┴──────┐       │         │
│                 ▼                     ▼       │         │
│            LoginForm            QuickConnect │         │
│                 │                     │       │         │
│                 └──────────┬──────────┘       │         │
│                            │ succès           │         │
│                            ▼                  │         │
│                          Home ── logout ──────┘         │
└─────────────────────────────────────────────────────────┘
```

**Gestion du back stack**:
- Après login réussi → `replaceAll(Home)` pour vider le stack
- Depuis Home, switch serveur → `push(ServerList)` puis après sélection → `replaceAll(Home)`

## Dépendances à Ajouter

### libs.versions.toml

```toml
[versions]
voyager = "1.1.0-beta03"
koin = "4.0.0"
datastore = "1.1.1"
androidx-security = "1.1.0-alpha06"

[libraries]
voyager-navigator = { module = "cafe.adriel.voyager:voyager-navigator", version.ref = "voyager" }
voyager-screenmodel = { module = "cafe.adriel.voyager:voyager-screenmodel", version.ref = "voyager" }
voyager-koin = { module = "cafe.adriel.voyager:voyager-koin", version.ref = "voyager" }

koin-core = { module = "io.insert-koin:koin-core", version.ref = "koin" }
koin-compose = { module = "io.insert-koin:koin-compose", version.ref = "koin" }

datastore-preferences = { module = "androidx.datastore:datastore-preferences-core", version.ref = "datastore" }
androidx-security-crypto = { module = "androidx.security:security-crypto", version.ref = "androidx-security" }
```

### Répartition par sourceSet

| Librairie | commonMain | androidMain | jvmMain |
|-----------|------------|-------------|---------|
| Voyager | X | - | - |
| Koin | X | - | - |
| DataStore | X | - | - |
| Security Crypto | - | X | - |
| Jellyfin SDK | - | X | X |

## Phases d'Implémentation

### Phase 1 - Foundation
1. Ajouter les dépendances (Voyager, Koin, DataStore)
2. Setup Koin dans l'app
3. Créer la structure de packages
4. Setup le thème Material3 + Window Size Classes

### Phase 2 - Data Layer
5. `SecureStorage` expect/actual (Android Keystore, Desktop credentials)
6. `ServerStorage` avec DataStore
7. `JellyfinDataSource` wrapper autour du SDK
8. Implémentations des Repositories

### Phase 3 - Domain Layer
9. Modèles (Server, User)
10. Use Cases

### Phase 4 - Presentation
11. Navigation Voyager setup
12. `ServerListScreen` + ViewModel
13. `AddServerScreen` + ViewModel
14. `LoginScreen` + ViewModel
15. `QuickConnectScreen` + ViewModel
16. `HomeScreen` (Hello World)

### Phase 5 - Polish
17. Biométrie au lancement
18. Gestion d'erreurs propre
19. Tests

## Notes

- **iOS** : Sera géré dans une phase ultérieure (SDK iOS existe)
- **Jellyseer** : Configuration via Settings, hors scope de ce design
- **UI Responsive** : Window Size Classes pour adaptation mobile/desktop
