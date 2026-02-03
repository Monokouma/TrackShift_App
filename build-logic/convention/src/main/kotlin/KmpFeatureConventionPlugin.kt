import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.withType
import org.jetbrains.compose.ComposeExtension
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinAndroidTarget

class KmpFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.kotlin.multiplatform")
                apply("com.android.kotlin.multiplatform.library")
                apply("org.jetbrains.kotlin.plugin.serialization")
                apply("org.jetbrains.compose")
                apply("org.jetbrains.kotlin.plugin.compose")
            }

            val compose = extensions.getByType<ComposeExtension>().dependencies

            extensions.configure<KotlinMultiplatformExtension> {
                // The com.android.kotlin.multiplatform.library plugin creates the android target
                // We configure it through the kotlin extension
                targets.withType<KotlinAndroidTarget>().configureEach {
                    compilerOptions {
                        jvmTarget.set(JvmTarget.JVM_17)
                    }
                }

                val xcfName = "${project.name}Kit"

                iosX64 {
                    binaries.framework {
                        baseName = xcfName
                    }
                }

                iosArm64 {
                    binaries.framework {
                        baseName = xcfName
                    }
                }

                iosSimulatorArm64 {
                    binaries.framework {
                        baseName = xcfName
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
                        implementation(compose.components.uiToolingPreview)
                        implementation(compose.materialIconsExtended)

                        // Salt UI
                        implementation(libs.findLibrary("salt-ui").get())

                        // Lifecycle & ViewModel
                        implementation(libs.findLibrary("androidx-lifecycle-viewmodel-compose").get())
                        implementation(libs.findLibrary("androidx-lifecycle-runtime-compose").get())

                        // Navigation
                        implementation(libs.findLibrary("navigation-compose").get())

                        // Coroutines
                        implementation(libs.findLibrary("kotlinx-coroutines-core").get())

                        // Koin
                        implementation(libs.findBundle("koin-common").get())

                        // Serialization
                        implementation(libs.findLibrary("kotlinx-serialization-json").get())

                        // DateTime
                        implementation(libs.findLibrary("kotlinx-datetime").get())
                    }

                    commonTest.dependencies {
                        implementation(libs.findLibrary("kotlin-test").get())
                        implementation(libs.findLibrary("kotlinx-coroutines-test").get())
                    }

                    androidMain.dependencies {
                        // AndroidX Compose Tooling (required for Android Studio preview)
                        implementation(libs.findLibrary("androidx-compose-ui-tooling").get())

                        // AndroidX
                        implementation(libs.findLibrary("androidx-core-ktx").get())

                        // Koin Android
                        implementation(libs.findLibrary("koin-android").get())
                    }

                    iosMain.dependencies {
                        // iOS specific dependencies if needed
                    }
                }
            }
        }
    }
}
