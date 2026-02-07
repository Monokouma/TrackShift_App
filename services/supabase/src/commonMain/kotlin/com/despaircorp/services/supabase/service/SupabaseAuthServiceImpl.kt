package com.despaircorp.services.supabase.service

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.ktor.http.encodeURLParameter
import io.ktor.utils.io.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

class SupabaseAuthServiceImpl(
    private val supabaseClient: SupabaseClient
) : SupabaseAuthService {
    override fun handleSessionsStatus() = supabaseClient.auth.sessionStatus

    override suspend fun getOAuthUrl(provider: String) = withContext(Dispatchers.IO) {
        try {
            val redirectUrl = "trackshift://callback".encodeURLParameter()
            val baseUrl = supabaseClient.supabaseUrl
            val supabaseUrl = if (baseUrl.startsWith("http")) baseUrl else "https://$baseUrl"
            val url = "$supabaseUrl/auth/v1/authorize?provider=$provider&redirect_to=$redirectUrl"
            Result.success(url)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun handleOAuthCallback(url: String) = withContext(Dispatchers.IO) {
        try {
            val fragment = url.substringAfter("#", "")
            if (fragment.isNotEmpty()) {
                val params = fragment.split("&").mapNotNull { param ->
                    val parts = param.split("=", limit = 2)
                    if (parts.size == 2) parts[0] to parts[1] else null
                }.toMap()

                val accessToken = params["access_token"]
                val refreshToken = params["refresh_token"]

                if (accessToken != null && refreshToken != null) {
                    supabaseClient.auth.importAuthToken(accessToken, refreshToken)
                    Result.success(Unit)
                } else {
                    Result.failure(Exception("Missing tokens in callback"))
                }
            } else {
                Result.failure(Exception("Empty fragment in callback URL"))
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
