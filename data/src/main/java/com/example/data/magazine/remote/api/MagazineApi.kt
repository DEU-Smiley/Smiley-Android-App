package com.example.data.magazine.remote.api

import com.example.data.magazine.remote.response.MagazineListResponse
import com.example.domain.magazine.model.MagazineList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

internal interface MagazineApi {

    @GET("magazines/all")
    suspend fun getAllMagazines(): Response<MagazineListResponse>

    @GET("magazines/{number}")
    suspend fun getMagazine(@Path("number") cnt: Int): Response<MagazineListResponse>
}