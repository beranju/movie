package com.beran.domain.repository

import com.beran.data.local.pref.SessionManager
import kotlinx.coroutines.flow.Flow
import java.util.prefs.Preferences
import javax.inject.Inject

interface AuthRepository{
    fun isLogin(): Flow<Boolean>
    suspend fun createSession()

    fun getSavedData(keyName: String): Flow<String>

    suspend fun saveData(keyName: String, value: String)

    suspend fun logOut()
}