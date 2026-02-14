package com.despaircorp.domain.user.domain

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFailure
import assertk.assertions.isSuccess
import com.despaircorp.domain.auth.domain.repo.AuthRepository
import com.despaircorp.domain.auth.domain.use_cases.GetCurrentUserIdUseCase
import com.despaircorp.domain.user.domain.model.User
import com.despaircorp.domain.user.domain.repo.UserRepository
import com.despaircorp.domain.user.domain.use_cases.GetUserDataUseCase
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class GetUserDataUseCaseUnitTest {

    private val userRepository = mock<UserRepository>()
    private val authRepository = mock<AuthRepository>()
    private val getCurrentUserIdUseCase = GetCurrentUserIdUseCase(authRepository)

    @Test
    fun `nominal case - returns user when id and user are valid`() = runTest {
        val userId = "user_123"
        val user = provideUser()

        everySuspend { authRepository.getCurrentUserId() } returns Result.success(userId)
        everySuspend { userRepository.getUser(userId) } returns Result.success(user)

        val useCase = GetUserDataUseCase(userRepository, getCurrentUserIdUseCase)
        val result = useCase()

        assertThat(result).isSuccess()
        assertThat(result.getOrNull()).isEqualTo(user)

        verifySuspend { authRepository.getCurrentUserId() }
        verifySuspend { userRepository.getUser(userId) }
    }

    @Test
    fun `error case - returns failure when user id is null`() = runTest {
        everySuspend { authRepository.getCurrentUserId() } returns Result.failure(Exception("No session"))

        val useCase = GetUserDataUseCase(userRepository, getCurrentUserIdUseCase)
        val result = useCase()

        assertThat(result).isFailure()
        assertThat(result.exceptionOrNull()?.message).isEqualTo("User id null")
    }

    @Test
    fun `error case - returns failure when user is null`() = runTest {
        val userId = "user_123"

        everySuspend { authRepository.getCurrentUserId() } returns Result.success(userId)
        everySuspend { userRepository.getUser(userId) } returns Result.failure(Exception("User not found"))

        val useCase = GetUserDataUseCase(userRepository, getCurrentUserIdUseCase)
        val result = useCase()

        assertThat(result).isFailure()
        assertThat(result.exceptionOrNull()?.message).isEqualTo("Null user")
    }

    private fun provideUser() = User(
        id = "user_123",
        displayName = "Monokouma",
        avatarUrl = "https://example.com/avatar.jpg",
        isPro = true,
        createdAt = "2026-01-01",
        linksCreatedMonth = 5,
        linksConvertedMonth = 3,
        totalLinksCreated = 42,
        totalPlaylistsCreated = 10,
        totalLinksConverted = 28,
        proExpiresAt = "2026-12-31",
        lastResetAt = "2026-02-01"
    )
}
