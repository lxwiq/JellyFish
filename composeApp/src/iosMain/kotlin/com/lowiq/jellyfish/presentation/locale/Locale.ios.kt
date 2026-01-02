package com.lowiq.jellyfish.presentation.locale

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidedValue
import androidx.compose.runtime.staticCompositionLocalOf
import platform.Foundation.NSLocale
import platform.Foundation.NSUserDefaults
import platform.Foundation.currentLocale
import platform.Foundation.languageCode
import platform.Foundation.preferredLanguages

actual fun getSystemLanguageCode(): String = NSLocale.currentLocale.languageCode ?: "en"

actual object LocalAppLocale {
    private const val LANG_KEY = "AppleLanguages"
    private val defaultLanguage = (NSLocale.preferredLanguages.firstOrNull() as? String) ?: "en"
    private val LocalLocaleString = staticCompositionLocalOf { defaultLanguage }

    actual val current: String
        @Composable
        get() = LocalLocaleString.current

    @Composable
    actual infix fun provides(value: String?): ProvidedValue<*> {
        val newLanguage = value ?: defaultLanguage

        if (value == null) {
            // Reset to system default
            NSUserDefaults.standardUserDefaults.removeObjectForKey(LANG_KEY)
        } else {
            // Set custom language
            NSUserDefaults.standardUserDefaults.setObject(listOf(newLanguage), LANG_KEY)
        }

        return LocalLocaleString.provides(newLanguage)
    }
}
