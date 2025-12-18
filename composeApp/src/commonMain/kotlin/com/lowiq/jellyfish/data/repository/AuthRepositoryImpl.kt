package com.lowiq.jellyfish.data.repository

import com.lowiq.jellyfish.data.local.SecureStorage
import com.lowiq.jellyfish.data.local.ServerStorage
import com.lowiq.jellyfish.data.remote.JellyfinDataSource
import com.lowiq.jellyfish.data.remote.QuickConnectStatus
import com.lowiq.jellyfish.domain.model.Server
import com.lowiq.jellyfish.domain.model.User
import com.lowiq.jellyfish.domain.repository.AuthRepository
import kotlinx.coroutines.flow.first

class AuthRepositoryImpl(
    private val jellyfinDataSource: JellyfinDataSource,
    private val secureStorage: SecureStorage,
    private val serverStorage: ServerStorage
) : AuthRepository {

    override suspend fun login(
        serverUrl: String,
        username: String,
        password: String
    ): Result<User> {
        return jellyfinDataSource.login(serverUrl, username, password)
            .mapCatching { authResult ->
                // Save the token
                secureStorage.saveToken(authResult.serverId, authResult.token)

                // Update server with user info
                val servers = serverStorage.getServers().first()
                val server = servers.find { it.url == serverUrl }
                if (server != null) {
                    val updatedServer = server.copy(
                        userId = authResult.userId,
                        username = authResult.username
                    )
                    serverStorage.saveServer(updatedServer)
                }

                User(
                    id = authResult.userId,
                    name = authResult.username,
                    serverId = authResult.serverId
                )
            }
    }

    override suspend fun initiateQuickConnect(serverUrl: String): Result<String> {
        return jellyfinDataSource.initiateQuickConnect(serverUrl)
            .map { it.code }
    }

    override suspend fun pollQuickConnect(serverUrl: String, secret: String): Result<User?> {
        return jellyfinDataSource.checkQuickConnect(serverUrl, secret)
            .mapCatching { status ->
                when (status) {
                    is QuickConnectStatus.Authenticated -> {
                        // Save the token
                        val servers = serverStorage.getServers().first()
                        val server = servers.find { it.url == serverUrl }

                        if (server != null) {
                            secureStorage.saveToken(server.id, status.token)

                            // Get user info
                            val userResult = jellyfinDataSource.getCurrentUser(serverUrl, status.token)
                            userResult.getOrNull()?.also { user ->
                                val updatedServer = server.copy(
                                    userId = user.id,
                                    username = user.name
                                )
                                serverStorage.saveServer(updatedServer)
                            }
                        } else null
                    }
                    QuickConnectStatus.Waiting -> null
                    QuickConnectStatus.Expired -> throw Exception("Quick Connect session expired")
                }
            }
    }

    override suspend fun logout(serverId: String) {
        val servers = serverStorage.getServers().first()
        val server = servers.find { it.id == serverId } ?: return
        val token = secureStorage.getToken(serverId) ?: return

        jellyfinDataSource.logout(server.url, token)
        secureStorage.deleteToken(serverId)

        // Clear user info from server
        val updatedServer = server.copy(userId = null, username = null)
        serverStorage.saveServer(updatedServer)
    }

    override suspend fun getCurrentUser(serverId: String): User? {
        val servers = serverStorage.getServers().first()
        val server = servers.find { it.id == serverId } ?: return null
        val token = secureStorage.getToken(serverId) ?: return null

        return jellyfinDataSource.getCurrentUser(server.url, token).getOrNull()
    }

    override suspend fun isAuthenticated(serverId: String): Boolean {
        return secureStorage.getToken(serverId) != null
    }
}
