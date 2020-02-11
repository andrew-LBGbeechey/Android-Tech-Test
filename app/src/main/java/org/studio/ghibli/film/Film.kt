package org.studio.ghibli.film

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import arrow.core.Either
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.qualifier.named
import org.studio.ghibli.R
import org.studio.ghibli.background.WorkerManager
import org.studio.ghibli.base.BaseActivity
import org.studio.ghibli.film.effects.Action
import org.studio.ghibli.films.Films.Companion.EXTRA_FILM_ID
import org.studio.ghibli.koin.FilmViewModelName
import org.studio.ghibli.model.FilmUI
import org.studio.side_effects.Reaction
import org.studio.side_effects.SideEffect
import timber.log.Timber

@ExperimentalCoroutinesApi
@FlowPreview
class Film : BaseActivity<Action, Flow<Any>>(), Observer<FilmUI> {

    private val workManager: WorkerManager by inject()
    private val viewModel: FilmViewModel by viewModel(qualifier = named(FilmViewModelName))

    private lateinit var filmTitle : TextView
    private lateinit var filmDirector : TextView
    private lateinit var filmProducer : TextView
    private lateinit var filmDescription : TextView
    private lateinit var filmReleaseDate : TextView
    private lateinit var filmRottenTomatoesScore : TextView
    private lateinit var filmPeople : TextView
    private lateinit var filmSpecies : TextView
    private lateinit var filmLocations : TextView
    private lateinit var filmVehicles : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_film)

        manageCommonViews()

        filmTitle = findViewById(R.id.film_title)
        filmDirector = findViewById(R.id.film_director)
        filmProducer = findViewById(R.id.film_producer)
        filmDescription = findViewById(R.id.film_description)
        filmReleaseDate = findViewById(R.id.film_release_date)
        filmRottenTomatoesScore = findViewById(R.id.rotten_tomatoes_score)
        filmPeople = findViewById(R.id.film_people)
        filmSpecies = findViewById(R.id.film_species)
        filmLocations = findViewById(R.id.film_locations)
        filmVehicles = findViewById(R.id.film_vehicles)

        val filmId: String = intent.getStringExtra(EXTRA_FILM_ID)!!

        perform(Action.SelectFilm(id = filmId))

        launch {
            workManager.downloadFilms(applicationContext)
        }
    }

    override fun manageCommonViews() {
        toolbar = findViewById(R.id.toolbar)
        anchorView = findViewById(R.id.film_anchor_view)
        progressBar = findViewById(R.id.loadingProgress)

        toolbar?.subtitle = getString(R.string.film)
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
                    is Reaction.Success<*> -> successful(reaction.value as LiveData<FilmUI>)
                    is Reaction.Error -> unsuccessful(reaction)
                }
            }
        }
    }

    private fun successful(film: LiveData<FilmUI>) {
        film.observe(this, this)
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

    override fun onChanged(film: FilmUI) {

        filmTitle.text = film.title
        filmDirector.text = getString(R.string.film_director, film.director)
        filmProducer.text = getString(R.string.film_producer, film.producer)
        filmDescription.text = film.description
        filmReleaseDate.text = getString(R.string.film_release_date, film.releaseDate)
        filmRottenTomatoesScore.text = getString(R.string.film_rotten_tomatoes_score, film.rottenTomatoesScore)
        filmPeople.text = film.people.toString()
        filmSpecies.text = film.species.toString()
        filmLocations.text = film.locations.toString()
        filmVehicles.text = film.vehicles.toString()
    }
}