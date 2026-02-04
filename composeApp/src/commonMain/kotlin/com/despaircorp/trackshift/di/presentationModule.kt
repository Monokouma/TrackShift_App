package com.despaircorp.trackshift.di

import com.despaircorp.trackshift.view_model.TrackShiftAppViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {

    viewModel { TrackShiftAppViewModel(handleSessionStatusUseCase = get()) }
}