package com.kaanf.codebaseiocase.di

import com.kaanf.codebaseiocase.CodebaseApp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {
    @Provides
    @Singleton
    fun provideApplication(): CodebaseApp = CodebaseApp()
}