package org.studio.database.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update

interface BaseDAO<T> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(row: T) : Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(rows: List<T>) : List<Long>

    @Update
    suspend fun update(row: T)

    @Delete
    suspend fun delete(row: T)
}