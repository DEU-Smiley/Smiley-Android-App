package com.example.data.medicine

import com.example.data.AppDatabase
import com.example.data.common.module.NetworkModule
import com.example.data.common.module.RoomModule
import com.example.data.medicine.local.dao.MedicineDao
import com.example.data.medicine.remote.api.MedicineApi
import com.example.data.medicine.repository.MedicineRepositoryImpl
import com.example.domain.medicine.MedicineRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton


@Module(includes = [NetworkModule::class, RoomModule::class])
@InstallIn(SingletonComponent::class)
internal class MedicineModule {

    @Singleton
    @Provides
    fun provideMedicineApi(retrofit: Retrofit): MedicineApi {
        return retrofit.create(MedicineApi::class.java)
    }

    @Singleton
    @Provides
    fun provideMedicineRepository(
        medicineApi: MedicineApi,
        medicineDao: MedicineDao
    ): MedicineRepository {
        return MedicineRepositoryImpl(medicineApi, medicineDao)
    }

    @Singleton
    @Provides
    fun provideMedicineDao(appDatabase: AppDatabase): MedicineDao {
        return appDatabase.medicineDao()
    }
}