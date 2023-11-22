package com.beran.data.repository

import com.beran.data.mappers.toMovieDetail
import com.beran.data.mappers.toMovieItems
import com.beran.data.network.retrofit.MovieApi
import com.beran.data.network.utils.SafeApiRequest
import com.beran.domain.model.MovieDetail
import com.beran.domain.model.MovieItem
import com.beran.domain.repository.MovieRepository
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
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
}