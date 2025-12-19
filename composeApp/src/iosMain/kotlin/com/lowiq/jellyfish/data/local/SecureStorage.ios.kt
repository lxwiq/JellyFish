package com.lowiq.jellyfish.data.local

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.value
import platform.CoreFoundation.CFDictionaryAddValue
import platform.CoreFoundation.CFDictionaryCreateMutable
import platform.CoreFoundation.CFStringRef
import platform.CoreFoundation.CFTypeRef
import platform.CoreFoundation.kCFAllocatorDefault
import platform.CoreFoundation.kCFBooleanTrue
import platform.Foundation.CFBridgingRelease
import platform.Foundation.CFBridgingRetain
import platform.Foundation.NSData
import platform.Foundation.NSString
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.create
import platform.Foundation.dataUsingEncoding
import platform.Security.SecItemAdd
import platform.Security.SecItemCopyMatching
import platform.Security.SecItemDelete
import platform.Security.errSecSuccess
import platform.Security.kSecAttrAccount
import platform.Security.kSecAttrService
import platform.Security.kSecClass
import platform.Security.kSecClassGenericPassword
import platform.Security.kSecMatchLimit
import platform.Security.kSecMatchLimitOne
import platform.Security.kSecReturnData
import platform.Security.kSecValueData
import platform.darwin.OSStatus

@OptIn(ExperimentalForeignApi::class)
actual class SecureStorage {
    private val serviceName = "com.lowiq.jellyfish"

    actual suspend fun saveToken(serverId: String, token: String) {
        // Delete existing token first
        deleteToken(serverId)

        val tokenData = (token as NSString).dataUsingEncoding(NSUTF8StringEncoding) ?: return

        memScoped {
            val query = CFDictionaryCreateMutable(kCFAllocatorDefault, 4, null, null)
            CFDictionaryAddValue(query, kSecClass, kSecClassGenericPassword)
            CFDictionaryAddValue(query, kSecAttrService, CFBridgingRetain(serviceName))
            CFDictionaryAddValue(query, kSecAttrAccount, CFBridgingRetain(tokenKey(serverId)))
            CFDictionaryAddValue(query, kSecValueData, CFBridgingRetain(tokenData))

            SecItemAdd(query, null)
        }
    }

    actual suspend fun getToken(serverId: String): String? {
        memScoped {
            val query = CFDictionaryCreateMutable(kCFAllocatorDefault, 5, null, null)
            CFDictionaryAddValue(query, kSecClass, kSecClassGenericPassword)
            CFDictionaryAddValue(query, kSecAttrService, CFBridgingRetain(serviceName))
            CFDictionaryAddValue(query, kSecAttrAccount, CFBridgingRetain(tokenKey(serverId)))
            CFDictionaryAddValue(query, kSecReturnData, kCFBooleanTrue)
            CFDictionaryAddValue(query, kSecMatchLimit, kSecMatchLimitOne)

            val result = alloc<CFTypeRef?>()
            val status: OSStatus = SecItemCopyMatching(query, result.ptr)

            if (status == errSecSuccess) {
                val data = CFBridgingRelease(result.value) as? NSData
                return data?.let {
                    NSString.create(data = it, encoding = NSUTF8StringEncoding) as? String
                }
            }
            return null
        }
    }

    actual suspend fun deleteToken(serverId: String) {
        memScoped {
            val query = CFDictionaryCreateMutable(kCFAllocatorDefault, 3, null, null)
            CFDictionaryAddValue(query, kSecClass, kSecClassGenericPassword)
            CFDictionaryAddValue(query, kSecAttrService, CFBridgingRetain(serviceName))
            CFDictionaryAddValue(query, kSecAttrAccount, CFBridgingRetain(tokenKey(serverId)))

            SecItemDelete(query)
        }
    }

    actual suspend fun clearAll() {
        memScoped {
            val query = CFDictionaryCreateMutable(kCFAllocatorDefault, 2, null, null)
            CFDictionaryAddValue(query, kSecClass, kSecClassGenericPassword)
            CFDictionaryAddValue(query, kSecAttrService, CFBridgingRetain(serviceName))

            SecItemDelete(query)
        }
    }

    private fun tokenKey(serverId: String) = "token_$serverId"
}
