import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import java.util.Properties

plugins {
    id("trackshift.android.application")
}

// Load secrets from local.properties
val localProperties = Properties().apply {
    val file = rootProject.file("local.properties")
    if (file.exists()) load(file.inputStream())
}

buildkonfig {
    packageName = "com.despaircorp.trackshift"

    defaultConfigs {
        // Supabase
        buildConfigField(STRING, "SUPABASE_URL", localProperties.getProperty("supabase.url") ?: "")
        buildConfigField(STRING, "SUPABASE_KEY", localProperties.getProperty("supabase.key") ?: "")

        // API Secret
        buildConfigField(STRING, "API_SECRET_KEY", localProperties.getProperty("API_SECRET_KEY") ?: "")

        // RevenueCat
        buildConfigField(STRING, "REVENUE_CAT_TEST_KEY", localProperties.getProperty("REVENUE_CAT_TEST_KEY") ?: "")
        buildConfigField(STRING, "REVENUE_CAT_IOS_KEY", localProperties.getProperty("REVENUE_CAT_IOS_KEY") ?: "")
        buildConfigField(STRING, "REVENUE_CAT_ANDROID_KEY", localProperties.getProperty("REVENUE_CAT_ANDROID_KEY") ?: "")
    }
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.featureSplashScreen)
            implementation(projects.core.designSystem)
            implementation(projects.core.navigation)
            implementation(projects.core.network)
            implementation(projects.services.supabase)
            implementation(projects.domain.auth)
        }
    }
}
