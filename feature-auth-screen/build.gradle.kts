plugins {
    id("trackshift.kmp.feature")
}

kotlin {
    androidLibrary {
        namespace = "com.despaircorp.feature_auth_screen"
        compileSdk = 36
        minSdk = 28
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.core)
        }
    }
}
