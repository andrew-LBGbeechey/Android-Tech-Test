package org.studio.ghibli.background.state

import org.studio.ghibli.background.BaseWorkerManager

sealed class State {
    object Dormant : State()
    data class Blocked(val manager: BaseWorkerManager) : State()
    data class Cancelled(val manager: BaseWorkerManager) : State()
    data class Enqueued(val manager: BaseWorkerManager) : State()
    data class Failed(val manager: BaseWorkerManager) : State()
    data class Running(val manager: BaseWorkerManager) : State()
    data class Succeeded(val manager: BaseWorkerManager) : State()
}