package com.lowiq.jellyfish.presentation.locale

import platform.Foundation.NSLocale
import platform.Foundation.currentLocale
import platform.Foundation.languageCode

actual fun getSystemLanguageCode(): String = NSLocale.currentLocale.languageCode ?: "en"
