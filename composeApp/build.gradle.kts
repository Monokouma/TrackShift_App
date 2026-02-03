plugins {
    id("trackshift.android.application")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core)
            implementation(projects.featureSplashScreen)
            implementation(projects.core.designSystem)

        }
    }
}
