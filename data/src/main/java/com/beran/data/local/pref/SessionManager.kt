package com.beran.data.local.pref

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.beran.common.Constants.ADDRESS_KEY
import com.beran.common.Constants.BIRTHDAY_KEY
import com.beran.common.Constants.IMAGE_KEY
import com.beran.common.Constants.LOGIN_KEY
import com.beran.common.Constants.NAME_KEY
import com.beran.common.Constants.USERNAME_KEY
import com.beran.domain.model.UserData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class SessionManager(private val dataStore: DataStore<Preferences>) {

    companion object {
        val KEY_LOGIN = booleanPreferencesKey(LOGIN_KEY)
        val KEY_USERNAME = stringPreferencesKey(USERNAME_KEY)
        val KEY_NAME = stringPreferencesKey(NAME_KEY)
        val KEY_BIRTHDAY = stringPreferencesKey(BIRTHDAY_KEY)
        val KEY_ADDRESS = stringPreferencesKey(ADDRESS_KEY)
        val KEY_IMAGE = stringPreferencesKey(IMAGE_KEY)
    }

    fun isLogin() = dataStore.data.map {
        it[KEY_LOGIN] ?: false
    }

    suspend fun createSession() {
        dataStore.edit {
            it[KEY_LOGIN] = true
        }
    }

    fun getUser(): Flow<UserData> = dataStore.data.map {
        val name = it[KEY_NAME].orEmpty()
        val username = it[KEY_USERNAME].orEmpty()
        val birthDay = it[KEY_BIRTHDAY].orEmpty()
        val address = it[KEY_ADDRESS].orEmpty()
        val image = it[KEY_IMAGE].orEmpty()
        UserData(
            name = name,
            username = username,
            birthDay = birthDay,
            address = address,
            imageUrl = image
        )
    }

    suspend fun saveUser(userData: UserData) = dataStore.edit {
        it[KEY_NAME] = userData.name
        it[KEY_USERNAME] = userData.username
        it[KEY_BIRTHDAY] = userData.birthDay
        it[KEY_ADDRESS] = userData.address
        it[KEY_IMAGE] = userData.imageUrl
    }

    suspend fun logout() {
        dataStore.edit {
            it.clear()
        }
    }

}