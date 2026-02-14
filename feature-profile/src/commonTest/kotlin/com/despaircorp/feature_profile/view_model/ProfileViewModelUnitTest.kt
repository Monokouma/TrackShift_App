package com.despaircorp.feature_profile.view_model

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isInstanceOf
import assertk.assertions.isTrue
import com.despaircorp.domain.auth.domain.repo.AuthRepository
import com.despaircorp.domain.auth.domain.use_cases.GetCurrentUserIdUseCase
import com.despaircorp.domain.user.domain.model.User
import com.despaircorp.domain.user.domain.repo.UserRepository
import com.despaircorp.domain.user.domain.use_cases.GetUserDataUseCase
import com.despaircorp.domain.user.domain.use_cases.UpdateUserImageUseCase
import com.despaircorp.domain.user.domain.use_cases.UpdateUsernameUseCase
import com.despaircorp.feature_profile.ui_state.ProfileUiState
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
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
class ProfileViewModelUnitTest {
    private val testDispatcher = StandardTestDispatcher()

    private val authRepository = mock<AuthRepository>()
    private val userRepository = mock<UserRepository>()

    private val getCurrentUserIdUseCase = GetCurrentUserIdUseCase(authRepository)
    private val getUserDataUseCase = GetUserDataUseCase(userRepository, getCurrentUserIdUseCase)
    private val updateUsernameUseCase = UpdateUsernameUseCase(userRepository, getCurrentUserIdUseCase)
    private val updateUserImageUseCase = UpdateUserImageUseCase(userRepository, getCurrentUserIdUseCase)

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state - starts with Loading then emits Content on success`() = runTest(testDispatcher) {
        val userId = "user_123"
        everySuspend { authRepository.getCurrentUserId() } returns Result.success(userId)
        everySuspend { userRepository.getUser(userId) } returns Result.success(provideUser())

        val viewModel = ProfileViewModel(getUserDataUseCase, updateUsernameUseCase, updateUserImageUseCase)

        viewModel.uiState.test {
            assertThat(awaitItem()).isInstanceOf(ProfileUiState.Loading::class)
            testDispatcher.scheduler.advanceUntilIdle()
            assertThat(awaitItem()).isInstanceOf(ProfileUiState.Content::class)
        }

        verifySuspend { authRepository.getCurrentUserId() }
        verifySuspend { userRepository.getUser(userId) }
    }

    @Test
    fun `initial state - emits Error on failure when user id null`() = runTest(testDispatcher) {
        everySuspend { authRepository.getCurrentUserId() } returns Result.failure(Exception("No session"))

        val viewModel = ProfileViewModel(getUserDataUseCase, updateUsernameUseCase, updateUserImageUseCase)

        viewModel.uiState.test {
            assertThat(awaitItem()).isInstanceOf(ProfileUiState.Loading::class)
            testDispatcher.scheduler.advanceUntilIdle()
            val errorState = awaitItem()
            assertThat(errorState).isInstanceOf(ProfileUiState.Error::class)
            assertThat((errorState as ProfileUiState.Error).message).isEqualTo("User id null")
        }
    }

    @Test
    fun `initial state - emits Error on failure when user null`() = runTest(testDispatcher) {
        val userId = "user_123"
        everySuspend { authRepository.getCurrentUserId() } returns Result.success(userId)
        everySuspend { userRepository.getUser(userId) } returns Result.failure(Exception("User not found"))

        val viewModel = ProfileViewModel(getUserDataUseCase, updateUsernameUseCase, updateUserImageUseCase)

        viewModel.uiState.test {
            assertThat(awaitItem()).isInstanceOf(ProfileUiState.Loading::class)
            testDispatcher.scheduler.advanceUntilIdle()
            val errorState = awaitItem()
            assertThat(errorState).isInstanceOf(ProfileUiState.Error::class)
            assertThat((errorState as ProfileUiState.Error).message).isEqualTo("Null user")
        }
    }

    @Test
    fun `refresh - sets isRefreshing to true then false`() = runTest(testDispatcher) {
        val userId = "user_123"
        everySuspend { authRepository.getCurrentUserId() } returns Result.success(userId)
        everySuspend { userRepository.getUser(userId) } returns Result.success(provideUser())

        val viewModel = ProfileViewModel(getUserDataUseCase, updateUsernameUseCase, updateUserImageUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.isRefreshing.test {
            assertThat(awaitItem()).isFalse()
            viewModel.refresh()
            assertThat(awaitItem()).isTrue()
            testDispatcher.scheduler.advanceUntilIdle()
            assertThat(awaitItem()).isFalse()
        }
    }

    @Test
    fun `refresh - reloads user data`() = runTest(testDispatcher) {
        val userId = "user_123"
        everySuspend { authRepository.getCurrentUserId() } returns Result.success(userId)
        everySuspend { userRepository.getUser(userId) } returns Result.success(provideUser())

        val viewModel = ProfileViewModel(getUserDataUseCase, updateUsernameUseCase, updateUserImageUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.uiState.test {
            assertThat(awaitItem()).isInstanceOf(ProfileUiState.Content::class)
            viewModel.refresh()
            assertThat(awaitItem()).isInstanceOf(ProfileUiState.Loading::class)
            testDispatcher.scheduler.advanceUntilIdle()
            assertThat(awaitItem()).isInstanceOf(ProfileUiState.Content::class)
        }
    }

    @Test
    fun `onNameEdit - success reloads user data`() = runTest(testDispatcher) {
        val userId = "user_123"
        val newName = "NewName"
        everySuspend { authRepository.getCurrentUserId() } returns Result.success(userId)
        everySuspend { userRepository.getUser(userId) } returns Result.success(provideUser())
        everySuspend { userRepository.updateUserName(newName, userId) } returns Result.success(Unit)

        val viewModel = ProfileViewModel(getUserDataUseCase, updateUsernameUseCase, updateUserImageUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.uiState.test {
            assertThat(awaitItem()).isInstanceOf(ProfileUiState.Content::class)
            viewModel.onNameEdit(newName)
            assertThat(awaitItem()).isInstanceOf(ProfileUiState.Loading::class)
            testDispatcher.scheduler.advanceUntilIdle()
            assertThat(awaitItem()).isInstanceOf(ProfileUiState.Content::class)
        }

        verifySuspend { userRepository.updateUserName(newName, userId) }
    }

    @Test
    fun `onNameEdit - failure emits Error state`() = runTest(testDispatcher) {
        val userId = "user_123"
        val newName = "NewName"
        everySuspend { authRepository.getCurrentUserId() } returns Result.success(userId)
        everySuspend { userRepository.getUser(userId) } returns Result.success(provideUser())
        everySuspend { userRepository.updateUserName(newName, userId) } returns Result.failure(Exception("Update failed"))

        val viewModel = ProfileViewModel(getUserDataUseCase, updateUsernameUseCase, updateUserImageUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.uiState.test {
            assertThat(awaitItem()).isInstanceOf(ProfileUiState.Content::class)
            viewModel.onNameEdit(newName)
            testDispatcher.scheduler.advanceUntilIdle()
            val errorState = awaitItem()
            assertThat(errorState).isInstanceOf(ProfileUiState.Error::class)
            assertThat((errorState as ProfileUiState.Error).message).isEqualTo("Update failed")
        }
    }

    @Test
    fun `updateUserImage - success reloads user data`() = runTest(testDispatcher) {
        val userId = "user_123"
        val imageBytes = byteArrayOf(1, 2, 3)
        everySuspend { authRepository.getCurrentUserId() } returns Result.success(userId)
        everySuspend { userRepository.getUser(userId) } returns Result.success(provideUser())
        everySuspend { userRepository.updateUserImage(any(), any()) } returns Result.success(Unit)

        val viewModel = ProfileViewModel(getUserDataUseCase, updateUsernameUseCase, updateUserImageUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.uiState.test {
            assertThat(awaitItem()).isInstanceOf(ProfileUiState.Content::class)
            viewModel.updateUserImage(imageBytes)
            assertThat(awaitItem()).isInstanceOf(ProfileUiState.Loading::class)
            testDispatcher.scheduler.advanceUntilIdle()
            assertThat(awaitItem()).isInstanceOf(ProfileUiState.Content::class)
        }

        verifySuspend { userRepository.updateUserImage(any(), any()) }
    }

    @Test
    fun `updateUserImage - failure emits Error state`() = runTest(testDispatcher) {
        val userId = "user_123"
        val imageBytes = byteArrayOf(1, 2, 3)
        everySuspend { authRepository.getCurrentUserId() } returns Result.success(userId)
        everySuspend { userRepository.getUser(userId) } returns Result.success(provideUser())
        everySuspend { userRepository.updateUserImage(any(), any()) } returns Result.failure(Exception("Upload failed"))

        val viewModel = ProfileViewModel(getUserDataUseCase, updateUsernameUseCase, updateUserImageUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.uiState.test {
            assertThat(awaitItem()).isInstanceOf(ProfileUiState.Content::class)
            viewModel.updateUserImage(imageBytes)
            testDispatcher.scheduler.advanceUntilIdle()
            val errorState = awaitItem()
            assertThat(errorState).isInstanceOf(ProfileUiState.Error::class)
            assertThat((errorState as ProfileUiState.Error).message).isEqualTo("Upload failed")
        }
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
