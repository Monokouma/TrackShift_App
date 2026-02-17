package com.despaircorp.feature_link_generation.ui_state

sealed interface LinkGenerationUiState {
    data class Error(val message: String) : LinkGenerationUiState
    data class Success(val trackshiftUrl: String) : LinkGenerationUiState
    data object LimitReach : LinkGenerationUiState
    data object Idle : LinkGenerationUiState

    data object Loading : LinkGenerationUiState
}