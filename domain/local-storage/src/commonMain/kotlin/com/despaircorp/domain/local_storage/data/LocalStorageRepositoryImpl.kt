package com.despaircorp.domain.local_storage.data

import com.despaircorp.domain.local_storage.domain.repo.LocalStorageRepository
import com.despaircorp.services.storage.service.StorageService

class LocalStorageRepositoryImpl(
    private val storageService: StorageService
): LocalStorageRepository {
    override fun getOnboardCompletionStatus(): Boolean = storageService.getIsOnboardDone()

    override fun setOnboardCompletionStatus(done: Boolean) {
        storageService.setIsOnboardDone(done)
    }

}