package com.example.domain.reservation.model

import com.example.domain.common.base.BaseModel
import kotlinx.parcelize.Parcelize

@Parcelize
class ReservList (
    val reservList: List<Reserv>
): BaseModel