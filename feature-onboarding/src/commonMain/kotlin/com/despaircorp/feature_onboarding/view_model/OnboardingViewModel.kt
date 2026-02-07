package com.despaircorp.feature_onboarding.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.despaircorp.domain.local_storage.domain.use_cases.ManageOnboardStorageUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class OnboardingViewModel(
    private val manageOnboardStorageUseCase: ManageOnboardStorageUseCase
): ViewModel() {

    private val _isOnboardCompleted = MutableStateFlow(false)
    val isOnboardCompleted = _isOnboardCompleted.asStateFlow()


    fun setOnboardCompleted() {
        viewModelScope.launch {
            manageOnboardStorageUseCase.invokeSet(true)
            _isOnboardCompleted.value = true
        }
    }
}