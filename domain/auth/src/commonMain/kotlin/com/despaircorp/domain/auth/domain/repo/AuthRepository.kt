package com.despaircorp.domain.auth.domain.repo

import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun handleSessionsStatus(): Flow<SessionStatus>
}