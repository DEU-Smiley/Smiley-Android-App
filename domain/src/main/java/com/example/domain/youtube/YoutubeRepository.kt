package com.example.domain.youtube

import com.example.domain.common.base.ResponseState
import com.example.domain.youtube.model.YoutubeVideoList
import kotlinx.coroutines.flow.Flow

interface YoutubeRepository {
    suspend fun getRecommendVideos(): Flow<ResponseState<YoutubeVideoList>>
}