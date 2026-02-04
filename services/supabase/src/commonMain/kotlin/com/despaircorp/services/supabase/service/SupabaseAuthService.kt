package com.despaircorp.services.supabase.service

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.AuthProvider
import io.github.jan.supabase.auth.status.SessionStatus
import io.ktor.utils.io.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class SupabaseAuthService(
    private val supabaseClient: SupabaseClient
) {
    fun handleSessionsStatus(): Flow<SessionStatus> = supabaseClient.auth.sessionStatus

    suspend fun authWithProvider(authProvider: AuthProvider<out Any, out Any>): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            supabaseClient.auth.signInWith(authProvider)
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}