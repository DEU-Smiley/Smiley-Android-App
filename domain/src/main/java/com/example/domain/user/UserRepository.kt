package com.example.domain.user

import com.example.domain.common.base.ResponseState
import com.example.domain.user.model.User
import kotlinx.coroutines.flow.Flow
import java.util.*

interface UserRepository {
    suspend fun signUp(
        name: String,
        userId:String,
        birthDate: String,
        deviceToken: String
    ): Flow<ResponseState<User>>
    suspend fun login(userId: String): Flow<ResponseState<User>>
    suspend fun getSavedUserInfo(): Flow<String?>
}