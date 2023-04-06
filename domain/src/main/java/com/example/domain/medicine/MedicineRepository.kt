package com.example.domain.medicine

import com.example.domain.common.base.ResponseState
import com.example.domain.medicine.model.MedicineList
import kotlinx.coroutines.flow.Flow

interface MedicineRepository {
    suspend fun getAllMedicines(): Flow<ResponseState<MedicineList>>
}