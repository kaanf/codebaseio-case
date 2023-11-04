package com.kaanf.codebaseiocase.data.repository

import android.util.Log
import com.kaanf.codebaseiocase.data.model.AdList
import com.kaanf.codebaseiocase.data.network.APIClient
import com.kaanf.codebaseiocase.domain.repository.HomeRepository
import com.kaanf.codebaseiocase.utils.IOStatus
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(private val apiClient: APIClient) : HomeRepository {
    override suspend fun fetchAds(): IOStatus<AdList> {
        Log.i("App.tag", "fetchAds: called.")
        apiClient.getAds().let { response ->
            if (response.isSuccessful)
                response.body()?.let { adList ->
                    return IOStatus.Success(adList)
                }
            else
                return IOStatus.Failure(Exception())
        }

        return IOStatus.Failure(Exception())
    }
}