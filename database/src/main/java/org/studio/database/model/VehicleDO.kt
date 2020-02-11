package org.studio.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "vehicle", indices = [Index(value = ["name"]),
    Index(value = ["url"], unique = true),
    Index(value = ["description"])])
data class VehicleDO(
    @PrimaryKey
    @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "vehicle_class") val vehicleClass: String,
    @ColumnInfo(name = "length") val length: String,
    @ColumnInfo(name = "pilot") val pilot: String,
    @ColumnInfo(name = "films") val films: String,
    @ColumnInfo(name = "url") val url: String
)