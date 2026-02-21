package com.despaircorp.domain.auth.data

import com.despaircorp.domain.auth.domain.repo.AuthRepository
import com.despaircorp.services.supabase.service.SupabaseAuthService
import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.coroutines.flow.Flow

class AuthRepositoryImpl(
    private val supabaseAuthService: SupabaseAuthService
) : AuthRepository {

    override fun handleSessionsStatus(): Flow<SessionStatus> =
        supabaseAuthService.handleSessionsStatus()

    override suspend fun getOAuthUrl(provider: String): Result<String> =
        supabaseAuthService.getOAuthUrl(provider)

    override suspend fun handleOAuthCallback(url: String): Result<Unit> =
        supabaseAuthService.handleOAuthCallback(url)

    override suspend fun getCurrentUserId(): Result<String> =
        supabaseAuthService.getCurrentUser().fold(
            onSuccess = {
                Result.success(it.id)
            },
            onFailure = {
                Result.failure(it)
            }
        )

    override suspend fun logout(): Result<Unit> = supabaseAuthService.logout()

}
