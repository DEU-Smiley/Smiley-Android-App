package com.example.data.common.network

import com.example.data.common.mapper.DataMapper
import com.example.domain.common.base.NetworkError
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
class ErrorResponse(
    val timestamp   : String? = null,
    val status      : String? = null,
    val error       : String? = null,
    val code        : String? = null,
    val message     : String? = null,
) : BaseResponse {
    companion object: DataMapper<ErrorResponse, NetworkError> {
        override fun ErrorResponse.toDomainModel(): NetworkError {
            // 서버측에서 status와 code가 반대로 오고 있어 바꿔서 매핑
            return NetworkError(
                error = error ?: "NO_ERROR",
                code = code ?: "NO_CODE",
                status  = status ?: "NO_STATUS",
                message = message ?: "알 수 없는 에러"
            )
        }
    }
}


