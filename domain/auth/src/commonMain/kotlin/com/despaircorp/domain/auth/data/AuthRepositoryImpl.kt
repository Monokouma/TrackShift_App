package com.despaircorp.domain.auth.data

import com.despaircorp.domain.auth.domain.repo.AuthRepository
import com.despaircorp.services.supabase.service.SupabaseAuthService
import io.github.jan.supabase.auth.providers.AuthProvider
import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.coroutines.flow.Flow

class AuthRepositoryImpl(
    private val supabaseAuthService: SupabaseAuthService
): AuthRepository {

    override fun handleSessionsStatus(
    ): Flow<SessionStatus> = supabaseAuthService
        .handleSessionsStatus()

    override suspend fun authByProvider(authProvider: AuthProvider<out Any, out Any>): Result<Unit> = supabaseAuthService.authWithProvider(authProvider)
}