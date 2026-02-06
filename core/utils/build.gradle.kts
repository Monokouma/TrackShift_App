plugins {
    id("trackshift.kmp.library")
}

kotlin {
    androidLibrary {
        namespace = "com.despaircorp.utils"
        compileSdk = 36
        minSdk = 28
    }
}