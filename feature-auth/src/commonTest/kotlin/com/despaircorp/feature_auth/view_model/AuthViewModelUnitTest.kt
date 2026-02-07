package com.despaircorp.feature_auth.view_model

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import com.despaircorp.domain.auth.domain.use_cases.AuthByProviderUseCase
import com.despaircorp.domain.auth.domain.use_cases.HandleOAuthCallbackUseCase
import com.despaircorp.feature_auth.model.AuthProvider
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
class AuthViewModelUnitTest {
    private val testDispatcher = StandardTestDispatcher()

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `nominal case - onProviderPick with google emits oauth url`() = runTest(testDispatcher) {
        val authByProviderUseCase = mock<AuthByProviderUseCase>()
        val handleOAuthCallbackUseCase = mock<HandleOAuthCallbackUseCase>()
        val viewModel = AuthViewModel(authByProviderUseCase, handleOAuthCallbackUseCase)

        everySuspend {
            authByProviderUseCase("GOOGLE")
        } returns Result.success("https://supabase.com/auth/google")

        viewModel.oAuthUrlToLaunch.test {
            viewModel.onProviderPick(AuthProvider.GOOGLE)
            testDispatcher.scheduler.advanceUntilIdle()

            assertThat(awaitItem()).isEqualTo("https://supabase.com/auth/google")
        }

        verifySuspend {
            authByProviderUseCase("GOOGLE")
        }
    }

    @Test
    fun `nominal case - onProviderPick with apple emits oauth url`() = runTest(testDispatcher) {
        val authByProviderUseCase = mock<AuthByProviderUseCase>()
        val handleOAuthCallbackUseCase = mock<HandleOAuthCallbackUseCase>()
        val viewModel = AuthViewModel(authByProviderUseCase, handleOAuthCallbackUseCase)

        everySuspend {
            authByProviderUseCase("APPLE")
        } returns Result.success("https://supabase.com/auth/apple")

        viewModel.oAuthUrlToLaunch.test {
            viewModel.onProviderPick(AuthProvider.APPLE)
            testDispatcher.scheduler.advanceUntilIdle()

            assertThat(awaitItem()).isEqualTo("https://supabase.com/auth/apple")
        }

        verifySuspend {
            authByProviderUseCase("APPLE")
        }
    }

    @Test
    fun `nominal case - onProviderPick sets loading to true then false on success`() = runTest(testDispatcher) {
        val authByProviderUseCase = mock<AuthByProviderUseCase>()
        val handleOAuthCallbackUseCase = mock<HandleOAuthCallbackUseCase>()
        val viewModel = AuthViewModel(authByProviderUseCase, handleOAuthCallbackUseCase)

        everySuspend {
            authByProviderUseCase("GOOGLE")
        } returns Result.success("https://supabase.com/auth/google")

        viewModel.authLoading.test {
            assertThat(awaitItem()).isFalse()

            viewModel.onProviderPick(AuthProvider.GOOGLE)
            assertThat(awaitItem()).isTrue()

            testDispatcher.scheduler.advanceUntilIdle()
            assertThat(awaitItem()).isFalse()
        }
    }

    @Test
    fun `error case - onProviderPick sets error to true on failure`() = runTest(testDispatcher) {
        val authByProviderUseCase = mock<AuthByProviderUseCase>()
        val handleOAuthCallbackUseCase = mock<HandleOAuthCallbackUseCase>()
        val viewModel = AuthViewModel(authByProviderUseCase, handleOAuthCallbackUseCase)

        everySuspend {
            authByProviderUseCase("GOOGLE")
        } returns Result.failure(Exception("Network error"))

        viewModel.authError.test {
            assertThat(awaitItem()).isFalse()

            viewModel.onProviderPick(AuthProvider.GOOGLE)
            testDispatcher.scheduler.advanceUntilIdle()

            assertThat(awaitItem()).isTrue()
        }
    }

    @Test
    fun `nominal case - onOAuthCallback success sets loading to false`() = runTest(testDispatcher) {
        val authByProviderUseCase = mock<AuthByProviderUseCase>()
        val handleOAuthCallbackUseCase = mock<HandleOAuthCallbackUseCase>()
        val viewModel = AuthViewModel(authByProviderUseCase, handleOAuthCallbackUseCase)

        everySuspend {
            handleOAuthCallbackUseCase("trackshift://callback#access_token=abc&refresh_token=xyz")
        } returns Result.success(Unit)

        viewModel.authLoading.test {
            assertThat(awaitItem()).isFalse()

            viewModel.onOAuthCallback("trackshift://callback#access_token=abc&refresh_token=xyz")
            testDispatcher.scheduler.advanceUntilIdle()
        }

        verifySuspend {
            handleOAuthCallbackUseCase("trackshift://callback#access_token=abc&refresh_token=xyz")
        }
    }

    @Test
    fun `error case - onOAuthCallback failure sets error to true`() = runTest(testDispatcher) {
        val authByProviderUseCase = mock<AuthByProviderUseCase>()
        val handleOAuthCallbackUseCase = mock<HandleOAuthCallbackUseCase>()
        val viewModel = AuthViewModel(authByProviderUseCase, handleOAuthCallbackUseCase)

        everySuspend {
            handleOAuthCallbackUseCase("trackshift://callback#error=access_denied")
        } returns Result.failure(Exception("OAuth failed"))

        viewModel.authError.test {
            assertThat(awaitItem()).isFalse()

            viewModel.onOAuthCallback("trackshift://callback#error=access_denied")
            testDispatcher.scheduler.advanceUntilIdle()

            assertThat(awaitItem()).isTrue()
        }

        verifySuspend {
            handleOAuthCallbackUseCase("trackshift://callback#error=access_denied")
        }
    }
}
