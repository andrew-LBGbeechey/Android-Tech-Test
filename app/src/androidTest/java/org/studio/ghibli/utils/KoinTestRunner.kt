package org.studio.ghibli.utils

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import timber.log.Timber

class KoinTestRunner : AndroidJUnitRunner() {
    override fun newApplication(
        classloader: ClassLoader?,
        className: String?,
        context: Context?
    ): Application {
        Timber.e("newApplication() when testing")
        return super.newApplication(classloader, KoinTestApplication::class.java.name, context)
    }
}