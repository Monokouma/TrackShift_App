package com.despaircorp.services.supabase.service

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.coroutines.flow.Flow

class SupabaseAuthService(
    private val supabaseClient: SupabaseClient
) {
    fun handleSessionsStatus(): Flow<SessionStatus> = supabaseClient.auth.sessionStatus

}