package com.example.data.youtube.response

import android.util.Base64
import com.example.data.common.mapper.DataMapper
import com.example.data.common.network.BaseResponse
import com.example.domain.youtube.model.YoutubeVideo
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
class YoutubeVideoResponse(
    @SerializedName("id")           val id          : Int,
    @SerializedName("title")        val title       : String,
    @SerializedName("youtubeUrl")   val youtubeUrl  : String,
    @SerializedName("thumbnail")    val thumbnail   : String
): BaseResponse {
    companion object: DataMapper<YoutubeVideoResponse, YoutubeVideo> {
        override fun YoutubeVideoResponse.toDomainModel(): YoutubeVideo {
            return YoutubeVideo(
                id = id,
                title = title,
                youtubeUrl = youtubeUrl,
                thumbnail = Base64.decode(thumbnail, Base64.DEFAULT),
            )
        }
    }
}