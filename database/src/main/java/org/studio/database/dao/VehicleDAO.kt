package org.studio.database.dao

import androidx.room.Dao
import androidx.room.Query
import org.studio.database.model.VehicleDO

@Dao
interface VehicleDAO : BaseDAO<VehicleDO> {

    @Query("SELECT * from vehicle ORDER BY name")
    suspend fun fetch(): List<VehicleDO>

    @Query("SELECT * from vehicle WHERE id = :id")
    suspend fun select(id: String): VehicleDO
}


