package org.studio.network

import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class LocationTest : BaseTest() {

    @Test
    fun getLocation() {
        runBlocking {
            val response = service.locations(250)

            assertTrue(response.isSuccessful)
            assertNotNull(response.body())
            assertNotEquals(response.body()!!.size, 0)

            response.body()!!.forEach {
                assertNotNull(it.url)
                assertNotNull(it.id)
                assertNotNull(it.name)
                assertNotNull(it.terrain)
                assertNotNull(it.climate)
                assertNotEquals(it.films, 0)
                assertNotEquals(it.residents, 0)
            }
        }
    }
}