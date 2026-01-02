package com.lowiq.jellyfish.presentation.locale

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidedValue
import androidx.compose.runtime.staticCompositionLocalOf
import java.util.Locale

actual fun getSystemLanguageCode(): String = Locale.getDefault().language

actual object LocalAppLocale {
    private var defaultLocale: Locale? = null
    private val LocalLocaleString = staticCompositionLocalOf { Locale.getDefault().language }

    actual val current: String
        @Composable
        get() = LocalLocaleString.current

    @Composable
    actual infix fun provides(value: String?): ProvidedValue<*> {
        // Save default locale on first call
        if (defaultLocale == null) {
            defaultLocale = Locale.getDefault()
        }

        // Determine new locale
        val newLocale = when (value) {
            null -> defaultLocale!!
            else -> Locale(value)
        }

        // Update JVM default locale
        Locale.setDefault(newLocale)

        return LocalLocaleString.provides(newLocale.language)
    }
}
