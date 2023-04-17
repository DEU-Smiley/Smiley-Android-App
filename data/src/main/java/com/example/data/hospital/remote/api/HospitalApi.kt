package com.example.data.hospital.remote.api

import com.example.data.hospital.remote.response.HospitalResponse
import com.example.data.hospital.remote.response.SimpleHospitalListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

internal interface HospitalApi {

    @GET("hospitals/simpleinfo")
    suspend fun getAllHospitals(): Response<SimpleHospitalListResponse>

    @GET("hospitals/{hpid}")
    suspend fun getHospitalByHpid(@Path("hpid") hpid:String): Response<HospitalResponse>
}