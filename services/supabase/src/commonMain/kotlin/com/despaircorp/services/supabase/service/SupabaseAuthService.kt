package com.despaircorp.services.supabase.service

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.status.SessionStatus
import io.ktor.http.encodeURLParameter
import io.ktor.utils.io.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class SupabaseAuthService(
    private val supabaseClient: SupabaseClient
) {
    fun handleSessionsStatus(): Flow<SessionStatus> = supabaseClient.auth.sessionStatus

    suspend fun getOAuthUrl(provider: String): Result<String> = withContext(Dispatchers.IO) {
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

    suspend fun handleOAuthCallback(url: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val fragment = url.substringAfter("#", "")
            if (fragment.isNotEmpty()) {
                val params = fragment.split("&").associate {
                    val (key, value) = it.split("=", limit = 2)
                    key to value
                }
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
