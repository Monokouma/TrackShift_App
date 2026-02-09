package com.despaircorp.trackshift.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.despaircorp.domain.auth.domain.use_cases.HandleSessionStatusUseCase
import com.despaircorp.domain.local_storage.domain.use_cases.ManageOnboardStorageUseCase
import com.despaircorp.trackshift.ui_state.TrackShiftAppUiState
import io.github.jan.supabase.auth.status.SessionStatus
import io.ktor.client.utils.EmptyContent.status
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

class TrackShiftAppViewModel(
    private val handleSessionStatusUseCase: HandleSessionStatusUseCase,
    private val manageOnboardStorageUseCase: ManageOnboardStorageUseCase
): ViewModel() {

    val uiState = handleSessionStatusUseCase().map { status ->
        when (status) {
            is SessionStatus.Initializing -> TrackShiftAppUiState.SplashRedirection
            is SessionStatus.Authenticated if manageOnboardStorageUseCase.invokeGet() -> TrackShiftAppUiState.HomeRedirection
            is SessionStatus.Authenticated if !manageOnboardStorageUseCase.invokeGet() -> TrackShiftAppUiState.OnboardRedirection
            is SessionStatus.NotAuthenticated -> TrackShiftAppUiState.AuthRedirection
            is SessionStatus.RefreshFailure -> TrackShiftAppUiState.AuthRedirection
            else -> TrackShiftAppUiState.SplashRedirection
        }

    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = TrackShiftAppUiState.SplashRedirection
    )

}