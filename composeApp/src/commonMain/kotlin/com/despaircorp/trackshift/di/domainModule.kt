package com.despaircorp.trackshift.di

import com.despaircorp.domain.auth.domain.use_cases.AuthByProviderUseCase
import com.despaircorp.domain.auth.domain.use_cases.GetCurrentUserIdUseCase
import com.despaircorp.domain.auth.domain.use_cases.HandleOAuthCallbackUseCase
import com.despaircorp.domain.auth.domain.use_cases.HandleSessionStatusUseCase
import com.despaircorp.domain.auth.domain.use_cases.LogoutUserUseCase
import com.despaircorp.domain.link_generation.domain.use_cases.GenerateTrackShiftLinkFromPlaylistUrlUseCase
import com.despaircorp.domain.link_generation.domain.use_cases.GenerateTrackShiftLinkFromScreenshotsUseCase
import com.despaircorp.domain.local_storage.domain.use_cases.ManageOnboardStorageUseCase
import com.despaircorp.domain.user.domain.use_cases.DeleteUserAccountUseCase
import com.despaircorp.domain.user.domain.use_cases.GetUserDataUseCase
import com.despaircorp.domain.user.domain.use_cases.IsUserLimitReachUseCase
import com.despaircorp.domain.user.domain.use_cases.UpdateUserImageUseCase
import com.despaircorp.domain.user.domain.use_cases.UpdateUsernameUseCase
import org.koin.dsl.module

val domainModule = module {
    factory { HandleSessionStatusUseCase(authRepository = get()) }
    factory { AuthByProviderUseCase(authRepository = get()) }
    factory { HandleOAuthCallbackUseCase(authRepository = get()) }
    factory { ManageOnboardStorageUseCase(localStorageRepository = get()) }
    factory { GetCurrentUserIdUseCase(authRepository = get()) }
    factory { GetUserDataUseCase(userRepository = get(), getCurrentUserIdUseCase = get()) }
    factory { UpdateUsernameUseCase(userRepository = get(), getCurrentUserIdUseCase = get()) }
    factory { UpdateUserImageUseCase(userRepository = get(), getCurrentUserIdUseCase = get()) }
    factory { IsUserLimitReachUseCase(getUserDataUseCase = get()) }
    factory {
        GenerateTrackShiftLinkFromPlaylistUrlUseCase(
            getCurrentUserIdUseCase = get(),
            linkGenerationRepository = get()
        )
    }
    factory {
        GenerateTrackShiftLinkFromScreenshotsUseCase(
            getCurrentUserIdUseCase = get(),
            linkGenerationRepository = get()
        )
    }

    factory { LogoutUserUseCase(authRepository = get()) }
    factory {
        DeleteUserAccountUseCase(
            getCurrentUserIdUseCase = get(),
            logoutUserUseCase = get(),
            userRepository = get()
        )
    }
}
