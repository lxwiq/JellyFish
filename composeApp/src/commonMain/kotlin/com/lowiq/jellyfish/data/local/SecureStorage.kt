package com.lowiq.jellyfish.data.local

expect class SecureStorage {
    suspend fun saveToken(serverId: String, token: String)
    suspend fun getToken(serverId: String): String?
    suspend fun deleteToken(serverId: String)
    suspend fun clearAll()
}
