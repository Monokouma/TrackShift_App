package com.despaircorp.domain.user.domain.use_cases

import com.despaircorp.domain.auth.domain.use_cases.GetCurrentUserIdUseCase
import com.despaircorp.domain.auth.domain.use_cases.LogoutUserUseCase
import com.despaircorp.domain.user.domain.repo.UserRepository

open class DeleteUserAccountUseCase(
    private val getCurrentUserIdUseCase: GetCurrentUserIdUseCase,
    private val logoutUserUseCase: LogoutUserUseCase,
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): Result<Unit> {
        val userId = getCurrentUserIdUseCase()
            .getOrNull() ?: return Result.failure(Exception("Null id"))

        return logoutUserUseCase().fold(
            onSuccess = {
                userRepository.deleteAccount(userId)
            },
            onFailure = {
                Result.failure(it)
            }
        )
    }
}