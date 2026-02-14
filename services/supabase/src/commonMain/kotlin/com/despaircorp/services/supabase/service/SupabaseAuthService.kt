package com.despaircorp.services.supabase.service

import io.github.jan.supabase.auth.status.SessionStatus
import io.github.jan.supabase.auth.user.UserInfo
import kotlinx.coroutines.flow.Flow

interface SupabaseAuthService {
    fun handleSessionsStatus(): Flow<SessionStatus>
    suspend fun getOAuthUrl(provider: String): Result<String>
    suspend fun handleOAuthCallback(url: String): Result<Unit>

    suspend fun getCurrentUser(): Result<UserInfo>
}
