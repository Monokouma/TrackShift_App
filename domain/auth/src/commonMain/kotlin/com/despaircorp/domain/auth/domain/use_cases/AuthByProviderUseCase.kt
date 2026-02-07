package com.despaircorp.domain.auth.domain.use_cases

import com.despaircorp.domain.auth.domain.repo.AuthRepository

open class AuthByProviderUseCase(
    private val authRepository: AuthRepository
) {

    open suspend operator fun invoke(authProvider: String): Result<String> {
        val provider = when (authProvider) {
            "GOOGLE" -> "google"
            "APPLE" -> "apple"
            "DISCORD" -> "discord"
            else -> return Result.failure(Exception("Unknown auth provider"))
        }

        return authRepository.getOAuthUrl(provider)
    }
}
