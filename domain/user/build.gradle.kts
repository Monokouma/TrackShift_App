plugins {
    id("trackshift.kmp.library")
}

kotlin {
    androidLibrary {
        namespace = "com.despaircorp.domain.user"
        compileSdk = 36
        minSdk = 28
    }


    sourceSets {
        commonMain.dependencies {
            implementation(projects.services.trackshiftApi)
            implementation(projects.domain.auth)
        }
    }
}