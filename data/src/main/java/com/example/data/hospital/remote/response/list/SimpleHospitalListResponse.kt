package com.example.data.hospital.remote.response.list

import com.example.data.common.mapper.DataMapper
import com.example.data.common.network.BaseResponse
import com.example.data.hospital.remote.response.single.SimpleHospitalResponse
import com.example.data.hospital.remote.response.single.SimpleHospitalResponse.Companion.toDomainModel
import com.example.domain.hospital.model.SimpleHospitalList
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
class SimpleHospitalListResponse(
    @SerializedName("hospitals") val hospitals: List<SimpleHospitalResponse>?
): BaseResponse {
    companion object: DataMapper<SimpleHospitalListResponse, SimpleHospitalList>{
        override fun SimpleHospitalListResponse.toDomainModel(): SimpleHospitalList {
            return SimpleHospitalList(
                simpleHospitals = this.hospitals?.map { it.toDomainModel() } ?: emptyList()
            )
        }
    }

}

