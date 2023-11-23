package com.beran.domain.usecase.movie

import com.beran.common.Resource
import com.beran.domain.model.MovieDetail
import com.beran.domain.repository.MovieRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class DetailMovieUseCase @Inject constructor(private val repository: MovieRepository) {
    operator fun invoke(id: Int): Flow<Resource<MovieDetail>> = flow {
        emit(Resource.Loading)
        try {
            val response = repository.getMovieDetail(id)
            emit(Resource.Success(response))
        } catch (e: Exception) {
            emit(Resource.Error("An unexpected error occurred"))
        }
    }.flowOn(Dispatchers.IO)
}