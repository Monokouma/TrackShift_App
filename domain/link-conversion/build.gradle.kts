plugins {
    id("trackshift.kmp.library")
}

kotlin {
    androidLibrary {
        namespace = "com.despaircorp.domain.link_conversion"
        compileSdk = 36
        minSdk = 28
    }
}