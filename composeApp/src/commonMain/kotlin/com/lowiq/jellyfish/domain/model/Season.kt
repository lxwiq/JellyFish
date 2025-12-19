package com.lowiq.jellyfish.domain.model

data class Season(
    val id: String,
    val name: String,
    val number: Int,
    val episodeCount: Int,
    val imageUrl: String?
)
