package org.studio.ghibli.background

import android.content.Context
import androidx.work.WorkerParameters

class FinalWorker(context: Context, params: WorkerParameters) : BaseWorker(context, params) {
  override suspend fun doActualWork(): Result {
        return Result.success()
    }
}