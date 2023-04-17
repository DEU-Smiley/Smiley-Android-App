package com.example.domain.hospital.usecase

import com.example.domain.common.base.ResponseState
import com.example.domain.hospital.HospitalRepository
import com.example.domain.hospital.model.Hospital
import dagger.Reusable
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


@Reusable
class GetHospitalByHpidUseCase @Inject constructor(
    private val hospitalRepository: HospitalRepository
) {
    suspend operator fun invoke(hpid: String): Flow<ResponseState<Hospital>> {
        return hospitalRepository.getHospitalByHpid(hpid)
    }
}