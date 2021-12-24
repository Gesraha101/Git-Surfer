package com.example.gitsurfer.di

import android.content.Context
import com.example.gitsurfer.BuildConfig
import com.example.gitsurfer.data.SearchDataSource
import com.example.gitsurfer.data.apis.GithubService
import com.example.gitsurfer.data.sources.LocalDataSource
import com.example.gitsurfer.data.sources.RemoteDataSource
import com.example.gitsurfer.utils.FirebaseRemoteConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class MockedDataSource

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class ApiDataSource

    @Singleton
    @Provides
    @MockedDataSource
    fun provideLocalDataSource(@ApplicationContext context: Context): SearchDataSource {
        return LocalDataSource(context)
    }

    @Singleton
    @Provides
    @ApiDataSource
    fun provideRemoteDataSource(githubService: GithubService): SearchDataSource {
        return RemoteDataSource(githubService)
    }

    @Singleton
    @Provides
    fun provideHttpClient() : OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request()
                    .newBuilder()
                    .addHeader("Authorization", FirebaseRemoteConfig.header)
                    .build()
                val proceed = chain.proceed(request)
                proceed
            }
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    setLevel(
                        HttpLoggingInterceptor.Level.BODY
                    )
                })
            .build()
    }

    @Singleton
    @Provides
    fun provideApisModule(client: OkHttpClient): GithubService {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(client)
            .build()
            .create(GithubService::class.java)
    }
}