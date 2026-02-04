package com.despaircorp.trackshift.di

import com.despaircorp.domain.auth.data.AuthRepositoryImpl
import com.despaircorp.domain.auth.domain.repo.AuthRepository
import org.koin.dsl.module

val dataModule = module {
    single<AuthRepository> { AuthRepositoryImpl(supabaseAuthService = get()) }

}