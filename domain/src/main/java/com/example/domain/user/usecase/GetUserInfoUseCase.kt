package com.example.domain.user.usecase

import com.example.domain.user.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserInfoUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): Flow<String?> {
        return userRepository.getSavedUserInfo()
    }
}