package com.despaircorp.domain.user.data

import co.touchlab.kermit.Logger.Companion.i
import com.despaircorp.domain.user.domain.model.User
import com.despaircorp.domain.user.domain.repo.UserRepository
import com.despaircorp.domain.user.mapper.toDomain
import com.despaircorp.services.trackshift_api.service.TrackShiftApiService

class UserRepositoryImpl(
    private val trackShiftApiService: TrackShiftApiService
): UserRepository {

    override suspend fun getUser(
        id: String
    ): Result<User> = trackShiftApiService
        .getUser(id)
        .getOrNull()
        .let { userDto ->
            if (userDto == null) {
                return@let Result.failure(Exception("Null user"))
            }

            return@let Result.success(userDto.toDomain())
    }
}