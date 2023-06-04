package com.example.data.reserv.repository

import com.example.data.hilt.module.NetworkModule
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module(includes = [NetworkModule::class])
@InstallIn(SingletonComponent::class)
internal class ReservModule {

}