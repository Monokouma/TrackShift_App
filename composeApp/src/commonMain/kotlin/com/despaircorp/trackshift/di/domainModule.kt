package com.despaircorp.trackshift.di

import com.despaircorp.domain.auth.domain.use_cases.AuthByProviderUseCase
import com.despaircorp.domain.auth.domain.use_cases.GetCurrentUserIdUseCase
import com.despaircorp.domain.auth.domain.use_cases.HandleOAuthCallbackUseCase
import com.despaircorp.domain.auth.domain.use_cases.HandleSessionStatusUseCase
import com.despaircorp.domain.local_storage.domain.use_cases.ManageOnboardStorageUseCase
import com.despaircorp.domain.user.domain.use_cases.GetUserDataUseCase
import org.koin.dsl.module

val domainModule = module {
    factory { HandleSessionStatusUseCase(authRepository = get()) }
    factory { AuthByProviderUseCase(authRepository = get()) }
    factory { HandleOAuthCallbackUseCase(authRepository = get()) }
    factory { ManageOnboardStorageUseCase(localStorageRepository = get()) }
    factory { GetCurrentUserIdUseCase(authRepository = get()) }
    factory { GetUserDataUseCase(userRepository = get(), getCurrentUserIdUseCase = get()) }
}
