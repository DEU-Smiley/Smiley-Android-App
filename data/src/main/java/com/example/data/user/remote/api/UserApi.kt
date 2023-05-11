package com.example.data.user.remote.api

import com.example.data.user.remote.request.UserLoginRequest
import com.example.data.user.remote.response.UserLoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

internal interface UserApi {
    @POST("users/login")
    suspend fun login(@Body user:UserLoginRequest): Response<UserLoginResponse>
}