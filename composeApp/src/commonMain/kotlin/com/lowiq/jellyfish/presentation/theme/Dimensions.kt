package com.lowiq.jellyfish.presentation.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Shadcn/ui dimension tokens
 * Based on the CSS variables from shadcn/ui theme
 */
data class JellyFishDimensions(
    // Border radius - based on --radius: 0.625rem (10px)
    val radiusNone: Dp = 0.dp,
    val radiusSm: Dp = 6.dp,      // radius - 4px
    val radius: Dp = 10.dp,       // --radius: 0.625rem
    val radiusMd: Dp = 12.dp,     // radius + 2px
    val radiusLg: Dp = 14.dp,     // radius + 4px
    val radiusXl: Dp = 18.dp,     // radius + 8px
    val radiusFull: Dp = 9999.dp, // Full rounded (pill shape)

    // Spacing scale
    val spacing0: Dp = 0.dp,
    val spacing1: Dp = 4.dp,
    val spacing2: Dp = 8.dp,
    val spacing3: Dp = 12.dp,
    val spacing4: Dp = 16.dp,
    val spacing5: Dp = 20.dp,
    val spacing6: Dp = 24.dp,
    val spacing8: Dp = 32.dp,
    val spacing10: Dp = 40.dp,
    val spacing12: Dp = 48.dp,

    // Border widths
    val borderWidth: Dp = 1.dp,
    val borderWidthMedium: Dp = 2.dp,

    // Component specific
    val buttonHeight: Dp = 40.dp,
    val buttonHeightSm: Dp = 32.dp,
    val buttonHeightLg: Dp = 48.dp,
    val inputHeight: Dp = 40.dp,
    val iconSizeSm: Dp = 16.dp,
    val iconSize: Dp = 20.dp,
    val iconSizeLg: Dp = 24.dp,
    val iconSizeXl: Dp = 32.dp,

    // Card dimensions
    val cardPadding: Dp = 16.dp,
    val cardPaddingSm: Dp = 12.dp,

    // Avatar sizes
    val avatarSm: Dp = 32.dp,
    val avatar: Dp = 40.dp,
    val avatarLg: Dp = 56.dp,
    val avatarXl: Dp = 80.dp,

    // Poster card dimensions (mobile defaults, use posterCardWidth/posterCardHeight for responsive)
    val posterWidthMobile: Dp = 120.dp,
    val posterHeightMobile: Dp = 180.dp,  // 2:3 ratio
    val posterWidthDesktop: Dp = 160.dp,
    val posterHeightDesktop: Dp = 240.dp,  // 2:3 ratio

    // Episode card dimensions (mobile defaults)
    val episodeWidthMobile: Dp = 160.dp,
    val episodeHeightMobile: Dp = 90.dp,  // 16:9 ratio
    val episodeWidthDesktop: Dp = 200.dp,
    val episodeHeightDesktop: Dp = 112.dp  // 16:9 ratio
)

/**
 * Pre-defined shapes using the dimension tokens
 */
data class JellyFishShapes(
    private val dimensions: JellyFishDimensions = JellyFishDimensions()
) {
    val none = RoundedCornerShape(dimensions.radiusNone)
    val sm = RoundedCornerShape(dimensions.radiusSm)
    val default = RoundedCornerShape(dimensions.radius)
    val md = RoundedCornerShape(dimensions.radiusMd)
    val lg = RoundedCornerShape(dimensions.radiusLg)
    val xl = RoundedCornerShape(dimensions.radiusXl)
    val full = RoundedCornerShape(dimensions.radiusFull)

    // Component specific shapes
    val card = RoundedCornerShape(dimensions.radius)
    val button = RoundedCornerShape(dimensions.radiusSm)
    val input = RoundedCornerShape(dimensions.radiusSm)
    val badge = RoundedCornerShape(dimensions.radiusFull)
    val avatar = RoundedCornerShape(dimensions.radiusFull)
}

val LocalJellyFishDimensions = staticCompositionLocalOf { JellyFishDimensions() }
val LocalJellyFishShapes = staticCompositionLocalOf { JellyFishShapes() }

/**
 * Responsive card dimensions based on platform
 */
data class ResponsiveCardDimensions(
    val posterWidth: Dp,
    val posterHeight: Dp,
    val episodeWidth: Dp,
    val episodeHeight: Dp
)

/**
 * Returns responsive card dimensions based on whether running on desktop or mobile
 */
fun getResponsiveCardDimensions(isDesktop: Boolean): ResponsiveCardDimensions {
    val dimensions = JellyFishDimensions()
    return if (isDesktop) {
        ResponsiveCardDimensions(
            posterWidth = dimensions.posterWidthDesktop,
            posterHeight = dimensions.posterHeightDesktop,
            episodeWidth = dimensions.episodeWidthDesktop,
            episodeHeight = dimensions.episodeHeightDesktop
        )
    } else {
        ResponsiveCardDimensions(
            posterWidth = dimensions.posterWidthMobile,
            posterHeight = dimensions.posterHeightMobile,
            episodeWidth = dimensions.episodeWidthMobile,
            episodeHeight = dimensions.episodeHeightMobile
        )
    }
}
