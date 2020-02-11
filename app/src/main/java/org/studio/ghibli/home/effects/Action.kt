package org.studio.ghibli.home.effects

import org.studio.side_effects.SideEffect

private val commonSideEffects: MutableList<SideEffect> =
    mutableListOf(SideEffect.UiSideEffect.Disable)

sealed class Action(open val sideEffects: MutableList<SideEffect>) {

    data class Films(override val sideEffects: MutableList<SideEffect> = commonSideEffects.toMutableList()) :
        Action(sideEffects) {
        init {
            sideEffects.add(SideEffect.Logging.Log("Films Action Clicked"))
        }
    }

    data class Location(override val sideEffects: MutableList<SideEffect> = commonSideEffects.toMutableList()) :
        Action(sideEffects) {
        init {
            sideEffects.add(SideEffect.Logging.Log("Location Action Clicked"))
        }
    }

    data class People(override val sideEffects: MutableList<SideEffect> = commonSideEffects.toMutableList()) :
        Action(sideEffects) {
        init {
            sideEffects.add(SideEffect.Logging.Log("People Action Clicked"))
        }
    }

    data class Species(override val sideEffects: MutableList<SideEffect> = commonSideEffects.toMutableList()) :
        Action(sideEffects) {
        init {
            sideEffects.add(SideEffect.Logging.Log("Species Action Clicked"))
        }
    }

    data class Vehicle(override val sideEffects: MutableList<SideEffect> = commonSideEffects.toMutableList()) :
        Action(sideEffects) {
        init {
            sideEffects.add(SideEffect.Logging.Log("Vehicle Action Clicked"))
        }
    }

    object NO_OP : Action(mutableListOf())
}