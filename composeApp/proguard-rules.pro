# Kotlin Serialization
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt

-keepclassmembers class kotlinx.serialization.json.** {
    *** Companion;
}
-keepclasseswithmembers class kotlinx.serialization.json.** {
    kotlinx.serialization.KSerializer serializer(...);
}

-keep,includedescriptorclasses class com.despaircorp.**$$serializer { *; }
-keepclassmembers class com.despaircorp.** {
    *** Companion;
}
-keepclasseswithmembers class com.despaircorp.** {
    kotlinx.serialization.KSerializer serializer(...);
}

# Ktor
-keep class io.ktor.** { *; }
-dontwarn io.ktor.**

# Supabase
-keep class io.github.jan.supabase.** { *; }
-dontwarn io.github.jan.supabase.**

# Koin
-keep class org.koin.** { *; }
-dontwarn org.koin.**

# Coroutines
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepclassmembers class kotlinx.coroutines.** {
    volatile <fields>;
}

# BuildKonfig
-keep class com.despaircorp.core.secrets.BuildKonfig { *; }

# Keep data classes / DTOs
-keep class com.despaircorp.services.trackshift_api.service.dto.** { *; }
-keep class com.despaircorp.services.trackshift_api.service.request.** { *; }

# OkHttp
-dontwarn okhttp3.**
-dontwarn okio.**

# Keep Compose
-keep class androidx.compose.** { *; }
-dontwarn androidx.compose.**
