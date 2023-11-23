package com.beran.data.repository

import com.beran.data.local.pref.SessionManager
import com.beran.data.network.retrofit.ImgBbApi
import com.beran.data.network.utils.SafeApiRequest
import com.beran.domain.model.UserData
import com.beran.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val sessionManager: SessionManager,
    private val api: ImgBbApi
) :
    AuthRepository, SafeApiRequest() {
    override fun isLogin(): Flow<Boolean> = sessionManager.isLogin()

    override suspend fun createSession() = sessionManager.createSession()

    override fun getSavedData(): Flow<UserData> = sessionManager.getUser()

    override suspend fun saveUser(userData: UserData) {
        sessionManager.saveUser(userData)
    }

    override suspend fun uploadPhoto(file: File): String {
        val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData(
            "image",
            file.name,
            requestFile
        )
        val response = safeApiRequest { api.uploadPhoto(image = body) }
        return response.data?.url.orEmpty()
    }

    override suspend fun logOut() = sessionManager.logout()
}