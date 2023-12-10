package com.beran.data.repository.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.beran.data.local.pref.SessionManager
import com.beran.data.local.room.MovieDao
import com.beran.data.network.retrofit.ImgBbApi
import com.beran.data.network.retrofit.MovieApi
import com.beran.data.repository.AuthRepositoryImpl
import com.beran.data.repository.MovieRepositoryImpl
import com.beran.data.utils.dataStore
import com.beran.domain.repository.AuthRepository
import com.beran.domain.repository.MovieRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> =
        context.dataStore

    @Provides
    @Singleton
    fun provideSessionManager(dataStore: DataStore<Preferences>): SessionManager =
        SessionManager(dataStore)

    @Provides
    @Singleton
    fun provideAuthRepository(sessionManager: SessionManager, api: ImgBbApi): AuthRepository =
        AuthRepositoryImpl(sessionManager, api)

    @Singleton
    @Provides
    fun provideMovieRepository(movieDao: MovieDao, api: MovieApi): MovieRepository =
        MovieRepositoryImpl(movieDao, api)

}