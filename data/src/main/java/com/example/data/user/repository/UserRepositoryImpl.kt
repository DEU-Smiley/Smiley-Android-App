package com.example.data.user.repository

import com.example.data.common.network.ApiResponse
import com.example.data.common.network.ApiResponseHandler
import com.example.data.common.network.ErrorResponse.Companion.toDomainModel
import com.example.data.user.remote.api.UserApi
import com.example.data.user.remote.request.UserLoginRequest
import com.example.data.user.remote.response.UserLoginResponse.Companion.toDomainModel
import com.example.domain.common.base.ResponseState
import com.example.domain.user.UserRepository
import com.example.domain.user.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

internal class UserRepositoryImpl @Inject constructor(
    private val userApi: UserApi
): UserRepository {
    override suspend fun login(
        name: String,
        userId: String,
        birthDate: String,
        deviceToken: String
    ): Flow<ResponseState<User>> {
        return flow{
            ApiResponseHandler().handle {
                userApi.login(
                    UserLoginRequest(
                        name = name,
                        userId = userId,
                        birthDate = birthDate,
                        deviceToken = deviceToken
                    )
                )
            }.onEach { result ->
                when(result){
                    is ApiResponse.Success -> {
                        emit(ResponseState.Success(result.data.toDomainModel()))
                    }
                    is ApiResponse.Error -> {
                        emit(ResponseState.Error(result.error.toDomainModel()))
                    }
                }
            }.collect()
        }
    }

}