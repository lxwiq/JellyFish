package com.lowiq.jellyfish.data.local

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.prefs.Preferences
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec
import java.util.Base64

actual class SecureStorage {

    private val prefs = Preferences.userNodeForPackage(SecureStorage::class.java)

    // Simple encryption for desktop - uses a derived key from machine-specific data
    private val secretKey by lazy {
        val password = System.getProperty("user.name") + "jellyfish_secret"
        val salt = "jellyfish_salt_v1".toByteArray()
        val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
        val spec = PBEKeySpec(password.toCharArray(), salt, 65536, 256)
        SecretKeySpec(factory.generateSecret(spec).encoded, "AES")
    }

    actual suspend fun saveToken(serverId: String, token: String) {
        withContext(Dispatchers.IO) {
            val encrypted = encrypt(token)
            prefs.put(tokenKey(serverId), encrypted)
            prefs.flush()
        }
    }

    actual suspend fun getToken(serverId: String): String? {
        return withContext(Dispatchers.IO) {
            val encrypted = prefs.get(tokenKey(serverId), null)
            encrypted?.let { decrypt(it) }
        }
    }

    actual suspend fun deleteToken(serverId: String) {
        withContext(Dispatchers.IO) {
            prefs.remove(tokenKey(serverId))
            prefs.flush()
        }
    }

    actual suspend fun clearAll() {
        withContext(Dispatchers.IO) {
            prefs.keys().filter { it.startsWith("token_") }.forEach { prefs.remove(it) }
            prefs.flush()
        }
    }

    private fun encrypt(data: String): String {
        val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)
        val encrypted = cipher.doFinal(data.toByteArray(Charsets.UTF_8))
        return Base64.getEncoder().encodeToString(encrypted)
    }

    private fun decrypt(data: String): String {
        val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
        cipher.init(Cipher.DECRYPT_MODE, secretKey)
        val decoded = Base64.getDecoder().decode(data)
        return String(cipher.doFinal(decoded), Charsets.UTF_8)
    }

    private fun tokenKey(serverId: String) = "token_$serverId"
}
