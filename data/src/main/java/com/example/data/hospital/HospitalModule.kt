package com.example.data.hospital

import com.example.data.hilt.module.NetworkModule
import com.example.data.hilt.module.RoomModule
import com.example.data.hilt.qualifier.BaseRetrofit
import com.example.data.hilt.qualifier.MockRetrofit
import com.example.data.hospital.remote.api.HospitalApi
import com.example.data.hospital.remote.api.HospitalMockApi
import com.example.data.hospital.repository.HospitalRepositoryImpl
import com.example.domain.hospital.HospitalRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(includes = [NetworkModule::class, RoomModule::class])
@InstallIn(SingletonComponent::class)
internal class HospitalModule {

    @Singleton
    @Provides
    fun provideHospitalApi(
        @BaseRetrofit retrofit: Retrofit
    ): HospitalApi {
        return retrofit.create(HospitalApi::class.java)
    }

    @Singleton
    @Provides
    fun provideHospitalMockApi(
        @MockRetrofit retrofit: Retrofit
    ): HospitalMockApi {
        return retrofit.create(HospitalMockApi::class.java)
    }

    @Singleton
    @Provides
    fun provideHospitalRepository(
        hospitalApi: HospitalApi,
        mockApi: HospitalMockApi
    ): HospitalRepository {
        return HospitalRepositoryImpl(hospitalApi, mockApi)
    }
}