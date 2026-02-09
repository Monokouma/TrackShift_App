package com.despaircorp.feature_home.view_model

import androidx.lifecycle.ViewModel
import com.despaircorp.feature_home.model.HomeTab
import com.despaircorp.feature_home.ui_state.HomeUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class HomeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    fun onTabSelected(tab: HomeTab) {
        _uiState.update { it.copy(currentTab = tab) }
    }
}
