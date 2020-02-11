package org.studio.ghibli.film.effects

import org.studio.side_effects.SideEffect

private val commonSideEffects: MutableList<SideEffect> =
    mutableListOf(SideEffect.UiSideEffect.Busy)

sealed class Action(open val sideEffects: MutableList<SideEffect>) {

    data class SelectFilm(
        val id: String,
        override val sideEffects: MutableList<SideEffect> = commonSideEffects.toMutableList()
    ) : Action(sideEffects) {
        init {
            sideEffects.add(SideEffect.Logging.Log("Select Film"))
        }
    }

    object NO_OP : Action(mutableListOf())
}