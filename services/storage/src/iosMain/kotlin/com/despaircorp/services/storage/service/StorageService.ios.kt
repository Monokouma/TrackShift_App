package com.despaircorp.services.storage.service

import platform.Foundation.NSUserDefaults

actual class StorageService {
    private val defaults = NSUserDefaults.standardUserDefaults

    actual fun setIsOnboardDone(isDone: Boolean) {
        defaults.setObject(isDone, forKey = IS_TUTO_DONE_KEY)
    }

    actual fun getIsOnboardDone(): Boolean = defaults.boolForKey(IS_TUTO_DONE_KEY)

    companion object {
        private const val IS_TUTO_DONE_KEY = "isTutoDone"
    }
}