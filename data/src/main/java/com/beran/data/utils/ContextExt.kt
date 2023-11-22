package com.beran.data.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.beran.common.Constants.PREF_NAME

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(PREF_NAME)