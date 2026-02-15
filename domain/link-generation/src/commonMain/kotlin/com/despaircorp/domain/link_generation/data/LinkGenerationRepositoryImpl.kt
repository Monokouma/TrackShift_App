package com.despaircorp.domain.link_generation.data

import com.despaircorp.domain.link_generation.domain.repo.LinkGenerationRepository
import com.despaircorp.services.trackshift_api.service.TrackShiftApiService

class LinkGenerationRepositoryImpl(
    private val trackShiftApiService: TrackShiftApiService
): LinkGenerationRepository {

}