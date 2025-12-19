package com.lowiq.jellyfish.domain.model

data class MovieDetails(
    val id: String,
    val title: String,
    val overview: String?,
    val backdropUrl: String?,
    val posterUrl: String?,
    val year: String?,
    val runtime: String?,
    val rating: Float?,
    val genres: List<String>,
    val studio: String?,
    val cast: List<CastMember>,
    val similarItems: List<MediaItem>,
    val trailerUrl: String?,
    val isFavorite: Boolean,
    val isWatched: Boolean
)
