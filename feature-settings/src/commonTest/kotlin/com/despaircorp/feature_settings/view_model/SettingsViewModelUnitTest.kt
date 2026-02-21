package com.despaircorp.feature_settings.view_model

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import com.despaircorp.domain.auth.domain.repo.AuthRepository
import com.despaircorp.domain.auth.domain.use_cases.GetCurrentUserIdUseCase
import com.despaircorp.domain.auth.domain.use_cases.LogoutUserUseCase
import com.despaircorp.domain.user.domain.model.User
import com.despaircorp.domain.user.domain.repo.UserRepository
import com.despaircorp.domain.user.domain.use_cases.DeleteUserAccountUseCase
import com.despaircorp.domain.user.domain.use_cases.GetUserDataUseCase
import com.despaircorp.feature_settings.ui_state.SettingsUiState
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsViewModelUnitTest {
    private val testDispatcher = StandardTestDispatcher()

    private val authRepository = mock<AuthRepository>()
    private val userRepository = mock<UserRepository>()

    private val getCurrentUserIdUseCase = GetCurrentUserIdUseCase(authRepository)
    private val getUserDataUseCase = GetUserDataUseCase(userRepository, getCurrentUserIdUseCase)
    private val logoutUserUseCase = LogoutUserUseCase(authRepository)
    private val deleteUserAccountUseCase = DeleteUserAccountUseCase(
        getCurrentUserIdUseCase,
        logoutUserUseCase,
        userRepository
    )

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state - user is pro emits Nominal with isUserPro true`() =
        runTest(testDispatcher) {
            val userId = "user_123"
            everySuspend { authRepository.getCurrentUserId() } returns Result.success(userId)
            everySuspend { userRepository.getUser(userId) } returns Result.success(provideUser(isPro = true))

            val viewModel =
                SettingsViewModel(getUserDataUseCase, logoutUserUseCase, deleteUserAccountUseCase)

            viewModel.uiState.test {
                assertThat(awaitItem()).isInstanceOf(SettingsUiState.Idle::class)
                testDispatcher.scheduler.advanceUntilIdle()
                val nominal = awaitItem()
                assertThat(nominal).isEqualTo(SettingsUiState.Nominal(isUserPro = true))
            }

            verifySuspend { authRepository.getCurrentUserId() }
            verifySuspend { userRepository.getUser(userId) }
        }

    @Test
    fun `initial state - user is free emits Nominal with isUserPro false`() =
        runTest(testDispatcher) {
            val userId = "user_123"
            everySuspend { authRepository.getCurrentUserId() } returns Result.success(userId)
            everySuspend { userRepository.getUser(userId) } returns Result.success(provideUser(isPro = false))

            val viewModel =
                SettingsViewModel(getUserDataUseCase, logoutUserUseCase, deleteUserAccountUseCase)

            viewModel.uiState.test {
                assertThat(awaitItem()).isInstanceOf(SettingsUiState.Idle::class)
                testDispatcher.scheduler.advanceUntilIdle()
                val nominal = awaitItem()
                assertThat(nominal).isEqualTo(SettingsUiState.Nominal(isUserPro = false))

            }
        }

    @Test
    fun `initial state - user id null emits Error`() = runTest(testDispatcher) {
        everySuspend { authRepository.getCurrentUserId() } returns Result.failure(Exception("No session"))

        val viewModel =
            SettingsViewModel(getUserDataUseCase, logoutUserUseCase, deleteUserAccountUseCase)

        viewModel.uiState.test {
            assertThat(awaitItem()).isInstanceOf(SettingsUiState.Idle::class)
            testDispatcher.scheduler.advanceUntilIdle()
            val errorState = awaitItem()
            assertThat(errorState).isEqualTo(SettingsUiState.Error("User id null"))

        }
    }

    @Test
    fun `initial state - user null emits Error`() = runTest(testDispatcher) {
        val userId = "user_123"
        everySuspend { authRepository.getCurrentUserId() } returns Result.success(userId)
        everySuspend { userRepository.getUser(userId) } returns Result.failure(Exception("User not found"))

        val viewModel =
            SettingsViewModel(getUserDataUseCase, logoutUserUseCase, deleteUserAccountUseCase)

        viewModel.uiState.test {
            assertThat(awaitItem()).isInstanceOf(SettingsUiState.Idle::class)
            testDispatcher.scheduler.advanceUntilIdle()
            val errorState = awaitItem()
            assertThat(errorState).isEqualTo(SettingsUiState.Error("Null user"))
        }
    }

    @Test
    fun `onLogout - success emits Loading then Success`() = runTest(testDispatcher) {
        val userId = "user_123"
        everySuspend { authRepository.getCurrentUserId() } returns Result.success(userId)
        everySuspend { userRepository.getUser(userId) } returns Result.success(provideUser())
        everySuspend { authRepository.logout() } returns Result.success(Unit)

        val viewModel =
            SettingsViewModel(getUserDataUseCase, logoutUserUseCase, deleteUserAccountUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.uiState.test {
            assertThat(awaitItem()).isInstanceOf(SettingsUiState.Nominal::class)
            viewModel.onLogout()
            assertThat(awaitItem()).isInstanceOf(SettingsUiState.Loading::class)
            testDispatcher.scheduler.advanceUntilIdle()
            assertThat(awaitItem()).isInstanceOf(SettingsUiState.Success::class)
        }

        verifySuspend { authRepository.logout() }
    }

    @Test
    fun `onLogout - failure emits Loading then Error`() = runTest(testDispatcher) {
        val userId = "user_123"
        everySuspend { authRepository.getCurrentUserId() } returns Result.success(userId)
        everySuspend { userRepository.getUser(userId) } returns Result.success(provideUser())
        everySuspend { authRepository.logout() } returns Result.failure(Exception("Logout failed"))

        val viewModel =
            SettingsViewModel(getUserDataUseCase, logoutUserUseCase, deleteUserAccountUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.uiState.test {
            assertThat(awaitItem()).isInstanceOf(SettingsUiState.Nominal::class)
            viewModel.onLogout()
            assertThat(awaitItem()).isInstanceOf(SettingsUiState.Loading::class)
            testDispatcher.scheduler.advanceUntilIdle()
            val errorState = awaitItem()
            assertThat(errorState).isInstanceOf(SettingsUiState.Error::class)
            assertThat((errorState as SettingsUiState.Error).message).isEqualTo("Logout failed")
        }
    }

    @Test
    fun `onDeleteAccount - success emits Loading then Success`() = runTest(testDispatcher) {
        val userId = "user_123"
        everySuspend { authRepository.getCurrentUserId() } returns Result.success(userId)
        everySuspend { userRepository.getUser(userId) } returns Result.success(provideUser())
        everySuspend { authRepository.logout() } returns Result.success(Unit)
        everySuspend { userRepository.deleteAccount(userId) } returns Result.success(Unit)

        val viewModel =
            SettingsViewModel(getUserDataUseCase, logoutUserUseCase, deleteUserAccountUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.uiState.test {
            assertThat(awaitItem()).isInstanceOf(SettingsUiState.Nominal::class)
            viewModel.onDeleteAccount()
            assertThat(awaitItem()).isInstanceOf(SettingsUiState.Loading::class)
            testDispatcher.scheduler.advanceUntilIdle()
            assertThat(awaitItem()).isInstanceOf(SettingsUiState.Success::class)
        }

        verifySuspend { authRepository.logout() }
        verifySuspend { userRepository.deleteAccount(userId) }
    }

    @Test
    fun `onDeleteAccount - logout fails emits Error`() = runTest(testDispatcher) {
        val userId = "user_123"
        everySuspend { authRepository.getCurrentUserId() } returns Result.success(userId)
        everySuspend { userRepository.getUser(userId) } returns Result.success(provideUser())
        everySuspend { authRepository.logout() } returns Result.failure(Exception("Logout failed"))

        val viewModel =
            SettingsViewModel(getUserDataUseCase, logoutUserUseCase, deleteUserAccountUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.uiState.test {
            assertThat(awaitItem()).isInstanceOf(SettingsUiState.Nominal::class)
            viewModel.onDeleteAccount()
            assertThat(awaitItem()).isInstanceOf(SettingsUiState.Loading::class)
            testDispatcher.scheduler.advanceUntilIdle()
            val errorState = awaitItem()
            assertThat(errorState).isInstanceOf(SettingsUiState.Error::class)
            assertThat((errorState as SettingsUiState.Error).message).isEqualTo("Logout failed")
        }
    }

    @Test
    fun `onDeleteAccount - delete fails emits Error`() = runTest(testDispatcher) {
        val userId = "user_123"
        everySuspend { authRepository.getCurrentUserId() } returns Result.success(userId)
        everySuspend { userRepository.getUser(userId) } returns Result.success(provideUser())
        everySuspend { authRepository.logout() } returns Result.success(Unit)
        everySuspend { userRepository.deleteAccount(userId) } returns Result.failure(Exception("Delete failed"))

        val viewModel =
            SettingsViewModel(getUserDataUseCase, logoutUserUseCase, deleteUserAccountUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.uiState.test {
            assertThat(awaitItem()).isInstanceOf(SettingsUiState.Nominal::class)
            viewModel.onDeleteAccount()
            assertThat(awaitItem()).isInstanceOf(SettingsUiState.Loading::class)
            testDispatcher.scheduler.advanceUntilIdle()
            val errorState = awaitItem()
            assertThat(errorState).isInstanceOf(SettingsUiState.Error::class)
            assertThat((errorState as SettingsUiState.Error).message).isEqualTo("Delete failed")
        }
    }

    @Test
    fun `onEventConsumed - resets state to Idle`() = runTest(testDispatcher) {
        val userId = "user_123"
        everySuspend { authRepository.getCurrentUserId() } returns Result.success(userId)
        everySuspend { userRepository.getUser(userId) } returns Result.success(provideUser())
        everySuspend { authRepository.logout() } returns Result.success(Unit)

        val viewModel =
            SettingsViewModel(getUserDataUseCase, logoutUserUseCase, deleteUserAccountUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.uiState.test {
            assertThat(awaitItem()).isInstanceOf(SettingsUiState.Nominal::class)
            viewModel.onLogout()
            assertThat(awaitItem()).isInstanceOf(SettingsUiState.Loading::class)
            testDispatcher.scheduler.advanceUntilIdle()
            assertThat(awaitItem()).isInstanceOf(SettingsUiState.Success::class)
            viewModel.onEventConsumed()
            assertThat(awaitItem()).isInstanceOf(SettingsUiState.Idle::class)
        }
    }

    private fun provideUser(isPro: Boolean = true) = User(
        id = "user_123",
        displayName = "Monokouma",
        avatarUrl = "https://example.com/avatar.jpg",
        isPro = isPro,
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
