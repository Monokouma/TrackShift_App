import com.codingfeline.buildkonfig.gradle.BuildKonfigExtension
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
                apply("com.codingfeline.buildkonfig")
                apply("dev.mokkery")
            }

            // Configure BuildKonfig with default package name based on project path
            extensions.configure<BuildKonfigExtension> {
                val projectPath = project.path.replace(":", ".").removePrefix(".")
                packageName = "com.despaircorp.$projectPath".replace("-", "_")

                defaultConfigs {
                    // Empty default config - modules can add their own fields
                }
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

                        // Collections Immutable
                        implementation(libs.findLibrary("kotlinx-collections-immutable").get())

                        // Coil (Image loading)
                        implementation(libs.findBundle("coil").get())

                        // Peekaboo (Image picker)
                        implementation(libs.findLibrary("onseok-peekaboo-image-picker").get())
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
