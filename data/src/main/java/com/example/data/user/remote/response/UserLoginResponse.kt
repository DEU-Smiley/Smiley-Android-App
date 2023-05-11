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
    @SerializedName("id")           val id          : String,
    @SerializedName("email")        val email       : String,
    @SerializedName("name")         val name        : String,
    @SerializedName("bitrhDate")    val birthDate   : String,
    @SerializedName("phoneToken")   val deviceToken : String
): BaseResponse {
    companion object: DataMapper<UserLoginResponse, User> {
        override fun UserLoginResponse.toDomainModel(): User {
            return User(
                id = id,
                email = email,
                name = name,
                birthDate = birthDate,
                deviceToken = deviceToken
            )
        }
    }
}