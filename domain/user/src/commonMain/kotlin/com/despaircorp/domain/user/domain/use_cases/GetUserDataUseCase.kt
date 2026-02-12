package com.despaircorp.domain.user.domain.use_cases

import com.despaircorp.domain.auth.domain.use_cases.GetCurrentUserIdUseCase
import com.despaircorp.domain.user.domain.model.User
import com.despaircorp.domain.user.domain.repo.UserRepository

class GetUserDataUseCase(
    private val userRepository: UserRepository,
    private val getCurrentUserIdUseCase: GetCurrentUserIdUseCase
) {
    suspend operator fun invoke(): Result<User> {
        val id = getCurrentUserIdUseCase
            .invoke()
            .getOrNull() ?: return Result.failure(Exception("User id null"))

        return Result.success(
            userRepository
                .getUser(id)
                .getOrNull()
                ?: return Result.failure(Exception("Null user"))
        )
    }
}