package com.example.domain.reservation.model

import com.example.domain.common.base.BaseModel
import kotlinx.android.parcel.Parcelize
import java.time.LocalDateTime

@Parcelize
class Reserv (
    val id: Int,
    val reservDate: LocalDateTime,
    val memo: String,
    val hospitalName: String,
    val hospitalAddress: String
): BaseModel