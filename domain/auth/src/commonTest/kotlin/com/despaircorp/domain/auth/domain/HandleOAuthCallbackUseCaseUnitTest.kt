package com.despaircorp.domain.auth.domain

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isTrue
import com.despaircorp.domain.auth.domain.repo.AuthRepository
import com.despaircorp.domain.auth.domain.use_cases.HandleOAuthCallbackUseCase
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlin.test.Test


class HandleOAuthCallbackUseCaseUnitTest {
    private val testDispatcher = StandardTestDispatcher()

    @Test
    fun `nominal case - callback handled successfully`() = runTest(testDispatcher) {
        val repository = mock<AuthRepository>()
        val useCase = HandleOAuthCallbackUseCase(repository)

        everySuspend {
            repository.handleOAuthCallback("https://app.trackshift.com/callback?code=abc123")
        } returns Result.success(Unit)

        val result = useCase("https://app.trackshift.com/callback?code=abc123")

        assertThat(result.isSuccess).isTrue()

        verifySuspend {
            repository.handleOAuthCallback("https://app.trackshift.com/callback?code=abc123")
        }
    }

    @Test
    fun `error case - callback handling fails`() = runTest(testDispatcher) {
        val repository = mock<AuthRepository>()
        val useCase = HandleOAuthCallbackUseCase(repository)

        everySuspend {
            repository.handleOAuthCallback("https://app.trackshift.com/callback?error=access_denied")
        } returns Result.failure(Exception("OAuth callback failed"))

        val result = useCase("https://app.trackshift.com/callback?error=access_denied")

        assertThat(result.isFailure).isTrue()
        result.onFailure {
            assertThat(it.message).isEqualTo("OAuth callback failed")
        }

        verifySuspend {
            repository.handleOAuthCallback("https://app.trackshift.com/callback?error=access_denied")
        }
    }
}
