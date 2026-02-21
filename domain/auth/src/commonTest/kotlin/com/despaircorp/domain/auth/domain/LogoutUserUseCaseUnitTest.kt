package com.despaircorp.domain.auth.domain

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFailure
import assertk.assertions.isSuccess
import com.despaircorp.domain.auth.domain.repo.AuthRepository
import com.despaircorp.domain.auth.domain.use_cases.LogoutUserUseCase
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class LogoutUserUseCaseUnitTest {

    private val authRepository = mock<AuthRepository>()

    @Test
    fun `nominal case - logout succeeds`() = runTest {
        everySuspend { authRepository.logout() } returns Result.success(Unit)

        val useCase = LogoutUserUseCase(authRepository)
        val result = useCase()

        assertThat(result).isSuccess()

        verifySuspend { authRepository.logout() }
    }

    @Test
    fun `error case - logout fails`() = runTest {
        everySuspend { authRepository.logout() } returns Result.failure(Exception("Failure while logging out user"))

        val useCase = LogoutUserUseCase(authRepository)
        val result = useCase()

        assertThat(result).isFailure()
        assertThat(result.exceptionOrNull()?.message).isEqualTo("Failure while logging out user")

        verifySuspend { authRepository.logout() }
    }
}
