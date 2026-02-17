package com.despaircorp.domain.link_generation.domain.use_cases

import com.despaircorp.domain.auth.domain.use_cases.GetCurrentUserIdUseCase
import com.despaircorp.domain.link_generation.domain.repo.LinkGenerationRepository
import com.despaircorp.services.trackshift_api.service.request.GenerateLinkRequest

open class GenerateTrackShiftLinkFromPlaylistUrlUseCase(
    private val getCurrentUserIdUseCase: GetCurrentUserIdUseCase,
    private val linkGenerationRepository: LinkGenerationRepository
) {
    open suspend operator fun invoke(url: String): Result<String> {
        val userId = getCurrentUserIdUseCase()
            .getOrNull() ?: return Result.failure(Exception("Null id"))

        val generatedTrackShiftUrl = linkGenerationRepository
            .generateTrackShiftUrl(
                request = GenerateLinkRequest(
                    method = "url",
                    images = emptyList(),
                    url = url
                )
            ).getOrNull() ?: return Result.failure(Exception("Null trackshift url"))

        val isUrlSavedToHistory = linkGenerationRepository
            .saveConversionToHistory(userId, generatedTrackShiftUrl)
            .fold(
                onSuccess = {
                    true
                },
                onFailure = {
                    false
                }
            )

        val isGenerationCountUpdated = linkGenerationRepository
            .updateGenerationCount(userId)
            .getOrNull() ?: return Result.failure(Exception("Count update failure"))

        return if (
            isGenerationCountUpdated &&
            isUrlSavedToHistory &&
            generatedTrackShiftUrl.isNotBlank()
        ) {
            Result.success(generatedTrackShiftUrl)
        } else {
            Result.failure(Exception("Error while generating trackshift url"))
        }

    }
}