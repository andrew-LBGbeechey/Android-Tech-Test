package org.studio.ghibli.films

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import arrow.core.Either
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.qualifier.named
import org.studio.ghibli.R
import org.studio.ghibli.background.WorkerManager
import org.studio.ghibli.base.BaseActivity
import org.studio.ghibli.film.Film
import org.studio.ghibli.films.effects.Action
import org.studio.ghibli.koin.FilmsViewModelName
import org.studio.ghibli.model.FilmUI
import org.studio.side_effects.Reaction
import org.studio.side_effects.SideEffect
import timber.log.Timber

@ExperimentalCoroutinesApi
@FlowPreview
class Films : BaseActivity<Action, Flow<Any>>(), Observer<PagedList<FilmUI>>,
    FilmPagedListAdapter.FilmsListener {

    private val workManager: WorkerManager by inject()

    private val viewModel: FilmsViewModel by viewModel(qualifier = named(FilmsViewModelName))

    private lateinit var adapter: FilmPagedListAdapter
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mLayoutManager: LinearLayoutManager
    private lateinit var mLayoutManagerState: Parcelable

    private val LAYOUT_MANAGER_STATE = "LAYOUT_MANAGER_STATE"

    @ExperimentalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_films)

        manageCommonViews()

        adapter = FilmPagedListAdapter(this@Films)
        adapter.setHasStableIds(true)

        mRecyclerView = findViewById(R.id.films_rv)
        mRecyclerView.adapter = adapter

        mLayoutManager = LinearLayoutManager(this)
        mRecyclerView.layoutManager = mLayoutManager

        launch {
            workManager.downloadFilms(applicationContext)
        }
    }

    override fun onResume() {
        super.onResume()
        perform(Action.FetchFilms())
    }

    override fun onSaveInstanceState(state: Bundle) {
        super.onSaveInstanceState(state)
        state.putParcelable(LAYOUT_MANAGER_STATE, mLayoutManager.onSaveInstanceState()!!)
    }

    override fun onRestoreInstanceState(state: Bundle) {
        super.onRestoreInstanceState(state)
        mLayoutManagerState = state.getParcelable(LAYOUT_MANAGER_STATE)!!
    }

    override fun manageCommonViews() {
        toolbar = findViewById(R.id.toolbar)
        anchorView = findViewById(R.id.films_anchor_view)
        progressBar = findViewById(R.id.loadingProgress)

        toolbar?.subtitle = getString(R.string.films)
        toolbar?.setNavigationIcon(R.drawable.ic_up_arrow_back_24)
        setSupportActionBar(toolbar)
    }

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

    @Suppress("UNCHECKED_CAST")
    override fun render(reactions: Flow<Any>) {
        launch {
            reactions.collect { reaction ->
                when (reaction) {
                    is SideEffect -> sideEffects(reaction)
                    is Reaction.Success<*> -> successful(reaction.value as LiveData<PagedList<FilmUI>>)
                    is Reaction.Error -> unsuccessful(reaction)
                }
            }
        }
    }

    private fun successful(films: LiveData<PagedList<FilmUI>>) {
        films.observe(this, this)
    }

    private fun unsuccessful(error: Reaction.Error) {
        Timber.e(error.message)
        error.throwable?.let { throwable ->
            unsuccessful(throwable)
        }
    }

    override fun sideEffects(sideEffect: SideEffect) {
        when (sideEffect) {
            SideEffect.UiSideEffect.Busy -> progressBar?.visibility = View.VISIBLE
            SideEffect.UiSideEffect.Idle -> progressBar?.visibility = View.INVISIBLE
            SideEffect.UiSideEffect.Error -> showError(anchorView, R.string.something_went_wrong)
            is SideEffect.Logging.Log -> Timber.d(sideEffect.message)
        }
    }

    override fun onChanged(films: PagedList<FilmUI>) {
        adapter.submitList(films)

        launch {
            delay(500)
            if (::mLayoutManagerState.isInitialized) {
                mLayoutManager.onRestoreInstanceState(mLayoutManagerState)
            }
        }
    }

    override fun onFilmClicked(film: FilmUI) {
        val intent = Intent(this@Films, Film::class.java)
        intent.putExtra(EXTRA_FILM_ID, film.id)
        startActivity(intent)
    }

    companion object {
        const val EXTRA_FILM_ID: String = "EXTRA_FILM_ID"
    }
}