package com.lowiq.jellyfish.domain.model

data class EpisodeDetails(
    val id: String,
    val title: String,
    val overview: String?,
    val thumbnailUrl: String?,
    val seasonNumber: Int,
    val episodeNumber: Int,
    val runtime: String?,
    val rating: Float?,
    val seriesId: String,
    val seriesName: String,
    val guestStars: List<CastMember>,
    val seasonEpisodes: List<Episode>,
    val previousEpisodeId: String?,
    val nextEpisodeId: String?,
    val isFavorite: Boolean,
    val isWatched: Boolean,
    val progress: Float?,
    val playbackPositionTicks: Long?
)
