package org.studio.database

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.runBlocking
import org.junit.*
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.studio.database.dao.FilmDAO
import org.studio.database.model.FilmDO
import java.io.IOException
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

@RunWith(JUnit4::class)
abstract class BaseTest {

    internal lateinit var db: StudioGhibliDatabase

    internal val dummyText = "dummy-value"
    internal val people = listOf(
        "PEOPLE-00",
        "PEOPLE-01",
        "PEOPLE-02",
        "PEOPLE-03",
        "PEOPLE-04",
        "PEOPLE-05"
    )
    internal val residents = people
    internal val species = listOf(
        "SPECIES-00",
        "SPECIES-01",
        "SPECIES-02",
        "SPECIES-03",
        "SPECIES-04"
    )
    internal val locations = listOf(
        "LOCATION-00",
        "LOCATION-01",
        "LOCATION-02",
        "LOCATION-03",
        "LOCATION-04",
        "LOCATION-05"
    )
    internal val vehicles = listOf(
        "VEHICLE-00",
        "VEHICLE-01",
        "VEHICLE-02"
    )

    internal val films = listOf(
        "FILM-00",
        "FILM-01",
        "FILM-02"
    )

    internal val urls = listOf(
        "URLS-00",
        "URLS-01",
        "URLS-02",
        "URLS-03",
        "URLS-04"
    )

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, StudioGhibliDatabase::class.java).build()
    }

    @After
    fun closeDb() {
        db.close()
    }
}