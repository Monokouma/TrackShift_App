package com.despaircorp.trackshift.di

import org.koin.core.context.startKoin

fun initKoin() {
    startKoin {
        modules(
            servicesModule,
            networkModule,
            platformModule,
            dataModule,
            domainModule,
            presentationModule,
        )
    }
}