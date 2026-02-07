package com.despaircorp.services.storage.service

expect class StorageService : StorageServiceContract {
    override fun setIsOnboardDone(isDone: Boolean)
    override fun getIsOnboardDone(): Boolean
}