package org.studio.ghibli.background

import android.content.Context
import androidx.annotation.MainThread
import androidx.lifecycle.Observer
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import org.studio.ghibli.background.state.State

object WorkerManager : BaseWorkerManager() {

    private const val UNIQUE_WORK_NAME_FILMS = "FILMS_UNIQUE_WORK_NAME"
    private const val UNIQUE_WORK_NAME_PEOPLE = "PEOPLE_UNIQUE_WORK_NAME"

    @ExperimentalCoroutinesApi
    @MainThread
    suspend fun downloadFilms(applicationContext: Context) {
        manageWork(
            UNIQUE_WORK_NAME_FILMS,
            applicationContext,
            listOf(
                listOf(OneTimeWorkRequestBuilder<FilmsWorker>().build()),
                listOf(
                    OneTimeWorkRequestBuilder<PeopleWorker>().build(),
                    OneTimeWorkRequestBuilder<SpeciesWorker>().build(),
                    OneTimeWorkRequestBuilder<LocationsWorker>().build(),
                    OneTimeWorkRequestBuilder<VehiclesWorker>().build()
                )
            ),
            ::constructFinalObserver
        )
    }

    suspend fun downloadPeople(applicationContext: Context) {
        manageWork(
            UNIQUE_WORK_NAME_PEOPLE,
            applicationContext,
            listOf(
                listOf(OneTimeWorkRequestBuilder<PeopleWorker>().build()),
                listOf(OneTimeWorkRequestBuilder<SpeciesWorker>().build()),
                listOf(
                    OneTimeWorkRequestBuilder<PeopleUpdateWorker>().build(),
                    OneTimeWorkRequestBuilder<FilmsWorker>().build()
                )
            ),
            ::constructFinalObserver
        )
    }

    @ExperimentalCoroutinesApi
    private fun constructFinalObserver(): Observer<in WorkInfo> {
        return Observer { lastWorkInfo ->
            launch {
                when {
                    lastWorkInfo == null -> {
                    }
                    lastWorkInfo.state == WorkInfo.State.ENQUEUED -> workerStateChannel.send(
                        State.Enqueued(
                            this@WorkerManager
                        )
                    )
                    lastWorkInfo.state == WorkInfo.State.BLOCKED -> workerStateChannel.send(
                        State.Running(
                            this@WorkerManager
                        )
                    )
                    lastWorkInfo.state == WorkInfo.State.RUNNING -> workerStateChannel.send(
                        State.Running(
                            this@WorkerManager
                        )
                    )
                    lastWorkInfo.state == WorkInfo.State.SUCCEEDED -> workerStateChannel.send(
                        State.Succeeded(
                            this@WorkerManager
                        )
                    )
                    lastWorkInfo.state == WorkInfo.State.CANCELLED -> workerStateChannel.send(
                        State.Failed(
                            this@WorkerManager
                        )
                    )
                    lastWorkInfo.state == WorkInfo.State.FAILED -> workerStateChannel.send(
                        State.Failed(
                            this@WorkerManager
                        )
                    )
                }
            }
        }
    }

    fun cancelWorkers(applicationContext: Context) {
        cancelWorkers(UNIQUE_WORK_NAME_FILMS, applicationContext)
        cancelWorkers(UNIQUE_WORK_NAME_PEOPLE, applicationContext)
    }
}