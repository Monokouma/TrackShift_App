package com.despaircorp.trackshift.di

import org.koin.core.context.startKoin

fun initKoin() {
    startKoin {
        modules(
            networkModule,
            platformModule,
            dataModule,
            domainModule,
            presentationModule,
        )
    }
}