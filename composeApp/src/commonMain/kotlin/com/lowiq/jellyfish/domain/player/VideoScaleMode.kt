package com.lowiq.jellyfish.domain.player

/**
 * Video scaling modes for fullscreen playback.
 */
enum class VideoScaleMode(val displayName: String) {
    /** Full video visible, letterboxed if needed (default) */
    FIT("Fit"),

    /** Fill screen, may crop edges */
    FILL("Fill"),

    /** Stretch to fill (may distort) */
    STRETCH("Stretch");

    /**
     * Cycles to the next scale mode: FIT → FILL → STRETCH → FIT
     */
    fun next(): VideoScaleMode = when (this) {
        FIT -> FILL
        FILL -> STRETCH
        STRETCH -> FIT
    }
}
