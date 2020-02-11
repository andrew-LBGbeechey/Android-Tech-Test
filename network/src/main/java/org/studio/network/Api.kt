package org.studio.network

import org.studio.network.model.*
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {

    @GET("films")
    suspend fun films(@Query("limit") limit: Int): Response<List<FilmNM>>

    @GET("people")
    suspend fun people(@Query("limit") limit: Int): Response<List<PeopleNM>>

    @GET("locations")
    suspend fun locations(@Query("limit") limit: Int): Response<List<LocationNM>>

    @GET("species")
    suspend fun species(@Query("limit") limit: Int): Response<List<SpeciesNM>>

    @GET("vehicles")
    suspend fun vehicles(@Query("limit") limit: Int): Response<List<VehicleNM>>
}