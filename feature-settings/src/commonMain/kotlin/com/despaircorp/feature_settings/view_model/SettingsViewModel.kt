package com.despaircorp.feature_settings.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.despaircorp.domain.auth.domain.use_cases.LogoutUserUseCase
import com.despaircorp.domain.user.domain.use_cases.DeleteUserAccountUseCase
import com.despaircorp.domain.user.domain.use_cases.GetUserDataUseCase
import com.despaircorp.feature_settings.ui_state.SettingsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val getUserDataUseCase: GetUserDataUseCase,
    private val logoutUserUseCase: LogoutUserUseCase,
    private val deleteUserAccountUseCase: DeleteUserAccountUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<SettingsUiState>(SettingsUiState.Idle)
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            getUserDataUseCase.invoke().fold(
                onSuccess = { user ->
                    _uiState.update { SettingsUiState.Nominal(isUserPro = user.isPro) }
                },
                onFailure = { error ->
                    _uiState.update { SettingsUiState.Error(message = error.message.toString()) }
                }
            )
        }
    }

    fun onEventConsumed() {
        _uiState.update { SettingsUiState.Idle }
    }

    fun onLogout() {
        viewModelScope.launch {
            _uiState.update { SettingsUiState.Loading }
            logoutUserUseCase().fold(
                onSuccess = {
                    _uiState.update { SettingsUiState.Success }
                },
                onFailure = { error ->
                    _uiState.update { SettingsUiState.Error(message = error.message.toString()) }
                }
            )
        }
    }

    fun onDeleteAccount() {
        viewModelScope.launch {
            _uiState.update { SettingsUiState.Loading }
            deleteUserAccountUseCase.invoke().fold(
                onSuccess = {
                    _uiState.update { SettingsUiState.Success }
                },
                onFailure = { error ->
                    _uiState.update { SettingsUiState.Error(message = error.message.toString()) }
                }
            )
        }
    }
}