package com.example.data.magazine.remote.response

import com.example.data.common.mapper.DataMapper
import com.example.data.common.network.BaseResponse
import com.example.domain.magazine.model.Magazine
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class MagazineResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("author") val author: String,
    @SerializedName("title") val title: String,
    @SerializedName("subTitle") val subTitle: String,
    @SerializedName("thumbnail") val thumbnail: ByteArray,
    @SerializedName("likes") val likes: Int,
    @SerializedName("viewCount") val viewCount: Int,
    @Expose
    @SerializedName("mainContent") val mainContent: String
): BaseResponse {
    companion object: DataMapper<MagazineResponse, Magazine> {
        override fun MagazineResponse.toDomainModel(): Magazine {
            return Magazine(
                id = id,
                author = author,
                title = title,
                subTitle = subTitle,
                thumbnail = thumbnail,
                likes = likes,
                viewCount = viewCount,
                contentUrl = mainContent
            )
        }

    }
}
