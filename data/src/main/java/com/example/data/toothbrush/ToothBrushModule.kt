package com.example.data.toothbrush

import com.example.data.hilt.module.NetworkModule
import com.example.data.hilt.qualifier.BaseRetrofit
import com.example.data.toothbrush.remote.api.ToothBrushApi
import com.example.data.toothbrush.repository.ToothBrushRepositoryImpl
import com.example.domain.toothbrush.ToothBrushRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(includes = [NetworkModule::class])
@InstallIn(SingletonComponent::class)
internal class ToothBrushModule {

    @Singleton
    @Provides
    fun provideToothBrushApi(
        @BaseRetrofit retrofit: Retrofit
    ): ToothBrushApi {
        return retrofit.create(ToothBrushApi::class.java)
    }

    @Singleton
    @Provides
    fun provideToothBrushRepository(
        toothBrushApi: ToothBrushApi
    ): ToothBrushRepository {
        return ToothBrushRepositoryImpl(toothBrushApi)
    }
}