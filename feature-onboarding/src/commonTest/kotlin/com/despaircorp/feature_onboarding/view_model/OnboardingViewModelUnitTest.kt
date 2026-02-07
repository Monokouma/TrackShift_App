package com.despaircorp.feature_onboarding.view_model

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import com.despaircorp.domain.local_storage.domain.use_cases.ManageOnboardStorageUseCase
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.mock
import dev.mokkery.verify
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
class OnboardingViewModelUnitTest {
    private val testDispatcher = StandardTestDispatcher()

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state - isOnboardCompleted is false`() = runTest(testDispatcher) {
        val manageOnboardStorageUseCase = mock<ManageOnboardStorageUseCase>()
        val viewModel = OnboardingViewModel(manageOnboardStorageUseCase)

        viewModel.isOnboardCompleted.test {
            assertThat(awaitItem()).isFalse()
        }
    }

    @Test
    fun `nominal case - setOnboardCompleted sets storage and updates state`() = runTest(testDispatcher) {
        val manageOnboardStorageUseCase = mock<ManageOnboardStorageUseCase>()
        val viewModel = OnboardingViewModel(manageOnboardStorageUseCase)

        every {
            manageOnboardStorageUseCase.invokeSet(true)
        } returns Unit

        viewModel.isOnboardCompleted.test {
            assertThat(awaitItem()).isFalse()

            viewModel.setOnboardCompleted()
            testDispatcher.scheduler.advanceUntilIdle()

            assertThat(awaitItem()).isTrue()
        }

        verify {
            manageOnboardStorageUseCase.invokeSet(true)
        }
    }
}
