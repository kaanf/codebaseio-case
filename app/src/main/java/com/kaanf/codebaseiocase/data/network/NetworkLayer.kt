package com.kaanf.codebaseiocase.data.network

import com.kaanf.codebaseiocase.utils.AppConstants.Companion.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkLayer {
    private val gsonConverterFactory  = GsonConverterFactory.create()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(gsonConverterFactory)
        .build()

    private val networkService by lazy {
        retrofit.create(NetworkService::class.java)
    }

    val apiClient = APIClient(networkService = networkService)
}