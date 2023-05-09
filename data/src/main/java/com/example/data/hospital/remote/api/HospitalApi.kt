package com.example.data.hospital.remote.api

import com.example.data.hospital.remote.request.NearbyHospitalRequest
import com.example.data.hospital.remote.response.HospitalResponse
import com.example.data.hospital.remote.response.SimpleHospitalListResponse
import com.example.domain.hospital.model.SimpleHospitalList
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

internal interface HospitalApi {

    @GET("hospitals/simpleinfo")
    suspend fun getAllHospitals(): Response<SimpleHospitalListResponse>

    @GET("hospitals/{hpid}")
    suspend fun getHospitalByHpid(@Path("hpid") hpid:String): Response<HospitalResponse>

    @POST("hospitals/nearhospitalinfos")
    suspend fun getNearbyHospital(@Body nearbyHospitalRequest: NearbyHospitalRequest): Response<SimpleHospitalListResponse>
}