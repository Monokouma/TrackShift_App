package com.despaircorp.domain.link_generation.data

import com.despaircorp.domain.link_generation.domain.repo.LinkGenerationRepository
import com.despaircorp.services.trackshift_api.service.TrackShiftApiService
import com.despaircorp.services.trackshift_api.service.request.GenerateLinkRequest

class LinkGenerationRepositoryImpl(
    private val trackShiftApiService: TrackShiftApiService
): LinkGenerationRepository {

    override suspend fun generateTrackShiftUrl(
        request: GenerateLinkRequest
    ): Result<String> = trackShiftApiService
        .generateTrackShiftUrl(request)

    override suspend fun updateGenerationCount(
        userId: String
    ): Result<Boolean> = trackShiftApiService
        .updateGenerationCount(userId)

    override suspend fun saveConversionToHistory(
        userId: String,
        url: String
    ): Result<Unit> = trackShiftApiService
        .saveConversionToHistory(userId, url)

}