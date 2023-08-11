package com.example.data.youtube.api

import com.example.data.youtube.response.YoutubeListResponse
import retrofit2.Response
import retrofit2.http.GET

internal interface YoutubeMockApi {
    @GET("youtube-apis/recommend")
    suspend fun getRecommendVideos(): Response<YoutubeListResponse>
}