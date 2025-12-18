package com.lowiq.jellyfish.data.local

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

actual class SecureStorage(private val context: Context) {

    private val masterKey by lazy {
        MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
    }

    private val sharedPreferences by lazy {
        EncryptedSharedPreferences.create(
            context,
            "jellyfish_secure_prefs",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    actual suspend fun saveToken(serverId: String, token: String) {
        withContext(Dispatchers.IO) {
            sharedPreferences.edit()
                .putString(tokenKey(serverId), token)
                .apply()
        }
    }

    actual suspend fun getToken(serverId: String): String? {
        return withContext(Dispatchers.IO) {
            sharedPreferences.getString(tokenKey(serverId), null)
        }
    }

    actual suspend fun deleteToken(serverId: String) {
        withContext(Dispatchers.IO) {
            sharedPreferences.edit()
                .remove(tokenKey(serverId))
                .apply()
        }
    }

    actual suspend fun clearAll() {
        withContext(Dispatchers.IO) {
            sharedPreferences.edit()
                .clear()
                .apply()
        }
    }

    private fun tokenKey(serverId: String) = "token_$serverId"
}
