package com.example.data.medicine

import com.example.data.common.module.NetworkModule
import com.example.data.medicine.remote.api.MedicineApi
import com.example.data.medicine.repository.MedicineRepositoryImpl
import com.example.domain.medicine.MedicineRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton


@Module(includes = [NetworkModule::class])
@InstallIn(SingletonComponent::class)
internal class MedicineModule {

    @Singleton
    @Provides
    fun provideMedicineApi(retrofit: Retrofit): MedicineApi {
        return retrofit.create(MedicineApi::class.java)
    }

    @Singleton
    @Provides
    fun provideMedicineRepository(medicineApi: MedicineApi): MedicineRepository {
        return MedicineRepositoryImpl(medicineApi)
    }
}