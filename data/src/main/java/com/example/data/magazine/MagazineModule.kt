package com.example.data.magazine

import com.example.data.AppDatabase
import com.example.data.hilt.module.NetworkModule
import com.example.data.hilt.module.RoomModule
import com.example.data.magazine.local.dao.MagazineDao
import com.example.data.magazine.repository.MagazineRepositoryImpl
import com.example.domain.magazine.MagazineRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module(includes = [NetworkModule::class, RoomModule::class])
@InstallIn(SingletonComponent::class)
internal class MagazineModule {

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
        magazineDao: MagazineDao
    ): MagazineRepository {
        return MagazineRepositoryImpl(magazineDao)
    }
}