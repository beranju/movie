package com.beran.data.utils

import com.beran.data.network.model.ResultsItem
import com.beran.domain.model.MovieItem

object DataDummy {

    fun generateMovies(): List<ResultsItem> {
        val movies: MutableList<ResultsItem> = mutableListOf()
        for (i in 0..10) {
            val movie = ResultsItem(
                id = i
            )
            movies.add(movie)
        }
        return movies
    }
}