package com.despaircorp.domain.auth.domain.use_cases

import com.despaircorp.domain.auth.domain.repo.AuthRepository

class HandleOAuthCallbackUseCase(
    private val authRepository: AuthRepository
) {

    suspend operator fun invoke(url: String): Result<Unit> = authRepository.handleOAuthCallback(url)
}
