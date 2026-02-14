package com.despaircorp.domain.user.domain

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFailure
import assertk.assertions.isSuccess
import com.despaircorp.domain.auth.domain.repo.AuthRepository
import com.despaircorp.domain.auth.domain.use_cases.GetCurrentUserIdUseCase
import com.despaircorp.domain.user.domain.repo.UserRepository
import com.despaircorp.domain.user.domain.use_cases.UpdateUsernameUseCase
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class UpdateUsernameUseCaseUnitTest {

    private val userRepository = mock<UserRepository>()
    private val authRepository = mock<AuthRepository>()
    private val getCurrentUserIdUseCase = GetCurrentUserIdUseCase(authRepository)

    @Test
    fun `nominal case - updates username when id is valid`() = runTest {
        val userId = "user_123"
        val newName = "NewUsername"

        everySuspend { authRepository.getCurrentUserId() } returns Result.success(userId)
        everySuspend { userRepository.updateUserName(newName, userId) } returns Result.success(Unit)

        val useCase = UpdateUsernameUseCase(userRepository, getCurrentUserIdUseCase)
        val result = useCase(newName)

        assertThat(result).isSuccess()

        verifySuspend { authRepository.getCurrentUserId() }
        verifySuspend { userRepository.updateUserName(newName, userId) }
    }

    @Test
    fun `error case - returns failure when user id is null`() = runTest {
        val newName = "NewUsername"

        everySuspend { authRepository.getCurrentUserId() } returns Result.failure(Exception("No session"))

        val useCase = UpdateUsernameUseCase(userRepository, getCurrentUserIdUseCase)
        val result = useCase(newName)

        assertThat(result).isFailure()
        assertThat(result.exceptionOrNull()?.message).isEqualTo("User id is null")
    }

    @Test
    fun `error case - returns failure when repository fails`() = runTest {
        val userId = "user_123"
        val newName = "NewUsername"

        everySuspend { authRepository.getCurrentUserId() } returns Result.success(userId)
        everySuspend { userRepository.updateUserName(newName, userId) } returns Result.failure(Exception("Network error"))

        val useCase = UpdateUsernameUseCase(userRepository, getCurrentUserIdUseCase)
        val result = useCase(newName)

        assertThat(result).isFailure()
        assertThat(result.exceptionOrNull()?.message).isEqualTo("Network error")
    }
}
