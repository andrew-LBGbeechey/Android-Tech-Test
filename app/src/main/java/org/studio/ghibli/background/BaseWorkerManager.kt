package org.studio.ghibli.background

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.work.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.studio.ghibli.background.state.State
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

abstract class BaseWorkerManager : CoroutineScope by GlobalScope {

    companion object {

        private val mutex = Mutex()
        const val LAST_WORKER_UUID: String = "LAST_WORKER_UUID"

        private val observers: ConcurrentMap<LiveData<WorkInfo>, Observer<in WorkInfo>> = ConcurrentHashMap()

        @ExperimentalCoroutinesApi
        @JvmStatic
        internal val workerStateChannel = ConflatedBroadcastChannel<State>(State.Dormant)

        @ExperimentalCoroutinesApi
        @FlowPreview
        @JvmStatic
        val workerStateFlow: Flow<State> = workerStateChannel.asFlow()

        internal suspend fun manageWork(
            uniqueName: String,
            applicationContext: Context,
            workStages: List<List<OneTimeWorkRequest>>,
            constructObserver: () -> Observer<in WorkInfo>
        ) {
            mutex.withLock {
                lockedManagedWork(uniqueName, applicationContext, workStages, constructObserver)
            }
        }

        private fun lockedManagedWork(uniqueName: String, applicationContext: Context, workStages: List<List<OneTimeWorkRequest>>, constructObserver: () -> Observer<in WorkInfo>) {
            val finalWorkerRequest: OneTimeWorkRequest = OneTimeWorkRequestBuilder<FinalWorker>().build()

            val initialInputData: Data = workDataOf(LAST_WORKER_UUID to finalWorkerRequest.id.toString())
            val initialWorkerRequest: OneTimeWorkRequest = OneTimeWorkRequestBuilder<InitialWorker>().setInputData(initialInputData).build()

            var workContinuation: WorkContinuation = WorkManager.getInstance(applicationContext)
                .beginUniqueWork(uniqueName, ExistingWorkPolicy.KEEP, initialWorkerRequest)

            workStages.forEach { workStage ->
                workContinuation = workContinuation.then(workStage)
            }

            workContinuation.then(finalWorkerRequest)
                .enqueue()

            val liveData = WorkManager.getInstance(applicationContext).getWorkInfoByIdLiveData(initialWorkerRequest.id)
            val observer = Observer<WorkInfo> { workInfo ->
                when {
                    workInfo == null -> {
                    }

                    workInfo.state == WorkInfo.State.SUCCEEDED -> {
                        observe(applicationContext, workInfo, constructObserver)
                    }

                    workInfo.state == WorkInfo.State.ENQUEUED ||
                            workInfo.state == WorkInfo.State.FAILED ||
                            workInfo.state == WorkInfo.State.BLOCKED ||
                            workInfo.state == WorkInfo.State.CANCELLED ||
                            workInfo.state == WorkInfo.State.RUNNING -> {
                    }
                }
            }

            liveData.observeForever(observer)
            synchronized(observers) {
                observers[liveData] = observer
            }
        }

        private fun observe(applicationContext: Context, workInfo: WorkInfo, constructObserver: () -> Observer<in WorkInfo>) {
            val initialOutputData: Data = workInfo.outputData
            val finalWorkerUuid: UUID = UUID.fromString(initialOutputData.getString(LAST_WORKER_UUID))

            val liveData = WorkManager.getInstance(applicationContext).getWorkInfoByIdLiveData(finalWorkerUuid)
            val observer = constructObserver()
            liveData.observeForever(observer)
            synchronized(observers) {
                observers[liveData] = observer
            }
        }

        @ExperimentalCoroutinesApi
        @JvmStatic
        suspend fun resetWorkerState() {

            val currentValue: State? = workerStateChannel.valueOrNull

            if (currentValue != null && currentValue is State.Dormant) {
                return
            }

            workerStateChannel.send(State.Dormant)

            removeObservers()
        }

        private fun removeObservers() {
            synchronized(observers) {
                for ((liveData: LiveData<WorkInfo>, observer: Observer<in WorkInfo>) in observers) {
                    val workInfo = liveData.value
                    if (workInfo != null && workInfo.state.isFinished) liveData.removeObserver(observer)
                }

                val activeObservers: Map<LiveData<WorkInfo>, Observer<in WorkInfo>> =
                    observers
                        .filter { (liveData: LiveData<WorkInfo>, _: Observer<in WorkInfo>) ->
                            if (liveData.value == null) false
                            else !liveData.value?.state?.isFinished!!
                        }

                observers.clear()
                observers.putAll(activeObservers)
            }
        }

        @JvmStatic
        internal fun cancelWorkers(uniqueName: String, applicationContext: Context) {
            WorkManager.getInstance(applicationContext).cancelUniqueWork(uniqueName)
        }

        @JvmStatic
        fun isActiveWorkers(): Boolean {
            synchronized(observers) {
                return observers.isNotEmpty()
            }
        }
    }
}
