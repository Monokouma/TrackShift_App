package com.despaircorp.feature_profile.ui_state

import com.despaircorp.feature_profile.model.UiProfileModel

interface ProfileUiState {
    data object Loading: ProfileUiState

    data class Content(
        val uiProfileModel: UiProfileModel
    ): ProfileUiState

    data object Error: ProfileUiState
}