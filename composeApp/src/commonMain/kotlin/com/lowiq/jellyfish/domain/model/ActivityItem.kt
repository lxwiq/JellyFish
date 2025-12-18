package com.lowiq.jellyfish.domain.model

data class ActivityItem(
    val id: String,
    val title: String,
    val subtitle: String, // "Movie", "S1 E3", etc.
    val timestamp: String, // Relative time like "2 hours ago"
    val imageUrl: String?,
    val progress: Float?, // 0-1 for resume items
    val type: ActivityType
)

enum class ActivityType {
    ADDED, RESUME, FAVORITE, PLAYED
}
