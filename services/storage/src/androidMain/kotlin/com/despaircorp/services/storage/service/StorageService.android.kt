package com.despaircorp.services.storage.service

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

actual class StorageService(context: Context) : StorageServiceContract {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    actual override fun setIsOnboardDone(isDone: Boolean) {
        prefs.edit { putBoolean(IS_TUTO_DONE_KEY, isDone) }
    }

    actual override fun getIsOnboardDone(): Boolean = prefs.getBoolean(IS_TUTO_DONE_KEY, false)

    companion object {
        private const val IS_TUTO_DONE_KEY = "isTutoDone"
    }
}