package org.studio.network

import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class PeopleTest : BaseTest() {

    @Test
    fun getPeople() {
        runBlocking {
            val response = service.people(250)

            assertTrue(response.isSuccessful)
            assertNotNull(response.body())
            assertNotEquals(response.body()!!.size, 0)

            response.body()!!.forEach {
                assertNotNull(it.url)
                assertNotNull(it.id)
                assertNotNull(it.name)
                assertNotNull(it.age)
                assertNotNull(it.eyeColor)
                assertNotNull(it.hairColor)
                assertNotNull(it.gender)
                assertNotEquals(it.films, 0)
                assertNotEquals(it.species, 0)
            }
        }
    }
}