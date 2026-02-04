package com.despaircorp.feature_auth.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.despaircorp.domain.auth.domain.use_cases.AuthByProviderUseCase
import com.despaircorp.feature_auth.model.AuthProvider
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authByProviderUseCase: AuthByProviderUseCase
): ViewModel() {

    fun onProviderPick(provider: AuthProvider) {
        viewModelScope.launch {

            authByProviderUseCase(provider.name).fold(
                onSuccess = {

                },
                onFailure = {

                }
            )

        }
    }

    fun onMailAut(mail: String, pass: String, provider: AuthProvider) {
        viewModelScope.launch {

        }
    }
}