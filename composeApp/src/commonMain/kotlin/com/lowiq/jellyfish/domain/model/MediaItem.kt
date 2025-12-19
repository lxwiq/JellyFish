package com.lowiq.jellyfish.domain.model

enum class MediaType {
    MOVIE,
    SERIES,
    EPISODE,
    MUSIC,
    OTHER
}

data class MediaItem(
    val id: String,
    val title: String,
    val subtitle: String?,
    val imageUrl: String?,
    val progress: Float?, // 0-1 for resume items
    val isPoster: Boolean = false, // true for movies/series (vertical), false for episodes (horizontal)
    val type: MediaType = MediaType.OTHER // Type of media content (movie, series, episode, music, etc.)
)
