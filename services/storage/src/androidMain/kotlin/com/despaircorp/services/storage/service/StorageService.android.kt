package com.despaircorp.services.storage.service

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

actual class StorageService(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    actual fun setIsOnboardDone(isDone: Boolean) {
        prefs.edit { putBoolean(IS_TUTO_DONE_KEY, isDone) }
    }

    actual fun getIsOnboardDone(): Boolean = prefs.getBoolean(IS_TUTO_DONE_KEY, false)

    companion object {
        private const val IS_TUTO_DONE_KEY = "isTutoDone"
    }
}