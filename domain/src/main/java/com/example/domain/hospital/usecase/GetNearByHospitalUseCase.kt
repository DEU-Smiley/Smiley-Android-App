package com.example.domain.hospital.usecase

import com.example.domain.common.base.ResponseState
import com.example.domain.hospital.HospitalRepository
import com.example.domain.hospital.model.Hospital
import com.example.domain.hospital.model.SimpleHospital
import com.example.domain.hospital.model.SimpleHospitalList
import dagger.Reusable
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@Reusable
class GetNearByHospitalUseCase @Inject constructor(
    private val hospitalRepository: HospitalRepository
){
    suspend operator fun invoke(
        lat: Double,
        lng:Double,
        dis:Double
    ): Flow<ResponseState<SimpleHospitalList>> {
        return hospitalRepository.getNearByHospital(lat, lng, dis)
    }
}