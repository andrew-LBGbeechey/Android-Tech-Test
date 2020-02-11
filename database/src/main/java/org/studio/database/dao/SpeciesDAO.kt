package org.studio.database.dao

import androidx.room.Dao
import androidx.room.Query
import org.studio.database.model.SpeciesDO

@Dao
interface SpeciesDAO : BaseDAO<SpeciesDO> {

    @Query("SELECT * from species ORDER BY name")
    suspend fun fetch(): List<SpeciesDO>

    @Query("SELECT * from species WHERE id = :id")
    suspend fun select(id: String): SpeciesDO

    @Query("SELECT name from species WHERE url = :url")
    suspend fun selectName(url: String): String

    @Query("SELECT * FROM species LIMIT 1")
    fun exist(): SpeciesDO?
}


