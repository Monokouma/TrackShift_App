package com.despaircorp.feature_profile.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.despaircorp.domain.user.domain.use_cases.GetUserDataUseCase
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val getUserDataUseCase: GetUserDataUseCase
): ViewModel() {

    fun getUser() {
        viewModelScope.launch {
            println(getUserDataUseCase.invoke().getOrNull())
        }
    }
}