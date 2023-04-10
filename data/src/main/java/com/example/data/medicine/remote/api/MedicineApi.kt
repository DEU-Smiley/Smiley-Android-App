package com.example.data.medicine.remote.api

import com.example.data.medicine.remote.response.MedicineListResponse
import retrofit2.Response
import retrofit2.http.GET

internal interface MedicineApi {
    @GET("medicines")
    suspend fun getAllMedicines() : Response<MedicineListResponse>
}