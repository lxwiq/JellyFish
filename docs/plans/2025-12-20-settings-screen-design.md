# SettingsScreen Design

## Overview

Implement a comprehensive settings screen for JellyFish following the shadcn theme. Includes user account management, playback preferences, storage management, app info, and admin features (for admin users only).

## Sections

### 1. Compte (Account)
- Display connected user name and server
- Switch server button (navigate to server selection)
- Logout button with confirmation

### 2. Lecture (Playback)
- Streaming quality preference (Auto, 1080p, 720p, 480p)
- Download quality preference (existing in DownloadSettingsStorage)
- Preferred audio language
- Preferred subtitle language

### 3. Stockage (Storage)
- Display space used by downloads
- Storage limit setting
- Clear cache button

### 4. À propos (About)
- App version
- Open source licenses

### 5. Administration (Admin only)
Visible only if `User.isAdmin == true`

- **Users**: List users, create/delete users
- **Libraries**: Refresh library scan, view scan status
- **Logs**: View recent server logs
- **Tasks**: View/run scheduled tasks

## Architecture

```
SettingsScreen (Voyager Screen)
    └── SettingsScreenModel
            ├── UserPreferencesStorage (playback preferences)
            ├── DownloadSettingsStorage (existing)
            ├── DownloadRepository (storage info)
            ├── AuthRepository (logout, user info)
            ├── ServerRepository (server info)
            └── AdminRepository (new - admin API calls)
```

## Files to Create

- `presentation/screens/settings/SettingsScreen.kt`
- `presentation/screens/settings/SettingsScreenModel.kt`
- `presentation/screens/settings/SettingsState.kt`
- `domain/repository/AdminRepository.kt`
- `data/repository/AdminRepositoryImpl.kt`

## Files to Modify

- `domain/model/User.kt` - add `isAdmin: Boolean`
- `data/local/UserPreferencesStorage.kt` - add streaming/language prefs
- `data/remote/JellyfinDataSource.kt` - add admin API methods
- `androidMain/.../JellyfinDataSourceImpl.kt` - implement admin API
- `jvmMain/.../JellyfinDataSourceImpl.kt` - implement admin API
- `presentation/screens/home/HomeScreenModel.kt` - add NavigateToSettings event
- `presentation/screens/home/HomeScreen.kt` - handle settings navigation
- `di/AppModule.kt` - register new components

## Data Flow

### User Preferences
1. UserPreferencesStorage stores in DataStore
2. SettingsScreenModel observes and updates
3. VideoPlayerScreenModel reads preferences for playback

### Admin Features
1. Check `User.isAdmin` on screen load
2. If admin, show Administration section
3. API calls through AdminRepository → JellyfinDataSource

## API (Jellyfin SDK)

```kotlin
// User admin check
userApi.getCurrentUser() → user.policy?.isAdministrator

// Admin: Users
userApi.getUsers()
userApi.createUserByName(username, password)
userApi.deleteUser(userId)

// Admin: Libraries
libraryApi.refreshLibrary(itemId)
libraryApi.getPhysicalPaths()

// Admin: Logs
systemApi.getLogEntries(limit)

// Admin: Tasks
scheduledTasksApi.getScheduledTasks()
scheduledTasksApi.startTask(taskId)
```

## Navigation

- Index 4 in NavigationRail
- HomeEvent.NavigateToSettings added
- HomeScreen pushes SettingsScreen on event
