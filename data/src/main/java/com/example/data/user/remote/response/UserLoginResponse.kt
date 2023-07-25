package com.example.data.user.remote.response

import com.example.data.common.mapper.DataMapper
import com.example.data.common.network.BaseResponse
import com.example.domain.user.model.User
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.time.LocalDate
import java.util.*

@Parcelize
class UserLoginResponse(
    @SerializedName("id")               val idx         : String,
    @SerializedName("userNumber")       val userId      : String,
    @SerializedName("name")             val name        : String,
    @SerializedName("birthDate")        val birthDate   : String,
    @SerializedName("phoneToken")       val deviceToken : String,
    @SerializedName("userMedicalInfos") val medicalInfo : UserMedicalInfoResponse
): BaseResponse {
    companion object: DataMapper<UserLoginResponse, User> {
        override fun UserLoginResponse.toDomainModel(): User {
            return User(
                idx = idx,
                userId = userId,
                name = name,
                birthDate = birthDate,
                deviceToken = deviceToken
            )
        }
    }
}