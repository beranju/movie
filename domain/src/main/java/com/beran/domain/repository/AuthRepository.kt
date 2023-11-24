package com.beran.domain.repository

import android.net.Uri
import com.beran.domain.model.UserData
import kotlinx.coroutines.flow.Flow
import java.io.File

interface AuthRepository{
    fun isLogin(): Flow<Boolean>
    suspend fun createSession()

    fun getSavedData(): Flow<UserData>

    suspend fun saveUser(userData: UserData)
    suspend fun updateUserImage(url: String)

    suspend fun uploadPhoto(file: File): String

    suspend fun logOut()
}