package com.lowiq.jellyfish.presentation.locale

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidedValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/**
 * Global state for custom app locale.
 * - null = use system default
 * - "en", "fr", etc. = force specific language
 */
var customAppLocale by mutableStateOf<String?>(null)

/**
 * Platform-specific locale provider.
 * Each platform implements this to properly set the locale for Compose Resources.
 */
expect object LocalAppLocale {
    val current: String
        @Composable get

    @Composable
    infix fun provides(value: String?): ProvidedValue<*>
}

expect fun getSystemLanguageCode(): String

/**
 * Wraps content with locale-aware environment.
 * Forces recomposition when customAppLocale changes.
 */
@Composable
fun AppLocaleProvider(content: @Composable () -> Unit) {
    CompositionLocalProvider(
        LocalAppLocale provides customAppLocale,
    ) {
        // key() forces full recomposition when locale changes
        key(customAppLocale) {
            content()
        }
    }
}
