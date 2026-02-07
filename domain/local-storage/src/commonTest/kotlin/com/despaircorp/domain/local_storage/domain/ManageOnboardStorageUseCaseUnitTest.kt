package com.despaircorp.domain.local_storage.domain

import assertk.assertThat
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import com.despaircorp.domain.local_storage.domain.repo.LocalStorageRepository
import com.despaircorp.domain.local_storage.domain.use_cases.ManageOnboardStorageUseCase
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.mock
import dev.mokkery.verify
import kotlin.test.Test


class ManageOnboardStorageUseCaseUnitTest {

    @Test
    fun `nominal case - invokeGet returns true when onboard is done`() {
        val repository = mock<LocalStorageRepository>()
        val useCase = ManageOnboardStorageUseCase(repository)

        every {
            repository.getOnboardCompletionStatus()
        } returns true

        val result = useCase.invokeGet()

        assertThat(result).isTrue()

        verify {
            repository.getOnboardCompletionStatus()
        }
    }

    @Test
    fun `nominal case - invokeGet returns false when onboard is not done`() {
        val repository = mock<LocalStorageRepository>()
        val useCase = ManageOnboardStorageUseCase(repository)

        every {
            repository.getOnboardCompletionStatus()
        } returns false

        val result = useCase.invokeGet()

        assertThat(result).isFalse()

        verify {
            repository.getOnboardCompletionStatus()
        }
    }

    @Test
    fun `nominal case - invokeSet sets onboard done to true`() {
        val repository = mock<LocalStorageRepository>()
        val useCase = ManageOnboardStorageUseCase(repository)

        every {
            repository.setOnboardCompletionStatus(true)
        } returns Unit

        useCase.invokeSet(true)

        verify {
            repository.setOnboardCompletionStatus(true)
        }
    }

    @Test
    fun `nominal case - invokeSet sets onboard done to false`() {
        val repository = mock<LocalStorageRepository>()
        val useCase = ManageOnboardStorageUseCase(repository)

        every {
            repository.setOnboardCompletionStatus(false)
        } returns Unit

        useCase.invokeSet(false)

        verify {
            repository.setOnboardCompletionStatus(false)
        }
    }
}
