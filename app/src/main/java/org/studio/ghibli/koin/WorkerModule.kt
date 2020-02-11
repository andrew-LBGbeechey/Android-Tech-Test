package org.studio.ghibli.koin

import org.koin.dsl.module
import org.studio.ghibli.background.WorkerManager

val workerModule = module {
    single {
        WorkerManager
    }
}