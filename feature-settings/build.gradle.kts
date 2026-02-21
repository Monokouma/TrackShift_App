plugins {
    id("trackshift.kmp.feature")
}

kotlin {
    androidLibrary {
        namespace = "com.despaircorp.feature_settings"
        compileSdk = 36
        minSdk = 28

        experimentalProperties["android.experimental.kmp.enableAndroidResources"] = true
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.designSystem)
            implementation(projects.core.navigation)
            implementation(projects.core.utils)
            implementation(projects.core.secrets)
            implementation(projects.domain.user)
            implementation(projects.domain.auth)
        }
    }
}