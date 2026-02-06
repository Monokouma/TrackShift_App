package com.despaircorp.trackshift

interface TrackShiftAppUiState {
    data object AuthRedirection: TrackShiftAppUiState
    data object HomeRedirection: TrackShiftAppUiState
    data object OnboardRedirection: TrackShiftAppUiState
    data object SplashRedirection: TrackShiftAppUiState
}