package com.despaircorp.domain.auth.domain.use_cases

import com.despaircorp.domain.auth.domain.repo.AuthRepository
import io.github.jan.supabase.auth.providers.Apple
import io.github.jan.supabase.auth.providers.Discord
import io.github.jan.supabase.auth.providers.Google

class AuthByProviderUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(authProvider: String): Result<Unit> {

        val provider = when (authProvider) {
            "GOOGLE" -> Google
            "APPLE" -> Apple
            "DISCORD" -> Discord
            else -> return Result.failure(Exception("Unknown auth provider"))
        }

        return authRepository.authByProvider(provider)
    }

}