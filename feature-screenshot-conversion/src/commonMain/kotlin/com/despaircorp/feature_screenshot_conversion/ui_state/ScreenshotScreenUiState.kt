package com.despaircorp.feature_screenshot_conversion.ui_state

sealed interface ScreenshotScreenUiState {
    data class Error(val message: String): ScreenshotScreenUiState
    data object LimitReach: ScreenshotScreenUiState
    data class Success(val trackShiftUrl: String): ScreenshotScreenUiState
    data object Idle: ScreenshotScreenUiState
}