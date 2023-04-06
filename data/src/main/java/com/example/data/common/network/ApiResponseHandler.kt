package com.example.data.common.network

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response

/**
 * SafeApiCall 클래스
 * 네트워크 요청 메소드를 전달 받아서 예외를 처리
 */
class ApiResponseHandler {
    suspend fun<T> handle(call: suspend ()-> Response<T>): Flow<ApiResponse<T>> {
        return flow{
            val response = call.invoke()
            if(response.isSuccessful && response.body() != null) {
                emit(ApiResponse.Success(response.body()!!))
            } else {
                val errorBody = response.errorBody()?.string()
                val message =
                    if (errorBody.isNullOrEmpty()) response.message()
                    else errorBody

                emit(
                    ApiResponse.Error(
                        ErrorResponse(
                            code = response.code().toString(),
                            message = message ?: "알 수 없는 오류가 발생했습니다."
                        )
                    )
                )
            }
        }
    }
}