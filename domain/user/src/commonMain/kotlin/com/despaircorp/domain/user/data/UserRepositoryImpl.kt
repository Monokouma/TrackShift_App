package com.despaircorp.domain.user.data

import com.despaircorp.domain.user.domain.model.User
import com.despaircorp.domain.user.domain.repo.UserRepository
import com.despaircorp.domain.user.mapper.toDomain
import com.despaircorp.services.trackshift_api.service.TrackShiftApiService

class UserRepositoryImpl(
    private val trackShiftApiService: TrackShiftApiService
) : UserRepository {

    override suspend fun getUser(
        id: String
    ): Result<User> = trackShiftApiService
        .getUser(id)
        .mapCatching { userDto ->
            userDto.toDomain() ?: throw Exception("Invalid user data")
        }

    override suspend fun updateUserName(newName: String, id: String): Result<Unit> =
        trackShiftApiService.updateUsername(newName, id)

    override suspend fun updateUserImage(
        image: ByteArray,
        id: String
    ): Result<Unit> = trackShiftApiService.updateUserImage(image, id)

    override suspend fun deleteAccount(userId: String): Result<Unit> =
        trackShiftApiService.deleteAccount(userId)
}