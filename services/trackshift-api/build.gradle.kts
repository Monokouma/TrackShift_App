plugins {
    id("trackshift.kmp.library")
}

kotlin {
    androidLibrary {
        namespace = "com.despaircorp.services.trackshift_api"
        compileSdk = 36
        minSdk = 28
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.network)
            implementation(projects.core.secrets)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutines.test)
            implementation(libs.assertk)
            implementation(libs.ktor.client.mock)
        }
    }
}
