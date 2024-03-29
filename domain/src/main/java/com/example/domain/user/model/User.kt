package com.example.domain.user.model

import com.example.domain.common.base.BaseModel
import kotlinx.parcelize.Parcelize
import java.time.LocalDate

@Parcelize
class User (
    val idx             : Int,
    val userId          : String,
    val name            : String,
    val birthDate       : String,
    val deviceToken     : String,
    val profileImgUrl   : String
): BaseModel