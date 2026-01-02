package com.lowiq.jellyfish.presentation.locale

enum class AppLanguage(val code: String, val nativeName: String) {
    SYSTEM("system", "System"),
    ENGLISH("en", "English"),
    FRENCH("fr", "Français");

    companion object {
        fun fromCode(code: String): AppLanguage =
            entries.find { it.code == code } ?: SYSTEM
    }
}
