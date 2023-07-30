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
    override suspend fun login(
        name: String,
        userId: String,
        birthDate: String,
        deviceToken: String
    ): Flow<ResponseState<User>> {
        return flow{
            // Handler가 Flow<ApiResponse<T>>를 리턴
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
                // Handler가 방출한 값에 따라 ResponseState로 변환하여 방출
                when(result){
                    is ApiResponse.Success -> {
                        emit(ResponseState.Success(result.data.toDomainModel()))
                    }
                    is ApiResponse.Error -> {
                        emit(ResponseState.Error(result.error.toDomainModel()))
                    }
                }
            }.collect() // 방출된 ResponseState를 collect하여 Flow<ResponseState>로 반환
        }
    }

    /**
     * 최초 접속 플래그를 가져오는 메소드
     */
    override suspend fun getAccessFlag(): Flow<Boolean> {
        return dataStore.getAccessFlag()
    }

    /**
     * 최초 접속 플래그를 세팅하는 메소드
     */
    override suspend fun setAccessFlag(flag: Boolean) {
        dataStore.setAccessFlag(flag)
    }
}