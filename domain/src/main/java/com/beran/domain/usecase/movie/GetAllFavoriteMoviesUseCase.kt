package com.beran.domain.usecase.movie

import com.beran.common.Resource
import com.beran.domain.model.MovieDetail
import com.beran.domain.model.MovieItem
import com.beran.domain.repository.MovieRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetAllFavoriteMoviesUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    operator fun invoke(): Flow<Resource<List<MovieItem>>> = flow {
        emit(Resource.Loading)
        try {
            val result = repository.getFavoriteMovies()
            emit(Resource.Success(result))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        }
    }.flowOn(Dispatchers.IO)
}