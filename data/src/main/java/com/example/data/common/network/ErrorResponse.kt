package com.example.data.common.network

import com.example.data.common.mapper.DataMapper
import com.example.domain.common.base.NetworkError
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
            return NetworkError(
                error = error ?: "null",
                code = status ?: "null",
                message = message ?: "알 수 없는 에러"
            )
        }
    }
}


