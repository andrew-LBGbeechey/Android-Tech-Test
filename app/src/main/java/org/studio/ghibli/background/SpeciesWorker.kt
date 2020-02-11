package org.studio.ghibli.background

import android.content.Context
import androidx.work.WorkerParameters
import org.studio.database.model.SpeciesDO

class SpeciesWorker(context: Context, params: WorkerParameters) : BaseWorker(context, params) {

    private var syncResult: Result = Result.failure()

    override suspend fun doActualWork(): Result {

        if (database.speciesDAO().exist() != null) {
            return Result.success()
        }

        val response = service.species(250)

        if (validResponse(response)) {
            response.body()?.let { speciesNMs ->
                val species: MutableList<SpeciesDO> = mutableListOf()
                speciesNMs.forEach { speciesNM ->
                    species.add(
                        SpeciesDO(
                            speciesNM.id,
                            speciesNM.name,
                            speciesNM.classification,
                            speciesNM.eyeColors,
                            speciesNM.hairColors,
                            speciesNM.people,
                            speciesNM.films,
                            speciesNM.url
                        )
                    )
                }
                database.speciesDAO().insert(species)
                syncResult = Result.success()
            }
        }

        return syncResult
    }
}