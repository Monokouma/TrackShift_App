package com.despaircorp.feature_screenshot_conversion.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.despaircorp.domain.user.domain.model.UserAction
import com.despaircorp.domain.user.domain.use_cases.IsUserLimitReachUseCase
import com.despaircorp.feature_screenshot_conversion.ui_state.ScreenshotScreenUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ScreenShotViewModel(
    private val isUserLimitReachUseCase: IsUserLimitReachUseCase,

): ViewModel() {

    private val _uiState = MutableStateFlow<ScreenshotScreenUiState>(ScreenshotScreenUiState.Idle)
    val uiState = _uiState.asStateFlow()

    fun onUrlSubmit(url: String) {
        viewModelScope.launch {
            isUserLimitReachUseCase(forAction = UserAction.GENERATE).fold(
                onSuccess = { isLimitReach ->
                    if (isLimitReach) {
                        _uiState.update {
                            ScreenshotScreenUiState.LimitReach
                        }
                    } else {

                    }
                },
                onFailure = { error ->
                    _uiState.update {
                        ScreenshotScreenUiState.Error(error.message ?: "Erreur")
                    }
                }
            )


        }
    }
}