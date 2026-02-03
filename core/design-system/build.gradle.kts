plugins {
    id("trackshift.kmp.feature")
}

kotlin {
    androidLibrary {
        namespace = "com.despaircorp.design_system"
        compileSdk = 36
        minSdk = 28

        experimentalProperties["android.experimental.kmp.enableAndroidResources"] = true
    }
}

compose.resources {
    publicResClass = true
    packageOfResClass = "com.despaircorp.design_system.resources"
    generateResClass = always
}
