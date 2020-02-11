package org.studio.database

import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.studio.database.dao.PeopleDAO
import org.studio.database.model.PeopleDO
import java.util.*

class PeopleTest : BaseTest() {
    private lateinit var peopleDAO: PeopleDAO

    @Before
    fun initDao() {
        peopleDAO = db.peopleDAO()
    }

    @Test
    @Throws(Exception::class)
    fun persistPeople() {
        val dummyText = "dummy-value"
        val id = UUID.randomUUID().toString()

        val databaseObject: PeopleDO = PeopleDO(
            id,
            dummyText,
            dummyText,
            dummyText,
            dummyText,
            dummyText,
            films,
            dummyText,
            dummyText
        )

        runBlocking {
            peopleDAO.insert(databaseObject)
            val peopleDO = peopleDAO.select(id)
            Assert.assertNotNull(peopleDO)
            Assert.assertTrue(peopleDO.name == dummyText)
            Assert.assertTrue(peopleDO.gender == dummyText)
            Assert.assertTrue(peopleDO.age == dummyText)
            Assert.assertTrue(peopleDO.hairColor == dummyText)
            Assert.assertTrue(peopleDO.eyeColor == dummyText)
            Assert.assertTrue(peopleDO.species == dummyText)
            Assert.assertTrue(peopleDO.films == films)
            Assert.assertTrue(peopleDO.url == dummyText)
        }
    }
}