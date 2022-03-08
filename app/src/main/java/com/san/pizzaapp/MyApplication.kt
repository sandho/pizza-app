package com.san.pizzaapp

import android.app.Application
import com.san.pizzaapp.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)

            androidContext(this@MyApplication)
            loadKoinModules(listOf(
                roomDatabaseModule,
                roomDbViewModel,
                apiModule,
                retrofitModule,
                viewModelModule,
                repositoryModule
            ))
        }
    }

}