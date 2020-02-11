package org.studio.database

import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.studio.database.dao.LocationDAO
import org.studio.database.model.LocationDO
import java.util.*

class LocationTest : BaseTest() {
    private lateinit var locationDAO: LocationDAO

    @Before
    fun initDao() {
        locationDAO = db.locationDAO()
    }

    @Test
    @Throws(Exception::class)
    fun persistLocation() {
        val dummyText = "dummy-value"
        val id = UUID.randomUUID().toString()

        val databaseObject: LocationDO = LocationDO(
            id, dummyText, dummyText, dummyText, 12, residents, films, urls
        )

        runBlocking {
            locationDAO.insert(databaseObject)
            val locationDO = locationDAO.select(id)
            Assert.assertNotNull(locationDO)
            Assert.assertTrue(locationDO.name == dummyText)
            Assert.assertTrue(locationDO.climate == dummyText)
            Assert.assertTrue(locationDO.terrain == dummyText)
            Assert.assertTrue(locationDO.surfaceWater == 12)
            Assert.assertTrue(locationDO.residents == residents)
            Assert.assertTrue(locationDO.films == films)
            Assert.assertTrue(locationDO.url == urls)
        }
    }
}