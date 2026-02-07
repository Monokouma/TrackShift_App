package com.despaircorp.domain.auth.domain

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isInstanceOf
import com.despaircorp.domain.auth.domain.repo.AuthRepository
import com.despaircorp.domain.auth.domain.use_cases.HandleSessionStatusUseCase
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.mock
import dev.mokkery.verify
import io.github.jan.supabase.auth.status.SessionStatus
import io.github.jan.supabase.auth.user.UserSession
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlin.test.Test


class HandleSessionStatusUseCaseUnitTest {
    private val testDispatcher = StandardTestDispatcher()

    private val fakeUserSession = UserSession(
        accessToken = "fake_access_token",
        refreshToken = "fake_refresh_token",
        expiresIn = 3600L,
        tokenType = "Bearer"
    )

    @Test
    fun `nominal case - session is authenticated`() = runTest(testDispatcher) {
        val repository = mock<AuthRepository>()
        val useCase = HandleSessionStatusUseCase(repository)

        every {
            repository.handleSessionsStatus()
        } returns flowOf(SessionStatus.Authenticated(session = fakeUserSession))

        useCase().test {
            assertThat(awaitItem()).isInstanceOf<SessionStatus.Authenticated>()
            awaitComplete()
        }

        verify {
            repository.handleSessionsStatus()
        }
    }

    @Test
    fun `nominal case - session is not authenticated`() = runTest(testDispatcher) {
        val repository = mock<AuthRepository>()
        val useCase = HandleSessionStatusUseCase(repository)

        every {
            repository.handleSessionsStatus()
        } returns flowOf(SessionStatus.NotAuthenticated(isSignOut = false))

        useCase().test {
            val status = awaitItem()
            assertThat(status).isInstanceOf<SessionStatus.NotAuthenticated>()
            awaitComplete()
        }

        verify {
            repository.handleSessionsStatus()
        }
    }

    @Test
    fun `nominal case - session signed out`() = runTest(testDispatcher) {
        val repository = mock<AuthRepository>()
        val useCase = HandleSessionStatusUseCase(repository)

        every {
            repository.handleSessionsStatus()
        } returns flowOf(SessionStatus.NotAuthenticated(isSignOut = true))

        useCase().test {
            val status = awaitItem()
            assertThat(status).isInstanceOf<SessionStatus.NotAuthenticated>()
            awaitComplete()
        }

        verify {
            repository.handleSessionsStatus()
        }
    }

    @Test
    fun `nominal case - session initializing`() = runTest(testDispatcher) {
        val repository = mock<AuthRepository>()
        val useCase = HandleSessionStatusUseCase(repository)

        every {
            repository.handleSessionsStatus()
        } returns flowOf(SessionStatus.Initializing)

        useCase().test {
            assertThat(awaitItem()).isInstanceOf<SessionStatus.Initializing>()
            awaitComplete()
        }

        verify {
            repository.handleSessionsStatus()
        }
    }

    @Test
    fun `nominal case - session status changes`() = runTest(testDispatcher) {
        val repository = mock<AuthRepository>()
        val useCase = HandleSessionStatusUseCase(repository)

        every {
            repository.handleSessionsStatus()
        } returns flowOf(
            SessionStatus.Initializing,
            SessionStatus.NotAuthenticated(isSignOut = false),
            SessionStatus.Authenticated(session = fakeUserSession)
        )

        useCase().test {
            assertThat(awaitItem()).isInstanceOf<SessionStatus.Initializing>()
            assertThat(awaitItem()).isInstanceOf<SessionStatus.NotAuthenticated>()
            assertThat(awaitItem()).isInstanceOf<SessionStatus.Authenticated>()
            awaitComplete()
        }

        verify {
            repository.handleSessionsStatus()
        }
    }
}
