package com.beran.domain.repository

import com.beran.domain.model.UserData
import kotlinx.coroutines.flow.Flow

interface AuthRepository{
    fun isLogin(): Flow<Boolean>
    suspend fun createSession()

    fun getSavedData(): Flow<UserData>

    suspend fun saveUser(userData: UserData)

    suspend fun logOut()
}