package com.example.data.youtube.repository

import com.example.data.common.network.ApiResponse
import com.example.data.common.network.ApiResponseHandler
import com.example.data.common.network.ErrorResponse.Companion.toDomainModel
import com.example.data.youtube.api.YoutubeMockApi
import com.example.data.youtube.response.YoutubeListResponse.Companion.toDomainModel
import com.example.domain.common.base.ResponseState
import com.example.domain.youtube.YoutubeRepository
import com.example.domain.youtube.model.YoutubeVideoList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

internal class YoutubeRepositoryImpl @Inject constructor(
    private val youtubeMockApi: YoutubeMockApi
): YoutubeRepository {

    override suspend fun getRecommendVideos(): Flow<ResponseState<YoutubeVideoList>> {
        return flow {
            ApiResponseHandler().handle {
                youtubeMockApi.getRecommendVideos()
            }.onEach { result ->
                when(result){
                    is ApiResponse.Success -> {
                        emit(ResponseState.Success(result.data.toDomainModel()))
                    }
                    is ApiResponse.Error -> {
                        emit(ResponseState.Error(result.error.toDomainModel()))
                    }
                }
            }.collect()
        }
    }
}