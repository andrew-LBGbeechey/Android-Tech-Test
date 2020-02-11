package org.studio.database.koin

import androidx.room.Room
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import org.studio.database.StudioGhibliDatabase

private const val DATABASE_NAME: String = "studio-ghibli-database"

val databaseModule = module {
    single {
        Room.databaseBuilder(androidApplication(), StudioGhibliDatabase::class.java, DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }
}