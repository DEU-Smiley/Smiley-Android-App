package com.example.data.reserv.repository

import com.example.data.reserv.remote.api.ReservApi
import com.example.data.reserv.remote.response.ReservListResponse
import com.example.data.reserv.remote.response.ReservListResponse.Companion.toDomainModel
import com.example.data.reserv.remote.response.ReservResponse
import com.example.domain.common.base.ResponseState
import com.example.domain.reservation.ReservRepository
import com.example.domain.reservation.model.ReservList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.LocalDateTime
import javax.inject.Inject

internal class ReservRepositoryImpl @Inject constructor(
    private val reservApi: ReservApi
): ReservRepository {
    /**
     * 이전 예약 내역을 가져오는 메소드
     */
    override suspend fun getReservHistory(): Flow<ResponseState<ReservList>> {
        return flow {
            val testData = generateTestReservData()
            // 테스트 데이터 방출
            emit(
                ResponseState.Success(testData.toDomainModel())
            )
        }
    }

    /**
     * 예약 내역 테스트 데이터를 생성하는 메소드
     * 서버와 연동 후 제거 필요
     * (테스트 메소드에서도 Domain의 객체를 바로 쓰지 않음)
     * (매퍼를 통해 사용)
     */
    private fun generateTestReservData(): ReservListResponse {
        /** 테스트 데이터 */
        val reservs = arrayListOf<ReservResponse>()
        repeat(30){
            reservs.add(
                ReservResponse(
                    id = 1,
                    LocalDateTime.of(
                        2023,
                        (Math.random()*2+5).toInt(),
                        (Math.random()*15+15).toInt(),
                        (Math.random()*23+1).toInt(),
                        30
                    ),
                    "메모",
                    hospitalName = "개금다나아내과의원",
                    hospitalAddress = "부산시 부산진구 가야대로"
                )
            )
        }

        val comparator = compareBy<ReservResponse> { it.reservDate }
        reservs.sortWith(comparator)
        reservs.reverse()

        return ReservListResponse(
            reservList = reservs
        )
    }
}