package com.example.data.hospital.repository

import com.example.data.common.network.ApiResponse
import com.example.data.common.network.ApiResponseHandler
import com.example.data.common.network.ErrorResponse.Companion.toDomainModel
import com.example.data.hospital.remote.api.HospitalApi
import com.example.data.hospital.remote.api.HospitalMockApi
import com.example.data.hospital.remote.request.NearbyHospitalRequest
import com.example.data.hospital.remote.response.list.HospitalPositListResponse.Companion.toDomainModel
import com.example.data.hospital.remote.response.list.SimpleHospitalListResponse.Companion.toDomainModel
import com.example.data.hospital.remote.response.single.HospitalResponse.Companion.toDomainModel
import com.example.domain.common.base.ResponseState
import com.example.domain.hospital.HospitalRepository
import com.example.domain.hospital.model.Hospital
import com.example.domain.hospital.model.HospitalPositList
import com.example.domain.hospital.model.SimpleHospitalList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject


internal class HospitalRepositoryImpl @Inject constructor(
    private val hospitalApi: HospitalApi,
    private val mockApi: HospitalMockApi
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

    /**
     * 주변 반경 n미터 이내의 병원들을 가져오는 메소드
     * @param lat: Double
     * @param lng: Double
     * @param dis: Double
     * @return Flow<ResponseStaet<HospitalPositList>
     */
    override suspend fun getNearByHospital(
        lat: Double,
        lng: Double,
        dis: Double
    ): Flow<ResponseState<HospitalPositList>> {
        return flow {
            ApiResponseHandler().handle {
                hospitalApi.getNearbyHospital(
                    NearbyHospitalRequest(
                        lat = lat,
                        lng = lng,
                        dis = dis
                    )
                )
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
     * 가장 가까운 제휴 치과를 가져오는 메소드
     *
     * @param cnt Int
     */
    override suspend fun getNearByPartnerHospitals(cnt: Int): Flow<ResponseState<SimpleHospitalList>> {
        return flow {
            ApiResponseHandler().handle {
                mockApi.getNearByPartnerHospitals(cnt)
            }.onEach { result ->
                when(result){
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