package org.studio.database.koin

import androidx.room.Room
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import org.studio.database.StudioGhibliDatabase

val roomTestModule = module {
    single {
        Room.inMemoryDatabaseBuilder(androidApplication(), StudioGhibliDatabase::class.java)
            .allowMainThreadQueries()
            .build()
    }
}