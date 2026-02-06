package com.despaircorp.services.storage.service

expect class StorageService {
    fun setIsOnboardDone(isDone: Boolean)
    fun getIsOnboardDone(): Boolean
}