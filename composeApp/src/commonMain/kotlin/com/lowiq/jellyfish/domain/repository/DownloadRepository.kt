package com.lowiq.jellyfish.domain.repository

import com.lowiq.jellyfish.domain.model.Download
import com.lowiq.jellyfish.domain.model.DownloadStatus
import com.lowiq.jellyfish.domain.model.QualityOption
import kotlinx.coroutines.flow.Flow

interface DownloadRepository {
    fun getAllDownloads(): Flow<List<Download>>
    fun getActiveDownloads(): Flow<List<Download>>
    fun getCompletedDownloads(): Flow<List<Download>>
    suspend fun getDownloadByItemId(itemId: String): Download?

    suspend fun getAvailableQualities(serverId: String, itemId: String): Result<List<QualityOption>>
    suspend fun startDownload(
        serverId: String,
        itemId: String,
        title: String,
        subtitle: String?,
        imageUrl: String?,
        quality: QualityOption
    ): Result<Download>

    suspend fun pauseDownload(downloadId: String)
    suspend fun resumeDownload(downloadId: String)
    suspend fun cancelDownload(downloadId: String)
    suspend fun deleteDownload(downloadId: String)

    suspend fun getStorageInfo(): StorageInfo
}

data class StorageInfo(
    val usedBytes: Long,
    val availableBytes: Long,
    val limitBytes: Long?
)
