package com.example.data.hospital

import com.example.data.common.module.NetworkModule
import com.example.data.common.module.RoomModule
import com.example.data.hospital.remote.api.HospitalApi
import com.example.data.hospital.repository.HospitalRepositoryImpl
import com.example.domain.hospital.HospitalRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton

@Module(includes = [NetworkModule::class, RoomModule::class])
@InstallIn(SingletonComponent::class)
internal class HospitalModule {

    @Singleton
    @Provides
    fun provideHospitalApi(retrofit: Retrofit): HospitalApi {
        return retrofit.create(HospitalApi::class.java)
    }

    @Singleton
    @Provides
    fun provideHospitalRepository(
        hospitalApi: HospitalApi
    ): HospitalRepository {
        return HospitalRepositoryImpl(hospitalApi)
    }
}