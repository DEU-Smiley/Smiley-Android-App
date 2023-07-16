package com.example.data.hospital.remote.response

import com.example.data.common.mapper.DataMapper
import com.example.data.common.network.BaseResponse
import com.example.data.hospital.remote.response.HospitalPositResponse.Companion.toDomainModel
import com.example.domain.hospital.model.HospitalPositList
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
class HospitalPositListResponse (
    @SerializedName("hospitals") val hospitalPosits: List<HospitalPositResponse>?
): BaseResponse {
    companion object: DataMapper<HospitalPositListResponse, HospitalPositList> {
        override fun HospitalPositListResponse.toDomainModel(): HospitalPositList {
            return HospitalPositList(
                hospitalPosits = this.hospitalPosits?.map { it.toDomainModel() }?: emptyList()
            )
        }
    }
}