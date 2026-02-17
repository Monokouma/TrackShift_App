package com.despaircorp.domain.user.domain

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFailure
import assertk.assertions.isSuccess
import com.despaircorp.domain.auth.domain.repo.AuthRepository
import com.despaircorp.domain.auth.domain.use_cases.GetCurrentUserIdUseCase
import com.despaircorp.domain.user.domain.model.User
import com.despaircorp.domain.user.domain.model.UserAction
import com.despaircorp.domain.user.domain.repo.UserRepository
import com.despaircorp.domain.user.domain.use_cases.GetUserDataUseCase
import com.despaircorp.domain.user.domain.use_cases.IsUserLimitReachUseCase
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.mock
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class IsUserLimitReachUseCaseUnitTest {

    private val userRepository = mock<UserRepository>()
    private val authRepository = mock<AuthRepository>()
    private val getCurrentUserIdUseCase = GetCurrentUserIdUseCase(authRepository)
    private val getUserDataUseCase = GetUserDataUseCase(userRepository, getCurrentUserIdUseCase)

    // region GENERATE

    @Test
    fun `nominal case - generate limit not reached for free user under 10`() = runTest {
        everySuspend { authRepository.getCurrentUserId() } returns Result.success("user_123")
        everySuspend { userRepository.getUser("user_123") } returns Result.success(
            provideUser(isPro = false, linksCreatedMonth = 5)
        )

        val useCase = IsUserLimitReachUseCase(getUserDataUseCase)
        val result = useCase(forAction = UserAction.GENERATE)

        assertThat(result).isSuccess()
        assertThat(result.getOrNull()).isEqualTo(false)
    }

    @Test
    fun `nominal case - generate limit reached for free user at 10`() = runTest {
        everySuspend { authRepository.getCurrentUserId() } returns Result.success("user_123")
        everySuspend { userRepository.getUser("user_123") } returns Result.success(
            provideUser(isPro = false, linksCreatedMonth = 10)
        )

        val useCase = IsUserLimitReachUseCase(getUserDataUseCase)
        val result = useCase(forAction = UserAction.GENERATE)

        assertThat(result).isSuccess()
        assertThat(result.getOrNull()).isEqualTo(true)
    }

    @Test
    fun `nominal case - generate limit reached for free user above 10`() = runTest {
        everySuspend { authRepository.getCurrentUserId() } returns Result.success("user_123")
        everySuspend { userRepository.getUser("user_123") } returns Result.success(
            provideUser(isPro = false, linksCreatedMonth = 15)
        )

        val useCase = IsUserLimitReachUseCase(getUserDataUseCase)
        val result = useCase(forAction = UserAction.GENERATE)

        assertThat(result).isSuccess()
        assertThat(result.getOrNull()).isEqualTo(true)
    }

    @Test
    fun `nominal case - generate limit never reached for pro user`() = runTest {
        everySuspend { authRepository.getCurrentUserId() } returns Result.success("user_123")
        everySuspend { userRepository.getUser("user_123") } returns Result.success(
            provideUser(isPro = true, linksCreatedMonth = 100)
        )

        val useCase = IsUserLimitReachUseCase(getUserDataUseCase)
        val result = useCase(forAction = UserAction.GENERATE)

        assertThat(result).isSuccess()
        assertThat(result.getOrNull()).isEqualTo(false)
    }

    // endregion

    // region CONVERT

    @Test
    fun `nominal case - convert limit not reached for free user under 10`() = runTest {
        everySuspend { authRepository.getCurrentUserId() } returns Result.success("user_123")
        everySuspend { userRepository.getUser("user_123") } returns Result.success(
            provideUser(isPro = false, linksConvertedMonth = 5)
        )

        val useCase = IsUserLimitReachUseCase(getUserDataUseCase)
        val result = useCase(forAction = UserAction.CONVERT)

        assertThat(result).isSuccess()
        assertThat(result.getOrNull()).isEqualTo(false)
    }

    @Test
    fun `nominal case - convert limit reached for free user at 10`() = runTest {
        everySuspend { authRepository.getCurrentUserId() } returns Result.success("user_123")
        everySuspend { userRepository.getUser("user_123") } returns Result.success(
            provideUser(isPro = false, linksConvertedMonth = 10)
        )

        val useCase = IsUserLimitReachUseCase(getUserDataUseCase)
        val result = useCase(forAction = UserAction.CONVERT)

        assertThat(result).isSuccess()
        assertThat(result.getOrNull()).isEqualTo(true)
    }

    @Test
    fun `nominal case - convert limit reached for free user above 10`() = runTest {
        everySuspend { authRepository.getCurrentUserId() } returns Result.success("user_123")
        everySuspend { userRepository.getUser("user_123") } returns Result.success(
            provideUser(isPro = false, linksConvertedMonth = 15)
        )

        val useCase = IsUserLimitReachUseCase(getUserDataUseCase)
        val result = useCase(forAction = UserAction.CONVERT)

        assertThat(result).isSuccess()
        assertThat(result.getOrNull()).isEqualTo(true)
    }

    @Test
    fun `nominal case - convert limit never reached for pro user`() = runTest {
        everySuspend { authRepository.getCurrentUserId() } returns Result.success("user_123")
        everySuspend { userRepository.getUser("user_123") } returns Result.success(
            provideUser(isPro = true, linksConvertedMonth = 100)
        )

        val useCase = IsUserLimitReachUseCase(getUserDataUseCase)
        val result = useCase(forAction = UserAction.CONVERT)

        assertThat(result).isSuccess()
        assertThat(result.getOrNull()).isEqualTo(false)
    }

    // endregion

    // region Error

    @Test
    fun `error case - returns failure when user is null`() = runTest {
        everySuspend { authRepository.getCurrentUserId() } returns Result.failure(Exception("No session"))

        val useCase = IsUserLimitReachUseCase(getUserDataUseCase)
        val result = useCase(forAction = UserAction.GENERATE)

        assertThat(result).isFailure()
        assertThat(result.exceptionOrNull()?.message).isEqualTo("Null user")
    }

    // endregion

    private fun provideUser(
        isPro: Boolean = false,
        linksCreatedMonth: Int = 0,
        linksConvertedMonth: Int = 0
    ) = User(
        id = "user_123",
        displayName = "Monokouma",
        avatarUrl = "https://example.com/avatar.jpg",
        isPro = isPro,
        createdAt = "2026-01-01",
        linksCreatedMonth = linksCreatedMonth,
        linksConvertedMonth = linksConvertedMonth,
        totalLinksCreated = 42,
        totalPlaylistsCreated = 10,
        totalLinksConverted = 28,
        proExpiresAt = "2026-12-31",
        lastResetAt = "2026-02-01"
    )
}
