package com.despaircorp.trackshift.view_model

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import com.despaircorp.domain.auth.domain.use_cases.HandleSessionStatusUseCase
import com.despaircorp.domain.local_storage.domain.use_cases.ManageOnboardStorageUseCase
import com.despaircorp.trackshift.ui_state.TrackShiftAppUiState
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.mock
import io.github.jan.supabase.auth.status.SessionStatus
import io.github.jan.supabase.auth.user.UserSession
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test


@OptIn(ExperimentalCoroutinesApi::class)
class TrackShiftAppViewModelUnitTest {
    private val testDispatcher = StandardTestDispatcher()

    private val fakeUserSession = UserSession(
        accessToken = "fake_access_token",
        refreshToken = "fake_refresh_token",
        expiresIn = 3600L,
        tokenType = "Bearer"
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
    fun `initial state - uiState is SplashRedirection`() = runTest(testDispatcher) {
        val handleSessionStatusUseCase = mock<HandleSessionStatusUseCase>()
        val manageOnboardStorageUseCase = mock<ManageOnboardStorageUseCase>()

        every {
            handleSessionStatusUseCase()
        } returns flowOf(SessionStatus.Initializing)

        val viewModel = TrackShiftAppViewModel(handleSessionStatusUseCase, manageOnboardStorageUseCase)

        assertThat(viewModel.uiState.value).isEqualTo(TrackShiftAppUiState.SplashRedirection)
    }

    @Test
    fun `nominal case - session initializing returns SplashRedirection`() = runTest(testDispatcher) {
        val handleSessionStatusUseCase = mock<HandleSessionStatusUseCase>()
        val manageOnboardStorageUseCase = mock<ManageOnboardStorageUseCase>()

        every {
            handleSessionStatusUseCase()
        } returns flowOf(SessionStatus.Initializing)

        val viewModel = TrackShiftAppViewModel(handleSessionStatusUseCase, manageOnboardStorageUseCase)

        viewModel.uiState.test {
            assertThat(awaitItem()).isEqualTo(TrackShiftAppUiState.SplashRedirection)
        }
    }

    @Test
    fun `nominal case - session not authenticated returns AuthRedirection`() = runTest(testDispatcher) {
        val handleSessionStatusUseCase = mock<HandleSessionStatusUseCase>()
        val manageOnboardStorageUseCase = mock<ManageOnboardStorageUseCase>()

        every {
            handleSessionStatusUseCase()
        } returns flowOf(SessionStatus.NotAuthenticated(isSignOut = false))

        val viewModel = TrackShiftAppViewModel(handleSessionStatusUseCase, manageOnboardStorageUseCase)

        viewModel.uiState.test {
            skipItems(1)
            assertThat(awaitItem()).isEqualTo(TrackShiftAppUiState.AuthRedirection)
        }
    }

    @Test
    fun `nominal case - session authenticated with onboard done returns HomeRedirection`() = runTest(testDispatcher) {
        val handleSessionStatusUseCase = mock<HandleSessionStatusUseCase>()
        val manageOnboardStorageUseCase = mock<ManageOnboardStorageUseCase>()

        every {
            handleSessionStatusUseCase()
        } returns flowOf(SessionStatus.Authenticated(session = fakeUserSession))

        every {
            manageOnboardStorageUseCase.invokeGet()
        } returns true

        val viewModel = TrackShiftAppViewModel(handleSessionStatusUseCase, manageOnboardStorageUseCase)

        viewModel.uiState.test {
            skipItems(1)
            assertThat(awaitItem()).isEqualTo(TrackShiftAppUiState.HomeRedirection)
        }
    }

    @Test
    fun `nominal case - session authenticated with onboard not done returns OnboardRedirection`() = runTest(testDispatcher) {
        val handleSessionStatusUseCase = mock<HandleSessionStatusUseCase>()
        val manageOnboardStorageUseCase = mock<ManageOnboardStorageUseCase>()

        every {
            handleSessionStatusUseCase()
        } returns flowOf(SessionStatus.Authenticated(session = fakeUserSession))

        every {
            manageOnboardStorageUseCase.invokeGet()
        } returns false

        val viewModel = TrackShiftAppViewModel(handleSessionStatusUseCase, manageOnboardStorageUseCase)

        viewModel.uiState.test {
            skipItems(1)
            assertThat(awaitItem()).isEqualTo(TrackShiftAppUiState.OnboardRedirection)
        }
    }

    @Test
    fun `nominal case - session signed out returns AuthRedirection`() = runTest(testDispatcher) {
        val handleSessionStatusUseCase = mock<HandleSessionStatusUseCase>()
        val manageOnboardStorageUseCase = mock<ManageOnboardStorageUseCase>()

        every {
            handleSessionStatusUseCase()
        } returns flowOf(SessionStatus.NotAuthenticated(isSignOut = true))

        val viewModel = TrackShiftAppViewModel(handleSessionStatusUseCase, manageOnboardStorageUseCase)

        viewModel.uiState.test {
            skipItems(1)
            assertThat(awaitItem()).isEqualTo(TrackShiftAppUiState.AuthRedirection)
        }
    }
}
