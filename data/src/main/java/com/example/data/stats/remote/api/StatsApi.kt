package com.example.data.stats.remote.api

import com.example.data.stats.remote.response.StatsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

internal interface StatsApi {
    @GET("state/{day}")
    suspend fun getStatsAt(@Path("day") day:String): Response<StatsResponse>
}