package com.despaircorp.domain.auth.domain.repo

import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun handleSessionsStatus(): Flow<SessionStatus>
    suspend fun getOAuthUrl(provider: String): Result<String>
    suspend fun handleOAuthCallback(url: String): Result<Unit>
    suspend fun getCurrentUserId(): Result<String>
}
