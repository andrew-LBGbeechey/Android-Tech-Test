package org.studio.ghibli.model

data class PeopleUI(
    val id: String,
    val name: String,
    val gender: String,
    val age: String,
    val eyeColor: String,
    val hairColor: String,
    val films: List<String>,
    val species: String,
    val url: String
)