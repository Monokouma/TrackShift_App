package com.despaircorp.trackshift.di

import com.despaircorp.services.supabase.service.SupabaseAuthService
import org.koin.dsl.module

val servicesModule = module {
    single { SupabaseAuthService(get()) }

}