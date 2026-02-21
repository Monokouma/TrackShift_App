package com.despaircorp.domain.auth.domain.use_cases

import com.despaircorp.domain.auth.domain.repo.AuthRepository

open class LogoutUserUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Result<Unit> = authRepository.logout()
}