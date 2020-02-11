package org.studio.ghibli.people

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
import org.studio.ghibli.koin.PeopleViewModelName
import org.studio.ghibli.model.PeopleUI
import org.studio.ghibli.people.effects.Action
import org.studio.side_effects.Reaction
import org.studio.side_effects.SideEffect
import timber.log.Timber

@ExperimentalCoroutinesApi
@FlowPreview
class People : BaseActivity<Action, Flow<Any>>(), Observer<PagedList<PeopleUI>>,
    PeoplePagedListAdapter.PeopleListener {

    private val workManager: WorkerManager by inject()

    private val viewModel: PeopleViewModel by viewModel(qualifier = named(PeopleViewModelName))

    private lateinit var adapter: PeoplePagedListAdapter
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mLayoutManager: LinearLayoutManager
    private lateinit var mLayoutManagerState: Parcelable

    private val LAYOUT_MANAGER_STATE = "LAYOUT_MANAGER_STATE"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_people)

        manageCommonViews()

        adapter = PeoplePagedListAdapter(this@People)
        adapter.setHasStableIds(true)

        mRecyclerView = findViewById(R.id.people_rv)
        mRecyclerView.adapter = adapter

        mLayoutManager = LinearLayoutManager(this)
        mRecyclerView.layoutManager = mLayoutManager

        launch {
            workManager.downloadPeople(applicationContext)
        }
    }

    override fun manageCommonViews() {
        toolbar = findViewById(R.id.toolbar)
        anchorView = findViewById(R.id.people_anchor_view)
        progressBar = findViewById(R.id.loadingProgress)

        toolbar?.subtitle = getString(R.string.people)
        toolbar?.setNavigationIcon(R.drawable.ic_up_arrow_back_24)
        setSupportActionBar(toolbar)
    }

    override fun onResume() {
        super.onResume()
        perform(Action.FetchPeople())
    }

    override fun onSaveInstanceState(state: Bundle) {
        super.onSaveInstanceState(state)
        state.putParcelable(LAYOUT_MANAGER_STATE, mLayoutManager.onSaveInstanceState()!!)
    }

    override fun onRestoreInstanceState(state: Bundle) {
        super.onRestoreInstanceState(state)
        mLayoutManagerState = state.getParcelable(LAYOUT_MANAGER_STATE)!!
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
                    is Reaction.Success<*> -> successful(reaction.value as LiveData<PagedList<PeopleUI>>)
                    is Reaction.Error -> unsuccessful(reaction)
                }
            }
        }
    }

    private fun successful(people: LiveData<PagedList<PeopleUI>>) {
        people.observe(this, this)
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

    override fun onChanged(people: PagedList<PeopleUI>) {
        adapter.submitList(people)

        launch {
            delay(500)
            if (::mLayoutManagerState.isInitialized) {
                mLayoutManager.onRestoreInstanceState(mLayoutManagerState)
            }
        }
    }

    override fun onPeopleClicked(people: PeopleUI) {
        Timber.d("People CLICKED $people")
    }
}