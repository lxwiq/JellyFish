package com.lowiq.jellyfish.presentation.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import jellyfish.composeapp.generated.resources.Res
import jellyfish.composeapp.generated.resources.lato_medium
import jellyfish.composeapp.generated.resources.lato_regular
import jellyfish.composeapp.generated.resources.oswald_bold
import org.jetbrains.compose.resources.Font

/**
 * JellyFish Typography using Oswald for headings and Lato for body text.
 *
 * Oswald (700) - Condensed, high contrast, tech/UI look for titles
 * Lato (400/500) - Readable on dark backgrounds, elegant and neutral for body text
 */
object JellyFishTypography {

    @Composable
    fun oswaldFontFamily(): FontFamily = FontFamily(
        Font(Res.font.oswald_bold, FontWeight.Bold)
    )

    @Composable
    fun latoFontFamily(): FontFamily = FontFamily(
        Font(Res.font.lato_regular, FontWeight.Normal),
        Font(Res.font.lato_medium, FontWeight.Medium)
    )

    /**
     * Creates Material3 Typography with Oswald for display/headlines
     * and Lato for body/label text styles.
     */
    @Composable
    fun typography(): Typography {
        val oswald = oswaldFontFamily()
        val lato = latoFontFamily()

        return Typography(
            // Display styles - Oswald Bold for large, impactful text
            displayLarge = TextStyle(
                fontFamily = oswald,
                fontWeight = FontWeight.Bold,
                fontSize = 57.sp,
                lineHeight = 64.sp,
                letterSpacing = (-0.25).sp
            ),
            displayMedium = TextStyle(
                fontFamily = oswald,
                fontWeight = FontWeight.Bold,
                fontSize = 45.sp,
                lineHeight = 52.sp,
                letterSpacing = 0.sp
            ),
            displaySmall = TextStyle(
                fontFamily = oswald,
                fontWeight = FontWeight.Bold,
                fontSize = 36.sp,
                lineHeight = 44.sp,
                letterSpacing = 0.sp
            ),

            // Headline styles - Oswald Bold for section headers
            headlineLarge = TextStyle(
                fontFamily = oswald,
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp,
                lineHeight = 40.sp,
                letterSpacing = 0.sp
            ),
            headlineMedium = TextStyle(
                fontFamily = oswald,
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp,
                lineHeight = 36.sp,
                letterSpacing = 0.sp
            ),
            headlineSmall = TextStyle(
                fontFamily = oswald,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                lineHeight = 32.sp,
                letterSpacing = 0.sp
            ),

            // Title styles - Oswald Bold for smaller titles
            titleLarge = TextStyle(
                fontFamily = oswald,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                lineHeight = 28.sp,
                letterSpacing = 0.sp
            ),
            titleMedium = TextStyle(
                fontFamily = oswald,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                lineHeight = 24.sp,
                letterSpacing = 0.15.sp
            ),
            titleSmall = TextStyle(
                fontFamily = oswald,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                letterSpacing = 0.1.sp
            ),

            // Body styles - Lato for readable body text
            bodyLarge = TextStyle(
                fontFamily = lato,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                lineHeight = 24.sp,
                letterSpacing = 0.5.sp
            ),
            bodyMedium = TextStyle(
                fontFamily = lato,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                letterSpacing = 0.25.sp
            ),
            bodySmall = TextStyle(
                fontFamily = lato,
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                lineHeight = 16.sp,
                letterSpacing = 0.4.sp
            ),

            // Label styles - Lato Medium for UI labels
            labelLarge = TextStyle(
                fontFamily = lato,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                letterSpacing = 0.1.sp
            ),
            labelMedium = TextStyle(
                fontFamily = lato,
                fontWeight = FontWeight.Medium,
                fontSize = 12.sp,
                lineHeight = 16.sp,
                letterSpacing = 0.5.sp
            ),
            labelSmall = TextStyle(
                fontFamily = lato,
                fontWeight = FontWeight.Medium,
                fontSize = 11.sp,
                lineHeight = 16.sp,
                letterSpacing = 0.5.sp
            )
        )
    }
}
