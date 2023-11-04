package com.kaanf.codebaseiocase.di

import com.kaanf.codebaseiocase.data.repository.HomeRepositoryImpl
import com.kaanf.codebaseiocase.domain.usecase.GetAdsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UseCaseModule {
    @Singleton
    @Provides
    fun provideGetAdsUseCase(homeRepository: HomeRepositoryImpl): GetAdsUseCase = GetAdsUseCase(homeRepository = homeRepository)
}