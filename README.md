<div align="center">
  <img src="app-icon.png" alt="JellyFish Logo" width="128" height="128">

  # JellyFish

  A modern, cross-platform Jellyfin client built with Kotlin Multiplatform and Compose Multiplatform.

  [![Kotlin](https://img.shields.io/badge/Kotlin-2.1.0-purple.svg)](https://kotlinlang.org)
  [![Compose Multiplatform](https://img.shields.io/badge/Compose-1.7.3-blue.svg)](https://www.jetbrains.com/compose-multiplatform/)
  [![License](https://img.shields.io/badge/License-GPL--3.0-green.svg)](LICENSE)

  **Android** · **iOS** · **Desktop**
</div>

## Features

### Media Browsing
- Browse your complete Jellyfin library (movies, TV shows, music)
- Continue watching with resume support
- Search with history
- Filter and sort by genre, year, rating

### Video Playback
- Native playback with LibVLC (Android) and platform-specific players
- Quality selection (1080p, 720p, 480p, direct play)
- Audio and subtitle track switching
- Playback speed control
- Progress sync with Jellyfin server

### Offline Support
- Download movies and episodes for offline viewing
- Quality selection for downloads
- Storage management with configurable limits
- Resume offline playback

### Customization
- Dark theme with Material 3 design
- Multi-language support (English, French)
- Streaming and download quality preferences

### Administration
- User management (create, delete users)
- Library refresh
- Server logs access

### Coming Soon
- Jellyseerr integration for media requests
- Music player with queue
- Chromecast support

## Installation

### Android
Download the latest APK from the [Releases](https://github.com/lxwiq/JellyFish/releases) page.

### iOS
Coming soon on TestFlight.

### Desktop
Download the appropriate installer for your platform from the [Releases](https://github.com/lxwiq/JellyFish/releases) page:
- **Windows**: `.msi` installer
- **macOS**: `.dmg` image
- **Linux**: `.deb` package

## Building from Source

### Requirements
- JDK 17+
- Android SDK (for Android builds)
- Xcode 15+ (for iOS builds, macOS only)

### Android
```bash
./gradlew :composeApp:assembleDebug
```

### Desktop
```bash
./gradlew :composeApp:run
```

### iOS
Open the `iosApp/` directory in Xcode and run from there.

## Roadmap

- [ ] Jellyseerr integration (request movies & TV shows)
- [ ] Music player with queue and background playback
- [ ] Chromecast / AirPlay support
- [ ] Light theme
- [ ] Material You dynamic colors (Android)
- [ ] Full iOS player implementation

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

For translations, see [CONTRIBUTING_TRANSLATIONS.md](CONTRIBUTING_TRANSLATIONS.md).

## License

This project is licensed under the GPL-3.0 License - see the [LICENSE](LICENSE) file for details.

## Acknowledgments

- [Jellyfin](https://jellyfin.org/) - The free software media system
- [Kotlin Multiplatform](https://kotlinlang.org/docs/multiplatform.html)
- [Compose Multiplatform](https://www.jetbrains.com/compose-multiplatform/)
