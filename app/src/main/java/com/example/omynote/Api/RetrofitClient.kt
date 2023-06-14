package com.example.omynote.Api

import com.example.omynote.Commons.ApiConstants
import com.google.gson.GsonBuilder
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitClient {
    private lateinit var apiService: APIService;
    val mediaType = "application/json".toMediaType()
    val body = "{\"id\": 5148,\"selected_notes\": [272,244,43,28,459,562], \"added_notes\": [], \"latitude\": \"23.03\", \"longitude\": \"72.51\", \"mode\": [\"male\"], \"saved_recipe\": 1, \"recipe_name\": \"\"}".toRequestBody(mediaType)
    fun getApiResponse(baseUrl:String): APIService {


        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BASIC
        val client = OkHttpClient.Builder().addInterceptor(interceptor)
            .connectTimeout(2, TimeUnit.MINUTES)
            .readTimeout(100, TimeUnit.SECONDS)
            .writeTimeout(100, TimeUnit.SECONDS)
            .build()
        val gson = GsonBuilder()
            .setLenient()
            .create()
        apiService = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build().create(APIService::class.java)

        return apiService;
    }

  /*  fun getApiResponse1(baseUrl:String): APIService {


        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BASIC
        val client = OkHttpClient
        val gson = GsonBuilder()
            .setLenient()
            .create()
        val apiService = Retrofit.Builder()
            .baseUrl(baseUrl)

            .addHeader("Authorization", "Token 2dade523b354242b3dbbfc6e1107dadfa3e5c7a4")
            .addHeader("Content-Type", "application/json")
            .addHeader("Accept", "application/json, text/plain")
            .addHeader("Cookie", "sessionid=k305g4aafbge7ijyoh8nbq4y64bee453")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()

        return apiService;
    }*/
}