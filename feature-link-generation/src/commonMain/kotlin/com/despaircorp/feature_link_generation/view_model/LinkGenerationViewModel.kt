package com.despaircorp.feature_link_generation.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.despaircorp.domain.link_generation.domain.use_cases.GenerateTrackShiftLinkFromPlaylistUrlUseCase
import com.despaircorp.domain.link_generation.domain.use_cases.GenerateTrackShiftLinkFromScreenshotsUseCase
import com.despaircorp.domain.user.domain.model.UserAction
import com.despaircorp.domain.user.domain.use_cases.GetUserDataUseCase
import com.despaircorp.domain.user.domain.use_cases.IsUserLimitReachUseCase
import com.despaircorp.feature_link_generation.ui_state.LinkGenerationUiState
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LinkGenerationViewModel(
    private val isUserLimitReachUseCase: IsUserLimitReachUseCase,
    private val generateTrackShiftLinkFromPlaylistUrlUseCase: GenerateTrackShiftLinkFromPlaylistUrlUseCase,
    private val getUserDataUseCase: GetUserDataUseCase,
    private val generateTrackShiftLinkFromScreenshotsUseCase: GenerateTrackShiftLinkFromScreenshotsUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow<LinkGenerationUiState>(LinkGenerationUiState.Idle)
    val uiState = _uiState.asStateFlow()

    private val _isUserPro = MutableStateFlow(false)
    val isUserPro = _isUserPro.asStateFlow()

    val maxImages = _isUserPro.map { isPro -> if (isPro) 5 else 3 }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 3)

    private val _images = MutableStateFlow(persistentListOf<ByteArray>())
    val images = _images.asStateFlow()

    private var replaceIndex: Int? = null

    init {
        viewModelScope.launch {
            getUserDataUseCase()
                .fold(
                    onSuccess = { user ->
                        _isUserPro.update { user.isPro }
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
    }

    fun onUrlSubmit(url: String) {
        viewModelScope.launch {
            _uiState.update { LinkGenerationUiState.Loading }
            isUserLimitReachUseCase(forAction = UserAction.GENERATE).fold(
                onSuccess = { isLimitReach ->
                    if (isLimitReach) {
                        _uiState.update { LinkGenerationUiState.LimitReach }
                    } else {
                        generateTrackShiftLinkFromPlaylistUrlUseCase(url).fold(
                            onSuccess = { trackShiftUrl ->
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
                    _uiState.update {
                        LinkGenerationUiState.Error(
                            message = error.message ?: "Error"
                        )
                    }
                }
            )
        }
    }

    fun onImagePickerResult(result: List<ByteArray>) {
        val index = replaceIndex
        if (index != null && result.isNotEmpty()) {
            _images.update { it.set(index, result.first()) }
            replaceIndex = null
        } else {
            _images.update { current ->
                (current + result).take(maxImages.value).toPersistentList()
            }
        }
    }

    fun onAddScreenshot() {
        replaceIndex = null
    }

    fun onReplaceScreenshot(index: Int) {
        replaceIndex = index
    }

    fun onRemoveScreenshot(index: Int) {
        _images.update { it.removeAt(index) }
    }

    fun onScreenshotSubmit() {
        viewModelScope.launch {

            _uiState.update { LinkGenerationUiState.Loading }

            isUserLimitReachUseCase(forAction = UserAction.GENERATE)
                .fold(
                    onSuccess = { isLimitReach ->
                        if (isLimitReach) {
                            _uiState.update { LinkGenerationUiState.LimitReach }
                        } else {
                            generateTrackShiftLinkFromScreenshotsUseCase(_images.value)
                                .fold(
                                    onSuccess = { trackShiftUrl ->
                                        _uiState.update {
                                            LinkGenerationUiState.Success(
                                                trackshiftUrl = trackShiftUrl
                                            )
                                        }
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
                        _uiState.update {
                            LinkGenerationUiState.Error(
                                message = error.message ?: "Error"
                            )
                        }
                    }
                )
        }
    }

    fun onEventConsumed() {
        _uiState.update { LinkGenerationUiState.Idle }
    }
}
