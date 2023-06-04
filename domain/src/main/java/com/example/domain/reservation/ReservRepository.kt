package com.example.domain.reservation

import com.example.domain.common.base.ResponseState
import com.example.domain.reservation.model.ReservList
import kotlinx.coroutines.flow.Flow


interface ReservRepository {
    suspend fun getReservHistory(): Flow<ResponseState<ReservList>>
}