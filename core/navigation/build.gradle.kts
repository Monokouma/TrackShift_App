plugins {
    id("trackshift.kmp.library")
}

kotlin {
    androidLibrary {
        namespace = "com.despaircorp.navigation"
        compileSdk = 36
        minSdk = 28
    }
}