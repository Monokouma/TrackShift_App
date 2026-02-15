package com.despaircorp.trackshift.di

import com.despaircorp.feature_auth.view_model.AuthViewModel
import com.despaircorp.feature_home.view_model.HomeViewModel
import com.despaircorp.feature_onboarding.view_model.OnboardingViewModel
import com.despaircorp.feature_profile.view_model.ProfileViewModel
import com.despaircorp.feature_shift.view_model.ShiftViewModel
import com.despaircorp.trackshift.view_model.TrackShiftAppViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {

    viewModel { TrackShiftAppViewModel(
        handleSessionStatusUseCase = get(),
        manageOnboardStorageUseCase = get()
    ) }
    viewModel {
        AuthViewModel(
            authByProviderUseCase = get(),
            handleOAuthCallbackUseCase = get()
        )
    }

    viewModel {
        OnboardingViewModel(
            manageOnboardStorageUseCase = get()
        )
    }

    viewModel {
        HomeViewModel()
    }

    viewModel {
        ProfileViewModel(
            getUserDataUseCase = get(),
            updateUsernameUseCase = get(),
            updateUserImageUseCase = get()
        )
    }

    viewModel {
        ShiftViewModel()
    }
}