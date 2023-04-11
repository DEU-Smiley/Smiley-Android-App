package com.example.data.hospital.remote.response

import com.example.data.common.mapper.DataMapper
import com.example.data.common.network.BaseResponse
import com.example.data.hospital.remote.response.HospitalResponse.Companion.toDomainModel
import com.example.domain.hospital.model.HospitalList
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
class HospitalListResponse(
    @SerializedName("hospitals") val hospitals: List<HospitalResponse>?
): BaseResponse {
    companion object: DataMapper<HospitalListResponse, HospitalList>{
        override fun HospitalListResponse.toDomainModel(): HospitalList {
            return HospitalList(
                hospitals = this.hospitals?.map { it.toDomainModel() } ?: emptyList()
            )
        }
    }

}

