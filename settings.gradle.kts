rootProject.name = "TrackShift"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

include(":composeApp")

// Core - Shared infrastructure (UI, navigation, network base)
include(":core")
include(":core:design-system")
include(":core:navigation")
include(":core:network")

// Services - External API integrations (DTOs, API clients)
include(":services:supabase")

// Domain - Business logic (Entities, UseCases, Repositories)
include(":domain:auth")

// Features - UI layer (ViewModels, Screens)
include(":feature-splash-screen")

include(":feature-auth")
include(":core:utils")
include(":domain:local-storage")
include(":services:storage")