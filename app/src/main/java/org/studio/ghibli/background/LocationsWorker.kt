package org.studio.ghibli.background

import android.content.Context
import androidx.work.WorkerParameters
import org.studio.database.model.LocationDO

class LocationsWorker(context: Context, params: WorkerParameters) : BaseWorker(context, params) {

    private var syncResult: Result = Result.failure()

    override suspend fun doActualWork(): Result {

        if (database.locationDAO().exist() != null) {
            return Result.success()
        }

        val response = service.locations(250)

        if (validResponse(response)) {
            response.body()?.let { locationNMs ->
                val locations: MutableList<LocationDO> = mutableListOf()
                locationNMs.forEach { locationNM ->
                    locations.add(
                        LocationDO(
                            locationNM.id,
                            locationNM.name,
                            locationNM.climate,
                            locationNM.terrain,
                            locationNM.surfaceWater,
                            locationNM.residents,
                            locationNM.films,
                            locationNM.url
                        )
                    )
                }
                database.locationDAO().insert(locations)
                syncResult = Result.success()
            }
        }

        return syncResult
    }
}