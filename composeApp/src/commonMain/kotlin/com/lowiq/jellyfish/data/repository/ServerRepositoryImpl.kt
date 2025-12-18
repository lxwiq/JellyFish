package com.lowiq.jellyfish.data.repository

import com.lowiq.jellyfish.data.local.SecureStorage
import com.lowiq.jellyfish.data.local.ServerStorage
import com.lowiq.jellyfish.data.remote.JellyfinDataSource
import com.lowiq.jellyfish.domain.model.Server
import com.lowiq.jellyfish.domain.repository.ServerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class ServerRepositoryImpl(
    private val jellyfinDataSource: JellyfinDataSource,
    private val serverStorage: ServerStorage,
    private val secureStorage: SecureStorage
) : ServerRepository {

    override fun getServers(): Flow<List<Server>> = serverStorage.getServers()

    override fun getActiveServer(): Flow<Server?> {
        return combine(
            serverStorage.getServers(),
            serverStorage.getActiveServerId()
        ) { servers, activeId ->
            servers.find { it.id == activeId }
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun addServer(url: String): Result<Server> {
        // Normalize URL
        val normalizedUrl = normalizeUrl(url)

        // Check if server already exists
        val existingServers = serverStorage.getServers().first()
        val existing = existingServers.find { it.url == normalizedUrl }
        if (existing != null) {
            return Result.success(existing)
        }

        // Validate server and get info
        return jellyfinDataSource.getServerInfo(normalizedUrl)
            .mapCatching { serverInfo ->
                val server = Server(
                    id = Uuid.random().toString(),
                    name = serverInfo.name,
                    url = normalizedUrl,
                    userId = null,
                    username = null
                )
                serverStorage.saveServer(server)
                server
            }
    }

    override suspend fun updateServer(server: Server) {
        serverStorage.saveServer(server)
    }

    override suspend fun removeServer(serverId: String) {
        // Delete token first
        secureStorage.deleteToken(serverId)
        // Then remove server
        serverStorage.removeServer(serverId)
    }

    override suspend fun setActiveServer(serverId: String) {
        serverStorage.setActiveServer(serverId)
    }

    private fun normalizeUrl(url: String): String {
        var normalized = url.trim()

        // Add https if no protocol
        if (!normalized.startsWith("http://") && !normalized.startsWith("https://")) {
            normalized = "https://$normalized"
        }

        // Remove trailing slash
        if (normalized.endsWith("/")) {
            normalized = normalized.dropLast(1)
        }

        return normalized
    }
}
