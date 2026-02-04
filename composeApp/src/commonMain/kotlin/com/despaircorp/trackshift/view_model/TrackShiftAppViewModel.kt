package com.despaircorp.trackshift.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.despaircorp.domain.auth.domain.use_cases.HandleSessionStatusUseCase
import io.github.jan.supabase.auth.status.SessionStatus
import io.ktor.client.utils.EmptyContent.status
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

class TrackShiftAppViewModel(
    private val handleSessionStatusUseCase: HandleSessionStatusUseCase
): ViewModel() {

    private val _isAuthenticatedStateFlow = MutableStateFlow(false)
    val isAuthenticatedStateFlow = _isAuthenticatedStateFlow.asStateFlow()

    init {
        viewModelScope.launch {
            handleSessionStatusUseCase().collect { status ->
                when (status) {
                    is SessionStatus.Authenticated -> {
                        _isAuthenticatedStateFlow.value = true
                    }

                    is SessionStatus.NotAuthenticated -> {
                        _isAuthenticatedStateFlow.value = false
                    }

                    is SessionStatus.Initializing -> {

                    }

                    is SessionStatus.RefreshFailure -> {

                    }
                }
            }
        }
    }
}