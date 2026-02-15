package com.despaircorp.domain.user.domain.use_cases

import com.despaircorp.domain.user.domain.model.UserAction
import kotlin.compareTo

class IsUserLimitReachUseCase(
    private val getUserDataUseCase: GetUserDataUseCase
) {
    suspend operator fun invoke(forAction: UserAction): Result<Boolean> {
        val user = getUserDataUseCase.invoke().getOrNull() ?: return Result.failure(Exception("Null user"))

        return when (forAction) {
            UserAction.GENERATE -> {
                if (!user.isPro && user.linksCreatedMonth >= 10) {
                    Result.success(true)
                } else {
                    Result.success(false)
                }
            }
            UserAction.CONVERT -> {
                if (!user.isPro && user.linksConvertedMonth >= 10) {
                    Result.success(true)
                } else {
                    Result.success(false)
                }
            }
        }
    }
}