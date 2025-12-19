package com.lowiq.jellyfish.domain.model

data class SeriesDetails(
    val id: String,
    val title: String,
    val overview: String?,
    val backdropUrl: String?,
    val posterUrl: String?,
    val year: String?,
    val seasonCount: Int,
    val rating: Float?,
    val genres: List<String>,
    val studio: String?,
    val cast: List<CastMember>,
    val seasons: List<Season>,
    val similarItems: List<MediaItem>,
    val isFavorite: Boolean,
    val isWatched: Boolean
)
