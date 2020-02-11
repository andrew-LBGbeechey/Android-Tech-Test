package org.studio.ghibli.base

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.work.ListenableWorker
import androidx.work.testing.TestListenableWorkerBuilder
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.studio.ghibli.background.FilmsWorker
import org.koin.test.KoinTest
import org.studio.database.koin.roomTestModule
import org.studio.network.koin.networkModule

@RunWith(AndroidJUnit4::class)
abstract class BaseTest : KoinTest {
    lateinit var context: Context

    @Before
    fun startKoinForTest() {
        context = ApplicationProvider.getApplicationContext<Context>()

        if (GlobalContext.getOrNull() == null) {
            startKoin {
                androidLogger()
                androidContext(context)
                modules(listOf(roomTestModule, networkModule))
            }
        }
    }

    @After
    fun stopKoinAfterTest() = stopKoin()
}
