import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import java.util.Properties

plugins {
    id("trackshift.kmp.library")
}

val localProperties = Properties().apply {
    val file = rootProject.file("local.properties")
    if (file.exists()) load(file.inputStream())
}

buildkonfig {
    packageName = "com.despaircorp.core.secrets"
    exposeObjectWithName = "BuildKonfig"

    defaultConfigs {
        // Supabase
        buildConfigField(STRING, "SUPABASE_URL", localProperties.getProperty("supabase.url") ?: "")
        buildConfigField(STRING, "SUPABASE_KEY", localProperties.getProperty("supabase.key") ?: "")

        // Google OAuth
        buildConfigField(STRING, "GOOGLE_WEB_CLIENT_ID", localProperties.getProperty("GOOGLE_WEB_CLIENT_ID") ?: "")

        // TrackShift API
        buildConfigField(STRING, "TRACKSHIFT_API_URL", localProperties.getProperty("TRACKSHIFT_API_URL") ?: "")
        buildConfigField(STRING, "API_SECRET_KEY", localProperties.getProperty("API_SECRET_KEY") ?: "")

        // App
        buildConfigField(STRING, "VERSION_NAME", "1.0.0")

        // RevenueCat
        buildConfigField(STRING, "REVENUE_CAT_TEST_KEY", localProperties.getProperty("REVENUE_CAT_TEST_KEY") ?: "")
        buildConfigField(STRING, "REVENUE_CAT_IOS_KEY", localProperties.getProperty("REVENUE_CAT_IOS_KEY") ?: "")
        buildConfigField(STRING, "REVENUE_CAT_ANDROID_KEY", localProperties.getProperty("REVENUE_CAT_ANDROID_KEY") ?: "")
    }
}

kotlin {
    androidLibrary {
        namespace = "com.despaircorp.core.secrets"
        compileSdk = 36
        minSdk = 28
    }

    sourceSets {
        commonMain.dependencies {
            // No dependencies needed - this module only provides BuildKonfig
        }
    }
}
