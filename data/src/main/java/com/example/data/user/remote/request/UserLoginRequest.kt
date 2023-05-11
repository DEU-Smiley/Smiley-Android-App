package com.example.data.user.remote.request

import com.google.gson.annotations.SerializedName
import java.util.*

data class UserLoginRequest (
    @SerializedName("email")        val email       : String,
    @SerializedName("name")         val name        : String,
    @SerializedName("birthDate")    val birthDate   : String,
    @SerializedName("phoneToken")   val deviceToken : String
)