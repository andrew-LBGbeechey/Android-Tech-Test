package org.studio.ghibli.people

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import arrow.fx.IO
import arrow.fx.extensions.fx
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flowOf
import org.studio.database.model.PeopleDO
import org.studio.ghibli.base.BaseViewModel
import org.studio.ghibli.base.SHOULD_NOT_HAPPEN
import org.studio.ghibli.model.PeopleUI
import org.studio.ghibli.people.effects.Action
import org.studio.side_effects.Reaction
import org.studio.side_effects.SideEffect

class PeopleViewModel : BaseViewModel() {

    @ExperimentalCoroutinesApi
    @FlowPreview
    fun perform(action: Action): IO<Flow<Any>> = IO.fx {

        val actionSideEffects: Flow<SideEffect> = effect {
            action.sideEffects.asFlow()
        }.bind()

        val reaction: Reaction<LiveData<PagedList<PeopleUI>>> = effect {
            when (action) {
                is Action.FetchPeople -> Reaction.Success(
                    LivePagedListBuilder<Int, PeopleUI>(
                        database.peopleDAO().fetch()
                            .mapByPage { rows ->
                                transformPeople(rows)
                            },
                        PAGED_LIST_CONFIG
                    ).build()
                )
                Action.NO_OP -> SHOULD_NOT_HAPPEN()
            }
        }.bind()

        val reactionSideEffects: Flow<SideEffect> = effect {
            reaction.sideEffects.asFlow()
        }.bind()

        effect {
            merge(actionSideEffects, reactionSideEffects, flowOf(reaction))
        }.bind()
    }

    private fun transformPeople(rows: List<PeopleDO>): MutableList<PeopleUI> {
        val people: MutableList<PeopleUI> = mutableListOf()

        rows.forEach { row ->
            people.add(
                constructPeople(row)
            )
        }

        return people
    }

    companion object {
        private val PAGED_LIST_CONFIG = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setInitialLoadSizeHint(60)
            .setPrefetchDistance(20)
            .setPageSize(20)
            .build()
    }
}


fun constructPeople(row: PeopleDO): PeopleUI {

    return PeopleUI(
        row.id,
        row.name,
        row.gender,
        row.age,
        row.eyeColor,
        row.hairColor,
        row.films,
        row.species,
        row.url
    )
}