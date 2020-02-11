package org.studio.ghibli.base

import android.annotation.SuppressLint
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import org.studio.ghibli.background.BaseWorkerManager
import org.studio.ghibli.background.BaseWorkerManager.Companion.isActiveWorkers
import org.studio.ghibli.background.state.State
import org.studio.side_effects.SideEffect
import timber.log.Timber

@FlowPreview
@ExperimentalCoroutinesApi
@SuppressLint("Registered")
abstract class BaseActivity<A, R> : AppCompatActivity(), CoroutineScope by MainScope() {

    internal lateinit var anchorView : View

    internal var toolbar: Toolbar? = null
    internal var progressBar: ProgressBar? = null

    init {
        lifecycleScope.launchWhenStarted {
            BaseWorkerManager
                .workerStateFlow
                .distinctUntilChanged()
                .collectLatest { state ->
                    delay(LATEST_STATE_DELAY)
                    consumeWorkerState(state)
                }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return false
    }

    internal fun unsuccessful(throwable: Throwable) {
        Timber.e(throwable)
        showError(anchorView, org.studio.ghibli.R.string.something_went_wrong)
    }

    fun showError(view: View?, stringId: Int) {
        val toast = Toast.makeText(this, stringId, Toast.LENGTH_LONG)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()

        if (view == null) {
            return
        }

        Snackbar.make(view, stringId, Snackbar.LENGTH_SHORT).show()
    }

    private fun consumeWorkerState(workerState: State) {
        when (workerState) {
            is State.Dormant -> {
                sideEffects(SideEffect.UiSideEffect.Idle)
                if (isActiveWorkers()) sideEffects(SideEffect.UiSideEffect.Busy)
            }
            is State.Blocked -> sideEffects(SideEffect.UiSideEffect.Busy)
            is State.Cancelled -> sideEffects(SideEffect.UiSideEffect.Idle)
            is State.Enqueued -> sideEffects(SideEffect.UiSideEffect.Busy)
            is State.Failed -> sideEffects(SideEffect.UiSideEffect.Error)
            is State.Running -> sideEffects(SideEffect.UiSideEffect.Busy)
            is State.Succeeded -> sideEffects(SideEffect.UiSideEffect.Idle)
        }
    }

    abstract fun manageCommonViews()
    abstract fun perform(action: A)
    abstract fun render(reactions: R)
    abstract fun sideEffects(sideEffect: SideEffect)

    companion object {
        private const val LATEST_STATE_DELAY: Long = 3
    }
}

