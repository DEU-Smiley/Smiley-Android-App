package com.example.data.hospital.remote.api

import com.example.data.hospital.remote.response.HospitalListResponse
import retrofit2.Response
import retrofit2.http.GET

internal interface HospitalApi {

    @GET("hospitals/simpleinfo")
    suspend fun getAllHospitals(): Response<HospitalListResponse>
}