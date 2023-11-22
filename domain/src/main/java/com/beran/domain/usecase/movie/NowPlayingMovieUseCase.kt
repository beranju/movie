package com.beran.domain.usecase.movie

import com.beran.common.Resource
import com.beran.data.network.utils.ApiException
import com.beran.domain.model.MovieItem
import com.beran.domain.repository.MovieRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class NowPlayingMovieUseCase @Inject constructor(private val repository: MovieRepository) {
    operator fun invoke(): Flow<Resource<List<MovieItem>>> = flow {
        emit(Resource.Loading)
        try {
            val response = repository.getNowPlayingMovies()
            emit(Resource.Success(response))
        } catch (e: ApiException) {
            emit(Resource.Error("An error when fetching data"))
        } catch (e: Exception) {
            emit(Resource.Error("An unexpected error occurred"))
        }
    }.flowOn(Dispatchers.IO)
}