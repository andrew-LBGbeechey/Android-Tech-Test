package org.studio.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import org.studio.database.dao.*
import org.studio.database.model.*

@Database(
    entities = [
        FilmDO::class,
        LocationDO::class,
        PeopleDO::class,
        SpeciesDO::class,
        VehicleDO::class], version = 1
)
@TypeConverters(ListConverter::class)
abstract class StudioGhibliDatabase : RoomDatabase() {
    abstract fun filmDAO(): FilmDAO
    abstract fun locationDAO(): LocationDAO
    abstract fun peopleDAO(): PeopleDAO
    abstract fun speciesDAO(): SpeciesDAO
    abstract fun vehicleDAO(): VehicleDAO
}