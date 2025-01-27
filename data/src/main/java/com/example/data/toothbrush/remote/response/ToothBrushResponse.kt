package com.example.data.toothbrush.remote.response

import android.util.Base64
import com.example.data.common.mapper.DataMapper
import com.example.data.common.network.BaseResponse
import com.example.domain.toothbrush.model.ToothBrush
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
class ToothBrushResponse(
    @SerializedName("image") val image: String
): BaseResponse {
    companion object: DataMapper<ToothBrushResponse, ToothBrush> {
        override fun ToothBrushResponse.toDomainModel(): ToothBrush {
            return ToothBrush(
                Base64.decode(image, Base64.DEFAULT)
            )
        }
    }
}