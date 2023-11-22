package com.beran.domain.di

import com.beran.domain.repository.AuthRepository
import com.beran.domain.repository.MovieRepository
import com.beran.domain.usecase.auth.CreateSessionUseCase
import com.beran.domain.usecase.auth.GetSavedDataUseCase
import com.beran.domain.usecase.auth.SaveDataProfileUseCase
import com.beran.domain.usecase.movie.DetailMovieUseCase
import com.beran.domain.usecase.movie.NowPlayingMovieUseCase
import com.beran.domain.usecase.movie.PopularMovieUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DomainModule {

    @Provides
    @Singleton
    fun provideNowPlayingMovieUseCase(repository: MovieRepository): NowPlayingMovieUseCase =
        NowPlayingMovieUseCase(repository)

    @Provides
    @Singleton
    fun providePopularMovieUseCase(repository: MovieRepository): PopularMovieUseCase =
        PopularMovieUseCase(repository)

    @Provides
    @Singleton
    fun provideDetailMovieUseCase(repository: MovieRepository): DetailMovieUseCase =
        DetailMovieUseCase(repository)

    @Provides
    @Singleton
    fun provideCreateSessionUseCase(repository: AuthRepository): CreateSessionUseCase =
        CreateSessionUseCase(repository)

    @Provides
    @Singleton
    fun provideGetSavedDataUseCase(repository: AuthRepository): GetSavedDataUseCase =
        GetSavedDataUseCase(repository)

    @Provides
    @Singleton
    fun provideSaveDataProfileUseCase(repository: AuthRepository): SaveDataProfileUseCase =
        SaveDataProfileUseCase(repository)

}