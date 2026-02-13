package com.despaircorp.feature_profile.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.despaircorp.domain.user.domain.use_cases.GetUserDataUseCase
import com.despaircorp.domain.user.domain.use_cases.UpdateUserImageUseCase
import com.despaircorp.domain.user.domain.use_cases.UpdateUsernameUseCase
import com.despaircorp.feature_profile.model.toUiModel
import com.despaircorp.feature_profile.ui_state.ProfileUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

class ProfileViewModel(
    private val getUserDataUseCase: GetUserDataUseCase,
    private val updateUsernameUseCase: UpdateUsernameUseCase,
    private val updateUserImageUseCase: UpdateUserImageUseCase
): ViewModel() {

    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    init {
        getUser()
    }

    private fun getUser() {
        viewModelScope.launch {
            getUserDataUseCase().fold(
                onSuccess = { user ->
                    delay((0.5).seconds)
                    _uiState.update {
                        ProfileUiState.Content(
                            uiProfileModel = user.toUiModel()
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update {
                        ProfileUiState.Error(error.message ?: "Error")
                    }
                }
            )
        }
    }


    fun refresh() {
        viewModelScope.launch {
            _isRefreshing.value = true
            _uiState.update { ProfileUiState.Loading }
            getUser()
            _isRefreshing.value = false
        }
    }

    fun onNameEdit(newName: String) {
        viewModelScope.launch {
            updateUsernameUseCase(newName).fold(
                onSuccess = {
                    _uiState.update { ProfileUiState.Loading }
                    getUser()
                },
                onFailure = { error ->
                    _uiState.update {
                        ProfileUiState.Error(error.message ?: "Error")
                    }
                }
            )
        }
    }

    fun updateUserImage(imageBytes: ByteArray) {
        viewModelScope.launch {
            updateUserImageUseCase(imageBytes).fold(
                onSuccess = {
                    _uiState.update { ProfileUiState.Loading }
                    getUser()
                },
                onFailure = { error ->
                    _uiState.update {
                        ProfileUiState.Error(error.message ?: "Error")
                    }
                }
            )
        }
    }

}