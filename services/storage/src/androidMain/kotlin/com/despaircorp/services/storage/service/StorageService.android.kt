package com.despaircorp.services.storage.service

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

actual class StorageService(context: Context) : StorageServiceContract {

    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val prefs: SharedPreferences = EncryptedSharedPreferences.create(
        context,
        "encrypted_app_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    actual override fun setIsOnboardDone(isDone: Boolean) {
        prefs.edit { putBoolean(IS_TUTO_DONE_KEY, isDone) }
    }

    actual override fun getIsOnboardDone(): Boolean = prefs.getBoolean(IS_TUTO_DONE_KEY, false)

    companion object {
        private const val IS_TUTO_DONE_KEY = "isTutoDone"
    }
}