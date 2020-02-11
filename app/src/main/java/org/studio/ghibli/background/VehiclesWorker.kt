package org.studio.ghibli.background

import android.content.Context
import androidx.work.WorkerParameters
import org.studio.database.model.VehicleDO

class VehiclesWorker(context: Context, params: WorkerParameters) : BaseWorker(context, params) {

    private var syncResult: Result = Result.failure()

    override suspend fun doActualWork(): Result {

        if (database.locationDAO().exist() != null) {
            return Result.success()
        }

        val response = service.vehicles(250)

        if (validResponse(response)) {
            response.body()?.let { vehicleNMs ->
                val vehicles: MutableList<VehicleDO> = mutableListOf()
                vehicleNMs.forEach { vehicleNM ->
                    vehicles.add(
                        VehicleDO(
                            vehicleNM.id,
                            vehicleNM.name,
                            vehicleNM.description,
                            vehicleNM.vehicleClass,
                            vehicleNM.length,
                            vehicleNM.pilot,
                            vehicleNM.films,
                            vehicleNM.url
                        )
                    )
                }
                database.vehicleDAO().insert(vehicles)
                syncResult = Result.success()
            }
        }

        return syncResult
    }
}