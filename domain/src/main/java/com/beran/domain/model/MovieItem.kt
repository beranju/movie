package com.beran.domain.model

data class MovieItem(
    val overview: String,
    val originalTitle: String,
    val title: String,
    val genreIds: List<Int>,
    val posterPath: String,
    val backdropPath: String,
    val releaseDate: String,
    val popularity: Double,
    val voteAverage: Double,
    val id: Int,
    val voteCount: Int
)
