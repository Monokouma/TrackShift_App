plugins {
    id("trackshift.android.application")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.featureAuth)
            implementation(projects.featureSplashScreen)
            implementation(projects.featureHome)
            implementation(projects.featureOnboarding)
            implementation(projects.featureProfile)
            implementation(projects.core.designSystem)
            implementation(projects.core.navigation)
            implementation(projects.core.network)
            implementation(projects.core.secrets)
            implementation(projects.services.supabase)
            implementation(projects.services.trackshiftApi)
            implementation(projects.domain.auth)
            implementation(projects.domain.user)
            implementation(projects.domain.localStorage)
            implementation(projects.services.storage)
        }
    }
}
