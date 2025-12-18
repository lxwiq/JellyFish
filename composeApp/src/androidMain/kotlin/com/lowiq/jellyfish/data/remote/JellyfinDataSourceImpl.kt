package com.lowiq.jellyfish.data.remote

import android.content.Context
import com.lowiq.jellyfish.domain.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jellyfin.sdk.Jellyfin
import org.jellyfin.sdk.api.client.ApiClient
import org.jellyfin.sdk.api.client.exception.InvalidStatusException
import org.jellyfin.sdk.api.client.extensions.authenticateUserByName
import org.jellyfin.sdk.api.client.extensions.authenticateWithQuickConnect
import org.jellyfin.sdk.api.client.extensions.quickConnectApi
import org.jellyfin.sdk.api.client.extensions.sessionApi
import org.jellyfin.sdk.api.client.extensions.systemApi
import org.jellyfin.sdk.api.client.extensions.userApi
import org.jellyfin.sdk.createJellyfin
import org.jellyfin.sdk.model.ClientInfo

class JellyfinDataSourceImpl(
    private val context: Context
) : JellyfinDataSource {

    private val jellyfin: Jellyfin by lazy {
        createJellyfin {
            clientInfo = ClientInfo(
                name = "JellyFish",
                version = "1.0.0"
            )
            context = this@JellyfinDataSourceImpl.context
        }
    }

    private fun createApi(serverUrl: String, token: String? = null): ApiClient {
        return jellyfin.createApi(
            baseUrl = serverUrl,
            accessToken = token
        )
    }

    override suspend fun getServerInfo(serverUrl: String): Result<ServerInfo> = withContext(Dispatchers.IO) {
        runCatching {
            val api = createApi(serverUrl)
            val info by api.systemApi.getPublicSystemInfo()
            ServerInfo(
                id = info.id ?: "",
                name = info.serverName ?: "Jellyfin Server",
                version = info.version ?: ""
            )
        }
    }

    override suspend fun login(
        serverUrl: String,
        username: String,
        password: String
    ): Result<AuthResult> = withContext(Dispatchers.IO) {
        runCatching {
            val api = createApi(serverUrl)
            val authResult by api.userApi.authenticateUserByName(
                username = username,
                password = password
            )
            AuthResult(
                userId = authResult.user?.id?.toString() ?: "",
                username = authResult.user?.name ?: username,
                token = authResult.accessToken ?: "",
                serverId = authResult.serverId ?: ""
            )
        }
    }

    override suspend fun initiateQuickConnect(serverUrl: String): Result<QuickConnectResult> =
        withContext(Dispatchers.IO) {
            runCatching {
                val api = createApi(serverUrl)
                val state by api.quickConnectApi.initiateQuickConnect()
                QuickConnectResult(
                    secret = state.secret ?: "",
                    code = state.code ?: ""
                )
            }
        }

    override suspend fun checkQuickConnect(
        serverUrl: String,
        secret: String
    ): Result<QuickConnectStatus> = withContext(Dispatchers.IO) {
        runCatching {
            val api = createApi(serverUrl)
            val state by api.quickConnectApi.getQuickConnectState(secret = secret)

            if (state.authenticated == true) {
                val authResult by api.userApi.authenticateWithQuickConnect(secret = secret)
                QuickConnectStatus.Authenticated(
                    token = authResult.accessToken ?: "",
                    userId = authResult.user?.id?.toString() ?: ""
                )
            } else {
                QuickConnectStatus.Waiting
            }
        }
    }

    override suspend fun logout(serverUrl: String, token: String) {
        withContext(Dispatchers.IO) {
            runCatching {
                val api = createApi(serverUrl, token)
                api.sessionApi.reportSessionEnded()
            }
        }
    }

    override suspend fun getCurrentUser(serverUrl: String, token: String): Result<User> =
        withContext(Dispatchers.IO) {
            runCatching {
                val api = createApi(serverUrl, token)
                val user by api.userApi.getCurrentUser()
                User(
                    id = user.id.toString(),
                    name = user.name ?: "",
                    serverId = user.serverId ?: ""
                )
            }
        }
}
