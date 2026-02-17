package com.despaircorp.domain.link_generation.domain.repo

import com.despaircorp.services.trackshift_api.service.request.GenerateLinkRequest

interface LinkGenerationRepository {

    suspend fun generateTrackShiftUrl(
        request: GenerateLinkRequest
    ): Result<String>

    suspend fun updateGenerationCount(
        userId: String
    ): Result<Boolean>

    suspend fun saveConversionToHistory(
        userId: String, url: String
    ): Result<Unit>
}
