package org.studio.database.dao

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Query
import org.studio.database.model.FilmDO

@Dao
interface FilmDAO : BaseDAO<FilmDO> {

    @Query("SELECT * from film ORDER BY title")
    fun fetch(): DataSource.Factory<Int, FilmDO>

    @Query("SELECT * from film WHERE id = :id")
    fun select(id: String): LiveData<FilmDO>

    @Query("SELECT * FROM film LIMIT 1")
    fun exist(): FilmDO?
}


