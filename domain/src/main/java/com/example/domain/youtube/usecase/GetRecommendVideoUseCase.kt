package com.example.domain.youtube.usecase

import com.example.domain.common.base.ResponseState
import com.example.domain.youtube.YoutubeRepository
import com.example.domain.youtube.model.YoutubeVideoList
import dagger.Reusable
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@Reusable
class GetRecommendVideoUseCase @Inject constructor(
    private val youtubeRepository: YoutubeRepository
) {
    suspend operator fun invoke(): Flow<ResponseState<YoutubeVideoList>> {
        return youtubeRepository.getRecommendVideos()
    }
}