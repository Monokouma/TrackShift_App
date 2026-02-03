plugins {
    id("trackshift.kmp.feature")
}

kotlin {
    androidLibrary {
        namespace = "com.despaircorp.design_system"
        compileSdk = 36
        minSdk = 28
    }
}