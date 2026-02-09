package com.despaircorp.trackshift.utils

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.user.UserSession
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object SupabaseAuthHelper {
    private var supabaseClient: SupabaseClient? = null

    fun init(client: SupabaseClient) {
        supabaseClient = client
    }

    fun handleDeepLink(url: String) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val fragment = url.substringAfter("#", "")
                if (fragment.isEmpty()) {
                    return@launch
                }

                val params = fragment.split("&").mapNotNull { param ->
                    val parts = param.split("=", limit = 2)
                    if (parts.size == 2) parts[0] to parts[1] else null
                }.toMap()

                val accessToken = params["access_token"]
                val refreshToken = params["refresh_token"]
                val expiresIn = params["expires_in"]?.toLongOrNull() ?: 3600

                if (accessToken == null || refreshToken == null) {
                    return@launch
                }

                supabaseClient?.auth?.importSession(
                    UserSession(
                        accessToken = accessToken,
                        refreshToken = refreshToken,
                        expiresIn = expiresIn,
                        tokenType = "bearer",
                        user = null
                    )
                )

            } catch (e: Exception) {
            }
        }
    }
}