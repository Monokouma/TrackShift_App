package com.despaircorp.domain.local_storage.domain.use_cases

import com.despaircorp.domain.local_storage.domain.repo.LocalStorageRepository

open class ManageOnboardStorageUseCase(
    private val localStorageRepository: LocalStorageRepository
) {
    open fun invokeGet(): Boolean = localStorageRepository.getOnboardCompletionStatus()

    open fun invokeSet(isDone: Boolean) {
        localStorageRepository.setOnboardCompletionStatus(isDone)
    }

}