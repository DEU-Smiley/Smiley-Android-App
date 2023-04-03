package com.example.data.common.utils

import okhttp3.Interceptor
import okhttp3.Response

class RequestInterceptor constructor(private val pref: SharedPrefs): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = pref.getToken()
        val newRequest = chain.request().newBuilder()
            .addHeader("Authorization", token) // JWT 토큰 안쓰는 동안은 Interceptor에 헤더 추가 x
            .build()

        return chain.proceed(newRequest)
    }
}