package com.example.domain.medicine.usecase

import com.example.domain.common.base.ResponseState
import com.example.domain.medicine.MedicineRepository
import com.example.domain.medicine.model.MedicineList
import dagger.Reusable
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@Reusable
class GetAllMedicinesUseCase @Inject constructor(
    private val medicineRepository: MedicineRepository
){
    suspend operator fun invoke(): Flow<ResponseState<MedicineList>> {
        return medicineRepository.getAllMedicines()
    }
}