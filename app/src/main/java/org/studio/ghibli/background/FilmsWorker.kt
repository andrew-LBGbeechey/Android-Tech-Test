package org.studio.ghibli.background

import android.content.Context
import androidx.work.WorkerParameters
import org.studio.database.model.FilmDO

class FilmsWorker(context: Context, params: WorkerParameters) : BaseWorker(context, params) {

    private var syncResult: Result = Result.failure()

    override suspend fun doActualWork(): Result {

        if (database.filmDAO().exist() != null) {
            return Result.success()
        }

        val response = service.films(250)

        if (validResponse(response)) {
            response.body()?.let { filmsNM ->
                val films: MutableList<FilmDO> = mutableListOf()
                filmsNM.forEach { filmNM ->
                    films.add(
                        FilmDO(
                            filmNM.id,
                            filmNM.title,
                            filmNM.description,
                            filmNM.director,
                            filmNM.producer,
                            filmNM.releaseDate,
                            filmNM.rtScore,
                            filmNM.people,
                            filmNM.species,
                            filmNM.locations,
                            filmNM.vehicles,
                            filmNM.url
                        )
                    )
                }
                database.filmDAO().insert(films)
                syncResult = Result.success()
            }
        }

        return syncResult
    }
}