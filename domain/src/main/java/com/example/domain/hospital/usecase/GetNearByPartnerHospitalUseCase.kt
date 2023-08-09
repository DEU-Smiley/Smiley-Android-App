package com.example.domain.hospital.usecase

import com.example.domain.common.base.ResponseState
import com.example.domain.hospital.HospitalRepository
import com.example.domain.hospital.model.SimpleHospitalList
import dagger.Reusable
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@Reusable
class GetNearByPartnerHospitalUseCase @Inject constructor(
    private val hospitalRepository: HospitalRepository
) {
    suspend operator fun invoke(cnt: Int): Flow<ResponseState<SimpleHospitalList>> {
        return hospitalRepository.getNearByPartnerHospitals(cnt)
    }
}