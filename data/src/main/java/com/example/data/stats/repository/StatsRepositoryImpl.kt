package com.example.data.stats.repository

import com.example.data.common.network.ApiResponse
import com.example.data.common.network.ApiResponseHandler
import com.example.data.common.network.ErrorResponse.Companion.toDomainModel
import com.example.data.stats.remote.api.StatsApi
import com.example.data.stats.remote.response.StatsResponse.Companion.toDomainModel
import com.example.domain.common.base.ResponseState
import com.example.domain.stats.StatsRepository
import com.example.domain.stats.model.Stats
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

internal class StatsRepositoryImpl @Inject constructor(
    private val statsApi: StatsApi
): StatsRepository {
    /**
     * 특정 날짜의 착용 통계를 받아오는 메소드
     * @return Flow<ResponseStats<Stats>>
     */
    override suspend fun getStatsAt(): Flow<ResponseState<Stats>> {
        return flow {
            ApiResponseHandler().handle {
                statsApi.getStatsAt("testDate")
            }.onEach { result ->
                when(result) {
                    is ApiResponse.Success -> {
                        emit(ResponseState.Success(result.data.toDomainModel()))
                    }
                    is ApiResponse.Error -> {
                        emit(ResponseState.Error(result.error.toDomainModel()))
                    }
                }
            }.collect()
        }
    }
}