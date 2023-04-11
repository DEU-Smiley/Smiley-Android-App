package com.example.domain.hospital.usecase

import com.example.domain.common.base.ResponseState
import com.example.domain.hospital.HospitalRepository
import com.example.domain.hospital.model.HospitalList
import com.example.domain.medicine.model.MedicineList
import dagger.Reusable
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@Reusable
class GetAllHospitalUseCase @Inject constructor(
    private val hospitalRepository: HospitalRepository
) {
    suspend operator fun invoke(): Flow<ResponseState<HospitalList>> {
        return hospitalRepository.getAllHospitals()
    }
}