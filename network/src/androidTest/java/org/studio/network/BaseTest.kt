package org.studio.network

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.inject
import org.studio.network.koin.networkModule

@RunWith(AndroidJUnit4::class)
abstract class BaseTest : KoinTest, CoroutineScope by MainScope() {

    internal val service: Service by inject()

    @Before
    fun startKoinForTest() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        if (GlobalContext.getOrNull() == null) {
            startKoin {
                androidLogger()
                androidContext(appContext)
                modules(networkModule)
            }
        }
    }

    @After
    fun stopKoinAfterTest() = stopKoin()
}