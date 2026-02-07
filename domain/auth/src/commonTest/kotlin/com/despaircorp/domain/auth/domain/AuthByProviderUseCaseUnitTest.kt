package com.despaircorp.domain.auth.domain

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isTrue
import com.despaircorp.domain.auth.domain.repo.AuthRepository
import com.despaircorp.domain.auth.domain.use_cases.AuthByProviderUseCase
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlin.test.Test


class AuthByProviderUseCaseUnitTest {
    private val testDispatcher = StandardTestDispatcher()

    @Test
    fun `nominal case - auth with google`() = runTest(testDispatcher) {
        val repository = mock<AuthRepository>()
        val useCase = AuthByProviderUseCase(repository)

        everySuspend {
            repository.getOAuthUrl("google")
        } returns Result.success("https://supabaseurl.com/google-auth")

        val result = useCase("GOOGLE")

        result.onSuccess {
            assertThat(it).isEqualTo("https://supabaseurl.com/google-auth")
        }

        verifySuspend {
            repository.getOAuthUrl("google")
        }
    }

    @Test
    fun `nominal case - auth with apple`() = runTest(testDispatcher) {
        val repository = mock<AuthRepository>()
        val useCase = AuthByProviderUseCase(repository)

        everySuspend {
            repository.getOAuthUrl("apple")
        } returns Result.success("https://supabaseurl.com/apple-auth")

        val result = useCase("APPLE")

        result.onSuccess {
            assertThat(it).isEqualTo("https://supabaseurl.com/apple-auth")
        }

        verifySuspend {
            repository.getOAuthUrl("apple")
        }
    }

    @Test
    fun `nominal case - auth with discord`() = runTest(testDispatcher) {
        val repository = mock<AuthRepository>()
        val useCase = AuthByProviderUseCase(repository)

        everySuspend {
            repository.getOAuthUrl("discord")
        } returns Result.success("https://supabaseurl.com/discord-auth")

        val result = useCase("DISCORD")

        result.onSuccess {
            assertThat(it).isEqualTo("https://supabaseurl.com/discord-auth")
        }

        verifySuspend {
            repository.getOAuthUrl("discord")
        }
    }

    @Test
    fun `error case - unknown provider returns failure`() = runTest(testDispatcher) {
        val repository = mock<AuthRepository>()
        val useCase = AuthByProviderUseCase(repository)

        val result = useCase("UNKNOWN_PROVIDER")

        assertThat(result.isFailure).isTrue()
        result.onFailure {
            assertThat(it.message).isEqualTo("Unknown auth provider")
        }
    }

    @Test
    fun `error case - repository failure propagates`() = runTest(testDispatcher) {
        val repository = mock<AuthRepository>()
        val useCase = AuthByProviderUseCase(repository)

        everySuspend {
            repository.getOAuthUrl("google")
        } returns Result.failure(Exception("Network error"))

        val result = useCase("GOOGLE")

        assertThat(result.isFailure).isTrue()
        result.onFailure {
            assertThat(it.message).isEqualTo("Network error")
        }

        verifySuspend {
            repository.getOAuthUrl("google")
        }
    }
}