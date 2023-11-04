package com.kaanf.codebaseiocase.di

import com.kaanf.codebaseiocase.data.network.APIClient
import com.kaanf.codebaseiocase.data.network.NetworkService
import com.kaanf.codebaseiocase.utils.AppConstants.Companion.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    @Provides
    @Singleton
    fun provideNetworkService(): NetworkService = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(NetworkService::class.java)

    @Provides
    @Singleton
    fun provideAPIClient(networkService: NetworkService): APIClient = APIClient(networkService = networkService)
}