package com.despaircorp.services.trackshift_api.service

import com.despaircorp.services.trackshift_api.service.dto.UserDto

interface TrackShiftApiService {
    suspend fun getUser(id: String): Result<UserDto>
    suspend fun updateUsername(newName: String, id: String): Result<Unit>
    suspend fun updateUserImage(image: ByteArray, id: String): Result<Unit>
}

