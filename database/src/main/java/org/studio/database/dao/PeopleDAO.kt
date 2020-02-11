package org.studio.database.dao

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Query
import org.studio.database.model.PeopleDO

@Dao
interface PeopleDAO : BaseDAO<PeopleDO> {

    @Query("SELECT * from people ORDER BY name")
    fun fetch(): DataSource.Factory<Int, PeopleDO>

    @Query("SELECT DISTINCT species from people WHERE species LIKE 'https://%' ORDER BY species")
    suspend fun fetchSpecies(): List<String>

    @Query("UPDATE people SET species = :name WHERE species = :speciesUrl")
    suspend fun updateSpecies(speciesUrl: String, name: String)

    @Query("SELECT * from people WHERE id = :id")
    suspend fun select(id: String): PeopleDO

    @Query("SELECT * FROM people LIMIT 1")
    fun exist(): PeopleDO?
}


