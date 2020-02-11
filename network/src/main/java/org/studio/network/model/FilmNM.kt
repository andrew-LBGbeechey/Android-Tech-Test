package org.studio.network.model

import com.google.gson.annotations.SerializedName

data class FilmNM(
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("director") val director: String,
    @SerializedName("producer") val producer: String,
    @SerializedName("release_date") val releaseDate: Int,
    @SerializedName("rt_score") val rtScore: Int,
    @SerializedName("people") val people: List<String>,
    @SerializedName("species") val species: List<String>,
    @SerializedName("locations") val locations: List<String>,
    @SerializedName("vehicles") val vehicles: List<String>,
    @SerializedName("url") val url: String
)