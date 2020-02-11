package org.studio.ghibli

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.studio.database.koin.databaseModule
import org.studio.ghibli.koin.viewModelModule
import org.studio.ghibli.koin.workerModule
import org.studio.network.koin.networkModule
import timber.log.Timber

class StudioGhibliApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initTimber()
        initKoin()
    }

    private fun initKoin() {
        startKoin {
            printLogger(Level.DEBUG)
            androidContext(this@StudioGhibliApplication)
            modules(
                listOf(
                    networkModule,
                    viewModelModule,
                    workerModule,
                    databaseModule
                )
            )
        }
    }

    private fun initTimber() {
        Timber.plant(Timber.DebugTree())
        Timber.e("Initialised")
    }
}