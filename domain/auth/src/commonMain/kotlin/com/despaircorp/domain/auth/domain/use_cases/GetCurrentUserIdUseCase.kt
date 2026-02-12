package com.despaircorp.domain.auth.domain.use_cases

import com.despaircorp.domain.auth.domain.repo.AuthRepository

class GetCurrentUserIdUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Result<String> = authRepository.getCurrentUserId()
}