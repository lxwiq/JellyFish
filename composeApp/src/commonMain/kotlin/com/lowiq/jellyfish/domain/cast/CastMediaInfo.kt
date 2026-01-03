package com.lowiq.jellyfish.domain.cast

enum class CastMediaType {
    VIDEO,
    AUDIO
}

data class CastSubtitleTrack(
    val index: Int,
    val name: String,
    val language: String?,
    val url: String
)

data class CastAudioTrack(
    val index: Int,
    val name: String,
    val language: String?
)

data class CastMediaInfo(
    val itemId: String,
    val title: String,
    val subtitle: String? = null,
    val streamUrl: String,
    val imageUrl: String? = null,
    val durationMs: Long,
    val mediaType: CastMediaType,
    val subtitleTracks: List<CastSubtitleTrack> = emptyList(),
    val audioTracks: List<CastAudioTrack> = emptyList()
)
