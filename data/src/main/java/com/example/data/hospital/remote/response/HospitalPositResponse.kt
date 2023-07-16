package com.example.data.hospital.remote.response

import com.example.data.common.mapper.DataMapper
import com.example.data.common.network.BaseResponse
import com.example.domain.hospital.model.HospitalPosit
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
class HospitalPositResponse (
    @SerializedName("hpid")         val hpid: String,
    @SerializedName("latitude")     val lat: Double,
    @SerializedName("longitude")    val lng: Double
): BaseResponse {
    companion object: DataMapper<HospitalPositResponse, HospitalPosit> {
        override fun HospitalPositResponse.toDomainModel(): HospitalPosit {
            return HospitalPosit(
                hpid = hpid,
                lat = lat,
                lng = lng
            )
        }
    }
}


