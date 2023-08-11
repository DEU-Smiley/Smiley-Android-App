package com.example.data.youtube

import com.example.data.hilt.module.NetworkModule
import com.example.data.hilt.qualifier.MockRetrofit
import com.example.data.youtube.api.YoutubeMockApi
import com.example.data.youtube.repository.YoutubeRepositoryImpl
import com.example.domain.youtube.YoutubeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import java.util.PrimitiveIterator
import javax.inject.Singleton

@Module(includes = [NetworkModule::class])
@InstallIn(SingletonComponent::class)
internal class YoutubeModule {
    @Singleton
    @Provides
    fun provideYoutubeMockApi(
        @MockRetrofit retrofit: Retrofit
    ): YoutubeMockApi {
        return retrofit.create(YoutubeMockApi::class.java)
    }

    @Singleton
    @Provides
    fun provideYoutubeRepository(
        youtubeMockApi: YoutubeMockApi
    ): YoutubeRepository {
        return YoutubeRepositoryImpl(youtubeMockApi)
    }
}