package org.studio.side_effects

sealed class Reaction<out T : Any>(open val sideEffects: List<SideEffect>) {

    data class Success<out T : Any>(
        val value: T,
        override val sideEffects: List<SideEffect> = listOf(SideEffect.UiSideEffect.Idle, SideEffect.UiSideEffect.Enable)
    ) : Reaction<T>(sideEffects)

    data class Error(
        val message: String,
        val throwable: Throwable? = null,
        override val sideEffects: List<SideEffect> = listOf(SideEffect.UiSideEffect.Idle, SideEffect.UiSideEffect.Enable)
    ) : Reaction<Nothing>(sideEffects)
}