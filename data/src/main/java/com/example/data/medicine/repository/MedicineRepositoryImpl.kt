package com.example.data.medicine.repository

import com.example.data.common.network.ApiResponse
import com.example.data.common.network.ApiResponseHandler
import com.example.data.common.network.ErrorResponse.Companion.toDomainModel
import com.example.data.medicine.local.dao.MedicineDao
import com.example.data.medicine.local.entity.MedicineEntity.Companion.domainModelToEntity
import com.example.data.medicine.local.entity.MedicineEntity.Companion.entityToDomainModel
import com.example.data.medicine.remote.response.MedicineListResponse.Companion.toDomainModel
import com.example.data.medicine.remote.api.MedicineApi
import com.example.domain.common.base.ResponseState
import com.example.domain.medicine.MedicineRepository
import com.example.domain.medicine.model.MedicineList
import kotlinx.coroutines.flow.*
import javax.inject.Inject


internal class MedicineRepositoryImpl @Inject constructor(
    private val medicineApi: MedicineApi,
    private val medicineDao: MedicineDao
): MedicineRepository {
    /**
     * 전체 약품 리스트를 조회하는 메소드
     * @return Flow<ResponseState<MedicineList>>
     */
    override suspend fun getAllMedicines(): Flow<ResponseState<MedicineList>> {
        return flow {
            val list = medicineDao.findAll().map { it.entityToDomainModel() }
            if(list.isNotEmpty()){ // 내부 DB에 존재하면 내부 DB의 데이터를 사용
                emit(ResponseState.Success(MedicineList(list)))
            } else { // 내부 DB에 없는 경우에만 서버로 요청
                ApiResponseHandler().handle {
                    medicineApi.getAllMedicines()
                }.onEach { result ->
                    when(result) {
                        is ApiResponse.Success -> {
                            val data = result.data.toDomainModel()
                            // DB에 저장
                            medicineDao.insertAll(data.medicines.map { it.domainModelToEntity() })
                            emit(ResponseState.Success(data))
                        }
                        is ApiResponse.Error -> emit(ResponseState.Error(result.error.toDomainModel()))
                    }
                }.collect()
            }
        }
    }
}
