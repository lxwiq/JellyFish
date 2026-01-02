package com.lowiq.jellyfish.presentation.locale

import java.util.Locale

actual fun getSystemLanguageCode(): String = Locale.getDefault().language
