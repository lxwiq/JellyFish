package com.lowiq.jellyfish.domain.player

data class AudioTrack(
    val index: Int,
    val language: String?,
    val label: String,
    val codec: String?,
    val channels: Int?,
    val isSelected: Boolean = false
)

data class SubtitleTrack(
    val index: Int,
    val language: String?,
    val label: String,
    val isSelected: Boolean = false
)

data class QualityOption(
    val index: Int,
    val label: String, // "1080p", "720p", "Auto", "Original"
    val bitrate: Long?,
    val isSelected: Boolean = false
)
