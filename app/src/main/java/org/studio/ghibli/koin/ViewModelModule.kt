package org.studio.ghibli.koin

import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.studio.ghibli.film.FilmViewModel
import org.studio.ghibli.films.FilmsViewModel
import org.studio.ghibli.home.HomeViewModel
import org.studio.ghibli.people.PeopleViewModel

const val HomeViewModelName: String = "home-view-model"
const val FilmsViewModelName: String = "films-view-model"
const val FilmViewModelName: String = "film-view-model"
const val PeopleViewModelName: String = "people-view-model"

val viewModelModule = module {

    viewModel(qualifier = named(HomeViewModelName)) {
        HomeViewModel(androidApplication())
    }

    viewModel(qualifier = named(FilmsViewModelName)) {
        FilmsViewModel()
    }

    viewModel(qualifier = named(FilmViewModelName)) {
        FilmViewModel()
    }

    viewModel(qualifier = named(PeopleViewModelName)) {
        PeopleViewModel()
    }
}