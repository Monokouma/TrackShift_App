package com.despaircorp.trackshift.ui_state

interface TrackShiftAppUiState {
    data object AuthRedirection: TrackShiftAppUiState
    data object HomeRedirection: TrackShiftAppUiState
    data object OnboardRedirection: TrackShiftAppUiState
    data object SplashRedirection: TrackShiftAppUiState
}