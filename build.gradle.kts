plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.composeMultiplatform) apply false
    alias(libs.plugins.composeCompiler) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.kotlinxSerialization) apply false
    alias(libs.plugins.androidKotlinMultiplatformLibrary) apply false
    alias(libs.plugins.androidLint) apply false
    alias(libs.plugins.buildkonfig) apply false
}

// =============================================================================
// FEATURE MODULE TASK
// Creates feature modules with view_model and screen packages (UI layer only)
// Usage: ./gradlew createFeature --name=feature-xxx
// =============================================================================
abstract class CreateFeatureTask : DefaultTask() {

    @get:Input
    @get:Option(
        option = "name",
        description = "The name of the feature module (e.g., feature-auth-screen)"
    )
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
            "$name/src/commonMain/kotlin/$basePackage/view_model",
            "$name/src/commonMain/kotlin/$basePackage/screen",
            "$name/src/androidMain/kotlin/$basePackage",
            "$name/src/iosMain/kotlin/$basePackage"
        )

        directories.forEach { dir ->
            File(rootDir, dir).mkdirs()
            File(rootDir, "$dir/.gitkeep").createNewFile()
        }

        val packageName = "com.despaircorp.$packageSuffix"

        val buildGradleContent = """
            |plugins {
            |    id("trackshift.kmp.feature")
            |}
            |
            |kotlin {
            |    androidLibrary {
            |        namespace = "$packageName"
            |        compileSdk = 36
            |        minSdk = 28
            |
            |        experimentalProperties["android.experimental.kmp.enableAndroidResources"] = true
            |    }
            |
            |    sourceSets {
            |        commonMain.dependencies {
            |            implementation(projects.core.designSystem)
            |            implementation(projects.core.navigation)
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
        println("  │   │   ├── view_model/")
        println("  │   │   └── screen/")
        println("  │   ├── androidMain/kotlin/$basePackage/")
        println("  │   └── iosMain/kotlin/$basePackage/")
        println("  └── build.gradle.kts")
    }
}

tasks.register<CreateFeatureTask>("createFeature") {
    description = "Creates a new feature module with view_model and screen packages"
    group = "trackshift"
    projectDir.set(layout.projectDirectory)
}

// =============================================================================
// SERVICE MODULE TASK
// Creates service modules in services/ with service package (API/Network layer)
// Usage: ./gradlew createService --name=spotify
// =============================================================================
abstract class CreateServiceTask : DefaultTask() {

    @get:Input
    @get:Option(
        option = "name",
        description = "The name of the service module (e.g., spotify, apple-music)"
    )
    abstract val serviceName: Property<String>

    @get:Internal
    abstract val projectDir: DirectoryProperty

    @TaskAction
    fun create() {
        val name = serviceName.orNull
            ?: throw GradleException("Service name is required. Usage: ./gradlew createService --name=spotify")

        val rootDir = projectDir.get().asFile
        val moduleDir = File(rootDir, "services/$name")

        if (moduleDir.exists()) {
            throw GradleException("Service module 'services/$name' already exists!")
        }

        val packageSuffix = name.replace("-", "_")
        val basePackage = "com/despaircorp/services/$packageSuffix"

        println("Creating service module: services/$name")

        val directories = listOf(
            "services/$name/src/commonMain/kotlin/$basePackage/service",
            "services/$name/src/androidMain/kotlin/$basePackage",
            "services/$name/src/iosMain/kotlin/$basePackage"
        )

        directories.forEach { dir ->
            File(rootDir, dir).mkdirs()
            File(rootDir, "$dir/.gitkeep").createNewFile()
        }

        val packageName = "com.despaircorp.services.$packageSuffix"

        val buildGradleContent = """
            |plugins {
            |    id("trackshift.kmp.library")
            |}
            |
            |kotlin {
            |    androidLibrary {
            |        namespace = "$packageName"
            |        compileSdk = 36
            |        minSdk = 28
            |    }
            |
            |    sourceSets {
            |        commonMain.dependencies {
            |            implementation(projects.core.network)
            |        }
            |    }
            |}
        """.trimMargin()

        File(rootDir, "services/$name/build.gradle.kts").writeText(buildGradleContent)

        val settingsFile = File(rootDir, "settings.gradle.kts")
        val settingsContent = settingsFile.readText()

        if (!settingsContent.contains("include(\":services:$name\")")) {
            settingsFile.appendText("\ninclude(\":services:$name\")")
            println("Added 'services:$name' to settings.gradle.kts")
        }

        println("✅ Service module 'services/$name' created successfully!")
        println("")
        println("Structure created:")
        println("  services/$name/")
        println("  ├── src/")
        println("  │   ├── commonMain/kotlin/$basePackage/")
        println("  │   │   └── service/   (DTOs, API clients)")
        println("  │   ├── androidMain/kotlin/$basePackage/")
        println("  │   └── iosMain/kotlin/$basePackage/")
        println("  └── build.gradle.kts")
        println("")
        println("This module is for external API integrations.")
        println("Uses 'trackshift.kmp.library' with Ktor, Supabase, Serialization.")
    }
}

tasks.register<CreateServiceTask>("createService") {
    description = "Creates a new service module in services/ (API layer)"
    group = "trackshift"
    projectDir.set(layout.projectDirectory)
}

// =============================================================================
// DOMAIN MODULE TASK
// Creates domain modules in domain/ with data and domain packages (Business logic)
// Usage: ./gradlew createDomain --name=user
// =============================================================================
abstract class CreateDomainTask : DefaultTask() {

    @get:Input
    @get:Option(
        option = "name",
        description = "The name of the domain module (e.g., user, playlist, auth)"
    )
    abstract val domainName: Property<String>

    @get:Internal
    abstract val projectDir: DirectoryProperty

    @TaskAction
    fun create() {
        val name = domainName.orNull
            ?: throw GradleException("Domain name is required. Usage: ./gradlew createDomain --name=user")

        val rootDir = projectDir.get().asFile
        val moduleDir = File(rootDir, "domain/$name")

        if (moduleDir.exists()) {
            throw GradleException("Domain module 'domain/$name' already exists!")
        }

        val packageSuffix = name.replace("-", "_")
        val basePackage = "com/despaircorp/domain/$packageSuffix"

        println("Creating domain module: domain/$name")

        val directories = listOf(
            "domain/$name/src/commonMain/kotlin/$basePackage/data",
            "domain/$name/src/commonMain/kotlin/$basePackage/domain",
            "domain/$name/src/androidMain/kotlin/$basePackage",
            "domain/$name/src/iosMain/kotlin/$basePackage"
        )

        directories.forEach { dir ->
            File(rootDir, dir).mkdirs()
            File(rootDir, "$dir/.gitkeep").createNewFile()
        }

        val packageName = "com.despaircorp.domain.$packageSuffix"

        val buildGradleContent = """
            |plugins {
            |    id("trackshift.kmp.library")
            |}
            |
            |kotlin {
            |    androidLibrary {
            |        namespace = "$packageName"
            |        compileSdk = 36
            |        minSdk = 28
            |    }
            |}
        """.trimMargin()

        File(rootDir, "domain/$name/build.gradle.kts").writeText(buildGradleContent)

        val settingsFile = File(rootDir, "settings.gradle.kts")
        val settingsContent = settingsFile.readText()

        if (!settingsContent.contains("include(\":domain:$name\")")) {
            settingsFile.appendText("\ninclude(\":domain:$name\")")
            println("Added 'domain:$name' to settings.gradle.kts")
        }

        println("✅ Domain module 'domain/$name' created successfully!")
        println("")
        println("Structure created:")
        println("  domain/$name/")
        println("  ├── src/")
        println("  │   ├── commonMain/kotlin/$basePackage/")
        println("  │   │   ├── data/      (Repository implementations)")
        println("  │   │   └── domain/    (Entities, UseCases, Repository interfaces)")
        println("  │   ├── androidMain/kotlin/$basePackage/")
        println("  │   └── iosMain/kotlin/$basePackage/")
        println("  └── build.gradle.kts")
        println("")
        println("This module is for business logic (Entities, UseCases, Repositories).")
        println("Uses 'trackshift.kmp.library' with Koin, Coroutines, DateTime.")
    }
}

tasks.register<CreateDomainTask>("createDomain") {
    description = "Creates a new domain module in domain/ (Business logic)"
    group = "trackshift"
    projectDir.set(layout.projectDirectory)
}

// =============================================================================
// CORE MODULE TASK
// Creates core modules in core/ (Infrastructure/shared utilities)
// Usage: ./gradlew createCore -Pname=utils
// =============================================================================
abstract class CreateCoreTask : DefaultTask() {

    @get:Input
    @get:Option(
        option = "name",
        description = "The name of the core module (e.g., utils, analytics, logging)"
    )
    abstract val coreName: Property<String>

    @get:Internal
    abstract val projectDir: DirectoryProperty

    @TaskAction
    fun create() {
        val name = coreName.orNull
            ?: throw GradleException("Core name is required. Usage: ./gradlew createCore -Pname=utils")

        val rootDir = projectDir.get().asFile
        val moduleDir = File(rootDir, "core/$name")

        if (moduleDir.exists()) {
            throw GradleException("Core module 'core/$name' already exists!")
        }

        val packageSuffix = name.replace("-", "_")
        val basePackage = "com/despaircorp/$packageSuffix"

        println("Creating core module: core/$name")

        val directories = listOf(
            "core/$name/src/commonMain/kotlin/$basePackage",
            "core/$name/src/androidMain/kotlin/$basePackage",
            "core/$name/src/iosMain/kotlin/$basePackage"
        )

        directories.forEach { dir ->
            File(rootDir, dir).mkdirs()
            File(rootDir, "$dir/.gitkeep").createNewFile()
        }

        val packageName = "com.despaircorp.$packageSuffix"

        val buildGradleContent = """
            |plugins {
            |    id("trackshift.kmp.library")
            |}
            |
            |kotlin {
            |    androidLibrary {
            |        namespace = "$packageName"
            |        compileSdk = 36
            |        minSdk = 28
            |    }
            |}
        """.trimMargin()

        File(rootDir, "core/$name/build.gradle.kts").writeText(buildGradleContent)

        val settingsFile = File(rootDir, "settings.gradle.kts")
        val settingsContent = settingsFile.readText()

        if (!settingsContent.contains("include(\":core:$name\")")) {
            settingsFile.appendText("\ninclude(\":core:$name\")")
            println("Added 'core:$name' to settings.gradle.kts")
        }

        println("✅ Core module 'core/$name' created successfully!")
        println("")
        println("Structure created:")
        println("  core/$name/")
        println("  ├── src/")
        println("  │   ├── commonMain/kotlin/$basePackage/")
        println("  │   ├── androidMain/kotlin/$basePackage/")
        println("  │   └── iosMain/kotlin/$basePackage/")
        println("  └── build.gradle.kts")
        println("")
        println("This module is for infrastructure/shared utilities.")
        println("Uses 'trackshift.kmp.library' convention plugin.")
    }
}

tasks.register<CreateCoreTask>("createCore") {
    description = "Creates a new core module in core/ (Infrastructure)"
    group = "trackshift"
    projectDir.set(layout.projectDirectory)
}
