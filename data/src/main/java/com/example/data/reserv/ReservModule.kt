package com.example.data.reserv

import com.example.data.hilt.module.NetworkModule
import com.example.data.hilt.qualifier.BaseRetrofit
import com.example.data.reserv.remote.api.ReservApi
import com.example.data.reserv.repository.ReservRepositoryImpl
import com.example.domain.reservation.ReservRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(includes = [NetworkModule::class])
@InstallIn(SingletonComponent::class)
internal class ReservModule {

    @Singleton
    @Provides
    fun provideReservApi(
        @BaseRetrofit retrofit: Retrofit
    ): ReservApi{
        return retrofit.create(ReservApi::class.java)
    }

    @Singleton
    @Provides
    fun provideReservRepository(
        reservApi: ReservApi
    ): ReservRepository {
        return ReservRepositoryImpl(reservApi)
    }
}