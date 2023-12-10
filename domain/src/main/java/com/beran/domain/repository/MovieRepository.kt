package com.beran.domain.repository

import com.beran.domain.model.MovieDetail
import com.beran.domain.model.MovieItem

interface MovieRepository {
    suspend fun getNowPlayingMovies(): List<MovieItem>
    suspend fun getPopularMovies(): List<MovieItem>
    suspend fun getMovieDetail(id: Int): MovieDetail
    fun getFavoriteMovies(): List<MovieItem>
    fun getFavoriteMovieById(id: Int): MovieDetail
    suspend fun addFavoriteMovie(movieDetail: MovieDetail)
    suspend fun deleteFavoriteMovie(movieDetail: MovieDetail)
}