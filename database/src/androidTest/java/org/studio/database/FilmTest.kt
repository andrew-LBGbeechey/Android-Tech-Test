package org.studio.database

import androidx.lifecycle.Observer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.studio.database.dao.FilmDAO
import org.studio.database.model.FilmDO
import java.util.*

class FilmTest : BaseTest() {
    private lateinit var filmDAO: FilmDAO

    @Before
    fun initDao() {
        filmDAO = db.filmDAO()
    }

    @Test
    @Throws(Exception::class)
    fun persistFilm() {
        val id = UUID.randomUUID().toString()
        val databaseObject: FilmDO = FilmDO(
            id,
            dummyText,
            dummyText,
            dummyText,
            dummyText,
            2019,
            97,
            people,
            species,
            locations,
            vehicles,
            "https://$id"
        )

        runBlocking(Dispatchers.IO) {
            val rows = filmDAO.insert(databaseObject)
            Assert.assertTrue(rows > 0L)
            val liveData = filmDAO.select(id)
            val observer = Observer<FilmDO> {filmDO ->
                Assert.assertNotNull(filmDO)
                Assert.assertTrue(filmDO.title == dummyText)
                Assert.assertTrue(filmDO.description == dummyText)
                Assert.assertTrue(filmDO.director == dummyText)
                Assert.assertTrue(filmDO.people == people)
                Assert.assertTrue(filmDO.species == species)
                Assert.assertTrue(filmDO.locations == locations)
                Assert.assertTrue(filmDO.vehicles == vehicles)
            }

            withContext(Dispatchers.Main) {
                liveData.observeForever(observer)
                liveData.removeObserver(observer)
            }
        }
    }
}