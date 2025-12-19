package com.lowiq.jellyfish.domain.model

enum class DownloadStatus {
    QUEUED,
    DOWNLOADING,
    PAUSED,
    COMPLETED,
    FAILED
}

data class Download(
    val id: String,
    val itemId: String,
    val serverId: String,
    val title: String,
    val subtitle: String? = null,
    val imageUrl: String? = null,
    val quality: String,
    val bitrate: Int,
    val status: DownloadStatus,
    val progress: Float = 0f,
    val totalBytes: Long = 0,
    val downloadedBytes: Long = 0,
    val filePath: String? = null,
    val createdAt: Long,
    val completedAt: Long? = null,
    val errorMessage: String? = null
)

data class QualityOption(
    val id: String,
    val label: String,
    val bitrate: Int,
    val estimatedSizeBytes: Long
)

data class PendingPlaybackSync(
    val id: String,
    val itemId: String,
    val serverId: String,
    val positionTicks: Long,
    val playedPercentage: Float,
    val timestamp: Long
)
