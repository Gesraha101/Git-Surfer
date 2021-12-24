package com.example.gitsurfer.di

import com.example.gitsurfer.data.MainSearchRepository
import com.example.gitsurfer.data.SearchDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ReposModule {

    @Singleton
    @Provides
    fun provideSearchRepository(
        @AppModule.MockedDataSource dataSource: SearchDataSource
    ): MainSearchRepository {
        return MainSearchRepository(dataSource)
    }
}