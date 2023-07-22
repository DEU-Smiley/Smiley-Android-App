package com.example.data.magazine

import com.example.data.AppDatabase
import com.example.data.hilt.module.NetworkModule
import com.example.data.hilt.module.RoomModule
import com.example.data.hilt.qualifier.BaseRetrofit
import com.example.data.hilt.qualifier.MockApi
import com.example.data.hilt.qualifier.MockRetrofit
import com.example.data.magazine.local.dao.MagazineDao
import com.example.data.magazine.remote.api.MagazineApi
import com.example.data.magazine.repository.MagazineRepositoryImpl
import com.example.domain.magazine.MagazineRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(includes = [NetworkModule::class, RoomModule::class])
@InstallIn(SingletonComponent::class)
internal class MagazineModule {

    @Singleton
    @Provides
    fun provideMagazineApi(
        @BaseRetrofit retrofit: Retrofit
    ): MagazineApi {
        return retrofit.create(MagazineApi::class.java)
    }

    @Singleton
    @Provides
    @MockApi
    fun provideMockMagazineApi(
        @MockRetrofit retrofit: Retrofit
    ): MagazineApi {
        return retrofit.create(MagazineApi::class.java)
    }

    @Singleton
    @Provides
    fun provideMagazineDao(
        appDatabase: AppDatabase
    ): MagazineDao {
        return appDatabase.magazineDao()
    }

    @Singleton
    @Provides
    fun provideMagazineRepository(
        @MockApi magazineApi: MagazineApi
    ): MagazineRepository {
        return MagazineRepositoryImpl(magazineApi)
    }
}