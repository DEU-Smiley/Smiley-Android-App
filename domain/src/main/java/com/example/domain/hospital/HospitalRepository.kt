package com.example.domain.hospital

import com.example.domain.common.base.ResponseState
import com.example.domain.hospital.model.HospitalList
import kotlinx.coroutines.flow.Flow

interface HospitalRepository {
    suspend fun getAllHospitals(): Flow<ResponseState<HospitalList>>
}