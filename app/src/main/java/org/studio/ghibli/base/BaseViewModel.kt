package org.studio.ghibli.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flattenMerge
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.studio.database.StudioGhibliDatabase

abstract class BaseViewModel : ViewModel() , KoinComponent{

    private val dispatcher = Dispatchers.Main
    internal val database: StudioGhibliDatabase by inject()

    @ExperimentalCoroutinesApi
    @FlowPreview
    internal fun merge(vararg flows: Flow<Any>): Flow<Any> =
        flowOf(*flows).flattenMerge().flowOn(dispatcher)
}

fun SHOULD_NOT_HAPPEN(): Nothing = throw ShouldNotHappenException()