package com.despaircorp.domain.local_storage.data

import assertk.assertThat
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import com.despaircorp.services.storage.service.StorageServiceContract
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.mock
import dev.mokkery.verify
import kotlin.test.Test


class LocalStorageRepositoryImplUnitTest {

    @Test
    fun `nominal case - getOnboardCompletionStatus returns true`() {
        val service = mock<StorageServiceContract>()
        val repository = LocalStorageRepositoryImpl(service)

        every {
            service.getIsOnboardDone()
        } returns true

        val result = repository.getOnboardCompletionStatus()

        assertThat(result).isTrue()

        verify {
            service.getIsOnboardDone()
        }
    }

    @Test
    fun `nominal case - getOnboardCompletionStatus returns false`() {
        val service = mock<StorageServiceContract>()
        val repository = LocalStorageRepositoryImpl(service)

        every {
            service.getIsOnboardDone()
        } returns false

        val result = repository.getOnboardCompletionStatus()

        assertThat(result).isFalse()

        verify {
            service.getIsOnboardDone()
        }
    }

    @Test
    fun `nominal case - setOnboardCompletionStatus sets true`() {
        val service = mock<StorageServiceContract>()
        val repository = LocalStorageRepositoryImpl(service)

        every {
            service.setIsOnboardDone(true)
        } returns Unit

        repository.setOnboardCompletionStatus(true)

        verify {
            service.setIsOnboardDone(true)
        }
    }

    @Test
    fun `nominal case - setOnboardCompletionStatus sets false`() {
        val service = mock<StorageServiceContract>()
        val repository = LocalStorageRepositoryImpl(service)

        every {
            service.setIsOnboardDone(false)
        } returns Unit

        repository.setOnboardCompletionStatus(false)

        verify {
            service.setIsOnboardDone(false)
        }
    }
}
