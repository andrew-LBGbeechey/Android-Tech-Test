package org.studio.ghibli.background

import android.content.Context
import androidx.work.WorkerParameters
import org.studio.database.model.PeopleDO

class PeopleWorker(context: Context, params: WorkerParameters) : BaseWorker(context, params) {

    private var syncResult: Result = Result.failure()

    override suspend fun doActualWork(): Result {

        if (database.peopleDAO().exist() != null) {
            return Result.success()
        }

        val response = service.people(250)

        if (validResponse(response)) {
            response.body()?.let { peopleNMs ->
                val people: MutableList<PeopleDO> = mutableListOf()
                peopleNMs.forEach { peopleNM ->
                    people.add(
                        PeopleDO(
                            peopleNM.id,
                            peopleNM.name,
                            peopleNM.gender,
                            peopleNM.age,
                            peopleNM.eyeColor,
                            peopleNM.hairColor,
                            peopleNM.films,
                            peopleNM.species,
                            peopleNM.url
                        )
                    )
                }
                database.peopleDAO().insert(people)
                syncResult = Result.success()
            }
        }

        return syncResult
    }
}