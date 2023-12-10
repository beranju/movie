package com.beran.data.repository

import com.beran.data.local.room.MovieDao
import com.beran.data.mappers.movieEntityToMovieItems
import com.beran.data.mappers.toMovieDetail
import com.beran.data.mappers.toMovieEntity
import com.beran.data.mappers.toMovieItems
import com.beran.data.network.retrofit.MovieApi
import com.beran.data.network.utils.SafeApiRequest
import com.beran.domain.model.MovieDetail
import com.beran.domain.model.MovieItem
import com.beran.domain.repository.MovieRepository
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val movieDao: MovieDao,
    private val api: MovieApi
) : MovieRepository, SafeApiRequest() {
    override suspend fun getNowPlayingMovies(): List<MovieItem> {
        val response = safeApiRequest { api.getNowPlayingMovies() }
        return response.results?.mapNotNull { it }?.toMovieItems() ?: emptyList()
    }

    override suspend fun getPopularMovies(): List<MovieItem> {
        val response = safeApiRequest { api.getPopularMovies() }
        return response.results?.mapNotNull { it }?.toMovieItems() ?: emptyList()
    }

    override suspend fun getMovieDetail(id: Int): MovieDetail {
        val response = safeApiRequest { api.getDetailMovie(id) }
        return response.toMovieDetail()
    }

    override fun getFavoriteMovies(): List<MovieItem> =
        try {
            movieDao.getAll().movieEntityToMovieItems()
        } catch (e: Exception) {
            throw e
        }

    override fun getFavoriteMovieById(id: Int): MovieDetail =
        try {
            movieDao.getById(id).toMovieDetail()
        } catch (e: Exception) {
            throw e
        }

    override suspend fun addFavoriteMovie(movieDetail: MovieDetail) {
        val movie = movieDetail.toMovieEntity()
        try {
            movieDao.insert(movie)
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun deleteFavoriteMovie(movieDetail: MovieDetail) {
        val movie = movieDetail.toMovieEntity()
        try {
            movieDao.deleteMovie(movie)
        } catch (e: Exception) {
            throw e
        }
    }
}

