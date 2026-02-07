package com.despaircorp.domain.auth.domain.use_cases

import com.despaircorp.domain.auth.domain.repo.AuthRepository

open class HandleOAuthCallbackUseCase(
    private val authRepository: AuthRepository
) {

    open suspend operator fun invoke(url: String): Result<Unit> = authRepository.handleOAuthCallback(url)
}
