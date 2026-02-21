package com.despaircorp.domain.auth.data

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import assertk.assertions.isFailure
import assertk.assertions.isSuccess
import assertk.assertions.isTrue
import com.despaircorp.services.supabase.service.SupabaseAuthService
import io.github.jan.supabase.auth.user.UserInfo
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.everySuspend
import dev.mokkery.mock
import dev.mokkery.verify
import dev.mokkery.verifySuspend
import io.github.jan.supabase.auth.status.SessionStatus
import io.github.jan.supabase.auth.user.UserSession
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlin.test.Test


class AuthRepositoryImplUnitTest {
    private val testDispatcher = StandardTestDispatcher()

    private val fakeUserSession = UserSession(
        accessToken = "fake_access_token",
        refreshToken = "fake_refresh_token",
        expiresIn = 3600L,
        tokenType = "Bearer"
    )

    @Test
    fun `nominal case - handleSessionsStatus returns authenticated`() = runTest(testDispatcher) {
        val service = mock<SupabaseAuthService>()
        val repository = AuthRepositoryImpl(service)

        every {
            service.handleSessionsStatus()
        } returns flowOf(SessionStatus.Authenticated(session = fakeUserSession))

        repository.handleSessionsStatus().test {
            assertThat(awaitItem()).isInstanceOf<SessionStatus.Authenticated>()
            awaitComplete()
        }

        verify {
            service.handleSessionsStatus()
        }
    }

    @Test
    fun `nominal case - handleSessionsStatus returns not authenticated`() = runTest(testDispatcher) {
        val service = mock<SupabaseAuthService>()
        val repository = AuthRepositoryImpl(service)

        every {
            service.handleSessionsStatus()
        } returns flowOf(SessionStatus.NotAuthenticated(isSignOut = false))

        repository.handleSessionsStatus().test {
            assertThat(awaitItem()).isInstanceOf<SessionStatus.NotAuthenticated>()
            awaitComplete()
        }

        verify {
            service.handleSessionsStatus()
        }
    }

    @Test
    fun `nominal case - getOAuthUrl returns url`() = runTest(testDispatcher) {
        val service = mock<SupabaseAuthService>()
        val repository = AuthRepositoryImpl(service)

        everySuspend {
            service.getOAuthUrl("google")
        } returns Result.success("https://supabase.com/auth/google")

        val result = repository.getOAuthUrl("google")

        assertThat(result.isSuccess).isTrue()
        result.onSuccess {
            assertThat(it).isEqualTo("https://supabase.com/auth/google")
        }

        verifySuspend {
            service.getOAuthUrl("google")
        }
    }

    @Test
    fun `error case - getOAuthUrl returns failure`() = runTest(testDispatcher) {
        val service = mock<SupabaseAuthService>()
        val repository = AuthRepositoryImpl(service)

        everySuspend {
            service.getOAuthUrl("google")
        } returns Result.failure(Exception("Network error"))

        val result = repository.getOAuthUrl("google")

        assertThat(result.isFailure).isTrue()
        result.onFailure {
            assertThat(it.message).isEqualTo("Network error")
        }

        verifySuspend {
            service.getOAuthUrl("google")
        }
    }

    @Test
    fun `nominal case - handleOAuthCallback succeeds`() = runTest(testDispatcher) {
        val service = mock<SupabaseAuthService>()
        val repository = AuthRepositoryImpl(service)

        everySuspend {
            service.handleOAuthCallback("trackshift://callback#access_token=abc&refresh_token=xyz")
        } returns Result.success(Unit)

        val result = repository.handleOAuthCallback("trackshift://callback#access_token=abc&refresh_token=xyz")

        assertThat(result.isSuccess).isTrue()

        verifySuspend {
            service.handleOAuthCallback("trackshift://callback#access_token=abc&refresh_token=xyz")
        }
    }

    @Test
    fun `error case - handleOAuthCallback fails`() = runTest(testDispatcher) {
        val service = mock<SupabaseAuthService>()
        val repository = AuthRepositoryImpl(service)

        everySuspend {
            service.handleOAuthCallback("trackshift://callback#error=access_denied")
        } returns Result.failure(Exception("OAuth callback failed"))

        val result = repository.handleOAuthCallback("trackshift://callback#error=access_denied")

        assertThat(result.isFailure).isTrue()
        result.onFailure {
            assertThat(it.message).isEqualTo("OAuth callback failed")
        }

        verifySuspend {
            service.handleOAuthCallback("trackshift://callback#error=access_denied")
        }
    }

    @Test
    fun `nominal case - getCurrentUserId returns user id`() = runTest(testDispatcher) {
        val service = mock<SupabaseAuthService>()
        val repository = AuthRepositoryImpl(service)
        val userInfo = UserInfo(id = "user_123", aud = "")

        everySuspend { service.getCurrentUser() } returns Result.success(userInfo)

        val result = repository.getCurrentUserId()

        assertThat(result).isSuccess()
        assertThat(result.getOrNull()).isEqualTo("user_123")

        verifySuspend { service.getCurrentUser() }
    }

    @Test
    fun `error case - getCurrentUserId returns failure when user is null`() = runTest(testDispatcher) {
        val service = mock<SupabaseAuthService>()
        val repository = AuthRepositoryImpl(service)

        everySuspend { service.getCurrentUser() } returns Result.failure(Exception("Current user is null"))

        val result = repository.getCurrentUserId()

        assertThat(result).isFailure()
        assertThat(result.exceptionOrNull()?.message).isEqualTo("Current user is null")

        verifySuspend { service.getCurrentUser() }
    }

    @Test
    fun `nominal case - logout succeeds`() = runTest(testDispatcher) {
        val service = mock<SupabaseAuthService>()
        val repository = AuthRepositoryImpl(service)

        everySuspend { service.logout() } returns Result.success(Unit)

        val result = repository.logout()

        assertThat(result).isSuccess()

        verifySuspend { service.logout() }
    }

    @Test
    fun `error case - logout fails`() = runTest(testDispatcher) {
        val service = mock<SupabaseAuthService>()
        val repository = AuthRepositoryImpl(service)

        everySuspend { service.logout() } returns Result.failure(Exception("Failure while logging out user"))

        val result = repository.logout()

        assertThat(result).isFailure()
        assertThat(result.exceptionOrNull()?.message).isEqualTo("Failure while logging out user")

        verifySuspend { service.logout() }
    }
}