# JellyFish ProGuard Rules

# Suppress warnings for missing annotation classes (compile-time only dependencies)
-dontwarn com.google.errorprone.annotations.CanIgnoreReturnValue
-dontwarn com.google.errorprone.annotations.CheckReturnValue
-dontwarn com.google.errorprone.annotations.Immutable
-dontwarn com.google.errorprone.annotations.RestrictedApi
-dontwarn okhttp3.internal.Util

# Keep Compose
-keep class androidx.compose.** { *; }
-dontwarn androidx.compose.**

# Keep Kotlin Serialization
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt

-keepclassmembers class kotlinx.serialization.json.** {
    *** Companion;
}
-keepclasseswithmembers class kotlinx.serialization.json.** {
    kotlinx.serialization.KSerializer serializer(...);
}

# Keep Jellyfin SDK models
-keep class org.jellyfin.sdk.model.** { *; }
-keep class org.jellyfin.sdk.api.** { *; }

# Keep Ktor
-keep class io.ktor.** { *; }
-dontwarn io.ktor.**

# Keep VLC
-keep class org.videolan.libvlc.** { *; }

# Keep Coil
-keep class coil3.** { *; }

# Keep Koin
-keep class org.koin.** { *; }

# Keep app models
-keep class com.lowiq.jellyfish.data.** { *; }
-keep class com.lowiq.jellyfish.domain.** { *; }
