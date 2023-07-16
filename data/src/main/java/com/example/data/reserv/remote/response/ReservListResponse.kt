package com.example.data.reserv.remote.response

import com.example.data.common.mapper.DataMapper
import com.example.data.common.network.BaseResponse
import com.example.data.reserv.remote.response.ReservResponse.Companion.toDomainModel
import com.example.domain.reservation.model.ReservList
import kotlinx.parcelize.Parcelize

@Parcelize
class ReservListResponse(
    val reservList: List<ReservResponse>
): BaseResponse {
    companion object: DataMapper<ReservListResponse, ReservList>{
        override fun ReservListResponse.toDomainModel(): ReservList {
            return ReservList(
                reservList = this.reservList.map { it.toDomainModel() }
            )
        }
    }
}