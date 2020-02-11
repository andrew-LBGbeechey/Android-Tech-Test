package org.studio.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "film", indices = [Index(value = ["title"]),
        Index(value = ["url"], unique = true),
        Index(value = ["director"]),
        Index(value = ["producer"])])
data class FilmDO(
    @PrimaryKey
    @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "director") val director: String,
    @ColumnInfo(name = "producer") val producer: String,
    @ColumnInfo(name = "release_date") val releaseDate: Int,
    @ColumnInfo(name = "rt_score") val rtScore: Int,
    @ColumnInfo(name = "people") val people: List<String>,
    @ColumnInfo(name = "species") val species: List<String>,
    @ColumnInfo(name = "locations") val locations: List<String>,
    @ColumnInfo(name = "vehicles") val vehicles: List<String>,
    @ColumnInfo(name = "url") val url: String
)