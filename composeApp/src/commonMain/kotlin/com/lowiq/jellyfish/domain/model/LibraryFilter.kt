package com.lowiq.jellyfish.domain.model

data class LibraryFilters(
    val genres: List<String>,
    val years: List<Int>
)

data class PaginatedResult<T>(
    val items: List<T>,
    val totalCount: Int,
    val hasMore: Boolean
)

enum class SortOption(val apiValue: String, val displayName: String) {
    NAME("SortName", "Name"),
    DATE_ADDED("DateCreated", "Date Added"),
    YEAR("ProductionYear", "Year"),
    RATING("CommunityRating", "Rating")
}
