package com.example.data.reserv.remote.response

import com.example.data.common.mapper.DataMapper
import com.example.data.common.network.BaseResponse
import com.example.domain.reservation.model.Reserv
import kotlinx.android.parcel.Parcelize
import java.time.LocalDateTime

@Parcelize
class ReservResponse (
    val id: Int,
    val reservDate: LocalDateTime,
    val memo: String,
    val hospitalName: String,
    val hospitalAddress: String
): BaseResponse {
    companion object: DataMapper<ReservResponse, Reserv> {
        override fun ReservResponse.toDomainModel(): Reserv {
            return Reserv(
                id = id,
                reservDate = reservDate,
                memo = memo,
                hospitalName = hospitalName,
                hospitalAddress = hospitalAddress
            )
        }
    }
}