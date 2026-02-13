package com.despaircorp.feature_profile.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.despaircorp.domain.user.domain.use_cases.GetUserDataUseCase
import com.despaircorp.feature_profile.model.toUiModel
import com.despaircorp.feature_profile.ui_state.ProfileUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val getUserDataUseCase: GetUserDataUseCase
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
                    _uiState.update {
                        ProfileUiState.Content(
                            uiProfileModel = user.toUiModel()
                        )
                    }
                },
                onFailure = {
                    _uiState.update {
                        ProfileUiState.Error
                    }
                }
            )
        }
    }


    fun refresh() {
        viewModelScope.launch {
            _isRefreshing.value = true
            getUser()
            _isRefreshing.value = false
        }
    }
}