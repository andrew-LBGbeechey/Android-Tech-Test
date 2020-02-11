package org.studio.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "species", indices = [Index(value = ["name"]),
    Index(value = ["url"], unique = true),
    Index(value = ["classification"])])
data class SpeciesDO(
    @PrimaryKey
    @ColumnInfo(name = "id") val id : String,
    @ColumnInfo(name = "name") val name : String,
    @ColumnInfo(name = "classification") val classification : String,
    @ColumnInfo(name = "eye_colors") val eyeColors : String,
    @ColumnInfo(name = "hair_colors") val hairColors : String,
    @ColumnInfo(name = "people") val people : List<String>,
    @ColumnInfo(name = "films") val films : List<String>,
    @ColumnInfo(name = "url") val url : String
)