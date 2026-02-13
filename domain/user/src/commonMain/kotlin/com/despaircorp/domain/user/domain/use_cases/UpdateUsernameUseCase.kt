package com.despaircorp.domain.user.domain.use_cases

import com.despaircorp.domain.auth.domain.use_cases.GetCurrentUserIdUseCase
import com.despaircorp.domain.user.domain.repo.UserRepository

class UpdateUsernameUseCase(
    private val userRepository: UserRepository,
    private val getCurrentUserIdUseCase: GetCurrentUserIdUseCase
) {
    suspend operator fun invoke(newName: String): Result<Unit> {
        val id = getCurrentUserIdUseCase.invoke().getOrNull() ?: return Result.failure(Exception("User id is null"))

        return userRepository.updateUserName(newName, id)
    }
}