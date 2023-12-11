package com.beran.data.network.utils

import android.util.Log
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import java.io.IOException

abstract class SafeApiRequest {
    suspend fun <T : Any> safeApiRequest(call: suspend () -> Response<T>): T {
        try{
            val response = call.invoke()
            if (response.isSuccessful) {
                return response.body()!!
            } else {
                val respErr = response.errorBody()?.string()
                val message = StringBuilder()
                respErr?.let {
                    try {
                        message.append(JSONObject(it).getString("error"))
                    } catch (_: JSONException) {
                        message.append("Error parsing response body")
                    }
                }
                Log.e("TAG", "safeApiRequest: $message")
                throw ApiException(message.toString())
            }
        }catch (_: IOException){
            throw NoInternetException("Check you internet connection")
        }
    }
}