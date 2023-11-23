package com.beran.data.network.di

import com.beran.common.Constants
import com.beran.data.BuildConfig
import com.beran.data.network.retrofit.ImgBbApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ImgBbApiModule {

    @Provides
    @Singleton
    @Named("imgBbLogging")
    fun providesLoggingInterceptor(): Interceptor =
        HttpLoggingInterceptor().setLevel(if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE)

    @Provides
    @Singleton
    @Named("imgBbClient")
    fun provideClient(
        @Named("imgBbLogging") loggingInterceptor: Interceptor,
    ): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .build()

    @Provides
    @Singleton
    @Named("imgBbRetrofit")
    fun provideRetrofit(@Named("imgBbClient") client: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(Constants.IMG_BB_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

    @Provides
    @Singleton
    fun provideImgBbService(@Named("imgBbRetrofit") retrofit: Retrofit): ImgBbApi =
        retrofit.create(ImgBbApi::class.java)

}