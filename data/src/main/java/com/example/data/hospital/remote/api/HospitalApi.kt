package com.example.data.hospital.remote.api

import com.example.data.hospital.remote.request.NearbyHospitalRequest
import com.example.data.hospital.remote.response.HospitalPositListResponse
import com.example.data.hospital.remote.response.HospitalResponse
import com.example.data.hospital.remote.response.SimpleHospitalListResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

internal interface HospitalApi {

    @GET("hospitals/simpleInfos")
    suspend fun getAllHospitals(): Response<SimpleHospitalListResponse>

    @GET("hospitals/{hpid}")
    suspend fun getHospitalByHpid(@Path("hpid") hpid:String): Response<HospitalResponse>

    @POST("hospitals/nearBy")
    suspend fun getNearbyHospital(@Body nearbyHospitalRequest: NearbyHospitalRequest): Response<HospitalPositListResponse>
}