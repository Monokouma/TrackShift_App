<p align="center">
  <img src="https://img.shields.io/badge/Kotlin-2.3.0-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white" alt="Kotlin"/>
  <img src="https://img.shields.io/badge/Compose_Multiplatform-1.10.0-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white" alt="Compose"/>
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
  <img src="https://img.shields.io/badge/Security-EncryptedSharedPrefs-purple?style=flat-square" alt="Security"/>
</p>

---

## ✨ Features

- 🔐 **OAuth Authentication** — Sign in with Google, Apple, or Discord
- 🔗 **Link Generation** — Generate TrackShift links from playlist URLs or screenshots
- 🔄 **Playlist Transfer** — Move playlists between Spotify, Apple Music, YouTube Music
- 📱 **Cross-Platform** — Native Android & iOS from single codebase
- 🎨 **Material 3** — Modern, adaptive UI
- 👤 **User Profile** — Edit profile picture & username with image picker
- 💎 **Pro / Free Limits** — Monthly usage limits for free users, unlimited for Pro
- 🔒 **Secure Storage** — Encrypted SharedPreferences on Android, Keychain on iOS

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
        │                                       │
        │    ┌────────────────────────────┐     │
        │    │feature-link-generation     │     │
        │    │feature-shift               │     │
        │    │feature-profile             │     │
        │    │feature-paywall             │     │
        │    │   (Screens)                │     │
        │    └────────────────────────────┘     │
        │                     │                 │
        └─────────────────────┼─────────────────┘
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                      domain layer                           │
│              (UseCases, Repositories, Entities)             │
│  ┌───────────┐ ┌───────────┐ ┌──────────────┐ ┌──────────┐ │
│  │domain:auth│ │domain:user│ │domain:link-  │ │domain:   │ │
│  │           │ │           │ │  generation  │ │local-    │ │
│  │           │ │           │ │              │ │storage   │ │
│  └───────────┘ └───────────┘ └──────────────┘ └──────────┘ │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                     services layer                          │
│                (Platform APIs, External SDKs)               │
│   ┌────────────────┐  ┌────────────────┐  ┌──────────────┐  │
│   │services:supabase│ │services:storage│  │ services:    │  │
│   │  (Auth API)    │  │ (Preferences)  │  │trackshift-api│  │
│   └────────────────┘  └────────────────┘  └──────────────┘  │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                       core layer                            │
│              (Shared Infrastructure & Utils)                │
│ ┌─────────────┐ ┌────────────┐ ┌────────────┐ ┌───────────┐ │
│ │design-system│ │ navigation │ │  network   │ │  secrets  │ │
│ └─────────────┘ └────────────┘ └────────────┘ └───────────┘ │
└─────────────────────────────────────────────────────────────┘
```

---

## 📦 Module Structure

| Layer | Module | Description |
|-------|--------|-------------|
| **App** | `composeApp` | Entry point, DI setup, navigation host |
| **Feature** | `feature-auth` | Authentication screens & ViewModel |
| | `feature-onboarding` | Onboarding flow |
| | `feature-home` | Main app experience with tab navigation |
| | `feature-link-generation` | Generate TrackShift links from URL or screenshots |
| | `feature-shift` | Playlist shift flow |
| | `feature-profile` | User profile with image picker |
| | `feature-paywall` | RevenueCat subscription paywall |
| | `feature-splash-screen` | Launch screen |
| **Domain** | `domain:auth` | Auth business logic |
| | `domain:user` | User data, profile logic & usage limits |
| | `domain:link-generation` | Link generation & conversion history logic |
| | `domain:local-storage` | Local preferences logic |
| **Services** | `services:supabase` | Supabase auth client |
| | `services:trackshift-api` | TrackShift backend API |
| | `services:storage` | Platform storage (EncryptedSharedPrefs/NSUserDefaults) |
| **Core** | `core:design-system` | Theme, colors, typography |
| | `core:navigation` | Navigation routes |
| | `core:network` | HTTP client config |
| | `core:secrets` | BuildKonfig secrets provider |
| | `core:utils` | Platform utilities |

---

## 🛠️ Tech Stack

| Category | Technology |
|----------|------------|
| **Language** | Kotlin 2.3.0 |
| **UI** | Compose Multiplatform 1.10.0 |
| **Architecture** | Clean Architecture + MVVM |
| **DI** | Koin 4.1 |
| **Networking** | Ktor 3.4 |
| **Auth** | Supabase Auth |
| **Payments** | RevenueCat |
| **Image Loading** | Coil 3 |
| **Image Picker** | Peekaboo |
| **Collections** | kotlinx-collections-immutable |
| **Security** | EncryptedSharedPreferences / iOS Keychain |
| **Async** | Coroutines + Flow |
| **Testing** | Mokkery, AssertK, Turbine |
| **Build** | Gradle Convention Plugins + BuildKonfig |

---

## 🔒 Security

- **Encrypted Storage** — Android uses `EncryptedSharedPreferences` with AES256-GCM encryption, iOS uses Keychain
- **BuildKonfig** — Compile-time secret injection via `core:secrets` module
- **Backup Disabled** — `android:allowBackup="false"` prevents data extraction
- **R8 + Minification** — Enabled on release builds for code shrinking and obfuscation
- **Null-Safe URL Parsing** — Defensive parsing for OAuth callbacks

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

2. **Configure secrets**

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
./gradlew :domain:user:allTests
./gradlew :domain:link-generation:allTests
./gradlew :services:trackshift-api:allTests
./gradlew :feature-link-generation:allTests
./gradlew :feature-profile:allTests
```

### Test Coverage

| Module | Tests |
|--------|-------|
| `domain:auth` | UseCases + Repository |
| `domain:user` | UseCases + Repository |
| `domain:link-generation` | UseCases + Repository |
| `domain:local-storage` | UseCases + Repository |
| `services:trackshift-api` | API Service (MockEngine) |
| `feature-auth` | ViewModel |
| `feature-onboarding` | ViewModel |
| `feature-home` | ViewModel |
| `feature-link-generation` | ViewModel |
| `feature-profile` | ViewModel |
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
│   ├── secrets/                # BuildKonfig secrets
│   └── utils/                  # Platform utilities
├── domain/
│   ├── auth/                   # Auth business logic
│   ├── user/                   # User business logic
│   ├── link-generation/        # Link generation business logic
│   └── local-storage/          # Storage business logic
├── services/
│   ├── supabase/               # Supabase integration
│   ├── trackshift-api/         # Backend API client
│   └── storage/                # Platform storage (encrypted)
├── feature-auth/               # Auth UI
├── feature-onboarding/         # Onboarding UI
├── feature-home/               # Home UI (tab container)
├── feature-link-generation/    # Link generation UI
│   └── screen/components/      # Extracted components
├── feature-shift/              # Playlist shift UI
├── feature-profile/            # Profile UI
│   └── screen/components/      # Extracted components
├── feature-paywall/            # Subscription paywall
├── feature-splash-screen/      # Splash UI
└── build-logic/                # Convention plugins
    └── convention/
```

---

## 🔧 Build Logic

Custom Gradle convention plugins for consistent configuration:

- `AndroidApplicationConventionPlugin` — App module setup
- `KmpLibraryConventionPlugin` — Domain/Service modules
- `KmpFeatureConventionPlugin` — Feature modules with Compose, Coil, Peekaboo

### Feature Plugin Includes

- Compose Multiplatform
- Coil (Image loading)
- Peekaboo (Image picker)
- Koin DI
- Navigation Compose
- Lifecycle ViewModel
- kotlinx-collections-immutable
- Mokkery + AssertK + Turbine (Testing)

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
