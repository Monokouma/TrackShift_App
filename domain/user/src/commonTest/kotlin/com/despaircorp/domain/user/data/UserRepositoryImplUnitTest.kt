package com.despaircorp.domain.user.data

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFailure
import assertk.assertions.isNotNull
import assertk.assertions.isSuccess
import com.despaircorp.services.trackshift_api.service.TrackShiftApiService
import com.despaircorp.services.trackshift_api.service.dto.UserDto
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class UserRepositoryImplUnitTest {

    private val trackShiftApiService = mock<TrackShiftApiService>()

    @Test
    fun `getUser - returns user when api returns valid dto`() = runTest {
        val userId = "user_123"
        val userDto = provideUserDto()

        everySuspend { trackShiftApiService.getUser(userId) } returns Result.success(userDto)

        val repository = UserRepositoryImpl(trackShiftApiService)
        val result = repository.getUser(userId)

        assertThat(result).isSuccess()
        assertThat(result.getOrNull()).isNotNull()
        assertThat(result.getOrNull()?.id).isEqualTo(userId)
        assertThat(result.getOrNull()?.displayName).isEqualTo("Monokouma")

        verifySuspend { trackShiftApiService.getUser(userId) }
    }

    @Test
    fun `getUser - returns failure when api fails`() = runTest {
        val userId = "user_123"

        everySuspend { trackShiftApiService.getUser(userId) } returns Result.failure(Exception("Network error"))

        val repository = UserRepositoryImpl(trackShiftApiService)
        val result = repository.getUser(userId)

        assertThat(result).isFailure()
        assertThat(result.exceptionOrNull()?.message).isEqualTo("Network error")
    }

    @Test
    fun `getUser - returns failure when dto has null displayName`() = runTest {
        val userId = "user_123"
        val invalidDto = provideUserDto().copy(displayName = null)

        everySuspend { trackShiftApiService.getUser(userId) } returns Result.success(invalidDto)

        val repository = UserRepositoryImpl(trackShiftApiService)
        val result = repository.getUser(userId)

        assertThat(result).isFailure()
        assertThat(result.exceptionOrNull()?.message).isEqualTo("Invalid user data")
    }

    @Test
    fun `getUser - returns failure when dto has null createdAt`() = runTest {
        val userId = "user_123"
        val invalidDto = provideUserDto().copy(createdAt = null)

        everySuspend { trackShiftApiService.getUser(userId) } returns Result.success(invalidDto)

        val repository = UserRepositoryImpl(trackShiftApiService)
        val result = repository.getUser(userId)

        assertThat(result).isFailure()
        assertThat(result.exceptionOrNull()?.message).isEqualTo("Invalid user data")
    }

    @Test
    fun `updateUserName - returns success when api succeeds`() = runTest {
        val userId = "user_123"
        val newName = "NewName"

        everySuspend { trackShiftApiService.updateUsername(newName, userId) } returns Result.success(Unit)

        val repository = UserRepositoryImpl(trackShiftApiService)
        val result = repository.updateUserName(newName, userId)

        assertThat(result).isSuccess()

        verifySuspend { trackShiftApiService.updateUsername(newName, userId) }
    }

    @Test
    fun `updateUserName - returns failure when api fails`() = runTest {
        val userId = "user_123"
        val newName = "NewName"

        everySuspend { trackShiftApiService.updateUsername(newName, userId) } returns Result.failure(Exception("Update failed"))

        val repository = UserRepositoryImpl(trackShiftApiService)
        val result = repository.updateUserName(newName, userId)

        assertThat(result).isFailure()
        assertThat(result.exceptionOrNull()?.message).isEqualTo("Update failed")
    }

    @Test
    fun `updateUserImage - returns success when api succeeds`() = runTest {
        val userId = "user_123"
        val imageBytes = byteArrayOf(1, 2, 3)

        everySuspend { trackShiftApiService.updateUserImage(imageBytes, userId) } returns Result.success(Unit)

        val repository = UserRepositoryImpl(trackShiftApiService)
        val result = repository.updateUserImage(imageBytes, userId)

        assertThat(result).isSuccess()

        verifySuspend { trackShiftApiService.updateUserImage(imageBytes, userId) }
    }

    @Test
    fun `updateUserImage - returns failure when api fails`() = runTest {
        val userId = "user_123"
        val imageBytes = byteArrayOf(1, 2, 3)

        everySuspend { trackShiftApiService.updateUserImage(imageBytes, userId) } returns Result.failure(Exception("Upload failed"))

        val repository = UserRepositoryImpl(trackShiftApiService)
        val result = repository.updateUserImage(imageBytes, userId)

        assertThat(result).isFailure()
        assertThat(result.exceptionOrNull()?.message).isEqualTo("Upload failed")
    }

    @Test
    fun `deleteAccount - returns success when api succeeds`() = runTest {
        val userId = "user_123"

        everySuspend { trackShiftApiService.deleteAccount(userId) } returns Result.success(Unit)

        val repository = UserRepositoryImpl(trackShiftApiService)
        val result = repository.deleteAccount(userId)

        assertThat(result).isSuccess()

        verifySuspend { trackShiftApiService.deleteAccount(userId) }
    }

    @Test
    fun `deleteAccount - returns failure when api fails`() = runTest {
        val userId = "user_123"

        everySuspend { trackShiftApiService.deleteAccount(userId) } returns Result.failure(Exception("Delete failed"))

        val repository = UserRepositoryImpl(trackShiftApiService)
        val result = repository.deleteAccount(userId)

        assertThat(result).isFailure()
        assertThat(result.exceptionOrNull()?.message).isEqualTo("Delete failed")
    }

    private fun provideUserDto() = UserDto(
        id = "user_123",
        displayName = "Monokouma",
        avatarUrl = "https://example.com/avatar.jpg",
        isPro = true,
        createdAt = "2026-01-01",
        linksCreatedMonth = 5,
        linksConvertedMonth = 3,
        totalLinksCreated = 42,
        totalPlaylistsCreated = 10,
        totalLinksConverted = 28,
        proExpiresAt = "2026-12-31",
        lastResetAt = "2026-02-01"
    )
}
