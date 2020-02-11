package org.studio.ghibli.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import arrow.core.Either
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.qualifier.named
import org.studio.ghibli.R
import org.studio.ghibli.base.BaseActivity
import org.studio.ghibli.home.effects.Action
import org.studio.ghibli.koin.HomeViewModelName
import org.studio.side_effects.Reaction
import org.studio.side_effects.SideEffect
import timber.log.Timber

@ExperimentalCoroutinesApi
@FlowPreview
class Home : BaseActivity<Action, Flow<Any>>(), View.OnClickListener {

    private val viewModel: HomeViewModel by viewModel(qualifier = named(HomeViewModelName))
    private val buttons: MutableList<Button> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        manageCommonViews()

        buttons.add(findViewById(R.id.films_button))
        buttons.add(findViewById(R.id.locations_button))
        buttons.add(findViewById(R.id.people_button))
        buttons.add(findViewById(R.id.species_button))
        buttons.add(findViewById(R.id.vehicles_button))

        buttons.forEach { button ->
            button.setOnClickListener(this)
        }
    }

    @FlowPreview
    @ExperimentalCoroutinesApi
    override fun onClick(view: View) {
        val action = when (view.id) {
            R.id.films_button -> Action.Films()
            R.id.locations_button -> Action.Location()
            R.id.people_button -> Action.People()
            R.id.species_button -> Action.Species()
            R.id.vehicles_button -> Action.Vehicle()
            else -> Action.NO_OP
        }
        perform(action)
    }

    @FlowPreview
    @ExperimentalCoroutinesApi
    override fun perform(action: Action) {
        viewModel
            .perform(action)
            .unsafeRunAsync { effect: Either<Throwable, Flow<Any>> ->
                when (effect) {
                    is Either.Left -> unsuccessful(effect.a)
                    is Either.Right -> render(effect.b)
                }
            }
    }

    private fun unsuccessful(error: Reaction.Error) {
        Timber.e(error.message)
        error.throwable?.let { throwable ->
            unsuccessful(throwable)
        }
    }

    override fun render(reactions: Flow<Any>) {
        launch {
            reactions.collect { reaction ->
                when (reaction) {
                    is SideEffect -> sideEffects(reaction)
                    is Reaction.Success<*> -> successful(reaction.value as Intent)
                    is Reaction.Error -> unsuccessful(reaction)
                }
            }
        }
    }

    private fun successful(intent: Intent) {
        startActivity(intent)
    }

    override fun sideEffects(sideEffect: SideEffect) {
        when (sideEffect) {
            SideEffect.UiSideEffect.Busy -> progressBar?.visibility = View.VISIBLE
            SideEffect.UiSideEffect.Idle -> progressBar?.visibility = View.INVISIBLE
            SideEffect.UiSideEffect.Disable -> enableButtons(false)
            SideEffect.UiSideEffect.Enable -> enableButtons(true)
            is SideEffect.Logging.Log -> Timber.d(sideEffect.message)
        }
    }

    private fun enableButtons(enabled: Boolean) {
        buttons.forEach { button ->
            button.isEnabled = enabled
        }
    }


    override fun manageCommonViews() {
        toolbar = findViewById(R.id.toolbar)
        anchorView = findViewById(R.id.home_anchor_view)
        progressBar = findViewById(R.id.loadingProgress)

        toolbar?.title = getString(R.string.app_name)
        setSupportActionBar(toolbar)
    }
}