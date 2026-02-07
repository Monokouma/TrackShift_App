package com.despaircorp.domain.local_storage.data

import com.despaircorp.domain.local_storage.domain.repo.LocalStorageRepository
import com.despaircorp.services.storage.service.StorageServiceContract

class LocalStorageRepositoryImpl(
    private val storageService: StorageServiceContract
) : LocalStorageRepository {
    override fun getOnboardCompletionStatus(): Boolean = storageService.getIsOnboardDone()

    override fun setOnboardCompletionStatus(done: Boolean) {
        storageService.setIsOnboardDone(done)
    }
}