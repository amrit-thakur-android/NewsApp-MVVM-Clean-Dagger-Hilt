# NewsApp – MVVM + Clean Architecture + Dagger Hilt (Jetpack Compose)

A modular Android app that consumes the NewsAPI.org API to display news. Built with Clean
Architecture, MVVM, and Jetpack Compose with Dagger Hilt for dependency injection.

## Modules

- app: Android entry point, DI setup, app-level configs
- domain: Pure Kotlin business logic (entities, use cases, repository interfaces)
- data: Repository implementations, Retrofit API, DTOs, paging, and utilities
- presentation: Jetpack Compose UI, ViewModels, navigation

## Tech stack

- Language: Kotlin
- UI: Jetpack Compose, Material 3
- Architecture: Clean Architecture + MVVM
- DI: Dagger Hilt
- Concurrency: Kotlin Coroutines + Flow
- Networking: Retrofit, OkHttp, Moshi (code-gen)
- Pagination: Paging 3 (runtime + compose)
- Navigation: Navigation-Compose, Hilt Navigation Compose
- Images: Coil 3 (OkHttp-backed)
- AndroidX: Lifecycle (ViewModel/Runtime), Core KTX
- Testing: JUnit4, MockK, Coroutines Test

## Features

- Top headlines list with infinite scroll (Paging 3)
- Search news
- Browse by sources
- Filter by country and language
- Open article in browser Custom Tabs

## Architecture overview

- Domain (pure Kotlin): entities, use cases, repository interfaces. No Android deps.
- Data: Retrofit service (`NewsApiService`), repository implementations, paging source, connectivity
  checks, Moshi DTOs.
- Presentation: Compose screens, state holders (ViewModels), Hilt ViewModel injection, Navigation.
- App: Hilt `@HiltAndroidApp` application, DI modules (e.g., `NetworkModule`) wiring
  Retrofit/OkHttp/Moshi.

Networking:

- Base URL: https://newsapi.org/
- OkHttp adds header `X-Api-Key` from `BuildConfig.NEWS_API_KEY` (see
  `app/src/main/java/.../di/NetworkModule.kt`).

## Setup

1) Prerequisites
    - Android Studio (latest stable)
    - JDK 17 (required by AGP 8.x)
    - Android SDK 24+ (compile/target SDK 36)

2) NewsAPI key
    - Get a free API key from https://newsapi.org
    - Update the placeholder in `app/build.gradle.kts`:
        - `buildConfigField("String", "NEWS_API_KEY", "\"YOUR_KEY\"")`
    - Rebuild/sync after changing the key

3) Run
    - Open the project in Android Studio
    - Sync Gradle and select a device
    - Run the `app` configuration

4) CLI

- Build debug: `./gradlew :app:assembleDebug`
- Run unit tests: `./gradlew test`

## Module boundaries

- presentation depends on domain (UI + ViewModels only)
- data depends on domain (implements repositories)
- app depends on domain, data, presentation (wires DI + app configs)

## Notes

- For localization, only English resources are packaged in this sample for smaller APK size.

