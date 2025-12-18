package com.lowiq.jellyfish.domain.repository

import com.lowiq.jellyfish.domain.model.Server
import kotlinx.coroutines.flow.Flow

interface ServerRepository {
    fun getServers(): Flow<List<Server>>
    fun getActiveServer(): Flow<Server?>
    suspend fun addServer(url: String): Result<Server>
    suspend fun updateServer(server: Server)
    suspend fun removeServer(serverId: String)
    suspend fun setActiveServer(serverId: String)
}
