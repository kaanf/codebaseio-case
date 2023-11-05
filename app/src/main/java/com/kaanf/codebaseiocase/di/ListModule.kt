package com.kaanf.codebaseiocase.di

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import com.kaanf.codebaseiocase.ui.home.AdsAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ListModule {
    @Provides
    @Singleton
    fun provideAdsAdapter(): AdsAdapter = AdsAdapter()

    @Provides
    @Singleton
    fun provideLinearLayoutManager(@ApplicationContext context: Context): LinearLayoutManager = LinearLayoutManager(context)
}