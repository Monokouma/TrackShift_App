plugins {
    id("trackshift.kmp.library")
}

kotlin {
    androidLibrary {
        namespace = "com.despaircorp.domain.user"
        compileSdk = 36
        minSdk = 28
    }
}