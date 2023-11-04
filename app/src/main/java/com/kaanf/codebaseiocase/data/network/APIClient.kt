package com.kaanf.codebaseiocase.data.network

import com.kaanf.codebaseiocase.data.model.AdList
import retrofit2.Response
import javax.inject.Inject

class APIClient @Inject constructor(private val networkService: NetworkService) {
    suspend fun getAds(): Response<AdList> = networkService.getAds()
}