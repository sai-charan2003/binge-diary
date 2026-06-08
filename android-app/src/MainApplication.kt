package com.charan.bingediary

import android.app.Application
import com.charan.bingediary.di.AppModule
import com.charan.bingediary.di.module
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MainApplication)
            modules(AppModule().module())
        }
    }
}