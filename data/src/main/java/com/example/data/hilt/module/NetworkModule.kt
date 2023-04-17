package com.example.data.hilt.module

import com.example.data.common.utils.ApiLogger
import com.example.data.common.utils.RequestInterceptor
import com.example.data.common.utils.SharedPrefs
import com.example.data.hilt.qualifier.BaseRetrofit
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val BASE_URL = "http://113.198.236.100:8080"

    @Singleton
    @Provides
    @BaseRetrofit
    fun provideRetrofit(okHttp: OkHttpClient): Retrofit {
        return Retrofit.Builder().apply {
            addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder()
                    .setDateFormat("yyyy-MM-dd")
                    .create()
                )
            )
            client(okHttp)
            baseUrl(BASE_URL)
        }.build()
    }

    @Singleton
    @Provides
    fun provideOkHttp(requestInterceptor: RequestInterceptor): OkHttpClient {
        return OkHttpClient.Builder().apply {
            connectTimeout(60, TimeUnit.SECONDS)
            readTimeout(60, TimeUnit.SECONDS)
            writeTimeout(60, TimeUnit.SECONDS)
            addInterceptor(requestInterceptor)
            addInterceptor( // Http 요청/응답 중 Body만 로깅
                HttpLoggingInterceptor(ApiLogger())
                    .apply { setLevel(HttpLoggingInterceptor.Level.BODY) }
            )
        }.build()
    }

    @Provides
    fun provideRequestInterceptor(prefs: SharedPrefs): RequestInterceptor {
        return RequestInterceptor(prefs)
    }

}