package com.lowiq.jellyfish.presentation.locale

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidedValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
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
        val configuration = LocalConfiguration.current
        val context = LocalContext.current

        // Save default locale on first call
        if (defaultLocale == null) {
            defaultLocale = Locale.getDefault()
        }

        // Determine new locale
        val newLocale = when (value) {
            null -> defaultLocale!!
            else -> Locale(value)
        }

        // Update system locale
        Locale.setDefault(newLocale)

        // Update Android configuration
        val newConfiguration = android.content.res.Configuration(configuration)
        newConfiguration.setLocale(newLocale)
        context.resources.updateConfiguration(newConfiguration, context.resources.displayMetrics)

        return LocalLocaleString.provides(newLocale.language)
    }
}
