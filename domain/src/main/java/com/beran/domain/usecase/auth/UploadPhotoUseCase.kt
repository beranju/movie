package com.beran.domain.usecase.auth

import android.util.Log
import com.beran.common.Resource
import com.beran.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File
import javax.inject.Inject

class UploadPhotoUseCase @Inject constructor(private val repository: AuthRepository) {
    operator fun invoke(file: File): Flow<Resource<String>> = flow {
        emit(Resource.Loading)
        try {
            val result = repository.uploadPhoto(file)
            Log.d("UploadPhotoUseCase", "result: $result")
            emit(Resource.Success(result))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        }
    }
}