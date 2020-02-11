package org.studio.network

import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class SpeciesTest : BaseTest() {

    @Test
    fun getSpecies() {
        runBlocking {
            val response = service.species(250)

            assertTrue(response.isSuccessful)
            assertNotNull(response.body())
            assertNotEquals(response.body()!!.size, 0)

            response.body()!!.forEach {
                assertNotNull(it.url)
                assertNotNull(it.id)
                assertNotNull(it.name)
                assertNotNull(it.classification)
                assertNotEquals(it.films, 0)
                assertNotEquals(it.people, 0)
            }
        }
    }
}