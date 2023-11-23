package com.beran.data.network.retrofit

import com.beran.data.BuildConfig
import com.beran.data.network.model.ImgBbResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ImgBbApi {
    @POST("upload")
    @Multipart
    suspend fun uploadPhoto(
        @Query("key") apiKey: String = BuildConfig.IMG_BB_API_KEY,
        @Part image: MultipartBody.Part
    ): Response<ImgBbResponse>
}