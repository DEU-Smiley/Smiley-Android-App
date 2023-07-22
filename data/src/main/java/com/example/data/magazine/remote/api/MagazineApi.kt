package com.example.data.magazine.remote.api

import com.example.data.magazine.remote.response.MagazineListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

internal interface MagazineApi {
    @GET("magazines/{number}")
    suspend fun getMagazine(@Path("number") cnt: Int): Response<MagazineListResponse>
}