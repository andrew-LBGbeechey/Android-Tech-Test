package org.studio.ghibli.utils

import android.app.Application
import timber.log.Timber

class KoinTestApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.e("onCreate() when testing")
    }
}