plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.composeMultiplatform) apply false
    alias(libs.plugins.composeCompiler) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.kotlinSerialization) apply false
}

abstract class CreateFeatureTask : DefaultTask() {

    @get:Input
    @get:Option(option = "name", description = "The name of the feature module (e.g., feature-auth-screen)")
    abstract val featureName: Property<String>

    @get:Internal
    abstract val projectDir: DirectoryProperty

    @TaskAction
    fun create() {
        val name = featureName.orNull
            ?: throw GradleException("Feature name is required. Usage: ./gradlew createFeature --name=feature-xxx")

        if (!name.startsWith("feature-")) {
            throw GradleException("Feature name must start with 'feature-'. Example: feature-auth-screen")
        }

        val rootDir = projectDir.get().asFile
        val featureDir = File(rootDir, name)

        if (featureDir.exists()) {
            throw GradleException("Feature module '$name' already exists!")
        }

        val packageSuffix = name.replace("-", "_")
        val basePackage = "com/despaircorp/$packageSuffix"

        println("Creating feature module: $name")

        val directories = listOf(
            "$name/src/commonMain/kotlin/$basePackage/data",
            "$name/src/commonMain/kotlin/$basePackage/domain",
            "$name/src/commonMain/kotlin/$basePackage/presentation",
            "$name/src/androidMain/kotlin/$basePackage",
            "$name/src/iosMain/kotlin/$basePackage"
        )

        directories.forEach { dir ->
            File(rootDir, dir).mkdirs()
            File(rootDir, "$dir/.gitkeep").createNewFile()
        }

        val buildGradleContent = """
            |plugins {
            |    id("trackshift.kmp.feature")
            |}
            |
            |kotlin {
            |    androidLibrary {
            |        namespace = "com.despaircorp.$packageSuffix"
            |        compileSdk = 36
            |        minSdk = 28
            |    }
            |
            |    sourceSets {
            |        commonMain.dependencies {
            |            implementation(projects.core)
            |        }
            |    }
            |}
        """.trimMargin()

        File(rootDir, "$name/build.gradle.kts").writeText(buildGradleContent)

        val settingsFile = File(rootDir, "settings.gradle.kts")
        val settingsContent = settingsFile.readText()

        if (!settingsContent.contains("include(\":$name\")")) {
            settingsFile.appendText("\ninclude(\":$name\")")
            println("Added '$name' to settings.gradle.kts")
        }

        println("✅ Feature module '$name' created successfully!")
        println("")
        println("Structure created:")
        println("  $name/")
        println("  ├── src/")
        println("  │   ├── commonMain/kotlin/$basePackage/")
        println("  │   │   ├── data/")
        println("  │   │   ├── domain/")
        println("  │   │   └── presentation/")
        println("  │   ├── androidMain/kotlin/$basePackage/")
        println("  │   └── iosMain/kotlin/$basePackage/")
        println("  └── build.gradle.kts")
    }
}

tasks.register<CreateFeatureTask>("createFeature") {
    description = "Creates a new feature module with clean architecture structure"
    group = "trackshift"
    projectDir.set(layout.projectDirectory)
}
