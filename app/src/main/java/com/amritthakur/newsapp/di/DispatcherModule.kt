package com.amritthakur.newsapp.di

import com.amritthakur.newsapp.presentation.common.DefaultDispatcherProvider
import com.amritthakur.newsapp.presentation.common.DispatcherProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DispatcherModule {

    @Provides
    @Singleton
    fun bindDispatcherProvider(): DispatcherProvider {
        return DefaultDispatcherProvider()
    }
}