package com.despaircorp.domain.link_generation.domain

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFailure
import assertk.assertions.isSuccess
import com.despaircorp.domain.auth.domain.repo.AuthRepository
import com.despaircorp.domain.auth.domain.use_cases.GetCurrentUserIdUseCase
import com.despaircorp.domain.link_generation.domain.repo.LinkGenerationRepository
import com.despaircorp.domain.link_generation.domain.use_cases.GenerateTrackShiftLinkFromPlaylistUrlUseCase
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class GenerateTrackShiftLinkFromPlaylistUrlUseCaseUnitTest {

    private val authRepository = mock<AuthRepository>()
    private val linkGenerationRepository = mock<LinkGenerationRepository>()
    private val getCurrentUserIdUseCase = GetCurrentUserIdUseCase(authRepository)

    @Test
    fun `nominal case - returns trackshift url when all steps succeed`() = runTest {
        val userId = "user_123"
        val url = "https://open.spotify.com/playlist/123"
        val trackShiftUrl = "https://trackshift.fr/code/abc"

        everySuspend { authRepository.getCurrentUserId() } returns Result.success(userId)
        everySuspend { linkGenerationRepository.generateTrackShiftUrl(any()) } returns Result.success(trackShiftUrl)
        everySuspend { linkGenerationRepository.saveConversionToHistory(userId, trackShiftUrl) } returns Result.success(Unit)
        everySuspend { linkGenerationRepository.updateGenerationCount(userId) } returns Result.success(true)

        val useCase = GenerateTrackShiftLinkFromPlaylistUrlUseCase(getCurrentUserIdUseCase, linkGenerationRepository)
        val result = useCase(url)

        assertThat(result).isSuccess()
        assertThat(result.getOrNull()).isEqualTo(trackShiftUrl)

        verifySuspend { linkGenerationRepository.generateTrackShiftUrl(any()) }
        verifySuspend { linkGenerationRepository.saveConversionToHistory(userId, trackShiftUrl) }
        verifySuspend { linkGenerationRepository.updateGenerationCount(userId) }
    }

    @Test
    fun `error case - returns failure when user id is null`() = runTest {
        everySuspend { authRepository.getCurrentUserId() } returns Result.failure(Exception("No session"))

        val useCase = GenerateTrackShiftLinkFromPlaylistUrlUseCase(getCurrentUserIdUseCase, linkGenerationRepository)
        val result = useCase("https://open.spotify.com/playlist/123")

        assertThat(result).isFailure()
        assertThat(result.exceptionOrNull()?.message).isEqualTo("Null id")
    }

    @Test
    fun `error case - returns failure when generated url is null`() = runTest {
        val userId = "user_123"

        everySuspend { authRepository.getCurrentUserId() } returns Result.success(userId)
        everySuspend { linkGenerationRepository.generateTrackShiftUrl(any()) } returns Result.failure(Exception("API error"))

        val useCase = GenerateTrackShiftLinkFromPlaylistUrlUseCase(getCurrentUserIdUseCase, linkGenerationRepository)
        val result = useCase("https://open.spotify.com/playlist/123")

        assertThat(result).isFailure()
        assertThat(result.exceptionOrNull()?.message).isEqualTo("Null trackshift url")
    }

    @Test
    fun `error case - returns failure when generation count update fails`() = runTest {
        val userId = "user_123"
        val trackShiftUrl = "https://trackshift.fr/code/abc"

        everySuspend { authRepository.getCurrentUserId() } returns Result.success(userId)
        everySuspend { linkGenerationRepository.generateTrackShiftUrl(any()) } returns Result.success(trackShiftUrl)
        everySuspend { linkGenerationRepository.saveConversionToHistory(userId, trackShiftUrl) } returns Result.success(Unit)
        everySuspend { linkGenerationRepository.updateGenerationCount(userId) } returns Result.failure(Exception("DB error"))

        val useCase = GenerateTrackShiftLinkFromPlaylistUrlUseCase(getCurrentUserIdUseCase, linkGenerationRepository)
        val result = useCase("https://open.spotify.com/playlist/123")

        assertThat(result).isFailure()
        assertThat(result.exceptionOrNull()?.message).isEqualTo("Count update failure")
    }

    @Test
    fun `error case - returns failure when history save fails`() = runTest {
        val userId = "user_123"
        val trackShiftUrl = "https://trackshift.fr/code/abc"

        everySuspend { authRepository.getCurrentUserId() } returns Result.success(userId)
        everySuspend { linkGenerationRepository.generateTrackShiftUrl(any()) } returns Result.success(trackShiftUrl)
        everySuspend { linkGenerationRepository.saveConversionToHistory(userId, trackShiftUrl) } returns Result.failure(Exception("Save error"))
        everySuspend { linkGenerationRepository.updateGenerationCount(userId) } returns Result.success(true)

        val useCase = GenerateTrackShiftLinkFromPlaylistUrlUseCase(getCurrentUserIdUseCase, linkGenerationRepository)
        val result = useCase("https://open.spotify.com/playlist/123")

        assertThat(result).isFailure()
        assertThat(result.exceptionOrNull()?.message).isEqualTo("Error while generating trackshift url")
    }
}
