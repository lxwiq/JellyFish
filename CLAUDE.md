# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

JellyFish is a Kotlin Multiplatform (KMP) project using Compose Multiplatform for UI, targeting Android, iOS, and Desktop (JVM).

## Build Commands

```bash
# Android
./gradlew :composeApp:assembleDebug

# Desktop (JVM) - builds and runs
./gradlew :composeApp:run

# Run all common tests
./gradlew :composeApp:allTests

# Run specific platform tests
./gradlew :composeApp:testDebugUnitTest      # Android
./gradlew :composeApp:jvmTest                 # JVM/Desktop
```

For iOS, open `iosApp/` in Xcode and run from there.

## Architecture

### Source Set Structure

All shared code lives in `composeApp/src/`:

- **commonMain** - Cross-platform code (UI, business logic). This is where most development happens.
- **androidMain** - Android-specific implementations and the `MainActivity` entry point
- **iosMain** - iOS-specific implementations and `MainViewController` for SwiftUI bridge
- **jvmMain** - Desktop-specific implementations and `main()` entry point
- **commonTest** - Shared tests using `kotlin.test`

### Platform Abstraction Pattern

The project uses Kotlin's `expect`/`actual` mechanism for platform-specific code:

- `Platform.kt` in commonMain declares `expect fun getPlatform(): Platform`
- Each platform source set provides its `actual` implementation (e.g., `Platform.android.kt`, `Platform.ios.kt`, `Platform.jvm.kt`)

### Key Dependencies

- Compose Multiplatform 1.9.3 with Material3
- AndroidX Lifecycle (ViewModel & runtime-compose)
- Compose Hot Reload plugin enabled for faster iteration

### Package Structure

All code uses the `com.lowiq.jellyfish` package namespace.
