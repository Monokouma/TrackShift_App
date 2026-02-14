package com.despaircorp.services.trackshift_api.service

import com.despaircorp.core.secrets.BuildKonfig
import com.despaircorp.services.trackshift_api.service.dto.UserDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.request.header
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
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

    override suspend fun updateUsername(
        newName: String,
        id: String
    ): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val response = httpClient.submitFormWithBinaryData(
                url = "${BuildKonfig.TRACKSHIFT_API_URL}update_user_displayname",
                formData = formData {
                    append("user_id", id)
                    append("display_name", newName)

                }
            ) {
                header("Authorization", BuildKonfig.API_SECRET_KEY)
            }

            if (response.status == HttpStatusCode.OK)
                Result.success(Unit)
            else Result.failure(Exception("Name update failed"))

        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateUserImage(
        image: ByteArray,
        id: String
    ): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val response = httpClient.submitFormWithBinaryData(
                url = "${BuildKonfig.TRACKSHIFT_API_URL}update_user_picture",
                formData = formData {
                    append(key = "user_id", value = id)
                    append(
                        key = "images[]",
                        value = image,
                        headers = Headers.build {
                            append(HttpHeaders.ContentType, "image/png")
                            append(
                                HttpHeaders.ContentDisposition,
                                "filename=\"image.png\""
                            )
                        }
                    )

                }
            ) {
                header(key = "Authorization", value = BuildKonfig.API_SECRET_KEY)
            }

            if (response.status == HttpStatusCode.OK)
                Result.success(Unit)
            else Result.failure(Exception("Name update failed"))

        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}