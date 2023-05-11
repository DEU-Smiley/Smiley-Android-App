package com.example.data.user

import com.example.data.hilt.module.NetworkModule
import com.example.data.hilt.qualifier.BaseRetrofit
import com.example.data.user.remote.api.UserApi
import com.example.data.user.repository.UserRepositoryImpl
import com.example.domain.user.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton


@Module(includes = [NetworkModule::class])
@InstallIn(SingletonComponent::class)
internal class UserModule {

    @Singleton
    @Provides
    fun provideUserApi(
        @BaseRetrofit retrofit: Retrofit
    ): UserApi {
        return retrofit.create(UserApi::class.java)
    }

    @Singleton
    @Provides
    fun provideUserRepository(
        userApi: UserApi,
    ): UserRepository {
        return UserRepositoryImpl(userApi)
    }
}