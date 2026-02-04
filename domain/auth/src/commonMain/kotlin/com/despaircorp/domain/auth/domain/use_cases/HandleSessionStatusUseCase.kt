package com.despaircorp.domain.auth.domain.use_cases

import com.despaircorp.domain.auth.domain.repo.AuthRepository
import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.coroutines.flow.Flow

class HandleSessionStatusUseCase(
    private val authRepository: AuthRepository
) {
    operator fun invoke(): Flow<SessionStatus> = authRepository.handleSessionsStatus()
}