# Prochaines fonctionnalités à implémenter

## Priorité 1 : Écran de recherche (SearchScreen)

**Statut:** Non implémenté
**Navigation:** Index 1 dans NavigationRail (icône Search)

### Fonctionnalités requises

- Barre de recherche avec debounce (300ms)
- Recherche globale sur tous les types de médias (films, séries, épisodes, musique)
- Filtres par type de contenu
- Affichage des résultats en grille/liste
- Historique de recherche récent (local)
- Suggestions pendant la frappe (optionnel)

### API Jellyfin à utiliser

```kotlin
// Dans JellyfinDataSource
suspend fun search(
    serverUrl: String,
    token: String,
    userId: String,
    query: String,
    limit: Int = 20,
    includeItemTypes: List<BaseItemKind>? = null
): Result<List<MediaItem>>
```

### Fichiers à créer

- `presentation/screens/search/SearchScreen.kt`
- `presentation/screens/search/SearchScreenModel.kt`
- `presentation/screens/search/SearchState.kt`

### Estimation

~4-6 tâches atomiques

---

## Priorité 2 : Écran des favoris (FavoritesScreen)

**Statut:** Partiellement implémenté (carousel sur Home uniquement)
**Navigation:** Index 2 dans NavigationRail (icône Star)

### Fonctionnalités requises

- Liste complète des favoris avec pagination
- Tri par date d'ajout, titre, type
- Filtres par type de média
- Action pour retirer des favoris (swipe ou long press)
- Affichage poster/liste toggle

---

## Priorité 3 : Écran des paramètres (SettingsScreen)

**Statut:** Non implémenté
**Navigation:** Index 4 dans NavigationRail (icône Settings)

### Sections requises

- **Compte:** Utilisateur connecté, changer de serveur, déconnexion
- **Lecture:** Qualité par défaut (streaming/téléchargement), langue audio/sous-titres préférée
- **Stockage:** Espace utilisé par les téléchargements, limite de stockage, vider le cache
- **Apparence:** Thème (sombre/clair/système)
- **À propos:** Version de l'app, licences

---

## Priorité 4 : Lecteur audio dédié

**Statut:** Non implémenté
**Contexte:** Les albums sont affichés mais utilisent le lecteur vidéo

### Fonctionnalités requises

- Mini-player persistant en bas de l'écran
- Écran de lecture complet avec artwork
- Queue de lecture
- Contrôles: lecture/pause, suivant/précédent, shuffle, repeat
- Background playback (notification media controls)

---

## Navigation actuelle vs implémentée

| Index | Icône | Écran | Statut |
|-------|-------|-------|--------|
| 0 | Home | HomeScreen | ✅ Implémenté |
| 1 | Search | SearchScreen | ❌ À faire |
| 2 | Star | FavoritesScreen | ❌ À faire |
| 3 | Download | DownloadsScreen | ✅ Implémenté |
| 4 | Settings | SettingsScreen | ❌ À faire |
