package com.example.data.medicine.repository

import com.example.data.common.network.ApiResponse
import com.example.data.common.network.ApiResponseHandler
import com.example.data.common.network.ErrorResponse.Companion.toDataModel
import com.example.data.medicine.model.response.MedicineListResponse.Companion.toDataModel
import com.example.data.medicine.remote.api.MedicineApi
import com.example.domain.common.base.ResponseState
import com.example.domain.medicine.MedicineRepository
import com.example.domain.medicine.model.MedicineList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.collect
import javax.inject.Inject


internal class MedicineRepositoryImpl @Inject constructor(private val medicineApi: MedicineApi): MedicineRepository {
    /**
     * 전체 약품 리스트를 조회하는 메소드
     * @return Flow<ResponseState<MedicineList>>
     */
    override suspend fun getAllMedicines(): Flow<ResponseState<MedicineList>> {
        return flow {
            ApiResponseHandler().handle {
                medicineApi.getAllMedicines()
            }.onEach { result ->
                when(result) {
                    is ApiResponse.Success -> emit(ResponseState.Success(result.data.toDataModel()))
                    is ApiResponse.Error -> emit(ResponseState.Error(result.error.toDataModel()))
                }
            }.collect()
        }
    }
}
