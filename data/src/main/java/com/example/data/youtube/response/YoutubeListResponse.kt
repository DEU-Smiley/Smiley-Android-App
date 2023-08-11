package com.example.data.youtube.response

import com.example.data.common.mapper.DataMapper
import com.example.data.common.network.BaseResponse
import com.example.data.youtube.response.YoutubeVideoResponse.Companion.toDomainModel
import com.example.domain.youtube.model.YoutubeVideo
import com.example.domain.youtube.model.YoutubeVideoList
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
class YoutubeListResponse(
    @SerializedName("videos") val youtubeList: List<YoutubeVideoResponse>
): BaseResponse {
    companion object: DataMapper<YoutubeListResponse, YoutubeVideoList> {
        override fun YoutubeListResponse.toDomainModel(): YoutubeVideoList {
            return YoutubeVideoList(
                youtubeList = youtubeList.map { it.toDomainModel() } as ArrayList<YoutubeVideo>
            )
        }
    }
}