plugins {
    id("trackshift.kmp.library")
}

kotlin {
    androidLibrary {
        namespace = "com.despaircorp.services.supabase"
        compileSdk = 36
        minSdk = 28
    }
}
