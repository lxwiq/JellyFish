package com.lowiq.jellyfish.data.repository

import com.lowiq.jellyfish.data.local.SecureStorage
import com.lowiq.jellyfish.data.local.ServerStorage
import com.lowiq.jellyfish.data.remote.JellyfinDataSource
import com.lowiq.jellyfish.domain.model.AdminUser
import com.lowiq.jellyfish.domain.model.LogEntry
import com.lowiq.jellyfish.domain.model.ScheduledTask
import com.lowiq.jellyfish.domain.repository.AdminRepository
import kotlinx.coroutines.flow.first

class AdminRepositoryImpl(
    private val jellyfinDataSource: JellyfinDataSource,
    private val serverStorage: ServerStorage,
    private val secureStorage: SecureStorage
) : AdminRepository {

    private suspend fun getServerAndToken(): Pair<String, String>? {
        val serverId = serverStorage.getActiveServerId().first() ?: return null
        val servers = serverStorage.getServers().first()
        val server = servers.find { it.id == serverId } ?: return null
        val token = secureStorage.getToken(serverId) ?: return null
        return server.url to token
    }

    override suspend fun getUsers(): Result<List<AdminUser>> {
        val (serverUrl, token) = getServerAndToken()
            ?: return Result.failure(Exception("Not authenticated"))
        return jellyfinDataSource.getUsers(serverUrl, token)
    }

    override suspend fun createUser(username: String, password: String): Result<AdminUser> {
        val (serverUrl, token) = getServerAndToken()
            ?: return Result.failure(Exception("Not authenticated"))
        return jellyfinDataSource.createUser(serverUrl, token, username, password)
    }

    override suspend fun deleteUser(userId: String): Result<Unit> {
        val (serverUrl, token) = getServerAndToken()
            ?: return Result.failure(Exception("Not authenticated"))
        return jellyfinDataSource.deleteUser(serverUrl, token, userId)
    }

    override suspend fun refreshLibrary(): Result<Unit> {
        val (serverUrl, token) = getServerAndToken()
            ?: return Result.failure(Exception("Not authenticated"))
        return jellyfinDataSource.refreshLibrary(serverUrl, token)
    }

    override suspend fun getServerLogs(limit: Int): Result<List<LogEntry>> {
        val (serverUrl, token) = getServerAndToken()
            ?: return Result.failure(Exception("Not authenticated"))
        return jellyfinDataSource.getServerLogs(serverUrl, token, limit)
    }

    override suspend fun getScheduledTasks(): Result<List<ScheduledTask>> {
        val (serverUrl, token) = getServerAndToken()
            ?: return Result.failure(Exception("Not authenticated"))
        return jellyfinDataSource.getScheduledTasks(serverUrl, token)
    }

    override suspend fun runTask(taskId: String): Result<Unit> {
        val (serverUrl, token) = getServerAndToken()
            ?: return Result.failure(Exception("Not authenticated"))
        return jellyfinDataSource.runTask(serverUrl, token, taskId)
    }
}
