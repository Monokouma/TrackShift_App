import com.android.build.api.dsl.ApplicationExtension
import com.codingfeline.buildkonfig.gradle.BuildKonfigExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.compose.ComposeExtension
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.kotlin.multiplatform")
                apply("com.android.application")
                apply("org.jetbrains.compose")
                apply("org.jetbrains.kotlin.plugin.compose")
                apply("org.jetbrains.kotlin.plugin.serialization")
                apply("com.codingfeline.buildkonfig")
                apply("dev.mokkery")
            }

            // Configure BuildKonfig with default package name
            extensions.configure<BuildKonfigExtension> {
                packageName = "com.despaircorp.trackshift"

                defaultConfigs {
                    // Empty default config - add fields in composeApp/build.gradle.kts
                }
            }

            val compose = extensions.getByType<ComposeExtension>().dependencies

            extensions.configure<KotlinMultiplatformExtension> {
                androidTarget {
                    compilerOptions {
                        jvmTarget.set(JvmTarget.JVM_17)
                    }
                }

                listOf(
                    iosArm64(),
                    iosSimulatorArm64()
                ).forEach { iosTarget ->
                    iosTarget.binaries.framework {
                        baseName = "ComposeApp"
                        isStatic = true
                    }
                }

                sourceSets.apply {
                    commonMain.dependencies {
                        // Compose
                        implementation(compose.runtime)
                        implementation(compose.foundation)
                        implementation(compose.material3)
                        implementation(compose.ui)
                        implementation(compose.components.resources)
                        implementation(compose.materialIconsExtended)

                        // Preview
                        implementation(libs.findLibrary("compose-ui-tooling-preview").get())

                        // Lifecycle & ViewModel
                        implementation(libs.findLibrary("androidx-lifecycle-viewmodel-compose").get())
                        implementation(libs.findLibrary("androidx-lifecycle-runtime-compose").get())

                        // Navigation
                        implementation(libs.findLibrary("navigation-compose").get())

                        // Coroutines
                        implementation(libs.findLibrary("kotlinx-coroutines-core").get())

                        // Koin
                        implementation(libs.findBundle("koin-common").get())

                        // Ktor
                        implementation(libs.findBundle("ktor-common").get())

                        // Supabase
                        implementation(libs.findBundle("supabase").get())

                        // Serialization
                        implementation(libs.findLibrary("kotlinx-serialization-json").get())

                        // DateTime
                        implementation(libs.findLibrary("kotlinx-datetime").get())
                    }

                    commonTest.dependencies {
                        implementation(libs.findLibrary("kotlin-test").get())
                        implementation(libs.findLibrary("kotlinx-coroutines-test").get())
                        implementation(libs.findLibrary("assertk").get())
                        implementation(libs.findLibrary("turbine").get())
                    }

                    androidMain.dependencies {
                        // AndroidX Compose Tooling (required for Android Studio preview)
                        implementation(libs.findLibrary("androidx-compose-ui-tooling").get())

                        // AndroidX
                        implementation(libs.findLibrary("androidx-activity-compose").get())
                        implementation(libs.findLibrary("androidx-core-ktx").get())
                        implementation(libs.findLibrary("androidx-browser").get())

                        // Koin Android
                        implementation(libs.findLibrary("koin-android").get())

                        // Ktor Android engine
                        implementation(libs.findLibrary("ktor-client-okhttp").get())
                    }

                    iosMain.dependencies {
                        // Ktor iOS engine
                        implementation(libs.findLibrary("ktor-client-darwin").get())
                    }
                }
            }

            extensions.configure<ApplicationExtension> {
                namespace = "com.despaircorp.trackshift"
                compileSdk = 36

                defaultConfig {
                    applicationId = "com.despaircorp.trackshift"
                    minSdk = 28
                    targetSdk = 36
                    versionCode = 1
                    versionName = "1.0"
                }

                packaging {
                    resources {
                        excludes += "/META-INF/{AL2.0,LGPL2.1}"
                    }
                }

                buildTypes {
                    getByName("release") {
                        isMinifyEnabled = false
                    }
                }

                compileOptions {
                    sourceCompatibility = JavaVersion.VERSION_17
                    targetCompatibility = JavaVersion.VERSION_17
                }
            }
        }
    }
}
