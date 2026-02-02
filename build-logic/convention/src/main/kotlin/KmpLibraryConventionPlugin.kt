import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinAndroidTarget

class KmpLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.kotlin.multiplatform")
                apply("com.android.kotlin.multiplatform.library")
                apply("org.jetbrains.kotlin.plugin.serialization")
            }

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
                        // Coroutines
                        implementation(libs.findLibrary("kotlinx-coroutines-core").get())

                        // Koin
                        implementation(libs.findLibrary("koin-core").get())

                        // Ktor
                        implementation(libs.findBundle("ktor-common").get())

                        // Serialization
                        implementation(libs.findLibrary("kotlinx-serialization-json").get())

                        // DateTime
                        implementation(libs.findLibrary("kotlinx-datetime").get())
                    }

                    commonTest.dependencies {
                        implementation(libs.findLibrary("kotlin-test").get())
                        implementation(libs.findLibrary("kotlinx-coroutines-test").get())
                        implementation(libs.findLibrary("koin-test").get())
                    }

                    androidMain.dependencies {
                        // Ktor Android engine
                        implementation(libs.findLibrary("ktor-client-okhttp").get())

                        // Coroutines Android
                        implementation(libs.findLibrary("kotlinx-coroutines-android").get())

                        // Koin Android
                        implementation(libs.findLibrary("koin-android").get())
                    }

                    iosMain.dependencies {
                        // Ktor iOS engine
                        implementation(libs.findLibrary("ktor-client-darwin").get())
                    }
                }
            }
        }
    }
}

internal val Project.libs
    get() = extensions.getByType(org.gradle.api.artifacts.VersionCatalogsExtension::class.java)
        .named("libs")
