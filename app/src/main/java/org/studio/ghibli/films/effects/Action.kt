package org.studio.ghibli.films.effects

import org.studio.side_effects.SideEffect

private val commonSideEffects: MutableList<SideEffect> =
    mutableListOf(SideEffect.UiSideEffect.Busy)

sealed class Action(open val sideEffects: MutableList<SideEffect>) {

    data class FetchFilms(override val sideEffects: MutableList<SideEffect> = commonSideEffects.toMutableList()) :
        Action(sideEffects) {
        init {
            sideEffects.add(SideEffect.Logging.Log("Fetching Films"))
        }
    }

    object NO_OP : Action(mutableListOf())
}