package com.lowiq.jellyfish.data.remote

import com.lowiq.jellyfish.domain.model.User

interface JellyfinDataSource {
    suspend fun getServerInfo(serverUrl: String): Result<ServerInfo>
    suspend fun login(serverUrl: String, username: String, password: String): Result<AuthResult>
    suspend fun initiateQuickConnect(serverUrl: String): Result<QuickConnectResult>
    suspend fun checkQuickConnect(serverUrl: String, secret: String): Result<QuickConnectStatus>
    suspend fun logout(serverUrl: String, token: String)
    suspend fun getCurrentUser(serverUrl: String, token: String): Result<User>
}

data class ServerInfo(
    val id: String,
    val name: String,
    val version: String
)

data class AuthResult(
    val userId: String,
    val username: String,
    val token: String,
    val serverId: String
)

data class QuickConnectResult(
    val secret: String,
    val code: String
)

sealed class QuickConnectStatus {
    object Waiting : QuickConnectStatus()
    data class Authenticated(val token: String, val userId: String) : QuickConnectStatus()
    object Expired : QuickConnectStatus()
}
