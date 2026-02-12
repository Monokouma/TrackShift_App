package com.despaircorp.trackshift.di

import com.despaircorp.domain.auth.data.AuthRepositoryImpl
import com.despaircorp.domain.auth.domain.repo.AuthRepository
import com.despaircorp.domain.local_storage.data.LocalStorageRepositoryImpl
import com.despaircorp.domain.local_storage.domain.repo.LocalStorageRepository
import com.despaircorp.domain.user.data.UserRepositoryImpl
import com.despaircorp.domain.user.domain.repo.UserRepository
import org.koin.dsl.module

val dataModule = module {
    single<AuthRepository> { AuthRepositoryImpl(supabaseAuthService = get()) }
    single<LocalStorageRepository> { LocalStorageRepositoryImpl(storageService = get()) }
    single<UserRepository> { UserRepositoryImpl(trackShiftApiService = get()) }
}