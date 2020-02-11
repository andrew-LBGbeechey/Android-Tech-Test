package org.studio.database.dao

import androidx.room.Dao
import androidx.room.Query
import org.studio.database.model.LocationDO

@Dao
interface LocationDAO : BaseDAO<LocationDO> {

    @Query("SELECT * from location ORDER BY name")
    suspend fun fetch(): List<LocationDO>

    @Query("SELECT * from location WHERE id = :id")
    suspend fun select(id: String): LocationDO

    @Query("SELECT * FROM location LIMIT 1")
    fun exist(): LocationDO?
}


