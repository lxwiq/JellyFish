package com.lowiq.jellyfish.data.repository

import com.lowiq.jellyfish.data.local.DownloadSettingsStorage
import com.lowiq.jellyfish.data.local.DownloadStorage
import com.lowiq.jellyfish.data.local.FileManager
import com.lowiq.jellyfish.data.local.SecureStorage
import com.lowiq.jellyfish.data.local.ServerStorage
import com.lowiq.jellyfish.data.remote.DownloadClient
import com.lowiq.jellyfish.data.remote.DownloadResult
import com.lowiq.jellyfish.data.remote.JellyfinDataSource
import com.lowiq.jellyfish.domain.model.Download
import com.lowiq.jellyfish.domain.model.DownloadStatus
import com.lowiq.jellyfish.domain.model.QualityOption
import com.lowiq.jellyfish.domain.repository.DeleteAllResult
import com.lowiq.jellyfish.domain.repository.DownloadRepository
import com.lowiq.jellyfish.domain.repository.StorageInfo
import com.lowiq.jellyfish.util.currentTimeMillis
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class DownloadRepositoryImpl(
    private val downloadStorage: DownloadStorage,
    private val downloadSettings: DownloadSettingsStorage,
    private val serverStorage: ServerStorage,
    private val secureStorage: SecureStorage,
    private val fileManager: FileManager,
    private val jellyfinDataSource: JellyfinDataSource,
    private val downloadClient: DownloadClient
) : DownloadRepository {

    override fun getAllDownloads(): Flow<List<Download>> = downloadStorage.getAllDownloads()

    override fun getActiveDownloads(): Flow<List<Download>> = downloadStorage.getAllDownloads().map { downloads ->
        downloads.filter { it.status == DownloadStatus.QUEUED || it.status == DownloadStatus.DOWNLOADING }
    }

    override fun getCompletedDownloads(): Flow<List<Download>> = downloadStorage.getAllDownloads().map { downloads ->
        downloads.filter { it.status == DownloadStatus.COMPLETED }
    }

    override suspend fun getDownloadByItemId(itemId: String): Download? {
        return downloadStorage.getAllDownloads().first().find { it.itemId == itemId }
    }

    override suspend fun getAvailableQualities(serverId: String, itemId: String): Result<List<QualityOption>> {
        val servers = serverStorage.getServers().first()
        val server = servers.find { it.id == serverId } ?: return Result.failure(Exception("Server not found"))
        val token = secureStorage.getToken(serverId) ?: return Result.failure(Exception("Not authenticated"))

        // Check if user can transcode
        val canTranscode = jellyfinDataSource.canUserTranscode(server.url, token)

        return jellyfinDataSource.getMediaSources(server.url, token, server.userId ?: "", itemId)
            .map { sources ->
                val original = sources.firstOrNull()
                val originalBitrate = original?.bitrate ?: 20_000_000

                val allQualities = listOf(
                    QualityOption("original", "Original", originalBitrate, original?.size ?: 0),
                    QualityOption("1080p", "1080p", 8_000_000, estimateSize(8_000_000, original?.size, originalBitrate)),
                    QualityOption("720p", "720p", 4_000_000, estimateSize(4_000_000, original?.size, originalBitrate)),
                    QualityOption("480p", "480p", 1_500_000, estimateSize(1_500_000, original?.size, originalBitrate))
                ).filter { it.bitrate <= originalBitrate }

                // If user can't transcode, only return Original quality
                if (canTranscode) allQualities else allQualities.filter { it.id == "original" }
            }
    }

    private fun estimateSize(targetBitrate: Int, originalSize: Long?, originalBitrate: Int): Long {
        if (originalSize == null || originalSize == 0L || originalBitrate == 0) return 0
        return (originalSize * targetBitrate.toLong() / originalBitrate)
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun startDownload(
        serverId: String,
        itemId: String,
        title: String,
        subtitle: String?,
        imageUrl: String?,
        quality: QualityOption
    ): Result<Download> {
        val servers = serverStorage.getServers().first()
        val server = servers.find { it.id == serverId } ?: return Result.failure(Exception("Server not found"))
        val token = secureStorage.getToken(serverId) ?: return Result.failure(Exception("Not authenticated"))

        val downloadId = Uuid.random().toString()
        val download = Download(
            id = downloadId,
            itemId = itemId,
            serverId = serverId,
            title = title,
            subtitle = subtitle,
            imageUrl = imageUrl,
            quality = quality.label,
            bitrate = quality.bitrate,
            status = DownloadStatus.QUEUED,
            totalBytes = quality.estimatedSizeBytes,
            createdAt = currentTimeMillis()
        )

        downloadStorage.saveDownload(download)
        return Result.success(download)
    }

    override suspend fun pauseDownload(downloadId: String) {
        downloadStorage.updateStatus(downloadId, DownloadStatus.PAUSED)
    }

    override suspend fun resumeDownload(downloadId: String) {
        downloadStorage.updateStatus(downloadId, DownloadStatus.QUEUED)
    }

    override suspend fun cancelDownload(downloadId: String) {
        val download = downloadStorage.getAllDownloads().first().find { it.id == downloadId }
        download?.filePath?.let { fileManager.deleteFile(it) }
        downloadStorage.removeDownload(downloadId)
    }

    override suspend fun deleteDownload(downloadId: String) {
        val download = downloadStorage.getAllDownloads().first().find { it.id == downloadId }
        download?.filePath?.let { fileManager.deleteFile(it) }
        downloadStorage.removeDownload(downloadId)
    }

    override suspend fun deleteAllDownloads(): DeleteAllResult {
        val downloads = downloadStorage.getAllDownloads().first()
        var freedBytes = 0L

        downloads.forEach { download ->
            download.filePath?.let { path ->
                if (fileManager.fileExists(path)) {
                    freedBytes += fileManager.getFileSize(path)
                    fileManager.deleteFile(path)
                }
            }
        }

        downloadStorage.clearAll()

        return DeleteAllResult(
            deletedCount = downloads.size,
            freedBytes = freedBytes
        )
    }

    override suspend fun updatePlaybackPosition(downloadId: String, positionMs: Long) {
        downloadStorage.updatePlaybackPosition(downloadId, positionMs)
    }

    override suspend fun getStorageInfo(): StorageInfo {
        val limitMb = downloadSettings.storageLimitMb.first()
        val autoCleanup = downloadSettings.autoCleanup.first()

        return StorageInfo(
            usedBytes = fileManager.getUsedSpaceBytes(),
            availableBytes = fileManager.getAvailableSpaceBytes(),
            limitBytes = if (autoCleanup) limitMb.toLong() * 1024 * 1024 else null
        )
    }
}
