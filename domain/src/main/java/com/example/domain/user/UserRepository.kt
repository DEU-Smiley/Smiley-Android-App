package com.example.domain.user

import com.example.domain.common.base.ResponseState
import com.example.domain.user.model.User
import kotlinx.coroutines.flow.Flow
import java.util.*

interface UserRepository {
    suspend fun login(
        name: String,
        userId:String,
        birthDate: String,
        deviceToken: String
    ): Flow<ResponseState<User>>
    suspend fun getAccessFlag(): Flow<Boolean>
    suspend fun setAccessFlag(flag: Boolean)
}