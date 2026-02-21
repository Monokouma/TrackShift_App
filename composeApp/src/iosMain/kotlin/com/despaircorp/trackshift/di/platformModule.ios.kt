package com.despaircorp.trackshift.di

import com.despaircorp.services.storage.service.StorageService
import com.despaircorp.services.storage.service.StorageServiceContract
import com.despaircorp.utils.ClipboardManager
import com.despaircorp.utils.SubscriptionsManager
import org.koin.dsl.module

actual val platformModule = module {
    single<StorageServiceContract> {
        StorageService()
    }
    single { ClipboardManager() }

    single { SubscriptionsManager() }

}