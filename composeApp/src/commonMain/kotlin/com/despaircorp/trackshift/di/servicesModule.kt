package com.despaircorp.trackshift.di

import com.despaircorp.services.supabase.service.SupabaseAuthService
import com.despaircorp.services.supabase.service.SupabaseAuthServiceImpl
import com.despaircorp.services.trackshift_api.service.TrackShiftApiService
import com.despaircorp.services.trackshift_api.service.TrackShiftApiServiceImpl
import org.koin.dsl.module
import kotlin.math.sin

val servicesModule = module {
    single<SupabaseAuthService> { SupabaseAuthServiceImpl(supabaseClient = get()) }
    single<TrackShiftApiService> { TrackShiftApiServiceImpl(httpClient = get()) }
}