package org.studio.ghibli.background

import android.content.Context
import androidx.work.WorkerParameters

class PeopleUpdateWorker(context: Context, params: WorkerParameters) : BaseWorker(context, params) {

    override suspend fun doActualWork(): Result {

        if (database.peopleDAO().exist() == null || database.speciesDAO().exist() == null) {
            return Result.success()
        }

        val speciesUrls: List<String> = database.peopleDAO().fetchSpecies()

        speciesUrls.forEach { speciesUrl ->
            val name = database.speciesDAO().selectName(speciesUrl)
            database.peopleDAO().updateSpecies(speciesUrl, name)
        }

        return Result.success()
    }
}