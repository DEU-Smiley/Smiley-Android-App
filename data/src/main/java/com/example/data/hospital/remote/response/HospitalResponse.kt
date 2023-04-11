package com.example.data.hospital.remote.response

import com.example.data.common.mapper.DataMapper
import com.example.data.common.network.BaseResponse
import com.example.domain.hospital.model.Hospital
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
class HospitalResponse(
    @SerializedName("hpid")     val hpid        : String,
    @SerializedName("dutyName") val dutyName    : String,
    @SerializedName("dutyAddr") val dutyAddress : String
) : BaseResponse {
    companion object: DataMapper<HospitalResponse, Hospital> {
        override fun HospitalResponse.toDomainModel(): Hospital {
            return Hospital(
                hpid = this.hpid,
                dutyName = this.dutyName,
                dutyAddress = this.dutyAddress.split(" ").run {
                        "${this[0].substring(0..1)} ${this[1]}"
                    }
            )
        }
    }
}
