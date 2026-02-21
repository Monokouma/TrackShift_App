package com.despaircorp.domain.user.domain

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFailure
import assertk.assertions.isSuccess
import com.despaircorp.domain.auth.domain.repo.AuthRepository
import com.despaircorp.domain.auth.domain.use_cases.GetCurrentUserIdUseCase
import com.despaircorp.domain.auth.domain.use_cases.LogoutUserUseCase
import com.despaircorp.domain.user.domain.repo.UserRepository
import com.despaircorp.domain.user.domain.use_cases.DeleteUserAccountUseCase
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class DeleteUserAccountUseCaseUnitTest {

    private val authRepository = mock<AuthRepository>()
    private val userRepository = mock<UserRepository>()

    private val getCurrentUserIdUseCase = GetCurrentUserIdUseCase(authRepository)
    private val logoutUserUseCase = LogoutUserUseCase(authRepository)

    @Test
    fun `nominal case - deletes account after logout`() = runTest {
        val userId = "user_123"
        everySuspend { authRepository.getCurrentUserId() } returns Result.success(userId)
        everySuspend { authRepository.logout() } returns Result.success(Unit)
        everySuspend { userRepository.deleteAccount(userId) } returns Result.success(Unit)

        val useCase = DeleteUserAccountUseCase(getCurrentUserIdUseCase, logoutUserUseCase, userRepository)
        val result = useCase()

        assertThat(result).isSuccess()

        verifySuspend { authRepository.getCurrentUserId() }
        verifySuspend { authRepository.logout() }
        verifySuspend { userRepository.deleteAccount(userId) }
    }

    @Test
    fun `error case - user id is null`() = runTest {
        everySuspend { authRepository.getCurrentUserId() } returns Result.failure(Exception("No session"))

        val useCase = DeleteUserAccountUseCase(getCurrentUserIdUseCase, logoutUserUseCase, userRepository)
        val result = useCase()

        assertThat(result).isFailure()
        assertThat(result.exceptionOrNull()?.message).isEqualTo("Null id")
    }

    @Test
    fun `error case - logout fails does not delete account`() = runTest {
        val userId = "user_123"
        everySuspend { authRepository.getCurrentUserId() } returns Result.success(userId)
        everySuspend { authRepository.logout() } returns Result.failure(Exception("Logout failed"))

        val useCase = DeleteUserAccountUseCase(getCurrentUserIdUseCase, logoutUserUseCase, userRepository)
        val result = useCase()

        assertThat(result).isFailure()
        assertThat(result.exceptionOrNull()?.message).isEqualTo("Logout failed")
    }

    @Test
    fun `error case - delete account fails after successful logout`() = runTest {
        val userId = "user_123"
        everySuspend { authRepository.getCurrentUserId() } returns Result.success(userId)
        everySuspend { authRepository.logout() } returns Result.success(Unit)
        everySuspend { userRepository.deleteAccount(userId) } returns Result.failure(Exception("Delete failed"))

        val useCase = DeleteUserAccountUseCase(getCurrentUserIdUseCase, logoutUserUseCase, userRepository)
        val result = useCase()

        assertThat(result).isFailure()
        assertThat(result.exceptionOrNull()?.message).isEqualTo("Delete failed")
    }
}
