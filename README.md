<p align="center">
  <img src="https://img.shields.io/badge/Kotlin-2.1.20-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white" alt="Kotlin"/>
  <img src="https://img.shields.io/badge/Compose_Multiplatform-1.8.0-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white" alt="Compose"/>
  <img src="https://img.shields.io/badge/Platform-Android_|_iOS-green?style=for-the-badge" alt="Platform"/>
</p>

<h1 align="center">
  🎵 TrackShift
</h1>

<p align="center">
  <strong>Transfer your playlists between streaming platforms.</strong><br/>
  Built with Kotlin Multiplatform & Compose Multiplatform.
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Architecture-Clean_Architecture-blue?style=flat-square" alt="Architecture"/>
  <img src="https://img.shields.io/badge/DI-Koin-orange?style=flat-square" alt="DI"/>
  <img src="https://img.shields.io/badge/Auth-Supabase-3ECF8E?style=flat-square" alt="Auth"/>
  <img src="https://img.shields.io/badge/Tests-Mokkery_+_AssertK-red?style=flat-square" alt="Tests"/>
</p>

---

## ✨ Features

- 🔐 **OAuth Authentication** — Sign in with Google, Apple, or Discord
- 🔄 **Playlist Transfer** — Move playlists between Spotify, Apple Music, YouTube Music
- 📱 **Cross-Platform** — Native Android & iOS from single codebase
- 🎨 **Material 3** — Modern, adaptive UI with dark mode support

---

## 🏗️ Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                        composeApp                           │
│                    (Android & iOS Entry)                    │
└─────────────────────────────────────────────────────────────┘
                              │
        ┌─────────────────────┼─────────────────────┐
        ▼                     ▼                     ▼
┌───────────────┐   ┌───────────────┐   ┌───────────────┐
│ feature-auth  │   │feature-onboard│   │  feature-home │
│   (Screen)    │   │   (Screen)    │   │   (Screen)    │
└───────────────┘   └───────────────┘   └───────────────┘
        │                     │                     │
        └─────────────────────┼─────────────────────┘
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                      domain layer                           │
│              (UseCases, Repositories, Entities)             │
│         ┌──────────────┐    ┌──────────────────┐            │
│         │  domain:auth │    │ domain:local-    │            │
│         │              │    │      storage     │            │
│         └──────────────┘    └──────────────────┘            │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                     services layer                          │
│                (Platform APIs, External SDKs)               │
│       ┌────────────────┐    ┌────────────────┐              │
│       │services:supabase│   │services:storage│              │
│       │  (Auth API)    │    │ (Preferences)  │              │
│       └────────────────┘    └────────────────┘              │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                       core layer                            │
│              (Shared Infrastructure & Utils)                │
│   ┌─────────────┐  ┌────────────┐  ┌────────────┐           │
│   │design-system│  │ navigation │  │  network   │           │
│   └─────────────┘  └────────────┘  └────────────┘           │
└─────────────────────────────────────────────────────────────┘
```

---

## 📦 Module Structure

| Layer | Module | Description |
|-------|--------|-------------|
| **App** | `composeApp` | Entry point, DI setup, navigation host |
| **Feature** | `feature-auth` | Authentication screens & ViewModel |
| | `feature-onboarding` | Onboarding flow |
| | `feature-home` | Main app experience |
| | `feature-splash-screen` | Launch screen |
| **Domain** | `domain:auth` | Auth business logic |
| | `domain:local-storage` | Local preferences logic |
| **Services** | `services:supabase` | Supabase auth client |
| | `services:storage` | Platform storage (SharedPrefs/NSUserDefaults) |
| **Core** | `core:design-system` | Theme, colors, typography |
| | `core:navigation` | Navigation routes |
| | `core:network` | HTTP client config |
| | `core:utils` | Platform utilities |

---

## 🛠️ Tech Stack

| Category | Technology |
|----------|------------|
| **Language** | Kotlin 2.1.20 |
| **UI** | Compose Multiplatform 1.8.0 |
| **Architecture** | Clean Architecture + MVVM |
| **DI** | Koin 4.0 |
| **Networking** | Ktor |
| **Auth** | Supabase Auth |
| **Async** | Coroutines + Flow |
| **Testing** | Mokkery, AssertK, Turbine |
| **Build** | Gradle Convention Plugins |

---

## 🚀 Getting Started

### Prerequisites

- Android Studio Ladybug or later
- Xcode 15+ (for iOS)
- JDK 17+

### Setup

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/TrackShift.git
   cd TrackShift
   ```

2. **Configure Supabase**

   Create `local.properties` in root:
   ```properties
   SUPABASE_URL=your_supabase_url
   SUPABASE_KEY=your_supabase_anon_key
   ```

3. **Run Android**
   ```bash
   ./gradlew :composeApp:installDebug
   ```

4. **Run iOS**
   ```bash
   open iosApp/iosApp.xcodeproj
   ```
   Then run from Xcode.

---

## 🧪 Testing

```bash
# Run all tests
./gradlew allTests

# Run specific module tests
./gradlew :domain:auth:allTests
./gradlew :feature-auth:allTests
./gradlew :composeApp:allTests
```

### Test Coverage

| Module | Tests |
|--------|-------|
| `domain:auth` | UseCases + Repository |
| `domain:local-storage` | UseCases + Repository |
| `feature-auth` | ViewModel |
| `feature-onboarding` | ViewModel |
| `composeApp` | App ViewModel |

---

## 📁 Project Structure

```
TrackShift/
├── composeApp/                 # Main application module
│   ├── src/
│   │   ├── commonMain/         # Shared code
│   │   ├── androidMain/        # Android-specific
│   │   └── iosMain/            # iOS-specific
├── core/
│   ├── design-system/          # Theme & components
│   ├── navigation/             # Route definitions
│   ├── network/                # HTTP configuration
│   └── utils/                  # Platform utilities
├── domain/
│   ├── auth/                   # Auth business logic
│   └── local-storage/          # Storage business logic
├── services/
│   ├── supabase/               # Supabase integration
│   └── storage/                # Platform storage
├── feature-auth/               # Auth UI
├── feature-onboarding/         # Onboarding UI
├── feature-home/               # Home UI
├── feature-splash-screen/      # Splash UI
└── build-logic/                # Convention plugins
    └── convention/
```

---

## 🔧 Build Logic

Custom Gradle convention plugins for consistent configuration:

- `AndroidApplicationConventionPlugin` — App module setup
- `KmpLibraryConventionPlugin` — Domain/Service modules
- `KmpFeatureConventionPlugin` — Feature modules with Compose

---

## 📄 License

```
MIT License

Copyright (c) 2026 TrackShift

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.
```

---

<p align="center">
  Made with ❤️ and Kotlin
</p>
