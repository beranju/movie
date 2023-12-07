package com.beran.data.utils

import com.beran.domain.model.MovieDetail
import com.beran.domain.model.MovieItem

object DataDummy {

    fun generateMovies(): List<MovieItem> {
        val movies: MutableList<MovieItem> = mutableListOf()
        for (i in 0..10) {
            val movie = MovieItem(
                id = i,
                overview = i.toString(),
                originalTitle = i.toString(),
                title = i.toString(),
                genreIds = listOf(i),
                posterPath = i.toString(),
                backdropPath = i.toString(),
                releaseDate = i.toString(),
                popularity = i.toDouble(),
                voteAverage = i.toDouble(),
                voteCount = i

            )
            movies.add(movie)
        }
        return movies
    }

    fun generateMovieDetail() =
        MovieDetail(
            id = 1,
            originalTitle = "",
            title = "",
            backdropPath = "",
            revenue = 1,
            genres = "",
            popularity = 0.0,
            voteCount = 1,
            budget = 1,
            overview = "",
            originalLanguage = "",
            posterPath = "",
            releaseDate = "",
            voteAverage = 0.0,
            tagline = "",
            homepage = "",
            status = ""
        )
}