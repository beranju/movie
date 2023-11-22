package com.beran.data.repository

import com.beran.data.local.pref.SessionManager
import com.beran.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(private val sessionManager: SessionManager) :
    AuthRepository {
    override fun isLogin(): Flow<Boolean> = sessionManager.isLogin()

    override suspend fun createSession() = sessionManager.createSession()

    override fun getSavedData(keyName: String): Flow<String> = sessionManager.getSavedData(keyName)

    override suspend fun saveData(keyName: String, value: String) {
        sessionManager.saveData(keyName, value)
    }

    override suspend fun logOut() = sessionManager.logout()
}