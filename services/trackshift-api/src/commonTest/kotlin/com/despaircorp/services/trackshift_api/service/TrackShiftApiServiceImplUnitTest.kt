package com.despaircorp.services.trackshift_api.service

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFailure
import assertk.assertions.isSuccess
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import kotlin.test.Test

class TrackShiftApiServiceImplUnitTest {

    private fun createMockClientWithResponse(
        content: String,
        status: HttpStatusCode = HttpStatusCode.OK
    ): HttpClient {
        val mockEngine = MockEngine { _ ->
            respond(
                content = content,
                status = status,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }
        return HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
        }
    }

    private fun createMockClientThatThrows(exception: Exception): HttpClient {
        val mockEngine = MockEngine { _ ->
            throw exception
        }
        return HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
        }
    }

    @Test
    fun `getUser - nominal case returns UserDto`() = runTest {
        val userJson = """
            {
                "id": "user_123",
                "display_name": "Monokouma",
                "avatar_url": "https://example.com/avatar.jpg",
                "is_pro": true,
                "created_at": "2026-01-01",
                "links_created_month": 5,
                "links_converted_month": 3,
                "total_links_created": 42,
                "total_playlists_created": 10,
                "total_links_converted": 28,
                "pro_expires_at": "2026-12-31",
                "last_reset_at": "2026-02-01"
            }
        """.trimIndent()

        val client = createMockClientWithResponse(userJson)
        val service = TrackShiftApiServiceImpl(client)

        val result = service.getUser("user_123")

        assertThat(result).isSuccess()
        assertThat(result.getOrNull()?.id).isEqualTo("user_123")
        assertThat(result.getOrNull()?.displayName).isEqualTo("Monokouma")
        assertThat(result.getOrNull()?.isPro).isEqualTo(true)
    }

    @Test
    fun `getUser - error case returns failure on exception`() = runTest {
        val client = createMockClientThatThrows(Exception("Network error"))
        val service = TrackShiftApiServiceImpl(client)

        val result = service.getUser("user_123")

        assertThat(result).isFailure()
        assertThat(result.exceptionOrNull()?.message).isEqualTo("Network error")
    }

    @Test
    fun `updateUsername - nominal case returns success`() = runTest {
        val client = createMockClientWithResponse("", HttpStatusCode.OK)
        val service = TrackShiftApiServiceImpl(client)

        val result = service.updateUsername("NewName", "user_123")

        assertThat(result).isSuccess()
    }

    @Test
    fun `updateUsername - error case returns failure on non-OK status`() = runTest {
        val client = createMockClientWithResponse("", HttpStatusCode.InternalServerError)
        val service = TrackShiftApiServiceImpl(client)

        val result = service.updateUsername("NewName", "user_123")

        assertThat(result).isFailure()
        assertThat(result.exceptionOrNull()?.message).isEqualTo("Name update failed")
    }

    @Test
    fun `updateUsername - error case returns failure on exception`() = runTest {
        val client = createMockClientThatThrows(Exception("Network error"))
        val service = TrackShiftApiServiceImpl(client)

        val result = service.updateUsername("NewName", "user_123")

        assertThat(result).isFailure()
        assertThat(result.exceptionOrNull()?.message).isEqualTo("Network error")
    }

    @Test
    fun `updateUserImage - nominal case returns success`() = runTest {
        val client = createMockClientWithResponse("", HttpStatusCode.OK)
        val service = TrackShiftApiServiceImpl(client)
        val imageBytes = byteArrayOf(1, 2, 3, 4, 5)

        val result = service.updateUserImage(imageBytes, "user_123")

        assertThat(result).isSuccess()
    }

    @Test
    fun `updateUserImage - error case returns failure on non-OK status`() = runTest {
        val client = createMockClientWithResponse("", HttpStatusCode.InternalServerError)
        val service = TrackShiftApiServiceImpl(client)
        val imageBytes = byteArrayOf(1, 2, 3, 4, 5)

        val result = service.updateUserImage(imageBytes, "user_123")

        assertThat(result).isFailure()
    }

    @Test
    fun `updateUserImage - error case returns failure on exception`() = runTest {
        val client = createMockClientThatThrows(Exception("Upload failed"))
        val service = TrackShiftApiServiceImpl(client)
        val imageBytes = byteArrayOf(1, 2, 3, 4, 5)

        val result = service.updateUserImage(imageBytes, "user_123")

        assertThat(result).isFailure()
        assertThat(result.exceptionOrNull()?.message).isEqualTo("Upload failed")
    }
}
