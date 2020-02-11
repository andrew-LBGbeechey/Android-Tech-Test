package org.studio.ghibli.home

import android.app.Application
import android.content.Intent
import arrow.fx.IO
import arrow.fx.extensions.fx
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flowOf
import org.studio.ghibli.base.BaseViewModel
import org.studio.ghibli.base.SHOULD_NOT_HAPPEN
import org.studio.ghibli.films.Films
import org.studio.ghibli.home.effects.Action
import org.studio.ghibli.location.Location
import org.studio.ghibli.people.People
import org.studio.ghibli.species.Species
import org.studio.ghibli.vehicle.Vehicle
import org.studio.side_effects.Reaction
import org.studio.side_effects.SideEffect

class HomeViewModel constructor(private val application: Application) : BaseViewModel() {

    @ExperimentalCoroutinesApi
    @FlowPreview
    fun perform(action: Action): IO<Flow<Any>> = IO.fx {

        val actionSideEffects: Flow<SideEffect> = effect {
            action.sideEffects.asFlow()
        }.bind()

        val reaction: Reaction<Intent> = effect {
            when(action){
                is Action.Films -> Reaction.Success(Intent(application, Films::class.java))
                is Action.Location -> Reaction.Success(Intent(application, Location::class.java))
                is Action.People -> Reaction.Success(Intent(application, People::class.java))
                is Action.Species -> Reaction.Success(Intent(application, Species::class.java))
                is Action.Vehicle -> Reaction.Success(Intent(application, Vehicle::class.java))
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