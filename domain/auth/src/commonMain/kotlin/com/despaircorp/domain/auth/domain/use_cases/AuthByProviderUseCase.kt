package com.despaircorp.domain.auth.domain.use_cases

import com.despaircorp.domain.auth.domain.repo.AuthRepository

class AuthByProviderUseCase(
    private val authRepository: AuthRepository
) {
    /**
     * Get the OAuth URL for the given provider.
     * @param authProvider One of: "GOOGLE", "APPLE", "DISCORD"
     * @return Result with OAuth URL to open in browser
     */
    suspend operator fun invoke(authProvider: String): Result<String> {
        val provider = when (authProvider) {
            "GOOGLE" -> "google"
            "APPLE" -> "apple"
            "DISCORD" -> "discord"
            else -> return Result.failure(Exception("Unknown auth provider"))
        }

        return authRepository.getOAuthUrl(provider)
    }
}
