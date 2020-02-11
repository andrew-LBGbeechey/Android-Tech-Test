package org.studio.side_effects

sealed class SideEffect {
    sealed class UiSideEffect : SideEffect() {

        object Error : UiSideEffect()

        object Busy : UiSideEffect()
        object Idle : UiSideEffect()

        object Disable : UiSideEffect()
        object Enable : UiSideEffect()
    }

    sealed class Logging(open val message: String) : SideEffect() {
        data class Log(override val message: String) : Logging(message)
    }

}