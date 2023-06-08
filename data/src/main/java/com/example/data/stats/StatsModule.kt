package com.example.data.stats

import com.example.data.hilt.module.NetworkModule
import com.example.data.hilt.qualifier.BaseRetrofit
import com.example.data.stats.remote.api.StatsApi
import com.example.data.stats.repository.StatsRepositoryImpl
import com.example.domain.stats.StatsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton


@Module(includes = [NetworkModule::class])
@InstallIn(SingletonComponent::class)
internal class StatsModule {

    @Singleton
    @Provides
    fun provideStatsApi(
        @BaseRetrofit retrofit: Retrofit
    ): StatsApi {
        return retrofit.create(StatsApi::class.java)
    }

    @Singleton
    @Provides
    fun provideStatsRepository(
        statsApi: StatsApi
    ): StatsRepository {
        return StatsRepositoryImpl(statsApi)
    }
}