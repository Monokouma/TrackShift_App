plugins {
    id("trackshift.kmp.feature")
}

kotlin {
    androidLibrary {
        namespace = "com.despaircorp.feature_login"
        compileSdk = 36
        minSdk = 28
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.core)
        }
    }
}