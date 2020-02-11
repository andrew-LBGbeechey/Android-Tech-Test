package org.studio.ghibli.films

import arrow.core.Either
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test
import org.studio.ghibli.base.BaseTest
import org.studio.ghibli.films.effects.Action

class FilmsViewModelTest: BaseTest() {

    @FlowPreview
    @ExperimentalCoroutinesApi
    @Test
    fun testViewModel() {
        val viewModel = FilmsViewModel()
        runBlocking {
            viewModel
                .perform(Action.FetchFilms())
                .unsafeRunAsync { effect: Either<Throwable, Flow<Any>> ->
                    when (effect) {
                        is Either.Left -> throw effect.a
                        is Either.Right -> Assert.assertNotNull(effect.b)
                    }
                }

        }
    }
}