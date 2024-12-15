package com.johny.cryptoApp

import android.app.Application
import com.johny.cryptoApp.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class CryptoApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@CryptoApp)
            androidLogger()
            modules(appModule)
        }
    }
}