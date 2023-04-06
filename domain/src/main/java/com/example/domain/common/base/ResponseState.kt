package com.example.domain.common.base

sealed class ResponseState<out T> {
    data class Success<T>(val data: T): ResponseState<T>()
    data class Error(val error: NetworkError): ResponseState<Nothing>()
}
