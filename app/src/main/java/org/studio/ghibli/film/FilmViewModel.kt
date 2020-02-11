package org.studio.ghibli.film

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import arrow.fx.IO
import arrow.fx.extensions.fx
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flowOf
import org.studio.database.model.FilmDO
import org.studio.ghibli.base.BaseViewModel
import org.studio.ghibli.base.SHOULD_NOT_HAPPEN
import org.studio.ghibli.film.effects.Action
import org.studio.ghibli.model.FilmUI
import org.studio.side_effects.Reaction
import org.studio.side_effects.SideEffect

class FilmViewModel : BaseViewModel() {

    @ExperimentalCoroutinesApi
    @FlowPreview
    fun perform(action: Action): IO<Flow<Any>> = IO.fx {

        continueOn(Dispatchers.IO)

        val actionSideEffects: Flow<SideEffect> = effect {
            action.sideEffects.asFlow()
        }.bind()

        val reaction: Reaction<LiveData<FilmUI>> = effect {
            when (action) {
                is Action.SelectFilm -> Reaction.Success(
                    Transformations.map<FilmDO, FilmUI>(database.filmDAO().select(action.id)) {
                        constructFilm(it)
                    }
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
}

fun constructFilm(row: FilmDO): FilmUI {

    return FilmUI(
        row.id,
        row.title,
        row.description,
        row.director,
        row.producer,
        row.releaseDate,
        row.rtScore,
        row.people,
        row.species,
        row.locations,
        row.vehicles,
        row.url
    )
}