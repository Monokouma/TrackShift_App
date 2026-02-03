plugins {
    id("trackshift.kmp.library")
}

kotlin {
    androidLibrary {
        namespace = "com.despaircorp.core"
        compileSdk = 36
        minSdk = 28
    }
}
