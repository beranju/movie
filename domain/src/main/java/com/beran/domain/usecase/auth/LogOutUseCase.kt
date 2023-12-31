package com.beran.domain.usecase.auth

import com.beran.common.Resource
import com.beran.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LogOutUseCase @Inject constructor(private val repository: AuthRepository) {
    operator fun invoke(): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading)
        try {
            val result = repository.logOut()
            emit(Resource.Success(result))
        } catch (e: Exception) {
            emit(Resource.Error("An unexpected error occurred"))
        }
    }
}