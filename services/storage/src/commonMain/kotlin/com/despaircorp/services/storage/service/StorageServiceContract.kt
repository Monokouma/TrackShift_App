package com.despaircorp.services.storage.service

interface StorageServiceContract {
    fun setIsOnboardDone(isDone: Boolean)
    fun getIsOnboardDone(): Boolean
}
