package com.lowiq.jellyfish.domain.model

data class Episode(
    val id: String,
    val title: String,
    val overview: String?,
    val seasonNumber: Int,
    val episodeNumber: Int,
    val runtime: String?,
    val rating: Float?,
    val thumbnailUrl: String?,
    val progress: Float?,
    val isWatched: Boolean
)
