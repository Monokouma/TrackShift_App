plugins {
    id("trackshift.kmp.feature")
}

kotlin {
    androidLibrary {
        namespace = "com.despaircorp.feature_splash_screen"
        compileSdk = 36
        minSdk = 28

        experimentalProperties["android.experimental.kmp.enableAndroidResources"] = true
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.core)
            implementation(projects.core.designSystem)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
        }
    }
}
