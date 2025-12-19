package com.lowiq.jellyfish.domain.model

data class MediaItem(
    val id: String,
    val title: String,
    val subtitle: String?,
    val imageUrl: String?,
    val progress: Float?, // 0-1 for resume items
    val isPoster: Boolean = false // true for movies/series (vertical), false for episodes (horizontal)
)
