package org.studio.database

import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.studio.database.dao.SpeciesDAO
import org.studio.database.model.SpeciesDO
import java.util.*

class SpeciesTest : BaseTest() {
    private lateinit var speciesDAO: SpeciesDAO

    @Before
    fun initDao() {
        speciesDAO = db.speciesDAO()
    }

    @Test
    @Throws(Exception::class)
    fun persistSpecies() {
        val dummyText = "dummy-value"
        val id = UUID.randomUUID().toString()

        val databaseObject: SpeciesDO = SpeciesDO(
            id,
            dummyText,
            dummyText,
            dummyText,
            dummyText,
            people,
            films,
            dummyText
        )

        runBlocking {
            speciesDAO.insert(databaseObject)
            val speciesDO = speciesDAO.select(id)
            Assert.assertNotNull(speciesDO)
            Assert.assertTrue(speciesDO.name == dummyText)
            Assert.assertTrue(speciesDO.classification == dummyText)
            Assert.assertTrue(speciesDO.eyeColors == dummyText)
            Assert.assertTrue(speciesDO.hairColors == dummyText)
            Assert.assertTrue(speciesDO.films == films)
            Assert.assertTrue(speciesDO.url == dummyText)
        }
    }
}