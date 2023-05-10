package com.example.data.hospital.remote.request

import com.google.gson.annotations.SerializedName

data class NearbyHospitalRequest(
    @SerializedName("latitude")     private val lat: Double,
    @SerializedName("longitude")    private val lng: Double,
    @SerializedName("distance")     private val dis: Double
)