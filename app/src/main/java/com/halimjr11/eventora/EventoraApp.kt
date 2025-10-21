package com.halimjr11.eventora

import android.app.Application
import androidx.work.Configuration
import com.halimjr11.eventora.di.AppModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.workmanager.factory.KoinWorkerFactory
import org.koin.core.context.startKoin

class EventoraApp : Application(), Configuration.Provider {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@EventoraApp)
            modules(AppModules.getAppModules())
        }
    }

    override val workManagerConfiguration: Configuration = Configuration.Builder()
        .setWorkerFactory(KoinWorkerFactory())
        .build()
}