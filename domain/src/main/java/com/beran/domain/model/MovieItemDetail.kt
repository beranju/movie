package com.beran.domain.model

data class MovieDetail(
    val originalLanguage: String? = null,
    val title: String? = null,
    val backdropPath: String? = null,
    val revenue: Int? = null,
    val genres: List<GenresMovie?>? = null,
    val popularity: Any? = null,
    val id: Int? = null,
    val voteCount: Int? = null,
    val budget: Int? = null,
    val overview: String? = null,
    val originalTitle: String? = null,
    val posterPath: String? = null,
    val releaseDate: String? = null,
    val voteAverage: Any? = null,
    val tagline: String? = null,
    val homepage: String? = null,
    val status: String? = null
)

data class GenresMovie(
    val name: String? = null,
    val id: Int? = null
)
