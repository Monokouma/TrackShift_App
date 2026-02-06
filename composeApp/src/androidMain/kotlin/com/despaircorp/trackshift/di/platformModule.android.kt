package com.despaircorp.trackshift.di

import com.despaircorp.services.storage.service.StorageService
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule = module {
    single {
        StorageService(androidContext())
    }
}