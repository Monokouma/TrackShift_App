package com.despaircorp.services.trackshift_api.service

import com.despaircorp.core.secrets.BuildKonfig
import com.despaircorp.services.trackshift_api.service.dto.UserDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.request.header
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

class TrackShiftApiServiceImpl(
    private val httpClient: HttpClient
): TrackShiftApiService {
    override suspend fun getUser(id: String): Result<UserDto> = withContext(Dispatchers.IO) {
        try {
            val response = httpClient.submitFormWithBinaryData(
                url = "${BuildKonfig.TRACKSHIFT_API_URL}get_user",
                formData = formData {
                    append("user_id", id)
                }
            ) {
                header("Authorization", BuildKonfig.API_SECRET_KEY)
            }
            Result.success(response.body<UserDto>())

        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}