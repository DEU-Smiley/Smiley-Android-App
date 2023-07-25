package com.example.data.user.remote.response

import com.example.data.common.network.BaseResponse
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
class UserMedicalInfoResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("calibrationStatus") val calibrationStatus: Int,
    @SerializedName("startDate") val startDate: Date
): BaseResponse {
    
}