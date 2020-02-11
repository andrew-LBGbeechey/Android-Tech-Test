package org.studio.ghibli.model

data class FilmUI(
    val id: String,
    val title: String,
    val description: String,
    val director: String,
    val producer: String,
    val releaseDate: Int,
    val rottenTomatoesScore: Int,
    val people: List<String>,
    val species: List<String>,
    val locations: List<String>,
    val vehicles: List<String>,
    val url: String
)