package com.despaircorp.domain.local_storage.domain.repo

interface LocalStorageRepository {
    fun getOnboardCompletionStatus(): Boolean
    fun setOnboardCompletionStatus(done: Boolean)
}