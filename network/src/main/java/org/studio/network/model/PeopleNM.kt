package org.studio.network.model

import com.google.gson.annotations.SerializedName

data class PeopleNM(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("gender") val gender: String,
    @SerializedName("age") val age: String,
    @SerializedName("eye_color") val eyeColor: String,
    @SerializedName("hair_color") val hairColor: String,
    @SerializedName("films") val films: List<String>,
    @SerializedName("species") val species: String,
    @SerializedName("url") val url: String
)