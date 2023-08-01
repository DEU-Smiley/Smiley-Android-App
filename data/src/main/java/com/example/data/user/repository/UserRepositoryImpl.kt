package com.example.data.user.repository

import com.example.data.common.network.ApiResponse
import com.example.data.common.network.ApiResponseHandler
import com.example.data.common.network.ErrorResponse.Companion.toDomainModel
import com.example.data.common.utils.PrefsDataStore
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
    private val userApi: UserApi,
    private val dataStore: PrefsDataStore
): UserRepository {
    companion object{
        const val ACCESS_FLAG_KEY = "ACCESS_FLAG"
        const val SAVED_USER_ID = "SAVED_USER_ID"
    }

    override suspend fun signUp(
        name: String,
        userId: String,
        birthDate: String,
        deviceToken: String
    ): Flow<ResponseState<User>> {
        return flow{
            ApiResponseHandler().handle {
                userApi.signUp(
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
                        dataStore.put(SAVED_USER_ID, result.data.userId)
                        emit(ResponseState.Success(result.data.toDomainModel()))
                    }
                    is ApiResponse.Error -> {
                        emit(ResponseState.Error(result.error.toDomainModel()))
                    }
                }
            }.collect() // 방출된 ResponseState를 collect하여 Flow<ResponseState>로 반환
        }
    }

    override suspend fun login(userId: String): Flow<ResponseState<User>> {
        return flow {
            ApiResponseHandler().handle {
                userApi.login(userId)
            }.onEach { result ->
                when(result) {
                    is ApiResponse.Success -> {
                        dataStore.put(SAVED_USER_ID, result.data.userId)
                        emit(ResponseState.Success(result.data.toDomainModel()))
                    }
                    is ApiResponse.Error -> {
                        emit(ResponseState.Error(result.error.toDomainModel()))
                    }
                }
            }.collect()
        }
    }

    override suspend fun getSavedUserInfo(): Flow<String?> {
        return dataStore.get(SAVED_USER_ID, String::class.java)
    }
}