# Internationalization (i18n) Design

## Overview

Add multi-language support to JellyFish with community contribution capabilities.

## Requirements

- **Multi-language architecture** with easy community contributions
- **Automatic language detection** from system, fallback to English
- **Manual language selector** in Settings
- **Native Compose Resources XML format** (type-safe, well-integrated)

## Architecture

### File Structure

```
composeApp/src/commonMain/composeResources/
├── values/
│   └── strings.xml          ← Default language (English)
├── values-fr/
│   └── strings.xml          ← French
├── values-es/
│   └── strings.xml          ← Spanish (future)
└── values-de/
    └── strings.xml          ← German (future)
```

### String Resource Format

```xml
<!-- values/strings.xml (English - default) -->
<resources>
    <string name="app_name">JellyFish</string>
    <string name="action_play">Play</string>
    <string name="action_cancel">Cancel</string>
    <string name="login_button">Sign in</string>
</resources>
```

### Usage in Compose

```kotlin
import jellyfish.composeapp.generated.resources.*
import org.jetbrains.compose.resources.stringResource

Text(stringResource(Res.string.action_play))
```

## User Language Preference

### Storage

Preference stored in DataStore with key `user_language`:
- `"system"` (default) - follows system language
- `"en"`, `"fr"`, `"es"`, etc. - forced language

### Locale Provider

```kotlin
// LocaleProvider.kt
@Composable
fun LocaleProvider(
    userLanguage: String,
    content: @Composable () -> Unit
) {
    val locale = if (userLanguage == "system") {
        getSystemLocale()
    } else {
        Locale(userLanguage)
    }

    CompositionLocalProvider(
        LocalLocale provides locale
    ) {
        content()
    }
}
```

### Platform Implementation

`expect/actual` for `getSystemLocale()`:
- Android: `Locale.getDefault()`
- iOS: `NSLocale.currentLocale`
- Desktop: `Locale.getDefault()`

## Settings UI

### Language Selector

```kotlin
enum class AppLanguage(val code: String, val nativeName: String) {
    SYSTEM("system", "System"),
    ENGLISH("en", "English"),
    FRENCH("fr", "Français"),
}
```

Features:
- Shows current language with native name
- Dialog with available languages list
- "System" option shows detected language
- Instant change (no restart required)

## String Key Conventions

### Naming Pattern

```
[screen]_[component]_[action/description]
```

### Categories

| Category | Examples | Count |
|----------|----------|-------|
| `common_*` | Cancel, Delete, Play, Confirm | ~10 |
| `login_*` | Sign in, Quick Connect | ~5 |
| `player_*` | Resume, Audio & Subtitles | ~10 |
| `settings_*` | Logout, Language, Clear downloads | ~15 |
| `server_*` | Add server, Delete server | ~5 |
| `downloads_*` | Clear all downloads | ~5 |
| `library_*` | Section titles | ~5 |

**Total: ~55 strings to extract**

## Contributor Documentation

A `CONTRIBUTING_TRANSLATIONS.md` file will guide contributors:

1. **Adding a new language**
   - Create `composeResources/values-XX/strings.xml`
   - Copy content from `values/strings.xml`
   - Translate values, keep keys intact
   - Add entry in `AppLanguage` enum

2. **Translation rules**
   - Keep placeholders `%s`, `%d` in same order
   - Respect approximate length (UI constraints)
   - Follow language conventions (formal/informal)

3. **Testing**
   - Change language in Settings
   - Verify main screens

## Implementation Steps

1. Create `values/strings.xml` with all English strings
2. Create `values-fr/strings.xml` with French translations
3. Implement `LocaleProvider` with `expect/actual` for system locale
4. Add language preference to DataStore/SettingsRepository
5. Add language selector to SettingsScreen
6. Replace all hardcoded strings with `stringResource()` calls
7. Create `CONTRIBUTING_TRANSLATIONS.md`
