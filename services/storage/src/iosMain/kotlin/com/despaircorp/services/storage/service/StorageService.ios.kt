package com.despaircorp.services.storage.service

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.value
import platform.CoreFoundation.CFDictionaryAddValue
import platform.CoreFoundation.CFDictionaryCreateMutable
import platform.CoreFoundation.CFTypeRefVar
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
import platform.Security.kSecReturnData
import platform.Security.kSecValueData

@OptIn(ExperimentalForeignApi::class)
actual class StorageService : StorageServiceContract {

    actual override fun setIsOnboardDone(isDone: Boolean) {
        setKeychainValue(IS_TUTO_DONE_KEY, if (isDone) "true" else "false")
    }

    actual override fun getIsOnboardDone(): Boolean {
        return getKeychainValue(IS_TUTO_DONE_KEY) == "true"
    }

    private fun setKeychainValue(key: String, value: String) {
        deleteKeychainValue(key)

        memScoped {
            val query = CFDictionaryCreateMutable(null, 4, null, null)
            CFDictionaryAddValue(query, kSecClass, kSecClassGenericPassword)
            CFDictionaryAddValue(query, kSecAttrService, CFBridgingRetain(KEYCHAIN_SERVICE))
            CFDictionaryAddValue(query, kSecAttrAccount, CFBridgingRetain(key))
            val data = (value as NSString).dataUsingEncoding(NSUTF8StringEncoding)
            CFDictionaryAddValue(query, kSecValueData, CFBridgingRetain(data))

            SecItemAdd(query, null)
        }
    }

    private fun getKeychainValue(key: String): String? {
        memScoped {
            val query = CFDictionaryCreateMutable(null, 4, null, null)
            CFDictionaryAddValue(query, kSecClass, kSecClassGenericPassword)
            CFDictionaryAddValue(query, kSecAttrService, CFBridgingRetain(KEYCHAIN_SERVICE))
            CFDictionaryAddValue(query, kSecAttrAccount, CFBridgingRetain(key))
            CFDictionaryAddValue(query, kSecReturnData, kCFBooleanTrue)

            val result = alloc<CFTypeRefVar>()
            val status = SecItemCopyMatching(query, result.ptr)

            if (status == errSecSuccess) {
                val data = CFBridgingRelease(result.value) as? NSData ?: return null
                return NSString.create(data = data, encoding = NSUTF8StringEncoding) as? String
            }
        }
        return null
    }

    private fun deleteKeychainValue(key: String) {
        memScoped {
            val query = CFDictionaryCreateMutable(null, 3, null, null)
            CFDictionaryAddValue(query, kSecClass, kSecClassGenericPassword)
            CFDictionaryAddValue(query, kSecAttrService, CFBridgingRetain(KEYCHAIN_SERVICE))
            CFDictionaryAddValue(query, kSecAttrAccount, CFBridgingRetain(key))

            SecItemDelete(query)
        }
    }

    companion object {
        private const val KEYCHAIN_SERVICE = "com.despaircorp.trackshift"
        private const val IS_TUTO_DONE_KEY = "isTutoDone"
    }
}
