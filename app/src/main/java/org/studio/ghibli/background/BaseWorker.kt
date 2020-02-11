package org.studio.ghibli.background

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.studio.database.StudioGhibliDatabase
import org.studio.network.Service
import retrofit2.Response

abstract class BaseWorker(val context: Context, val params: WorkerParameters) :
    CoroutineWorker(context, params),
    KoinComponent {

    internal val service: Service by inject()
    internal val database: StudioGhibliDatabase by inject()

    override suspend fun doWork(): Result = coroutineScope {
        withContext(Dispatchers.IO) {
            try {
                doActualWork()
            } finally {
                if (isStopped) {
                    Result.failure()
                }
            }
        }
    }

    internal fun validResponse(response: Response<*>): Boolean {
        if (response.isSuccessful && response.body() != null) {
            return true
        }
        return false
    }

    abstract suspend fun doActualWork(): Result
}
