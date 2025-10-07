package com.halimjr11.eventora

import android.app.Application
import com.halimjr11.eventora.di.AppModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class EventoraApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@EventoraApp)
            modules(AppModules.getAppModules())
        }
    }
}