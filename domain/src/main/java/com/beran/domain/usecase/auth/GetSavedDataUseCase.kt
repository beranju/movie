package com.beran.domain.usecase.auth

import com.beran.common.Resource
import com.beran.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetSavedDataUseCase @Inject constructor(private val repository: AuthRepository) {
    operator fun invoke(keyName: String): Flow<Resource<String>> = flow {
        emit(Resource.Loading)
        try {
            val value = repository.getSavedData(keyName).first()
            emit(Resource.Success(value))
        } catch (e: Exception) {
            emit(Resource.Error("An unexpected error occurred"))
        }
    }
}