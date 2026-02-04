package com.despaircorp.domain.auth.data

import com.despaircorp.domain.auth.domain.repo.AuthRepository
import com.despaircorp.services.supabase.service.SupabaseAuthService
import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class AuthRepositoryImpl(
    private val supabaseAuthService: SupabaseAuthService
): AuthRepository {

    override fun handleSessionsStatus(
    ): Flow<SessionStatus> = supabaseAuthService
        .handleSessionsStatus()
        .flowOn(Dispatchers.IO)
}