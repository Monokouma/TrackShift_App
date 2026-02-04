plugins {
    `kotlin-dsl`
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
    }
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.compose.gradlePlugin)
    compileOnly(libs.buildkonfig.gradle.plugin)
}

gradlePlugin {
    plugins {
        register("kmpLibrary") {
            id = "trackshift.kmp.library"
            implementationClass = "KmpLibraryConventionPlugin"
        }
        register("kmpFeature") {
            id = "trackshift.kmp.feature"
            implementationClass = "KmpFeatureConventionPlugin"
        }
        register("androidApplication") {
            id = "trackshift.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
    }
}
