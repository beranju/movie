package com.beran.data.local.pref

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.beran.common.Constants.ADDRESS_KEY
import com.beran.common.Constants.BIRTHDAY_KEY
import com.beran.common.Constants.LOGIN_KEY
import com.beran.common.Constants.NAME_KEY
import com.beran.common.Constants.USERNAME_KEY
import kotlinx.coroutines.flow.map


class SessionManager(private val dataStore: DataStore<Preferences>) {

    companion object {
        val KEY_LOGIN = booleanPreferencesKey(LOGIN_KEY)
        val KEY_USERNAME = stringPreferencesKey(USERNAME_KEY)
        val KEY_NAME = stringPreferencesKey(NAME_KEY)
        val KEY_BIRTHDAY = stringPreferencesKey(BIRTHDAY_KEY)
        val KEY_ADDRESS = stringPreferencesKey(ADDRESS_KEY)


        @Volatile
        private var instance: SessionManager? = null
        fun getInstance(dataStore: DataStore<Preferences>): SessionManager =
            instance ?: synchronized(this) {
                instance ?: SessionManager(dataStore)
            }.also { instance = it }
    }

    fun isLogin() = dataStore.data.map {
        it[KEY_LOGIN] ?: false
    }

    fun getSavedData(keyName: String) = dataStore.data.map {
        it[stringPreferencesKey(keyName)] ?: ""
    }

    suspend fun createSession() {
        dataStore.edit {
            it[KEY_LOGIN] = true
        }
    }

    suspend fun saveData(keyName: String, value: String) = dataStore.edit {
        it[stringPreferencesKey(keyName)] = value
    }

    suspend fun logout() {
        dataStore.edit {
            it.clear()
        }
    }

}