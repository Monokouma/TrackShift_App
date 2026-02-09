package com.despaircorp.feature_home.view_model

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import com.despaircorp.feature_home.model.HomeTab
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
class HomeViewModelUnitTest {
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
    fun `initial state - currentTab is SHIFT`() = runTest(testDispatcher) {
        val viewModel = HomeViewModel()

        viewModel.uiState.test {
            assertThat(awaitItem().currentTab).isEqualTo(HomeTab.SHIFT)
        }
    }

    @Test
    fun `nominal case - onTabSelected PROFILE updates currentTab`() = runTest(testDispatcher) {
        val viewModel = HomeViewModel()

        viewModel.uiState.test {
            assertThat(awaitItem().currentTab).isEqualTo(HomeTab.SHIFT)

            viewModel.onTabSelected(HomeTab.PROFILE)
            assertThat(awaitItem().currentTab).isEqualTo(HomeTab.PROFILE)
        }
    }

    @Test
    fun `nominal case - onTabSelected HISTORY updates currentTab`() = runTest(testDispatcher) {
        val viewModel = HomeViewModel()

        viewModel.uiState.test {
            assertThat(awaitItem().currentTab).isEqualTo(HomeTab.SHIFT)

            viewModel.onTabSelected(HomeTab.HISTORY)
            assertThat(awaitItem().currentTab).isEqualTo(HomeTab.HISTORY)
        }
    }

    @Test
    fun `nominal case - multiple tab switches work correctly`() = runTest(testDispatcher) {
        val viewModel = HomeViewModel()

        viewModel.uiState.test {
            assertThat(awaitItem().currentTab).isEqualTo(HomeTab.SHIFT)

            viewModel.onTabSelected(HomeTab.PROFILE)
            assertThat(awaitItem().currentTab).isEqualTo(HomeTab.PROFILE)

            viewModel.onTabSelected(HomeTab.HISTORY)
            assertThat(awaitItem().currentTab).isEqualTo(HomeTab.HISTORY)

            viewModel.onTabSelected(HomeTab.SHIFT)
            assertThat(awaitItem().currentTab).isEqualTo(HomeTab.SHIFT)
        }
    }
}
