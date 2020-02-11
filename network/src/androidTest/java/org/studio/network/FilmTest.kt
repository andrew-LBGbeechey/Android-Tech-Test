package org.studio.network

import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class FilmTest : BaseTest() {

    @Test
    fun getFilms() {
        runBlocking {
            val response = service.films(250)

            assertTrue(response.isSuccessful)
            assertNotNull(response.body())
            assertNotEquals(response.body()!!.size, 0)

            response.body()!!.forEach {
                assertNotNull(it.url)
                assertNotNull(it.id)
                assertNotNull(it.title)
                assertNotNull(it.director)
                assertNotNull(it.producer)
                assertNotNull(it.releaseDate)
                assertNotNull(it.description)
                assertNotEquals(it.people, 0)
                assertNotEquals(it.locations, 0)
                assertNotEquals(it.species, 0)
            }
        }
    }
}