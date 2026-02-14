package com.despaircorp.domain.auth.domain

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFailure
import assertk.assertions.isSuccess
import com.despaircorp.domain.auth.domain.repo.AuthRepository
import com.despaircorp.domain.auth.domain.use_cases.GetCurrentUserIdUseCase
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class GetCurrentUserIdUseCaseUnitTest {

    private val authRepository = mock<AuthRepository>()

    @Test
    fun `nominal case - returns user id when repository succeeds`() = runTest {
        val userId = "user_123"

        everySuspend { authRepository.getCurrentUserId() } returns Result.success(userId)

        val useCase = GetCurrentUserIdUseCase(authRepository)
        val result = useCase()

        assertThat(result).isSuccess()
        assertThat(result.getOrNull()).isEqualTo(userId)

        verifySuspend { authRepository.getCurrentUserId() }
    }

    @Test
    fun `error case - returns failure when repository fails`() = runTest {
        everySuspend { authRepository.getCurrentUserId() } returns Result.failure(Exception("No session"))

        val useCase = GetCurrentUserIdUseCase(authRepository)
        val result = useCase()

        assertThat(result).isFailure()
        assertThat(result.exceptionOrNull()?.message).isEqualTo("No session")
    }

    @Test
    fun `error case - returns failure when user is not logged in`() = runTest {
        everySuspend { authRepository.getCurrentUserId() } returns Result.failure(Exception("User not authenticated"))

        val useCase = GetCurrentUserIdUseCase(authRepository)
        val result = useCase()

        assertThat(result).isFailure()
        assertThat(result.exceptionOrNull()?.message).isEqualTo("User not authenticated")
    }
}
