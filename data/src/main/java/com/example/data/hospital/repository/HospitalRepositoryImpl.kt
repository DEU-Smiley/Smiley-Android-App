package com.example.data.hospital.repository

import com.example.data.common.network.ApiResponse
import com.example.data.common.network.ApiResponseHandler
import com.example.data.common.network.ErrorResponse.Companion.toDomainModel
import com.example.data.hospital.remote.api.HospitalApi
import com.example.data.hospital.remote.response.HospitalListResponse.Companion.toDomainModel
import com.example.domain.common.base.ResponseState
import com.example.domain.hospital.HospitalRepository
import com.example.domain.hospital.model.HospitalList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject


internal class HospitalRepositoryImpl @Inject constructor(
    private val hospitalApi: HospitalApi
): HospitalRepository{
    /**
     * 전체 병원 리스트를 조회하는 메소드
     * @return Flow<ResponseState<HospitalList>>
     */
    override suspend fun getAllHospitals(): Flow<ResponseState<HospitalList>> {
        return flow {
            ApiResponseHandler().handle {
                hospitalApi.getAllHospitals()
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