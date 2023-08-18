package com.example.data.magazine.remote.response

import android.util.Base64
import com.example.data.common.mapper.DataMapper
import com.example.data.common.network.BaseResponse
import com.example.domain.magazine.model.Magazine
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
class MagazineResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("author") val author: String,
    @SerializedName("title") val title: String,
    @SerializedName("subTitle") val subTitle: String,
    @SerializedName("thumbnail") val thumbnail: String,
    @SerializedName("likes") val likes: Int,
    @SerializedName("viewCount") val viewCount: Int,
    @SerializedName("contentUrl") val mainContent: String
): BaseResponse {
    companion object: DataMapper<MagazineResponse, Magazine> {
        override fun MagazineResponse.toDomainModel(): Magazine {
            return Magazine(
                id = id,
                author = author,
                title = title,
                subTitle = subTitle,
                thumbnail = Base64.decode(thumbnail, Base64.DEFAULT),
                likes = likes,
                viewCount = viewCount,
                contentUrl = mainContent
            )
        }

    }
}
