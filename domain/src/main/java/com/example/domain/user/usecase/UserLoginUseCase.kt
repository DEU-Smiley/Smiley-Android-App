package com.example.domain.user.usecase

import com.example.domain.common.base.ResponseState
import com.example.domain.user.UserRepository
import com.example.domain.user.model.User
import dagger.Reusable
import kotlinx.coroutines.flow.Flow
import java.util.*
import javax.inject.Inject

@Reusable
class UserLoginUseCase @Inject constructor(
    private val userRepository: UserRepository
){
    suspend operator fun invoke(
        name: String,
        email: String,
        birthDate: String,
        deviceToken: String
    ): Flow<ResponseState<User>> {
        return userRepository.login(
            name = name,
            email = email,
            birthDate = birthDate,
            deviceToken = deviceToken
        )
    }
}