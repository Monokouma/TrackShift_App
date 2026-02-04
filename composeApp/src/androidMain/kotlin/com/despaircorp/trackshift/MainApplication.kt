package com.despaircorp.trackshift

import android.app.Application
import com.despaircorp.trackshift.di.dataModule
import com.despaircorp.trackshift.di.domainModule
import com.despaircorp.trackshift.di.networkModule
import com.despaircorp.trackshift.di.platformModule
import com.despaircorp.trackshift.di.presentationModule
import com.despaircorp.trackshift.di.servicesModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class MainApplication() : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MainApplication)
            modules(
                servicesModule,
                platformModule,
                networkModule,
                dataModule,
                domainModule,
                presentationModule,
            )
        }
    }
}