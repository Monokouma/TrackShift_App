package com.despaircorp.domain.link_generation.data

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFailure
import assertk.assertions.isSuccess
import com.despaircorp.services.trackshift_api.service.TrackShiftApiService
import com.despaircorp.services.trackshift_api.service.request.GenerateLinkRequest
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class LinkGenerationRepositoryImplUnitTest {

    private val trackShiftApiService = mock<TrackShiftApiService>()

    @Test
    fun `nominal case - generateTrackShiftUrl delegates to api service`() = runTest {
        val request = GenerateLinkRequest(method = "url", images = emptyList(), url = "https://spotify.com/playlist/123")
        val expectedUrl = "https://trackshift.fr/code/abc"

        everySuspend { trackShiftApiService.generateTrackShiftUrl(request) } returns Result.success(expectedUrl)

        val repository = LinkGenerationRepositoryImpl(trackShiftApiService)
        val result = repository.generateTrackShiftUrl(request)

        assertThat(result).isSuccess()
        assertThat(result.getOrNull()).isEqualTo(expectedUrl)

        verifySuspend { trackShiftApiService.generateTrackShiftUrl(request) }
    }

    @Test
    fun `error case - generateTrackShiftUrl returns failure from api`() = runTest {
        val request = GenerateLinkRequest(method = "url", images = emptyList(), url = "https://spotify.com/playlist/123")

        everySuspend { trackShiftApiService.generateTrackShiftUrl(request) } returns Result.failure(Exception("API error"))

        val repository = LinkGenerationRepositoryImpl(trackShiftApiService)
        val result = repository.generateTrackShiftUrl(request)

        assertThat(result).isFailure()
        assertThat(result.exceptionOrNull()?.message).isEqualTo("API error")
    }

    @Test
    fun `nominal case - updateGenerationCount delegates to api service`() = runTest {
        val userId = "user_123"

        everySuspend { trackShiftApiService.updateGenerationCount(userId) } returns Result.success(true)

        val repository = LinkGenerationRepositoryImpl(trackShiftApiService)
        val result = repository.updateGenerationCount(userId)

        assertThat(result).isSuccess()
        assertThat(result.getOrNull()).isEqualTo(true)

        verifySuspend { trackShiftApiService.updateGenerationCount(userId) }
    }

    @Test
    fun `error case - updateGenerationCount returns failure from api`() = runTest {
        val userId = "user_123"

        everySuspend { trackShiftApiService.updateGenerationCount(userId) } returns Result.failure(Exception("DB error"))

        val repository = LinkGenerationRepositoryImpl(trackShiftApiService)
        val result = repository.updateGenerationCount(userId)

        assertThat(result).isFailure()
        assertThat(result.exceptionOrNull()?.message).isEqualTo("DB error")
    }

    @Test
    fun `nominal case - saveConversionToHistory delegates to api service`() = runTest {
        val userId = "user_123"
        val url = "https://trackshift.fr/code/abc"

        everySuspend { trackShiftApiService.saveConversionToHistory(userId, url) } returns Result.success(Unit)

        val repository = LinkGenerationRepositoryImpl(trackShiftApiService)
        val result = repository.saveConversionToHistory(userId, url)

        assertThat(result).isSuccess()

        verifySuspend { trackShiftApiService.saveConversionToHistory(userId, url) }
    }

    @Test
    fun `error case - saveConversionToHistory returns failure from api`() = runTest {
        val userId = "user_123"
        val url = "https://trackshift.fr/code/abc"

        everySuspend { trackShiftApiService.saveConversionToHistory(userId, url) } returns Result.failure(Exception("Save error"))

        val repository = LinkGenerationRepositoryImpl(trackShiftApiService)
        val result = repository.saveConversionToHistory(userId, url)

        assertThat(result).isFailure()
        assertThat(result.exceptionOrNull()?.message).isEqualTo("Save error")
    }
}
