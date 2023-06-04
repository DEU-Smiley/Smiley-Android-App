package com.example.domain.reservation.usecase

import com.example.domain.common.base.ResponseState
import com.example.domain.reservation.ReservRepository
import com.example.domain.reservation.model.ReservList
import dagger.Reusable
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@Reusable
class GetReservHistoryUseCase @Inject constructor(
    private val reservRepository: ReservRepository
) {
    suspend operator fun invoke(): Flow<ResponseState<ReservList>> {
        return reservRepository.getReservHistory()
    }
}