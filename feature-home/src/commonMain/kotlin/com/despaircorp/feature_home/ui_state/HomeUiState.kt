package com.despaircorp.feature_home.ui_state

import com.despaircorp.feature_home.model.HomeTab

data class HomeUiState(
    val currentTab: HomeTab = HomeTab.SHIFT
)
