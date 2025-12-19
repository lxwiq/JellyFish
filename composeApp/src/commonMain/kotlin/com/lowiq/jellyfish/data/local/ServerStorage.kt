package com.lowiq.jellyfish.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.lowiq.jellyfish.domain.model.Server
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class ServerStorage(
    private val dataStore: DataStore<Preferences>,
    private val secureStorage: SecureStorage
) {

    private val serversKey = stringPreferencesKey("servers")
    private val activeServerKey = stringPreferencesKey("active_server_id")

    fun getServers(): Flow<List<Server>> = dataStore.data.map { prefs ->
        val json = prefs[serversKey] ?: "[]"
        parseServers(json)
    }

    fun getActiveServerId(): Flow<String?> = dataStore.data.map { prefs ->
        prefs[activeServerKey]
    }

    suspend fun saveServer(server: Server) {
        dataStore.edit { prefs ->
            val current = parseServers(prefs[serversKey] ?: "[]").toMutableList()
            val index = current.indexOfFirst { it.id == server.id }
            if (index >= 0) {
                current[index] = server
            } else {
                current.add(server)
            }
            prefs[serversKey] = serializeServers(current)
        }
    }

    suspend fun removeServer(serverId: String) {
        dataStore.edit { prefs ->
            val current = parseServers(prefs[serversKey] ?: "[]")
                .filter { it.id != serverId }
            prefs[serversKey] = serializeServers(current)

            if (prefs[activeServerKey] == serverId) {
                prefs.remove(activeServerKey)
            }
        }
    }

    suspend fun setActiveServer(serverId: String) {
        dataStore.edit { prefs ->
            prefs[activeServerKey] = serverId
        }
    }

    // Simple JSON serialization without external library
    private fun serializeServers(servers: List<Server>): String {
        return servers.joinToString(separator = "|||") { server ->
            "${server.id}::${server.name}::${server.url}::${server.userId.orEmpty()}::${server.username.orEmpty()}"
        }
    }

    private fun parseServers(data: String): List<Server> {
        if (data.isBlank() || data == "[]") return emptyList()
        return data.split("|||").mapNotNull { entry ->
            val parts = entry.split("::")
            if (parts.size >= 3) {
                Server(
                    id = parts[0],
                    name = parts[1],
                    url = parts[2],
                    userId = parts.getOrNull(3)?.takeIf { it.isNotEmpty() },
                    username = parts.getOrNull(4)?.takeIf { it.isNotEmpty() }
                )
            } else null
        }
    }

    suspend fun getServer(serverId: String): Server? {
        return getServers().first().find { it.id == serverId }
    }

    suspend fun getToken(serverId: String): String? {
        return secureStorage.getToken(serverId)
    }
}
