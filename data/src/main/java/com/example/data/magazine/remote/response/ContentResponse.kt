package com.example.data.magazine.remote.response

import com.example.data.common.mapper.DataMapper
import com.example.data.common.network.BaseResponse
import com.example.domain.magazine.enum.ContentType
import com.example.domain.magazine.model.Content
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class ContentResponse(
    @SerializedName("content")
    val content: String,
    @SerializedName("contentType")
    val contentType: ContentType,
    @SerializedName("img")
    val img: ByteArray?
): BaseResponse {
    companion object: DataMapper<ContentResponse, Content>{
        override fun ContentResponse.toDomainModel(): Content {
            return Content(
                content = content,
                contentType = contentType,
                img = img
            )
        }
    }
}