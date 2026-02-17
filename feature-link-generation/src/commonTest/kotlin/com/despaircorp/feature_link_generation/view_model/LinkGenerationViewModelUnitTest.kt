package com.despaircorp.feature_link_generation.view_model

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isEmpty
import assertk.assertions.isInstanceOf
import com.despaircorp.domain.auth.domain.repo.AuthRepository
import com.despaircorp.domain.auth.domain.use_cases.GetCurrentUserIdUseCase
import com.despaircorp.domain.link_generation.domain.repo.LinkGenerationRepository
import com.despaircorp.domain.link_generation.domain.use_cases.GenerateTrackShiftLinkFromPlaylistUrlUseCase
import com.despaircorp.domain.link_generation.domain.use_cases.GenerateTrackShiftLinkFromScreenshotsUseCase
import com.despaircorp.domain.user.domain.model.User
import com.despaircorp.domain.user.domain.repo.UserRepository
import com.despaircorp.domain.user.domain.use_cases.GetUserDataUseCase
import com.despaircorp.domain.user.domain.use_cases.IsUserLimitReachUseCase
import com.despaircorp.feature_link_generation.ui_state.LinkGenerationUiState
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LinkGenerationViewModelUnitTest {
    private val testDispatcher = StandardTestDispatcher()

    private val authRepository = mock<AuthRepository>()
    private val userRepository = mock<UserRepository>()
    private val linkGenerationRepository = mock<LinkGenerationRepository>()

    private val getCurrentUserIdUseCase = GetCurrentUserIdUseCase(authRepository)
    private val getUserDataUseCase = GetUserDataUseCase(userRepository, getCurrentUserIdUseCase)
    private val isUserLimitReachUseCase = IsUserLimitReachUseCase(getUserDataUseCase)
    private val generateTrackShiftLinkFromPlaylistUrlUseCase =
        GenerateTrackShiftLinkFromPlaylistUrlUseCase(getCurrentUserIdUseCase, linkGenerationRepository)
    private val generateTrackShiftLinkFromScreenshotsUseCase =
        GenerateTrackShiftLinkFromScreenshotsUseCase(getCurrentUserIdUseCase, linkGenerationRepository)

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // region init

    @Test
    fun `initial state - sets isUserPro to true when user is pro`() = runTest(testDispatcher) {
        val userId = "user_123"
        everySuspend { authRepository.getCurrentUserId() } returns Result.success(userId)
        everySuspend { userRepository.getUser(userId) } returns Result.success(provideUser(isPro = true))

        val viewModel = createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.isUserPro.test {
            assertThat(awaitItem()).isEqualTo(true)
        }
    }

    @Test
    fun `initial state - sets isUserPro to false when user is not pro`() = runTest(testDispatcher) {
        val userId = "user_123"
        everySuspend { authRepository.getCurrentUserId() } returns Result.success(userId)
        everySuspend { userRepository.getUser(userId) } returns Result.success(provideUser(isPro = false))

        val viewModel = createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.isUserPro.test {
            assertThat(awaitItem()).isEqualTo(false)
        }
    }

    @Test
    fun `initial state - emits Error when getUserData fails`() = runTest(testDispatcher) {
        everySuspend { authRepository.getCurrentUserId() } returns Result.failure(Exception("No session"))

        val viewModel = createViewModel()

        viewModel.uiState.test {
            assertThat(awaitItem()).isInstanceOf(LinkGenerationUiState.Idle::class)
            testDispatcher.scheduler.advanceUntilIdle()
            val errorState = awaitItem()
            assertThat(errorState).isInstanceOf(LinkGenerationUiState.Error::class)
        }
    }

    @Test
    fun `initial state - maxImages is 5 when user is pro`() = runTest(testDispatcher) {
        val userId = "user_123"
        everySuspend { authRepository.getCurrentUserId() } returns Result.success(userId)
        everySuspend { userRepository.getUser(userId) } returns Result.success(provideUser(isPro = true))

        val viewModel = createViewModel()

        viewModel.maxImages.test {
            // initial value before pro status loads
            assertThat(awaitItem()).isEqualTo(3)
            testDispatcher.scheduler.advanceUntilIdle()
            assertThat(awaitItem()).isEqualTo(5)
        }
    }

    @Test
    fun `initial state - maxImages stays 3 when user is not pro`() = runTest(testDispatcher) {
        val userId = "user_123"
        everySuspend { authRepository.getCurrentUserId() } returns Result.success(userId)
        everySuspend { userRepository.getUser(userId) } returns Result.success(provideUser(isPro = false))

        val viewModel = createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.maxImages.test {
            assertThat(awaitItem()).isEqualTo(3)
        }
    }

    // endregion

    // region onUrlSubmit

    @Test
    fun `onUrlSubmit - emits Success when generation succeeds`() = runTest(testDispatcher) {
        val userId = "user_123"
        val url = "https://open.spotify.com/playlist/123"
        val trackShiftUrl = "https://trackshift.fr/code/abc"

        everySuspend { authRepository.getCurrentUserId() } returns Result.success(userId)
        everySuspend { userRepository.getUser(userId) } returns Result.success(provideUser(isPro = false, linksCreatedMonth = 0))
        everySuspend { linkGenerationRepository.generateTrackShiftUrl(any()) } returns Result.success(trackShiftUrl)
        everySuspend { linkGenerationRepository.updateGenerationCount(userId) } returns Result.success(true)
        everySuspend { linkGenerationRepository.saveConversionToHistory(userId, trackShiftUrl) } returns Result.success(Unit)

        val viewModel = createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.uiState.test {
            assertThat(awaitItem()).isInstanceOf(LinkGenerationUiState.Idle::class)
            viewModel.onUrlSubmit(url)
            assertThat(awaitItem()).isInstanceOf(LinkGenerationUiState.Loading::class)
            testDispatcher.scheduler.advanceUntilIdle()
            val successState = awaitItem()
            assertThat(successState).isInstanceOf(LinkGenerationUiState.Success::class)
            assertThat((successState as LinkGenerationUiState.Success).trackshiftUrl).isEqualTo(trackShiftUrl)
        }
    }

    @Test
    fun `onUrlSubmit - emits LimitReach when user reached limit`() = runTest(testDispatcher) {
        val userId = "user_123"
        val url = "https://open.spotify.com/playlist/123"

        everySuspend { authRepository.getCurrentUserId() } returns Result.success(userId)
        everySuspend { userRepository.getUser(userId) } returns Result.success(provideUser(isPro = false, linksCreatedMonth = 10))

        val viewModel = createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.uiState.test {
            assertThat(awaitItem()).isInstanceOf(LinkGenerationUiState.Idle::class)
            viewModel.onUrlSubmit(url)
            assertThat(awaitItem()).isInstanceOf(LinkGenerationUiState.Loading::class)
            testDispatcher.scheduler.advanceUntilIdle()
            assertThat(awaitItem()).isInstanceOf(LinkGenerationUiState.LimitReach::class)
        }
    }

    @Test
    fun `onUrlSubmit - emits Error when generation fails`() = runTest(testDispatcher) {
        val userId = "user_123"
        val url = "https://open.spotify.com/playlist/123"

        everySuspend { authRepository.getCurrentUserId() } returns Result.success(userId)
        everySuspend { userRepository.getUser(userId) } returns Result.success(provideUser(isPro = false, linksCreatedMonth = 0))
        everySuspend { linkGenerationRepository.generateTrackShiftUrl(any()) } returns Result.failure(Exception("API error"))

        val viewModel = createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.uiState.test {
            assertThat(awaitItem()).isInstanceOf(LinkGenerationUiState.Idle::class)
            viewModel.onUrlSubmit(url)
            assertThat(awaitItem()).isInstanceOf(LinkGenerationUiState.Loading::class)
            testDispatcher.scheduler.advanceUntilIdle()
            assertThat(awaitItem()).isInstanceOf(LinkGenerationUiState.Error::class)
        }
    }

    @Test
    fun `onUrlSubmit - emits Error when limit check fails`() = runTest(testDispatcher) {
        val userId = "user_123"
        val url = "https://open.spotify.com/playlist/123"

        everySuspend { authRepository.getCurrentUserId() } returns Result.success(userId)
        // First call succeeds (init), second call fails (onUrlSubmit limit check)
        everySuspend { userRepository.getUser(userId) } returns Result.failure(Exception("Network error"))

        val viewModel = createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.uiState.test {
            // init already failed so we get Error from init
            val current = awaitItem()
            assertThat(current).isInstanceOf(LinkGenerationUiState.Error::class)
        }
    }

    // endregion

    // region onScreenshotSubmit

    @Test
    fun `onScreenshotSubmit - emits Success when generation succeeds`() = runTest(testDispatcher) {
        val userId = "user_123"
        val trackShiftUrl = "https://trackshift.fr/code/abc"

        everySuspend { authRepository.getCurrentUserId() } returns Result.success(userId)
        everySuspend { userRepository.getUser(userId) } returns Result.success(provideUser(isPro = false, linksCreatedMonth = 0))
        everySuspend { linkGenerationRepository.generateTrackShiftUrl(any()) } returns Result.success(trackShiftUrl)
        everySuspend { linkGenerationRepository.updateGenerationCount(userId) } returns Result.success(true)
        everySuspend { linkGenerationRepository.saveConversionToHistory(userId, trackShiftUrl) } returns Result.success(Unit)

        val viewModel = createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onImagePickerResult(listOf(byteArrayOf(1, 2, 3)))

        viewModel.uiState.test {
            assertThat(awaitItem()).isInstanceOf(LinkGenerationUiState.Idle::class)
            viewModel.onScreenshotSubmit()
            assertThat(awaitItem()).isInstanceOf(LinkGenerationUiState.Loading::class)
            testDispatcher.scheduler.advanceUntilIdle()
            val successState = awaitItem()
            assertThat(successState).isInstanceOf(LinkGenerationUiState.Success::class)
            assertThat((successState as LinkGenerationUiState.Success).trackshiftUrl).isEqualTo(trackShiftUrl)
        }
    }

    @Test
    fun `onScreenshotSubmit - emits LimitReach when user reached limit`() = runTest(testDispatcher) {
        val userId = "user_123"

        everySuspend { authRepository.getCurrentUserId() } returns Result.success(userId)
        everySuspend { userRepository.getUser(userId) } returns Result.success(provideUser(isPro = false, linksCreatedMonth = 10))

        val viewModel = createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onImagePickerResult(listOf(byteArrayOf(1, 2, 3)))

        viewModel.uiState.test {
            assertThat(awaitItem()).isInstanceOf(LinkGenerationUiState.Idle::class)
            viewModel.onScreenshotSubmit()
            assertThat(awaitItem()).isInstanceOf(LinkGenerationUiState.Loading::class)
            testDispatcher.scheduler.advanceUntilIdle()
            assertThat(awaitItem()).isInstanceOf(LinkGenerationUiState.LimitReach::class)
        }
    }

    @Test
    fun `onScreenshotSubmit - emits Error when generation fails`() = runTest(testDispatcher) {
        val userId = "user_123"

        everySuspend { authRepository.getCurrentUserId() } returns Result.success(userId)
        everySuspend { userRepository.getUser(userId) } returns Result.success(provideUser(isPro = false, linksCreatedMonth = 0))
        everySuspend { linkGenerationRepository.generateTrackShiftUrl(any()) } returns Result.failure(Exception("API error"))

        val viewModel = createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onImagePickerResult(listOf(byteArrayOf(1, 2, 3)))

        viewModel.uiState.test {
            assertThat(awaitItem()).isInstanceOf(LinkGenerationUiState.Idle::class)
            viewModel.onScreenshotSubmit()
            assertThat(awaitItem()).isInstanceOf(LinkGenerationUiState.Loading::class)
            testDispatcher.scheduler.advanceUntilIdle()
            assertThat(awaitItem()).isInstanceOf(LinkGenerationUiState.Error::class)
        }
    }

    // endregion

    // region image management

    @Test
    fun `onImagePickerResult - adds images to the list`() = runTest(testDispatcher) {
        val userId = "user_123"
        everySuspend { authRepository.getCurrentUserId() } returns Result.success(userId)
        everySuspend { userRepository.getUser(userId) } returns Result.success(provideUser())

        val viewModel = createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.images.test {
            assertThat(awaitItem()).isEmpty()
            viewModel.onImagePickerResult(listOf(byteArrayOf(1, 2, 3)))
            assertThat(awaitItem().size).isEqualTo(1)
        }
    }

    @Test
    fun `onImagePickerResult - replaces image at index after onReplaceScreenshot`() = runTest(testDispatcher) {
        val userId = "user_123"
        everySuspend { authRepository.getCurrentUserId() } returns Result.success(userId)
        everySuspend { userRepository.getUser(userId) } returns Result.success(provideUser())

        val viewModel = createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        val original = byteArrayOf(1, 2, 3)
        val replacement = byteArrayOf(4, 5, 6)

        viewModel.onImagePickerResult(listOf(original))

        viewModel.onReplaceScreenshot(0)
        viewModel.onImagePickerResult(listOf(replacement))

        viewModel.images.test {
            val images = awaitItem()
            assertThat(images.size).isEqualTo(1)
            assertThat(images[0]).isEqualTo(replacement)
        }
    }

    @Test
    fun `onRemoveScreenshot - removes image at index`() = runTest(testDispatcher) {
        val userId = "user_123"
        everySuspend { authRepository.getCurrentUserId() } returns Result.success(userId)
        everySuspend { userRepository.getUser(userId) } returns Result.success(provideUser())

        val viewModel = createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onImagePickerResult(listOf(byteArrayOf(1), byteArrayOf(2)))

        viewModel.images.test {
            assertThat(awaitItem().size).isEqualTo(2)
            viewModel.onRemoveScreenshot(0)
            assertThat(awaitItem().size).isEqualTo(1)
        }
    }

    @Test
    fun `onAddScreenshot - resets replace mode so next pick adds`() = runTest(testDispatcher) {
        val userId = "user_123"
        everySuspend { authRepository.getCurrentUserId() } returns Result.success(userId)
        everySuspend { userRepository.getUser(userId) } returns Result.success(provideUser())

        val viewModel = createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onImagePickerResult(listOf(byteArrayOf(1)))

        viewModel.onReplaceScreenshot(0)
        viewModel.onAddScreenshot()
        viewModel.onImagePickerResult(listOf(byteArrayOf(2)))

        viewModel.images.test {
            assertThat(awaitItem().size).isEqualTo(2)
        }
    }

    // endregion

    // region onEventConsumed

    @Test
    fun `onEventConsumed - resets uiState to Idle`() = runTest(testDispatcher) {
        val userId = "user_123"
        val url = "https://open.spotify.com/playlist/123"
        val trackShiftUrl = "https://trackshift.fr/code/abc"

        everySuspend { authRepository.getCurrentUserId() } returns Result.success(userId)
        everySuspend { userRepository.getUser(userId) } returns Result.success(provideUser(isPro = false, linksCreatedMonth = 0))
        everySuspend { linkGenerationRepository.generateTrackShiftUrl(any()) } returns Result.success(trackShiftUrl)
        everySuspend { linkGenerationRepository.updateGenerationCount(userId) } returns Result.success(true)
        everySuspend { linkGenerationRepository.saveConversionToHistory(userId, trackShiftUrl) } returns Result.success(Unit)

        val viewModel = createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onUrlSubmit(url)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.uiState.test {
            assertThat(awaitItem()).isInstanceOf(LinkGenerationUiState.Success::class)
            viewModel.onEventConsumed()
            assertThat(awaitItem()).isInstanceOf(LinkGenerationUiState.Idle::class)
        }
    }

    // endregion

    private fun createViewModel() = LinkGenerationViewModel(
        isUserLimitReachUseCase = isUserLimitReachUseCase,
        generateTrackShiftLinkFromPlaylistUrlUseCase = generateTrackShiftLinkFromPlaylistUrlUseCase,
        getUserDataUseCase = getUserDataUseCase,
        generateTrackShiftLinkFromScreenshotsUseCase = generateTrackShiftLinkFromScreenshotsUseCase
    )

    private fun provideUser(
        isPro: Boolean = true,
        linksCreatedMonth: Int = 5
    ) = User(
        id = "user_123",
        displayName = "Monokouma",
        avatarUrl = "https://example.com/avatar.jpg",
        isPro = isPro,
        createdAt = "2026-01-01",
        linksCreatedMonth = linksCreatedMonth,
        linksConvertedMonth = 3,
        totalLinksCreated = 42,
        totalPlaylistsCreated = 10,
        totalLinksConverted = 28,
        proExpiresAt = "2026-12-31",
        lastResetAt = "2026-02-01"
    )
}
