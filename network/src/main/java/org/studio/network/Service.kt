package org.studio.network

import org.studio.network.model.*
import retrofit2.Response
import retrofit2.Retrofit

class Service(retrofit: Retrofit) : Api {

    private val service: Api = retrofit.create(Api::class.java)

    override suspend fun films(limit: Int): Response<List<FilmNM>> {
        return service.films(limit)
    }

    override suspend fun people(limit: Int): Response<List<PeopleNM>> {
        return service.people(limit)
    }

    override suspend fun locations(limit: Int): Response<List<LocationNM>> {
        return service.locations(limit)
    }

    override suspend fun species(limit: Int): Response<List<SpeciesNM>> {
        return service.species(limit)
    }

    override suspend fun vehicles(limit: Int): Response<List<VehicleNM>> {
        return service.vehicles(limit)
    }
}