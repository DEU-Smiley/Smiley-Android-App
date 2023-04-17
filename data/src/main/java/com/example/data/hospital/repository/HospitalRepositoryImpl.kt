package com.example.data.hospital.repository

import android.util.Log
import com.example.data.common.network.ApiResponse
import com.example.data.common.network.ApiResponseHandler
import com.example.data.common.network.ErrorResponse.Companion.toDomainModel
import com.example.data.hospital.remote.api.HospitalApi
import com.example.data.hospital.remote.response.HospitalResponse.Companion.toDomainModel
import com.example.data.hospital.remote.response.SimpleHospitalListResponse.Companion.toDomainModel
import com.example.domain.common.base.ResponseState
import com.example.domain.hospital.HospitalRepository
import com.example.domain.hospital.model.Hospital
import com.example.domain.hospital.model.SimpleHospitalList
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
    override suspend fun getAllHospitals(): Flow<ResponseState<SimpleHospitalList>> {
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

    /**
     * 특정 병원의 정보를 조회하는 메소드
     * @param hpid: String
     * @return Flow<ResponseState<HospitalList>>
     */
    override suspend fun getHospitalByHpid(hpid: String): Flow<ResponseState<Hospital>> {
        return flow {
            ApiResponseHandler().handle {
                hospitalApi.getHospitalByHpid(hpid)
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