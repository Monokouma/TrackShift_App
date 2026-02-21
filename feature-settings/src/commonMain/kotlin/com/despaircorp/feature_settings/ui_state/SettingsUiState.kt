package com.despaircorp.feature_settings.ui_state

sealed interface SettingsUiState {
    data object Idle : SettingsUiState
    data class Nominal(val isUserPro: Boolean) : SettingsUiState
    data object Loading : SettingsUiState
    data class Error(val message: String) : SettingsUiState
    data object Success : SettingsUiState
}