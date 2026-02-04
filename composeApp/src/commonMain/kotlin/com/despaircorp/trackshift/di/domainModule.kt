package com.despaircorp.trackshift.di

import com.despaircorp.domain.auth.domain.use_cases.HandleSessionStatusUseCase
import org.koin.dsl.module

val domainModule = module {
    factory { HandleSessionStatusUseCase(authRepository = get()) }
}
