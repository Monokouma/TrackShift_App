package com.despaircorp.feature_link_generation.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.despaircorp.domain.link_generation.domain.use_cases.GenerateTrackShiftLinkFromPlaylistUrlUseCase
import com.despaircorp.domain.user.domain.model.UserAction
import com.despaircorp.domain.user.domain.use_cases.IsUserLimitReachUseCase
import com.despaircorp.feature_link_generation.ui_state.LinkGenerationUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LinkGenerationViewModel(
    private val isUserLimitReachUseCase: IsUserLimitReachUseCase,
    private val generateTrackShiftLinkFromPlaylistUrlUseCase: GenerateTrackShiftLinkFromPlaylistUrlUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow<LinkGenerationUiState>(LinkGenerationUiState.Idle)
    val uiState = _uiState.asStateFlow()

    fun onUrlSubmit(url: String) {
        viewModelScope.launch {
            _uiState.update { LinkGenerationUiState.Loading }
            isUserLimitReachUseCase(forAction = UserAction.GENERATE).fold(
                onSuccess = { isLimitReach ->
                    println("isLimit : $isLimitReach")
                    if (isLimitReach) {
                        _uiState.update { LinkGenerationUiState.LimitReach }
                    } else {
                        generateTrackShiftLinkFromPlaylistUrlUseCase(url).fold(
                            onSuccess = { trackShiftUrl ->
                                println(trackShiftUrl)
                                _uiState.update { LinkGenerationUiState.Success(trackshiftUrl = trackShiftUrl) }
                            },
                            onFailure = { error ->
                                _uiState.update {
                                    LinkGenerationUiState.Error(
                                        message = error.message ?: "Error"
                                    )
                                }
                            }
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update { LinkGenerationUiState.Error(message = error.message ?: "Error") }
                }
            )
        }
    }

    fun onEventConsumed() {
        _uiState.update { LinkGenerationUiState.Idle }
    }
}