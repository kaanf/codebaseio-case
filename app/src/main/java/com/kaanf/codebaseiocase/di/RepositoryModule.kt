package com.kaanf.codebaseiocase.di

import com.kaanf.codebaseiocase.data.network.APIClient
import com.kaanf.codebaseiocase.data.repository.HomeRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {
    @Singleton
    @Provides
    fun provideHomeRepository(apiClient: APIClient) = HomeRepositoryImpl(apiClient = apiClient)
}