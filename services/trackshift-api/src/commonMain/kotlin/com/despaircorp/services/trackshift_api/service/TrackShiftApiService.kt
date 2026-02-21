package com.despaircorp.services.trackshift_api.service

import com.despaircorp.services.trackshift_api.service.dto.UserDto
import com.despaircorp.services.trackshift_api.service.request.GenerateLinkRequest

interface TrackShiftApiService {

    suspend fun getUser(
        id: String
    ): Result<UserDto>

    suspend fun updateUsername(
        newName: String, id: String
    ): Result<Unit>

    suspend fun updateUserImage(
        image: ByteArray, id: String
    ): Result<Unit>

    suspend fun generateTrackShiftUrl(
        request: GenerateLinkRequest
    ): Result<String>

    suspend fun updateGenerationCount(
        userId: String
    ): Result<Boolean>

    suspend fun saveConversionToHistory(
        userId: String, url: String
    ): Result<Unit>

    suspend fun deleteAccount(userId: String): Result<Unit>

}