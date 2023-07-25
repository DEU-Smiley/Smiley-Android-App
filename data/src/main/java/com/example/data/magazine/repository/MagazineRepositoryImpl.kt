package com.example.data.magazine.repository

import com.example.data.common.network.ApiResponse
import com.example.data.common.network.ApiResponseHandler
import com.example.data.common.network.ErrorResponse.Companion.toDomainModel
import com.example.data.magazine.remote.api.MagazineApi
import com.example.data.magazine.remote.response.MagazineListResponse.Companion.toDomainModel
import com.example.domain.common.base.ResponseState
import com.example.domain.magazine.MagazineRepository
import com.example.domain.magazine.model.MagazineList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

internal class MagazineRepositoryImpl @Inject constructor(
    private val mockApi: MagazineApi
): MagazineRepository {

    override suspend fun getAllMagazines(): Flow<ResponseState<MagazineList>> {
        return flow {
            ApiResponseHandler().handle {
                mockApi.getAllMagazines()
            }.onEach { result ->
                when(result) {
                    is ApiResponse.Success -> {
                        val data = result.data.toDomainModel()
                        emit(ResponseState.Success(data))
                    }
                    is ApiResponse.Error -> {
                        val error = result.error.toDomainModel()
                        emit(ResponseState.Error(error))
                    }
                }
            }.collect()
        }
    }

    override suspend fun getRecentMagazine(cnt: Int): Flow<ResponseState<MagazineList>> {
        return flow {
            ApiResponseHandler().handle {
                mockApi.getMagazine(cnt)
            }.onEach { result ->
                when(result) {
                    is ApiResponse.Success -> {
                        val data = result.data.toDomainModel()
                        emit(ResponseState.Success(data))
                    }
                    is ApiResponse.Error -> {
                        val error = result.error.toDomainModel()
                        emit(ResponseState.Error(error))
                    }
                }
            }.collect()
        }
    }
}