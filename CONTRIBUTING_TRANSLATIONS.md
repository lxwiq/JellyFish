# Contributing Translations to JellyFish

Thank you for your interest in translating JellyFish! This guide explains how to add support for new languages.

## Overview

JellyFish uses Compose Multiplatform Resources for internationalization (i18n). Translations are stored in XML files following Android resource conventions.

**Currently supported languages:**
- English (default)
- French

## How to Add a New Language

### Step 1: Create the Strings File

Create a new directory and strings file for your language:

```
composeApp/src/commonMain/composeResources/values-XX/strings.xml
```

Replace `XX` with the [ISO 639-1 language code](https://en.wikipedia.org/wiki/List_of_ISO_639-1_codes) for your language (e.g., `es` for Spanish, `de` for German, `ja` for Japanese).

### Step 2: Copy the English Strings

Copy the content from the English strings file:

```
composeApp/src/commonMain/composeResources/values/strings.xml
```

### Step 3: Translate the Values

Translate all string values while keeping the keys (name attributes) unchanged.

**Example:**
```xml
<!-- English (original) -->
<string name="common_action_cancel">Cancel</string>

<!-- Spanish (translated) -->
<string name="common_action_cancel">Cancelar</string>
```

### Step 4: Register the Language

Add an entry for your language in the `AppLanguage` enum:

**File:** `composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/presentation/locale/AppLanguage.kt`

```kotlin
enum class AppLanguage(val code: String, val nativeName: String) {
    SYSTEM("system", "System"),
    ENGLISH("en", "English"),
    FRENCH("fr", "Francais"),
    SPANISH("es", "Espanol");  // Add your language here

    companion object {
        fun fromCode(code: String): AppLanguage =
            entries.find { it.code == code } ?: SYSTEM
    }
}
```

Use the native name of the language (e.g., "Deutsch" for German, not "German").

## Translation Rules

### Placeholders

Keep placeholders in the same order as the original:

- `%s` - String placeholder
- `%d` - Integer placeholder
- `%1$s`, `%2$s` - Positional placeholders

**Example:**
```xml
<!-- English -->
<string name="episode_info">Season %1$d, Episode %2$d</string>

<!-- French -->
<string name="episode_info">Saison %1$d, Episode %2$d</string>
```

### Text Length

Try to keep translations approximately the same length as the original. Very long translations may cause UI layout issues.

### Consistency

- Choose either formal or informal tone and use it consistently throughout
- If the source uses "you" informally, maintain that style in your translation

### App Name

Do not translate the app name "JellyFish". Keep it as-is in all languages.

## File Format Example

```xml
<?xml version="1.0" encoding="UTF-8"?>
<resources>
    <string name="app_name">JellyFish</string>
    <string name="common_action_cancel">Cancelar</string>
    <string name="common_action_ok">Aceptar</string>
    <string name="common_action_save">Guardar</string>
    <string name="settings_title">Configuracion</string>
    <!-- Add all other strings... -->
</resources>
```

## Testing Your Translation

1. **Build and run the app:**
   ```bash
   # Desktop
   ./gradlew :composeApp:run

   # Android
   ./gradlew :composeApp:assembleDebug
   ```

2. **Change the language:** Go to Settings and select your language

3. **Verify translations:** Navigate through all screens to ensure:
   - All text displays correctly
   - No strings are missing (falling back to English)
   - Text fits properly in UI elements
   - Special characters render correctly

## Submitting Your Translation

1. **Fork the repository** on GitHub

2. **Create a branch** for your translation:
   ```bash
   git checkout -b translations/XX
   ```
   Replace `XX` with your language code (e.g., `translations/es` for Spanish)

3. **Commit your changes:**
   ```bash
   git add .
   git commit -m "Add XX language translation"
   ```

4. **Push and create a Pull Request** targeting the `main` branch

5. **In your PR description**, include:
   - The language you added
   - Whether it's a complete or partial translation
   - Any strings you were unsure about

## Questions?

If you have questions about translations or need clarification on any strings, feel free to open an issue on GitHub.
