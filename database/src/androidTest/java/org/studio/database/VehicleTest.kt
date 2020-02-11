package org.studio.database

import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.studio.database.dao.VehicleDAO
import org.studio.database.model.VehicleDO
import java.util.*

class VehicleTest : BaseTest() {
    private lateinit var vehicleDAO: VehicleDAO

    @Before
    fun initDao() {
        vehicleDAO = db.vehicleDAO()
    }

    @Test
    @Throws(Exception::class)
    fun persistSpecies() {
        val dummyText = "dummy-value"
        val id = UUID.randomUUID().toString()

        val databaseObject: VehicleDO = VehicleDO(
            id,
            dummyText,
            dummyText,
            dummyText,
            dummyText,
            dummyText,
            dummyText,
            dummyText
        )

        runBlocking {
            vehicleDAO.insert(databaseObject)
            val vehicleDO = vehicleDAO.select(id)
            Assert.assertNotNull(vehicleDO)
            Assert.assertTrue(vehicleDO.name == dummyText)
            Assert.assertTrue(vehicleDO.description == dummyText)
            Assert.assertTrue(vehicleDO.vehicleClass == dummyText)
            Assert.assertTrue(vehicleDO.films == dummyText)
            Assert.assertTrue(vehicleDO.pilot == dummyText)
            Assert.assertTrue(vehicleDO.length == dummyText)
            Assert.assertTrue(vehicleDO.url == dummyText)
        }
    }
}