package org.studio.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "location", indices = [Index(value = ["name"]),
    Index(value = ["climate"])])
data class LocationDO(
    @PrimaryKey
    @ColumnInfo(name = "id") val id : String,
    @ColumnInfo(name = "name") val name : String,
    @ColumnInfo(name = "climate") val climate : String,
    @ColumnInfo(name = "terrain") val terrain : String,
    @ColumnInfo(name = "surface_water") val surfaceWater : Int,
    @ColumnInfo(name = "residents") val residents : List<String>,
    @ColumnInfo(name = "films") val films : List<String>,
    @ColumnInfo(name = "url") val url : List<String>
)