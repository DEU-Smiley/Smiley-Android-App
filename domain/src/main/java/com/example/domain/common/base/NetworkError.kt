package com.example.domain.common.base

import kotlinx.parcelize.Parcelize

@Parcelize
data class NetworkError(
    val error: String = "",
    val code: String = "",
    val status: String = "",
    val message: String = "알 수 없는 오류가 발생하였습니다."
) : BaseModel
