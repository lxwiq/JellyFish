package com.lowiq.jellyfish.domain.sync

import com.lowiq.jellyfish.data.local.PlaybackSyncStorage
import com.lowiq.jellyfish.data.local.ServerStorage
import com.lowiq.jellyfish.data.remote.JellyfinDataSource
import com.lowiq.jellyfish.data.remote.PlaybackProgressInfo
import com.lowiq.jellyfish.domain.model.PendingPlaybackSync
import com.lowiq.jellyfish.util.currentTimeMillis
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class PlaybackSyncService(
    private val playbackSyncStorage: PlaybackSyncStorage,
    private val serverStorage: ServerStorage,
    private val jellyfinDataSource: JellyfinDataSource
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var syncJob: Job? = null

    @OptIn(ExperimentalUuidApi::class)
    fun savePlaybackProgress(
        itemId: String,
        serverId: String,
        positionTicks: Long,
        playedPercentage: Float
    ) {
        scope.launch {
            val sync = PendingPlaybackSync(
                id = Uuid.random().toString(),
                itemId = itemId,
                serverId = serverId,
                positionTicks = positionTicks,
                playedPercentage = playedPercentage,
                timestamp = currentTimeMillis()
            )
            playbackSyncStorage.addPendingSync(sync)
        }
    }

    fun startPeriodicSync() {
        syncJob?.cancel()
        syncJob = scope.launch {
            while (isActive) {
                syncPendingProgress()
                delay(30_000) // Check every 30 seconds
            }
        }
    }

    fun stopPeriodicSync() {
        syncJob?.cancel()
        syncJob = null
    }

    @OptIn(ExperimentalUuidApi::class)
    suspend fun syncPendingProgress() {
        val pendingSyncs = playbackSyncStorage.getPendingSyncs().first()
        val servers = serverStorage.getServers().first()

        for (sync in pendingSyncs) {
            try {
                val server = servers.find { it.id == sync.serverId } ?: continue
                val token = serverStorage.getToken(sync.serverId) ?: continue

                // Create PlaybackProgressInfo for the sync
                val progressInfo = PlaybackProgressInfo(
                    itemId = sync.itemId,
                    mediaSourceId = sync.itemId, // Use itemId as mediaSourceId for offline syncs
                    positionTicks = sync.positionTicks,
                    isPaused = true,
                    playSessionId = Uuid.random().toString() // Generate a session ID for the sync
                )

                jellyfinDataSource.reportPlaybackProgress(
                    serverUrl = server.url,
                    token = token,
                    progress = progressInfo
                ).onSuccess {
                    playbackSyncStorage.removePendingSync(sync.id)
                }
            } catch (e: Exception) {
                // Will retry next time
            }
        }
    }

    fun syncNow() {
        scope.launch {
            syncPendingProgress()
        }
    }
}
