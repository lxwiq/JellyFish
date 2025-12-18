package com.lowiq.jellyfish.domain.repository

import com.lowiq.jellyfish.domain.model.User

interface AuthRepository {
    suspend fun login(serverUrl: String, username: String, password: String): Result<User>
    suspend fun initiateQuickConnect(serverUrl: String): Result<String>
    suspend fun pollQuickConnect(serverUrl: String, secret: String): Result<User?>
    suspend fun logout(serverId: String)
    suspend fun getCurrentUser(serverId: String): User?
    suspend fun isAuthenticated(serverId: String): Boolean
}
