package org.studio.ghibli.films

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
import org.studio.database.model.FilmDO
import org.studio.ghibli.base.BaseViewModel
import org.studio.ghibli.base.SHOULD_NOT_HAPPEN
import org.studio.ghibli.film.constructFilm
import org.studio.ghibli.films.effects.Action
import org.studio.ghibli.model.FilmUI
import org.studio.side_effects.Reaction
import org.studio.side_effects.SideEffect

class FilmsViewModel : BaseViewModel() {

    @ExperimentalCoroutinesApi
    @FlowPreview
    fun perform(action: Action): IO<Flow<Any>> = IO.fx {

        val actionSideEffects: Flow<SideEffect> = effect {
            action.sideEffects.asFlow()
        }.bind()

        val reaction: Reaction<LiveData<PagedList<FilmUI>>> = effect {
            when (action) {
                is Action.FetchFilms -> Reaction.Success(
                    LivePagedListBuilder<Int, FilmUI>(
                        database.filmDAO().fetch()
                            .mapByPage { rows -> transformFilms(rows) },
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

    private fun transformFilms(rows: List<FilmDO>): MutableList<FilmUI> {
        val films: MutableList<FilmUI> = mutableListOf()

        rows.forEach { row ->
            films.add(
               constructFilm(row)
            )
        }

        return films
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