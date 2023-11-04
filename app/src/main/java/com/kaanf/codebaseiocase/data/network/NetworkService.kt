package com.kaanf.codebaseiocase.data.network

import com.kaanf.codebaseiocase.data.model.AdList
import retrofit2.Response
import retrofit2.http.GET

interface NetworkService {
    @GET("android-test-case.json")
    suspend fun getAds(): Response<AdList>
}