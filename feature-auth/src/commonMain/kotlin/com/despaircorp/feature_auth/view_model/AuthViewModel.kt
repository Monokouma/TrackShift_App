package com.despaircorp.feature_auth.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.despaircorp.domain.auth.domain.use_cases.AuthByProviderUseCase
import com.despaircorp.domain.auth.domain.use_cases.HandleOAuthCallbackUseCase
import com.despaircorp.feature_auth.model.AuthProvider
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authByProviderUseCase: AuthByProviderUseCase,
    private val handleOAuthCallbackUseCase: HandleOAuthCallbackUseCase
) : ViewModel() {

    private val _oAuthUrlToLaunch = MutableSharedFlow<String>()
    val oAuthUrlToLaunch: SharedFlow<String> = _oAuthUrlToLaunch.asSharedFlow()

    private val _authError = MutableStateFlow(false)
    val authError = _authError.asStateFlow()

    private val _authLoading = MutableStateFlow(false)
    val authLoading = _authLoading.asStateFlow()



    fun onProviderPick(provider: AuthProvider) {
        viewModelScope.launch {
            _authLoading.value = true
            authByProviderUseCase(provider.name).fold(
                onSuccess = { url ->
                    _oAuthUrlToLaunch.emit(url)
                    _authLoading.value = false
                },
                onFailure = {
                    _authError.value = true
                    _authLoading.value = false
                }
            )
        }
    }

    fun onOAuthCallback(callbackUrl: String) {
        viewModelScope.launch {
            handleOAuthCallbackUseCase(callbackUrl).fold(
                onSuccess = {
                    _authLoading.value = false
                },
                onFailure = {
                    _authLoading.value = false
                    _authError.value = true
                }
            )
        }
    }

}
