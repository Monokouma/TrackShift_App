package com.despaircorp.services.supabase.service

import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.isEqualTo
import assertk.assertions.isFailure
import assertk.assertions.isSuccess
import dev.mokkery.answering.returns
import dev.mokkery.answering.throws
import dev.mokkery.every
import dev.mokkery.everySuspend
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.status.SessionStatus
import io.github.jan.supabase.auth.user.UserInfo
import io.github.jan.supabase.auth.user.UserSession
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class SupabaseAuthServiceImplUnitTest {

    @Test
    fun `getOAuthUrl - returns url with provider and redirect`() = runTest {
        val supabaseClient = mock<SupabaseClient>()
        every { supabaseClient.supabaseUrl } returns "https://myproject.supabase.co"

        val service = SupabaseAuthServiceImpl(supabaseClient)
        val result = service.getOAuthUrl("google")

        assertThat(result).isSuccess()
        assertThat(result.getOrNull()!!).contains("provider=google")
        assertThat(result.getOrNull()!!).contains("redirect_to=")
        assertThat(result.getOrNull()!!).contains("trackshift")
    }

    @Test
    fun `getOAuthUrl - handles url without http prefix`() = runTest {
        val supabaseClient = mock<SupabaseClient>()
        every { supabaseClient.supabaseUrl } returns "myproject.supabase.co"

        val service = SupabaseAuthServiceImpl(supabaseClient)
        val result = service.getOAuthUrl("apple")

        assertThat(result).isSuccess()
        assertThat(result.getOrNull()!!).contains("https://myproject.supabase.co")
        assertThat(result.getOrNull()!!).contains("provider=apple")
    }

    @Test
    fun `handleOAuthCallback - returns failure with empty fragment`() = runTest {
        val supabaseClient = mock<SupabaseClient>()

        val service = SupabaseAuthServiceImpl(supabaseClient)
        val result = service.handleOAuthCallback("trackshift://callback")

        assertThat(result).isFailure()
        assertThat(result.exceptionOrNull()?.message).isEqualTo("Empty fragment in callback URL")
    }

    @Test
    fun `handleOAuthCallback - returns failure when missing tokens`() = runTest {
        val supabaseClient = mock<SupabaseClient>()

        val service = SupabaseAuthServiceImpl(supabaseClient)
        val result = service.handleOAuthCallback("trackshift://callback#error=access_denied")

        assertThat(result).isFailure()
        assertThat(result.exceptionOrNull()?.message).isEqualTo("Missing tokens in callback")
    }

    @Test
    fun `handleOAuthCallback - returns failure when only access_token present`() = runTest {
        val supabaseClient = mock<SupabaseClient>()

        val service = SupabaseAuthServiceImpl(supabaseClient)
        val result = service.handleOAuthCallback("trackshift://callback#access_token=abc123")

        assertThat(result).isFailure()
        assertThat(result.exceptionOrNull()?.message).isEqualTo("Missing tokens in callback")
    }

    @Test
    fun `handleOAuthCallback - returns failure when only refresh_token present`() = runTest {
        val supabaseClient = mock<SupabaseClient>()

        val service = SupabaseAuthServiceImpl(supabaseClient)
        val result = service.handleOAuthCallback("trackshift://callback#refresh_token=xyz789")

        assertThat(result).isFailure()
        assertThat(result.exceptionOrNull()?.message).isEqualTo("Missing tokens in callback")
    }

}
