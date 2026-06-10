# Binge Diary

Binge Diary is a mobile application used to track movies and TV shows that users have watched, are currently watching or planned to watch
Users can also rate the movies and TV shows, write reviews, and share their thoughts with friends.

## Technologies
* Kotlin Multiplatform (KMP) for shared business logic across platforms.
* Jetpack Compose for building the user interface.
* Ktor for networking and API communication.
* Koin (with Koin Compiler) for dependency injection.
* MVI (Model-View-Intent) architecture for UI state management.
* Navigation 3 for application navigation.
* Amper (Kotlin Toolchain) as the build system: https://kotlin-toolchain.org/latest/
* The Movie Database (TMDB) API for fetching movie and TV show data: https://www.themoviedb.org/documentation/api
* Supabase Authentication for user authentication and account management.
* Supabase Database for storing user-generated data such as reviews, comments, ratings, watchlists, and other application data.


## Project Structure

### android-app

The entry point for the Android application.

### ios-app

The entry point for the iOS application.

### shared

Contains business logic, UI implementation, networking, and dependency injection.

#### data

Responsible for data retrieval and management.

Contents:

* Remote Data Sources (TMDB, Supabase, etc.)
* DTOs and response models
* Repository implementations

#### presentation

Responsible for the UI layer and follows a strict MVI architecture.

Contents:

* Screens
* ViewModels
* State classes
* Effects
* UI models
* Mappers
* Reusable UI components
* Navigation setup

Rules:

* UI must only consume UI models.
* Mapping from DTOs to UI models should be performed through dedicated mapper functions.
* Shared UI components should be placed under `presentation/common/components`.
* Shared UI models should be placed under `presentation/common/model`.
* Shared mappers should be placed under `presentation/common/mapper`.

Naming Conventions

**Screens** - Use the suffix `Screen`. - Examples: - `HomeScreen` - `MediaDetailsScreen` - `SearchScreen`

**ViewModels** - Use the suffix `ViewModel`. - Examples: - `HomeViewModel` - `MediaDetailsViewModel`

**State Classes** - Use the suffix `UiState`. - Examples: - `HomeState` - `MediaDetailsState`

**Effects** - Use the suffix `Effect`. - Examples: - `HomeEffect` - `SearchEffect`

**UI Models** - Use the suffix `UiModel` for feature-specific models. - Examples: - `MovieUiModel` - `ShowUiModel`.

**Mappers** - Use extension functions with explicit conversion names. - Examples: - `MovieDto.toMovieUiModel()` - `ShowDto.toShowUiModel()`

#### core

Contains shared infrastructure and utilities used across the application.

Contents:

* Network configuration
* HttpClient setup
* Constants
* Extension functions
* Image URL utilities
* Common helper functions

Rules:

* Core must not contain feature-specific business logic.
* Core must not contain UI code.
* Shared utilities such as image URL generation and formatting extensions should be placed here.

#### di

Contains all Koin dependency injection modules and dependency registrations.

### Platform-Specific Code

Platform-specific implementations should be placed inside using actual/expect.

```text
src@android/
src@ios/
```

### build-config

* This module contains the custom build configuration plugin used by the project.
* Build configuration values are generated at build time and exposed through the generated `BuildConfig` class.
* The application supports two build variants:

  * `debug`
  * `release`
* Debug-specific configuration values are stored in:

```text
shared/config-debug.properties
```

* Release-specific configuration values are stored in:

```text
shared/config-release.properties
```

* Sensitive values such as API keys, project URLs, and environment-specific configuration should be stored in the appropriate configuration file and accessed through `BuildConfig`.
* Never hardcode secrets, API keys, or environment-specific values directly in source code.


## AI Guidelines.

* Follow a strict MVI (Model-View-Intent) architecture for all features.
* Each feature should contain its own:
  * Screen
  * ViewModel
  * State
  * Effect
* Keep business logic inside ViewModels and repositories.
* UI should be driven entirely by state.
* When implementing a new feature, match the structure and patterns used by existing features.
* Prefer importing classes and functions directly; avoid using fully qualified package names within the code unless required to resolve naming conflicts.
* If the data is getting passed form one screen to another screen pass it to viewmodel using Injected parms https://insert-koin.io/docs/reference/koin-android/viewmodel/
* Don't write unnecessary comments .
* Always keep consistent padding across the app.

### UI

* Use Material 3 Expressive components and design patterns https://m3.material.io/blog/building-with-m3-expressive.
* Do not use `Card` components unless explicitly required by the design.
* Extract reusable UI elements if they can be used in different screen into `presentation/common/components`.
* Prefer creating reusable composables instead of duplicating UI code.



