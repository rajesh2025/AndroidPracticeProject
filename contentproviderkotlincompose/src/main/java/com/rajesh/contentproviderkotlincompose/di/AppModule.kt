package com.rajesh.contentproviderkotlincompose.di

import com.rajesh.contentproviderkotlincompose.model.content.MediaContentProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideContentProvider() : MediaContentProvider{
       return MediaContentProvider()
    }
}