package com.example.data.magazine.remote.response

import com.example.data.common.mapper.DataMapper
import com.example.data.common.network.BaseResponse
import com.example.data.magazine.remote.response.MagazineListResponse.Companion.toDomainModel
import com.example.data.magazine.remote.response.MagazineResponse.Companion.toDomainModel
import com.example.domain.magazine.model.Magazine
import com.example.domain.magazine.model.MagazineList
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
class MagazineListResponse(
    @SerializedName("magazines") val magazines: ArrayList<MagazineResponse>?
): BaseResponse {
    companion object: DataMapper<MagazineListResponse, MagazineList> {
        override fun MagazineListResponse.toDomainModel(): MagazineList {
            return MagazineList(
                magazines = magazines?.map { it.toDomainModel() } as ArrayList<Magazine>
            )
        }
    }
}