package com.despaircorp.domain.local_storage.domain.use_cases

import com.despaircorp.domain.local_storage.domain.repo.LocalStorageRepository

class ManageOnboardStorageUseCase(
    private val localStorageRepository: LocalStorageRepository
) {
    fun invokeGet(): Boolean = localStorageRepository.getOnboardCompletionStatus()

    fun invokeSet(isDone: Boolean) {
        localStorageRepository.setOnboardCompletionStatus(isDone)
    }

}