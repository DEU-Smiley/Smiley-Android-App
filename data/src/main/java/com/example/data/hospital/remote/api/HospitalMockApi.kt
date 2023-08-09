package com.example.data.hospital.remote.api

import com.example.data.hospital.remote.response.list.SimpleHospitalListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface HospitalMockApi {
    @GET("/hospitals/nearBy/partner/{cnt}")
    suspend fun getNearByPartnerHospitals(@Path("cnt") cnt: Int): Response<SimpleHospitalListResponse>
}