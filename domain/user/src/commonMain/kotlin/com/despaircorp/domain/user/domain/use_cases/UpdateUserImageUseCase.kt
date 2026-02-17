package com.despaircorp.domain.user.domain.use_cases

import com.despaircorp.domain.auth.domain.use_cases.GetCurrentUserIdUseCase
import com.despaircorp.domain.user.domain.repo.UserRepository

open class UpdateUserImageUseCase(
    private val userRepository: UserRepository,
    private val getCurrentUserIdUseCase: GetCurrentUserIdUseCase
) {
    open suspend operator fun invoke(image: ByteArray): Result<Unit> {
        val id = getCurrentUserIdUseCase.invoke().getOrNull()
            ?: return Result.failure(Exception("User id is null"))

        return userRepository.updateUserImage(image, id)
    }
}