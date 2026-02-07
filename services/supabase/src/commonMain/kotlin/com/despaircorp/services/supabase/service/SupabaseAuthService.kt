package com.despaircorp.services.supabase.service

import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.coroutines.flow.Flow

interface SupabaseAuthService {
    fun handleSessionsStatus(): Flow<SessionStatus>
    suspend fun getOAuthUrl(provider: String): Result<String>
    suspend fun handleOAuthCallback(url: String): Result<Unit>
}
