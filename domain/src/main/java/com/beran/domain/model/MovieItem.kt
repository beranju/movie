package com.beran.domain.model

data class MovieItem(
    val overview: String? = null,
    val originalTitle: String? = null,
    val title: String? = null,
    val genreIds: List<Int?>? = null,
    val posterPath: String? = null,
    val backdropPath: String? = null,
    val releaseDate: String? = null,
    val popularity: Any? = null,
    val voteAverage: Any? = null,
    val id: Int? = null,
    val voteCount: Int? = null
)
