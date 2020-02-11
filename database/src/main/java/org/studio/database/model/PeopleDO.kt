package org.studio.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "people", indices = [Index(value = ["name"]),
        Index(value = ["url"], unique = true),
        Index(value = ["gender"])]
)
data class PeopleDO(
    @PrimaryKey
    @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "gender") val gender: String,
    @ColumnInfo(name = "age") val age: String,
    @ColumnInfo(name = "eye_color") val eyeColor: String,
    @ColumnInfo(name = "hair_color") val hairColor: String,
    @ColumnInfo(name = "films") val films: List<String>,
    @ColumnInfo(name = "species") val species: String,
    @ColumnInfo(name = "url") val url: String
)