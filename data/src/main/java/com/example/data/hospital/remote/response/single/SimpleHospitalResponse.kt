package com.example.data.hospital.remote.response.single

import com.example.data.common.mapper.DataMapper
import com.example.data.common.network.BaseResponse
import com.example.domain.hospital.model.SimpleHospital
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
class SimpleHospitalResponse(
    @SerializedName("hpid")         val hpid        : String,
    @SerializedName("dutyName")     val dutyName    : String,
    @SerializedName("dutyAddr")     val dutyAddress : String,
    @SerializedName("latitude")     val lat         : Double?,
    @SerializedName("longitude")    val lng         : Double?
) : BaseResponse {
    companion object: DataMapper<SimpleHospitalResponse, SimpleHospital> {
        override fun SimpleHospitalResponse.toDomainModel(): SimpleHospital {
            return SimpleHospital(
                hpid = this.hpid,
                dutyName = this.dutyName,
                dutyAddress = this.dutyAddress.split(" ").run {
                        "${this[0].substring(0..1)} ${this[1]}"
                    },
                lat = lat ?: 0.0,
                lng = lng ?: 0.0
            )
        }
    }
}
