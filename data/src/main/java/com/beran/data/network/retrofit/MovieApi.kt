package com.beran.data.network.retrofit

import com.beran.data.network.model.MovieDetailResponse
import com.beran.data.network.model.NowPlayingResponse
import com.beran.data.network.model.PopularResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface MovieApi {
    @GET("movie/now_playing")
    suspend fun getNowPlayingMovies(): Response<NowPlayingResponse>

    @GET("movie/popular")
    suspend fun getPopularMovies(): Response<PopularResponse>

    @GET("movie/{movie_id}")
    suspend fun getDetailMovie(@Path("movie_id") movieId: Int): Response<MovieDetailResponse>
}