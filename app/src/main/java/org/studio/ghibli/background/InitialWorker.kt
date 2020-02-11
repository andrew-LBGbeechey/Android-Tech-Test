package org.studio.ghibli.background

import android.content.Context
import androidx.work.Data
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import org.studio.ghibli.background.BaseWorkerManager.Companion.LAST_WORKER_UUID

class InitialWorker(context: Context, params: WorkerParameters) : BaseWorker(context, params) {

    override suspend fun doActualWork(): Result {
        val initialOutputData: Data = workDataOf(LAST_WORKER_UUID to inputData.getString(LAST_WORKER_UUID))
        return Result.success(initialOutputData)
    }
}