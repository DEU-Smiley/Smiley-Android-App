package com.example.data.hilt.module

import com.example.data.common.utils.ApiLogger
import com.example.data.common.utils.RequestInterceptor
import com.example.data.common.utils.SharedPrefs
import com.example.data.hilt.qualifier.BaseRetrofit
import com.example.data.hilt.qualifier.MockRetrofit
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

    private const val BASE_URL = "http://113.198.236.222:8080"
    private const val MOCK_BASE_URL = "https://40f7f957-47a1-488e-ac1e-c8a882e2119d.mock.pstmn.io"
    // "https://21398c26-cbfe-4b79-8276-abe5ff68e880.mock.pstmn.io"
    // "https://980e509b-75c9-4e14-96a9-2691dedc1237.mock.pstmn.io"

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
    @MockRetrofit
    fun provideMockRetrofit(okHttp: OkHttpClient): Retrofit {
        return Retrofit.Builder().apply {
            addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder()
                        .setDateFormat("yyyy-MM-dd")
                        .setLenient()
                        .create()
                )
            )
            client(okHttp)
            baseUrl(MOCK_BASE_URL)
        }.build()
    }

    @Singleton
    @Provides
    fun provideOkHttp(requestInterceptor: RequestInterceptor): OkHttpClient {
        return OkHttpClient.Builder().apply {
            connectTimeout(10, TimeUnit.SECONDS)
            readTimeout(10, TimeUnit.SECONDS)
            writeTimeout(10, TimeUnit.SECONDS)
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