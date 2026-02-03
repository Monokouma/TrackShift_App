plugins {
    id("trackshift.kmp.library")
}

kotlin {
    androidLibrary {
        namespace = "com.despaircorp.network"
        compileSdk = 36
        minSdk = 28
    }
}
