package com.example.domain.hospital

import com.example.domain.common.base.ResponseState
import com.example.domain.hospital.model.Hospital
import com.example.domain.hospital.model.SimpleHospitalList
import kotlinx.coroutines.flow.Flow

interface HospitalRepository {
    suspend fun getAllHospitals(): Flow<ResponseState<SimpleHospitalList>>
    suspend fun getHospitalByHpid(hpid: String): Flow<ResponseState<Hospital>>
    suspend fun getNearByHospital(lat:Double, lng:Double, dis:Double): Flow<ResponseState<SimpleHospitalList>>
}