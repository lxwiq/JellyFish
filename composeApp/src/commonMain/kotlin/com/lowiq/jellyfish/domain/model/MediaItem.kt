package com.lowiq.jellyfish.domain.model

data class MediaItem(
    val id: String,
    val title: String,
    val subtitle: String?,
    val imageUrl: String?,
    val progress: Float? // 0-1 for resume items
)
